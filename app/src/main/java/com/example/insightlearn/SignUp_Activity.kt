package com.example.insightlearn

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase // Add the FirebaseDatabase import

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    // Declare the views globally
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var genderInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var reenterPasswordInput: EditText
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        genderInput = findViewById(R.id.genderInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        reenterPasswordInput = findViewById(R.id.reenterPasswordInput)
        signUpButton = findViewById(R.id.signUpButton)

        signUpButton.setOnClickListener {
            val name = nameInput.text.toString()
            val age = ageInput.text.toString()
            val gender = genderInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val reenterPassword = reenterPasswordInput.text.toString()

            // Validation
            if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != reenterPassword) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Firebase registration
            registerUser(email, password, name, age, gender)
        }
    }

    private fun registerUser(email: String, password: String, name: String, age: String, gender: String) {
        // Use FirebaseAuth to create a user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                    Toast.makeText(this, "Registration Successful! Welcome ${user?.email}", Toast.LENGTH_SHORT).show()

                    // Store additional user profile data (name, age, gender) in Firebase Realtime Database
                    val userProfile = mapOf(
                        "name" to name,
                        "age" to age,
                        "gender" to gender
                    )

                    // Store additional profile data in Firebase Realtime Database
                    val database = FirebaseDatabase.getInstance().reference
                    database.child("users").child(user?.uid ?: "").setValue(userProfile)

                    // Navigate to LoginActivity
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
