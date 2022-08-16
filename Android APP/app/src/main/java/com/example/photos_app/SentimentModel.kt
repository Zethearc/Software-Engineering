package com.example.photos_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class SentimentModel : AppCompatActivity() {
    private lateinit var resultTextView: TextView
    private lateinit var executorService: ExecutorService
    private lateinit var scrollView: ScrollView
    private lateinit var predictButton: Button


    // TODO 5: Define a NLClassifier variable
    private lateinit var textClassifier: BertNLClassifier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sentiment_model)
        Log.v(TAG, "onCreate")
        executorService = Executors.newSingleThreadExecutor()
        resultTextView = findViewById(R.id.result_text_view)
        scrollView = findViewById(R.id.scroll_view)
        downloadModel("MobileBERT")

        predictButton = findViewById(R.id.button_upload)


        predictButton.setOnClickListener(
            { v: View -> classify(caption_result) })

        // TODO 3: Call the method to download TFLite mode
    }

    /** Send input text to TextClassificationClient and get the classify messages.  */
    private fun classify(text: String) {
        executorService.execute {

            // TODO 7: Run sentiment analysis on the input text
            val results = textClassifier.classify(text)

            // Create an array to predict sentiment results
            //val names = arrayOf("Hello world","I hate you","I love you","You look awesome","You are beautiful")

            // TODO 8: Convert the result to a human-readable text
            var max_value  = arrayOfNulls<Float>(3)
            var max_label = arrayOfNulls<String>(3)
            for (i in results.indices) {
                val result = results[i]
                // textToShow += String.format("    %s: %s\n", result.label, result.score)
                // Storage the result.score to max_value array
                max_value[i] = result.score
                max_label[i] = result.label
            }
            // Encontrar el score mayor

            var largest = max_value[0]
            for (num in max_value) {
                if (num != null && num > largest!!) {
                    largest = num
                }

            }

            // Encontrar el label del score mayor

            var label_predict = String()
            for (i in results.indices) {
                if (max_value[i] == largest) {
                    label_predict = max_label[i].toString()
                }
            }

            // TODO: Jean -> Create if cycle that picks which .xml face display on screen and below the percentage
            val faceDisplay = findViewById<ImageView>(R.id.face_display)
            val percentage = largest?.times(100f)
            if (label_predict == results[0].label){
                faceDisplay.setImageResource(R.drawable.ic_sad_face)
            }
            else if (label_predict == results[1].label){
                faceDisplay.setImageResource(R.drawable.ic_neutral_face)
            }else if (label_predict == results[0].label){
                faceDisplay.setImageResource(R.drawable.ic_happy_face)
            }
            val textToShow = String.format("Your image is predominant: %s\n (%s)", label_predict, percentage)
            showResult(textToShow)

        }
    }

    /** Show classification result on the screen.  */
    private fun showResult(textToShow: String) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread {

            // Append the result to the UI.
            resultTextView.append(textToShow)


            // Scroll to the bottom to show latest entry's classification result.
            scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
        }
    }
    // TODO 2: Implement a method to download TFLite model from Firebase
    /** Download model from Firebase ML.  */
    @Synchronized
    fun downloadModel(modelName: String) {
        val remoteModel = FirebaseCustomRemoteModel.Builder(modelName)
            .build()
        val conditions = FirebaseModelDownloadConditions.Builder()
            .requireWifi()
            .build()
        val firebaseModelManager = FirebaseModelManager.getInstance()
        firebaseModelManager
            .download(remoteModel, conditions)
            .continueWithTask { task: Task<Void> ->
                firebaseModelManager.getLatestModelFile(
                    remoteModel
                )
            }
            .continueWith(executorService, Continuation<File, Void> { task: Task<File> ->
                // Initialize a text classifier instance with the model
                val modelFile = task.result

                // TODO 6: Initialize a TextClassifier with the downloaded model
                textClassifier = BertNLClassifier.createFromFile(modelFile)

                // Enable predict button
                predictButton.isEnabled = true
                null
            })
            .addOnFailureListener { e: Exception? ->
                Log.e(TAG, "Failed to download and initialize the model. ", e)
                Toast.makeText(
                    this@SentimentModel,
                    "Model download failed, please check your connection.",
                    Toast.LENGTH_LONG
                )
                    .show()
                predictButton.isEnabled = false
            }
    }
}