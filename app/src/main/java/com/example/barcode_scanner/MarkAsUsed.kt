package com.example.barcode_scanner

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.barcode_scanner.databinding.ActivityMarkAsUsedBinding
import com.google.firebase.firestore.FirebaseFirestore

class MarkAsUsed : AppCompatActivity() {
    private lateinit var binding: ActivityMarkAsUsedBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkAsUsedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Get the barcode value from the Intent
        val barcodeValue = intent.getStringExtra("BARCODE_VALUE")

        // Set the barcode value to the TextView or wherever you need it
        if (barcodeValue != null) {
            binding.barcodeval.text = barcodeValue

            // Button click listener to save barcode value to Firestore
            binding.button.setOnClickListener {
                val barcval = binding.barcodeval.text.toString()

                if (barcval.isNotEmpty()) {
                    saveBarcodeToFirestore(barcval)
                } else {
                    Toast.makeText(this, "Barcode is not valid", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to save barcode value to Firestore
    private fun saveBarcodeToFirestore(barcodeValue: String) {
        // Prepare data to be saved (as a HashMap)
        val barcodeData = hashMapOf(
            "barcode" to barcodeValue
        )

        // Save the barcode data to Firestore with a unique document ID
        firestore.collection("barcodes")  // Ensure this matches the collection name used in BarcodeScan
            .add(barcodeData)
            .addOnSuccessListener {
                Toast.makeText(this, "Successfully marked it as used!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to mark: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
