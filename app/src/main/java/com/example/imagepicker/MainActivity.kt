package com.example.imagepicker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imagepicker.databinding.ActivityMainBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.ImageView


import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val REQUEST_PERMISSION_CODE= 123
    private  val BACKGROUND_IMAGE_CODE=101
    private  val PROFILE_IMAGE_CODE=102
    private var selectedField:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.floatingActionButton.setOnClickListener {
            if (checkPermissions()) {


                openImagePicker(BACKGROUND_IMAGE_CODE)
            } else {
                selectedField=BACKGROUND_IMAGE_CODE
                Log.d("hello",selectedField.toString())
                requestPermissions()
            }
        }
        binding.profileImage.setOnClickListener {
            if (checkPermissions()) {

                openImagePicker(PROFILE_IMAGE_CODE)
            } else {
                selectedField=PROFILE_IMAGE_CODE
                requestPermissions()
            }
        }

    }
    private fun checkPermissions(): Boolean {
        val permissionCamera =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val permissionStorage =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return permissionCamera == PackageManager.PERMISSION_GRANTED && permissionStorage == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION_CODE
        )
    }
    private fun openImagePicker(code:Int) {
        Log.d("onclick",code.toString())
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(code)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode== REQUEST_PERMISSION_CODE){
            if (grantResults.isNotEmpty() &&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Log.d("selectedField",selectedField.toString())
                openImagePicker(selectedField)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri? =data?.data
        when (requestCode) {
            BACKGROUND_IMAGE_CODE -> {
                Glide.with(this)
                    .load(uri)
                    .fitCenter()
                    .into(binding.imageView)
            }
            PROFILE_IMAGE_CODE -> {
                Glide.with(this)
                    .load(uri)
                    .fitCenter()
                    .into(binding.profileImage)
            }
        }


    }
}
