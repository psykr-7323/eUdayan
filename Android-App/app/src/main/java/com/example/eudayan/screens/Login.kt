package com.example.eudayan.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eudayan.databinding.LoginBinding // 1. Change the import here

class Login : AppCompatActivity() {

    private lateinit var binding: LoginBinding // 2. Change the variable type here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater) // 3. Change the inflation call here
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString().trim()
            val password = binding.inputPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login Clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnGoToSignup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}
