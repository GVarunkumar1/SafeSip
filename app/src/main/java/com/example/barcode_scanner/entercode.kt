package com.example.barcode_scanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.barcode_scanner.databinding.ActivityEntercodeBinding
import com.example.barcode_scanner.databinding.ActivityUsedProductBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class entercode : AppCompatActivity() {
    private lateinit var binding: ActivityEntercodeBinding
    private var intentData = ""
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEntercodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseFirestore = FirebaseFirestore.getInstance()
        binding.check2.setOnClickListener {
            intentData = binding.editTextText.text.toString()
            if (intentData.length != 0) {
                val barcodeValue = intentData

                // Check if the barcode already exists in Firestore by querying the field "barcode"
                firebaseFirestore.collection("barcodes")
                    .whereEqualTo("barcode", barcodeValue)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val querySnapshot: QuerySnapshot = task.result
                            if (!querySnapshot.isEmpty) {
                                // Barcode already exists
                                Toast.makeText(
                                    applicationContext,
                                    "The barcode already exists !!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Navigate to MainActivity
                                val intent = Intent(this, usedProduct::class.java)
                                startActivity(intent)
                            } else {
                                // Barcode does not exist, proceed to the next activity
                                val intent = Intent(this, MarkAsUsed::class.java)
                                intent.putExtra("BARCODE_VALUE", intentData)
                                startActivity(intent)
                            }
                        } else {
                            // Handle the failure
                            Toast.makeText(
                                applicationContext,
                                "Failed to retrieve data: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Barcode value can't be empty !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

        }
    }
}