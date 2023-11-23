package com.example.kfitness

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class Feedback : AppCompatActivity() {

    private lateinit var imageUri: Uri
    private lateinit var storageReference: StorageReference
    private lateinit var progressDialog: ProgressDialog
    private lateinit var selectedImageView: ImageView
    private lateinit var feedbackEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val selectImageButton: Button = findViewById(R.id.selectImagebtn)
        val uploadImageButton: Button = findViewById(R.id.uploadimagebtn)
        val saveNoteButton: Button = findViewById(R.id.btnSaveNote)

        selectedImageView = findViewById(R.id.firebaseimage)
        feedbackEditText = findViewById(R.id.Feedback)

        selectImageButton.setOnClickListener {
            selectImage()
        }

        uploadImageButton.setOnClickListener {
            uploadImage()
        }

        saveNoteButton.setOnClickListener {
            saveNote()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun uploadImage() {
        if (::imageUri.isInitialized) {
            progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading File....")
            progressDialog.show()

            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
            val now = Date()
            val fileName = formatter.format(now)
            storageReference = FirebaseStorage.getInstance().getReference("FEEDBACK Report/$fileName.zip")

            // Using Kotlin Coroutine for asynchronous operations
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Create a ZIP file containing the text file and image
                    val zipFileName = createZipFile(fileName)

                    // Upload the ZIP file
                    storageReference.putFile(Uri.fromFile(File(filesDir, zipFileName))).await()

                    // Dismiss the progress dialog after successful upload
                    withContext(Dispatchers.Main) {
                        if (progressDialog.isShowing) progressDialog.dismiss()
                        Toast.makeText(this@Feedback, "Upload Successful", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        if (progressDialog.isShowing) progressDialog.dismiss()
                        Toast.makeText(this@Feedback, "Failed to Upload", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createZipFile(baseFileName: String): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
        val now = Date()
        val zipFileName = "combined_${formatter.format(now)}.zip"

        try {
            val zipFile = File(filesDir, zipFileName)
            ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use { zipStream ->
                // Add the image to the ZIP file
                val imageFile = File(filesDir, "$baseFileName.jpg")
                imageFile.writeBytes(contentResolver.openInputStream(imageUri)?.readBytes() ?: ByteArray(0))
                addFileToZip(zipStream, imageFile, "$baseFileName.jpg")

                // Add the text file to the ZIP file
                val feedback = feedbackEditText.text.toString().trim()
                val textFileName = saveFeedbackAsFile(feedback)
                addFileToZip(zipStream, File(filesDir, textFileName), textFileName)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return zipFileName
    }

    private fun addFileToZip(zipStream: ZipOutputStream, file: File, entryName: String) {
        FileInputStream(file).use { fileInputStream ->
            BufferedInputStream(fileInputStream).use { bufferedInputStream ->
                val entry = ZipEntry(entryName)
                zipStream.putNextEntry(entry)
                bufferedInputStream.copyTo(zipStream, bufferSize)
                zipStream.closeEntry()
            }
        }
    }

    private val bufferSize = 1024




    private fun saveNote() {
        val feedback = feedbackEditText.text.toString().trim()

        if (feedback.isNotEmpty() && ::imageUri.isInitialized) {
            val note = HashMap<String, Any>()
            note["feedback"] = feedback

            val db = FirebaseFirestore.getInstance()
            db.collection("feedbacks")
                .add(note)
                .addOnSuccessListener { documentReference ->
                    // Get the ID of the added document
                    val feedbackId = documentReference.id

                    // Update the document with the image URL
                    updateImageURL(feedbackId)
                }
                .addOnFailureListener {
                    Toast.makeText(this@Feedback, "Failed to Save Feedback", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please select an image and enter feedback", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateImageURL(feedbackId: String) {
        storageReference.downloadUrl
            .addOnSuccessListener { uri ->
                val imageURL = uri.toString()

                // Update the feedback document with the image URL
                val feedbackRef =
                    FirebaseFirestore.getInstance().collection("feedbacks").document(feedbackId)
                feedbackRef.update("imageURL", imageURL)
                    .addOnSuccessListener {
                        Toast.makeText(this@Feedback, "Feedback Successfully Saved", Toast.LENGTH_SHORT).show()
                        clearFields()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@Feedback, "Failed to Save Image URL", Toast.LENGTH_SHORT).show()
                    }

                // Clear the storage reference to avoid any potential issues
                storageReference = FirebaseStorage.getInstance().reference
            }
            .addOnFailureListener {
                Toast.makeText(this@Feedback, "Failed to Retrieve Image URL", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveFeedbackAsFile(feedback: String): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA)
        val now = Date()
        val fileName = "feedback_${formatter.format(now)}.txt"

        try {
            openFileOutput(fileName, Context.MODE_PRIVATE).use {
                it.write(feedback.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return fileName
    }

    private fun clearFields() {
        feedbackEditText.text.clear()
        selectedImageView.setImageResource(0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            // Display the selected image
            selectedImageView.setImageURI(imageUri)
        }

    }
}