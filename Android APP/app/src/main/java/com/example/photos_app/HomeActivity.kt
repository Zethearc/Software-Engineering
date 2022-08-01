package com.example.photos_app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*


enum class ProviderType {
    Email
}

class HomeActivity : AppCompatActivity(), View.OnClickListener,
    RecyclerAdaptor.CountOfImagesWhenRemoved {
    var recyclerView: RecyclerView? = null
    var textView: TextView? = null
    //var textView_msg: TextView? = null
    var button: Button? = null
    var button_remove: Button? = null
    var list: ArrayList<Uri>? = null
    var adaptor: RecyclerAdaptor? = null
    var colum = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        list = ArrayList()
        recyclerView = findViewById(R.id.recycler)
        button = findViewById(R.id.button)
        button_remove = findViewById(R.id.button_remove)
        adaptor = RecyclerAdaptor(list!!, applicationContext, this)
        recyclerView?.layoutManager = GridLayoutManager(this@HomeActivity, 4)
        recyclerView?.adapter = adaptor
        button?.setOnClickListener(this)
        button_remove?.setOnClickListener(this)
        if (ActivityCompat.checkSelfPermission(
                this, colum[0]
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, colum[1]
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(colum, 123)
        }
        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String? = bundle?.getString("provider")
        setup(email?: "", provider?: "")
    }

    private fun setup(email: String, provider: String) {
        title = "Página Principal"
        emailTextView.text = email
        providerTextView.text = provider

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button -> openGalley()
            R.id.button_remove -> {
                list!!.clear()
                adaptor?.notifyDataSetChanged()
                textView!!.text = "Image(" + list!!.size + ")"
            }
        }
    }

    private fun openGalley() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selcet Picture"), 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            if (data!!.clipData != null) {
                val x = data.clipData!!.itemCount
                for (i in 0 until x) {
                    if (list!!.size <= 7) {
                        list!!.add(data.clipData!!.getItemAt(i).uri)
                    }
                    else (showAlertImg())

                    adaptor?.notifyDataSetChanged()
                    textView!!.text = "Image(" + list!!.size + ")"
                }
            if (data.data != null) {
                val imgurl = data.data!!.path
                list!!.add(Uri.parse(imgurl))
                }
            }
        }
    }

    override fun clicked(getSize: Int) {
        textView!!.text = "Image(" + list!!.size + ")"
    }
    private fun showAlertImg() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Amount of images exceded (> 8)")
        builder.setPositiveButton("Accept", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}