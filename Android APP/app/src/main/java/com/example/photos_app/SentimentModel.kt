package com.example.photos_app
package org.tensorflow.lite.codelabs.textclassification

import androidx.appcompat.app.AppCompatActivity
import org.tensorflow.lite.task.text.nlclassifier.BertNLClassifier
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.gms.tasks.Continuation
import org.tensorflow.lite.codelabs.textclassification.R
import org.tensorflow.lite.codelabs.textclassification.MainActivity
import com.google.firebase.ml.custom.FirebaseCustomRemoteModel
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors




class SentimentModel {
}