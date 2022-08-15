package com.example.photos_app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
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
    private lateinit var inputImageCaptioning: EditText
    private lateinit var executorService: ExecutorService
    private lateinit var scrollView: ScrollView

    //piechart
    lateinit var pieChart: PieChart

    // TODO 5: Define a NLClassifier variable
    private lateinit var textClassifier: BertNLClassifier
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sentiment_model)
        Log.v(TAG, "onCreate")
        executorService = Executors.newSingleThreadExecutor()
        // TODO JEAN: Changed the var 'inputEditText' to "inputImageCaptioning" because will collect the captioning from the Franklin model, by the moment has an test array
        val inputImageCaptioning = arrayOf(
            "Hello world",
            "I hate you",
            "I love you",
            "You look awesome",
            "You are beautiful"
        )

        classify(inputImageCaptioning.toString())
        // JEAN: need to pass that classify to an new array that append

        // TODO 3: Call the method to download TFLite model
        downloadModel("MobileBERT")
    }

    /** Send input text to TextClassificationClient and get the classify into 3 lists.  */
    private fun classify(arrayText: Array) {
        executorService.execute {

            // TODO 7: Run sentiment analysis on the input text
            var negativeValues: Float
            var neutralValues: Float
            var positiveValues: Float
            //Append negative, neutral and positive values on the 3 vars, then pass to the pie chart
            for (element in arrayText.size) {
                val results = textClassifier.classify(arrayText[element])
                for (i in results.indices) {
                    negativeValues += (results[0].score * 100f)
                    neutralValues += (results[1].score * 100f)
                    positiveValues += (results[2].score * 100f)
                }
            }
        }

    }

    /** Show classification result on the screen.  */
    private fun showResult(textToShow: String) {
        // Run on UI thread as we'll updating our app UI
        runOnUiThread {

            // Append the result to the UI.
            resultTextView.append(textToShow)

            // Clear the input text.
            inputImageCaptioning.text.clear()

            // Scroll to the bottom to show latest entry's classification result.
            scrollView.post { scrollView.fullScroll(View.FOCUS_DOWN) }
        }
    }
    // TODO 2: Implement a method to download TFLite model from Firebase
    /** Download model from Firebase ML.  */
    @Synchronized
    private fun downloadModel(modelName: String) {
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
            }
    }
}