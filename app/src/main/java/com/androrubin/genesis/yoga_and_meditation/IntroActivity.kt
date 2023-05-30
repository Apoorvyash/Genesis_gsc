package com.androrubin.genesis.yoga_and_meditation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.androrubin.genesis.databinding.ActivityIntroBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.security.AccessController.getContext

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding
    final val PERMISSION_REQUEST_FOR_CAMERA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.openCameraBtn.setOnClickListener {
            if(!hasCameraPermission()) requestCameraPermission()
            else{
                /**
                 * Opening the camera activity
                 */
                startActivity(Intent(this, CameraActivity::class.java))

            }
        }
    }

    private fun hasCameraPermission() = EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.CAMERA
        )

    private fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Camera will be used by this application to detect your running pose",
            PERMISSION_REQUEST_FOR_CAMERA,
            android.Manifest.permission.CAMERA

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults,
            IntroActivity()
        )
    }

}

