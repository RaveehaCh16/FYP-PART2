package com.example.insightlearn

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var resetPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pass_screen)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        emailInput = findViewById(R.id.emailInput)
        resetPasswordButton = findViewById(R.id.resetPasswordButton)

        resetPasswordButton.setOnClickListener {
            val email = emailInput.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Send password reset email
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password reset email sent to $email", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    } else {
                        Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
