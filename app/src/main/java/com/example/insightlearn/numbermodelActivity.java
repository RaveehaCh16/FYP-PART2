package com.example.insightlearn;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.AssetFileDescriptor;
import java.io.FileInputStream;
import android.graphics.BitmapFactory;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays; // For array to string conversion
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;
import android.os.Looper;

public class numbermodelActivity extends AppCompatActivity {

    private Interpreter tfliteInterpreter;
    private ImageView imageView;
    private TextView resultText;
    private TextView rawResultText;
    private ProgressBar progressBar;
    private Button predictButton;
    private ExecutorService executorService;
    private Handler mainHandler;

    private static final String MODEL_PATH = "my_model.tflite"; // Ensure correct filename and path
    private static final String TAG = "numbermodelActivity";

    // Updated input dimensions to match model's expected input
    private int inputImageWidth = 64; // Adjust based on your model's input
    private int inputImageHeight = 64; // Adjust based on your model's input
    private int inputPixelSize = 3; // RGB

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numdetectresult); // Ensure this matches your XML layout name

        // Initialize views
        imageView = findViewById(R.id.imageView);
        resultText = findViewById(R.id.resultText);
        rawResultText = findViewById(R.id.rawResultText);
        progressBar = findViewById(R.id.progressBar);
        predictButton = findViewById(R.id.predictButton);

        // Initialize ExecutorService and Handler for background tasks
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());

        // Load the TensorFlow Lite model asynchronously
        loadModelAsync();

        // Handle the predict button click event
        predictButton.setOnClickListener(v -> {
            // Disable button to prevent multiple clicks
            predictButton.setEnabled(false);
            resultText.setText("Predicting...");
            rawResultText.setText("Result:");
            progressBar.setVisibility(View.VISIBLE);
            runPrediction();
        });
    }

    /**
     * Asynchronously loads the TensorFlow Lite model to prevent blocking the UI thread.
     */
    private void loadModelAsync() {
        executorService.execute(() -> {
            boolean isModelLoaded = loadModel();
            mainHandler.post(() -> {
                if (isModelLoaded) {
                    Log.d("TAG", "Model loaded successfully!");
                    resultText.setText("Model loaded successfully.");
                    logModelDetails();
                } else {
                    Log.e("TAG", "Failed to load model.");
                    resultText.setText("Model loading failed.");
                }
            });
        });
    }

    /**
     * Asynchronously runs the prediction to prevent blocking the UI thread.
     */
    private void runPrediction() {
        executorService.execute(() -> {
            Bitmap bitmap = loadImageFromInternalStorage();
            if (bitmap != null) {
                // Display the image in the ImageView
                mainHandler.post(() -> imageView.setImageBitmap(bitmap));

                float[] result = runModel(bitmap);
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    predictButton.setEnabled(true); // Re-enable button
                    if (result != null) {
                        // Display the raw result
                        rawResultText.setText("Raw Result: " + Arrays.toString(result));

                        // Get the probability for class 1 (positive class)
                        float probability = result[0];

                        // Log the probability for debugging
                        Log.d("TAG", "Prediction probability for class 1: " + probability);

                        // Determine the predicted class (0 or 1)
                        if (probability < 0.15f) {
                            resultText.setText("Predicted class: Normal");
                        } else {
                            resultText.setText("Predicted class: Dysgraphic");
                        }
                    } else {
                        // Handle failure
                        Log.e("TAG", "Prediction failed: Model output is null.");
                        resultText.setText("Prediction failed. Model output is null.");
                    }
                });
            } else {
                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    predictButton.setEnabled(true); // Re-enable button
                    Log.e("TAG", "Prediction failed: Image not found or could not be loaded.");
                    resultText.setText("Prediction failed. Image not found or could not be loaded.");
                });
            }
        });
    }

    /**
     * Loads the TensorFlow Lite model from the assets directory.
     *
     * @return true if the model is loaded successfully, false otherwise.
     */
    private boolean loadModel() {
        try {
            ByteBuffer modelBuffer = loadModelFile(MODEL_PATH);
            Log.i("TAG", "Model Buffer: " + modelBuffer);
            if (modelBuffer != null) {

                Interpreter.Options options = new Interpreter.Options();
                Log.i("TAG", "Interpreter Options: " + options);
                tfliteInterpreter = new Interpreter(modelBuffer, options);
                Log.d("TAG", "TensorFlow Lite Interpreter created.");
                return true;
            } else {
                Log.e("TAG", "Model buffer is null.");
                return false;
            }
        } catch (Exception e) {
            Log.e("TAG", "Error initializing model: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Loads the model file from the assets directory into a ByteBuffer.
     *
     * @param modelPath The relative path to the model file in the assets directory.
     * @return A ByteBuffer containing the model data, or null if an error occurs.
     */
    private ByteBuffer loadModelFile(String modelPath) {
        try {
            // Verify model file existence
            String[] assetFiles = getAssets().list("");
            if (assetFiles != null) {
                boolean modelExists = false;
                for (String file : assetFiles) {
                    if (file.equals(modelPath)) {
                        modelExists = true;
                        Log.e("TAG", "Model file found in assets.");
                        break;
                    }
                }
                if (!modelExists) {
                    Log.e("TAG", "Model file " + modelPath + " not found in assets.");
                    return null;
                }
            }

            AssetFileDescriptor fileDescriptor = getAssets().openFd(modelPath);
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            ByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
            buffer.order(ByteOrder.nativeOrder());
            inputStream.close();
            Log.d("TAG", "Model file loaded successfully.");
            return buffer;
        } catch (IOException e) {
            Log.e("TAG", "Error loading model: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Logs the details of the model's input and output tensors for debugging purposes.
     */
    private void logModelDetails() {
        try {
            int numInputs = tfliteInterpreter.getInputTensorCount();
            int numOutputs = tfliteInterpreter.getOutputTensorCount();
            Log.d("TAG", "Number of inputs: " + numInputs);
            Log.d("TAG", "Number of outputs: " + numOutputs);

            for (int i = 0; i < numInputs; i++) {
                Tensor inputTensor = tfliteInterpreter.getInputTensor(i);
                Log.d("TAG", "Input Tensor " + i + ":");
                Log.d("TAG", " - Type: " + inputTensor.dataType());
                Log.d("TAG", " - Shape: " + Arrays.toString(inputTensor.shape()));
            }

            for (int i = 0; i < numOutputs; i++) {
                Tensor outputTensor = tfliteInterpreter.getOutputTensor(i);
                Log.d("TAG", "Output Tensor " + i + ":");
                Log.d("TAG", " - Type: " + outputTensor.dataType());
                Log.d("TAG", " - Shape: " + Arrays.toString(outputTensor.shape()));
            }
        } catch (Exception e) {
            Log.e("TAG", "Error logging model details: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Runs the TensorFlow Lite model on the provided bitmap image.
     *
     * @param bitmap The input image as a Bitmap.
     * @return An array of floats representing the model's output, or null if an error occurs.
     */
    private float[] runModel(Bitmap bitmap) {
        try {
            // Resize bitmap to match input shape of the model
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true);
            Log.d("TAG", "Bitmap resized to: " + inputImageWidth + "x" + inputImageHeight);

            // Convert bitmap to ByteBuffer
            ByteBuffer inputBuffer = bitmapToByteBuffer(resizedBitmap);
            Log.d("TAG", "Input ByteBuffer created with capacity: " + inputBuffer.capacity());

            // Prepare output buffer based on model's output tensor shape
            Tensor outputTensor = tfliteInterpreter.getOutputTensor(0);
            int[] outputShape = outputTensor.shape();
            String outputDataType = outputTensor.dataType().name();
            Log.d("TAG", "Output Tensor Type: " + outputDataType);
            Log.d("TAG", "Output Tensor Shape: " + Arrays.toString(outputShape));

            // Allocate output buffer dynamically based on output tensor shape
            // Assuming float output; adjust if different
            float[][] output = new float[outputShape[0]][outputShape[1]];

            // Run inference
            tfliteInterpreter.run(inputBuffer, output);
            Log.d("TAG", "Inference run successfully.");

            // Log the output
            Log.d("TAG", "Model Output: " + Arrays.toString(output[0]));

            return output[0];
        } catch (Exception e) {
            Log.e("TAG", "Error during model inference: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a Bitmap image to a ByteBuffer suitable for TensorFlow Lite model input.
     *
     * @param bitmap The input image as a Bitmap.
     * @return A ByteBuffer containing the image data.
     */
    private ByteBuffer bitmapToByteBuffer(Bitmap bitmap) {
        int batchSize = 1;
        int bytesPerChannel = 4; // Float size
        int channels = 3; // RGB

        ByteBuffer buffer = ByteBuffer.allocateDirect(
                batchSize * inputImageWidth * inputImageHeight * channels * bytesPerChannel
        );
        buffer.order(ByteOrder.nativeOrder());

        int[] intValues = new int[inputImageWidth * inputImageHeight];
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int pixelValue : intValues) {
            float r = ((pixelValue >> 16) & 0xFF) / 255.0f;
            float g = ((pixelValue >> 8) & 0xFF) / 255.0f;
            float b = (pixelValue & 0xFF) / 255.0f;
            buffer.putFloat(r);
            buffer.putFloat(g);
            buffer.putFloat(b);
        }

        buffer.rewind(); // Reset buffer position to the beginning

        // **Removed the buffer reading loop to prevent altering the buffer's position**
        // buffer.position(0);
        // for (int i = 0; i < 10; i++) { // Log first 10 floats
        //     if (buffer.remaining() >= 4) { // Ensure there are enough bytes to read a float
        //         Log.d("TAG", "Input Float " + i + ": " + buffer.getFloat());
        //     }
        // }
        // buffer.rewind(); // Reset again after reading

        return buffer;
    }

    /**
     * Loads an image from the app's internal storage.
     *
     * @return The image as a Bitmap, or null if loading fails.
     */
    private Bitmap loadImageFromInternalStorage() {
        try {
            // Define the image file path
            File filePath = new File(getFilesDir(), "MyDrawingFolder/drawing.png");

            // Check if the file exists
            if (filePath.exists()) {
                // Decode the file into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeFile(filePath.getAbsolutePath());
                if (bitmap != null) {
                    Log.d("TAG", "Image loaded successfully from: " + filePath.getAbsolutePath());
                    return bitmap;
                } else {
                    Log.e("TAG", "Bitmap decoding returned null.");
                    return null;
                }
            } else {
                Log.e("TAG", "Image not found at: " + filePath.getAbsolutePath());
                return null;
            }
        } catch (Exception e) {
            Log.e("TAG", "Error loading image: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the TensorFlow Lite Interpreter and ExecutorService when the activity is destroyed to free resources.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tfliteInterpreter != null) {
            tfliteInterpreter.close();
            tfliteInterpreter = null;
            Log.d("TAG", "Interpreter closed.");
        }
        executorService.shutdown(); // Shutdown ExecutorService
    }
}

