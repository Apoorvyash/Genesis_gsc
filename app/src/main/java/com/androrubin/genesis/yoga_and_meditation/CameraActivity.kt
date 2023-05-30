package com.androrubin.genesis.yoga_and_meditation

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.androrubin.genesis.databinding.ActivityCameraBinding
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.accurate.AccuratePoseDetectorOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


interface PoseDetectionListener{
    fun poseDetection(poseDetection: Task<Pose>) : Unit
}


class CameraActivity : AppCompatActivity(){

    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var graphicOverlay: GraphicOverlay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //graphicOverlay = binding.graphicOverlay

        // set up listeners
        binding.run {

            // to take photos
            captureBtn.setOnClickListener{
                Log.d("MyCamera", "Camera Running")
                takePhoto()
            }

            //output directory
            outputDirectory = getOutputDirectory()
            cameraExecutor = Executors.newSingleThreadExecutor()

            // start camera
            startCamera()
        }

    }

    companion object{
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "RunAndDetect").apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir

    }

    // accurate pose detector
    // Accurate pose detector on static images, when depending on the pose-detection-accurate sdk
    val options = AccuratePoseDetectorOptions.Builder()
        .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
        .build()

    val poseDetector = PoseDetection.getClient(options)

    lateinit var resizedBitmap: Bitmap


    private fun takePhoto() {

        val imageCapture = imageCapture ?: return

        // creating time stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                .format(System.currentTimeMillis()) + ".jpg"
        )

        // creating output options object which contains file and metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // setting up image capture listener
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val msg = "Photograph captured"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

                    // run pose detect
                    val image: InputImage
                    try {
                        image = InputImage.fromFilePath(baseContext, Uri.fromFile(photoFile))

                        val inputStream = contentResolver.openInputStream(savedUri)
                        val galleryImage = BitmapFactory.decodeStream(inputStream)
                        // get the bitmap
                        Log.d("MyCamera", "Image created")
                        val resizedBitmap = createBitmap(galleryImage)


                        // pose detector to process
                        poseDetector.process(image)
                            .addOnSuccessListener {
                                Log.d("MyCamera", "Image Processing")
                                processImage(it)
                            }
                            .addOnFailureListener {
                                Log.d("MyCamera", "Image Processing failed")
                            }

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Log.d("MyCamera", "Image Processing failed")
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("MyCamera", "Photo capture failed %=${exception.message}", exception)
                }

            }
        )
    }

    fun createBitmap(bitmap: Bitmap){
        val width = bitmap.width
        val height = bitmap.height
        val rotationDegrees =0

        resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height)
    }

    private lateinit var angleText: String

    fun processImage(pose: Pose){
        try {
            // Get all PoseLandmarks. If no person was detected, the list will be empty
            val allPoseLandmarks = pose.getAllPoseLandmarks()

            // Or get specific PoseLandmarks individually. These will all be null if no person
            // was detected

            // Shoulder
            val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
            val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)

            // Elbow
            val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
            val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)

            // Wrist
            val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
            val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)

            // Hips
            val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
            val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)

            // Knee
            val leftKnee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
            val rightKnee = pose.getPoseLandmark(PoseLandmark.RIGHT_KNEE)

            // Ankle
            val leftAnkle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)
            val rightAnkle = pose.getPoseLandmark(PoseLandmark.RIGHT_ANKLE)

            // nose
            val nose = pose.getPoseLandmark(PoseLandmark.NOSE)

            val leftEye = pose.getPoseLandmark(PoseLandmark.LEFT_EYE)
            val rightEye = pose.getPoseLandmark(PoseLandmark.RIGHT_EYE)

            val leftEar = pose.getPoseLandmark(PoseLandmark.LEFT_EAR)
            val rightEar = pose.getPoseLandmark(PoseLandmark.RIGHT_EAR)

            val leftMouth = pose.getPoseLandmark(PoseLandmark.LEFT_MOUTH)
            val rightMouth = pose.getPoseLandmark(PoseLandmark.RIGHT_MOUTH)


            // Shoulders left and right
            val leftShoulderPoint = leftShoulder?.position
            val leftShoulderX = leftShoulderPoint?.x
            val leftShoulderY = leftShoulderPoint?.y
            val rightShoulderPoint = rightShoulder?.position
            val rightShoulderX = rightShoulderPoint?.x
            val rightShoulderY = rightShoulderPoint?.y

            // Elbows left and right
            val leftElbowP = leftElbow!!.position
            val lElbowX = leftElbowP.x
            val lElbowY = leftElbowP.y
            val rightElbowP = rightElbow!!.position
            val rElbowX = rightElbowP.x
            val rElbowY = rightElbowP.y

            // Wrist
            val leftWristP = leftWrist!!.position
            val lWristX = leftWristP.x
            val lWristY = leftWristP.y
            val rightWristP = rightWrist!!.position
            val rWristX = rightWristP.x
            val rWristY = rightWristP.y

            // Hips
            val leftHipP = leftHip!!.position
            val lHipX = leftHipP.x
            val lHipY = leftHipP.y
            val rightHipP = rightHip!!.position
            val rHipX = rightHipP.x
            val rHipY = rightHipP.y

            // Knees
            val leftKneeP = leftKnee!!.position
            val lKneeX = leftKneeP.x
            val lKneeY = leftKneeP.y
            val rightKneeP = rightKnee!!.position
            val rKneeX = rightKneeP.x
            val rKneeY = rightKneeP.y


            // Ankles
            val leftAnkleP = leftAnkle!!.position
            val lAnkleX = leftAnkleP.x
            val lAnkleY = leftAnkleP.y
            val rightAnkleP = rightAnkle!!.position
            val rAnkleX = rightAnkleP.x
            val rAnkleY = rightAnkleP.y

            // nose
            val noseP = nose!!.position
            val noseX = noseP.x
            val noseY = noseP.y

            //Eye
            val leftEyeP = leftEye!!.position
            val leftEyeX = leftEyeP.x
            val leftEyeY = leftEyeP.y
            val rightEyeP = rightEye!!.position
            val rightEyeX = rightEyeP.x
            val rightEyeY = rightEyeP.y


            // Mouth
            val leftMouthP = leftMouth!!.position
            val leftMouthX = leftMouthP.x
            val leftMouthY = leftMouthP.y
            val rightMouthP = rightMouth!!.position
            val rightMouthX = rightMouthP.x
            val rightMouthY = rightMouthP.y

            // Ear
            val leftEarP = leftEar!!.position
            val leftEarX = leftEarP.x
            val leftEarY = leftEarP.y
            val rightEarP = rightEar!!.position
            val rightEarX = rightEarP.x
            val rightEarY = rightEarP.y

            val leftArmAngle = leftShoulder?.let { getAngle(it, leftElbow, leftWrist) }
            val leftArmAngleText = String.format("%.2f", leftArmAngle)

            val rightArmAngle = rightShoulder?.let { getAngle(it, rightElbow, rightWrist) }
            val rightArmAngleText = String.format("%.2f", rightArmAngle)

            val leftLegAngle = getAngle(leftHip, leftKnee, leftAnkle)
            val leftLegAngleText = String.format("%.2f", leftLegAngle)

            val rightLegAngle = getAngle(rightHip, rightKnee , rightAnkle)
            val rightLegAngleText = String.format("%.2f", rightLegAngle)

            angleText = "Left Arm angle: " + leftArmAngleText + "\n" +
                    "Right Arm angle text: " + rightArmAngleText + "\n"
            "Left Leg angle text: " + leftLegAngleText + "\n"
            "Right Leg angle text: " + rightLegAngleText + "\n"


            Log.d("MyCamera" , "NoseX: " + noseX.toString() + "NoseY: " + noseY.toString())

            if (leftShoulderX != null) {
                if (leftShoulderY != null) {
                    if (rightShoulderX != null) {
                        if (rightShoulderY != null) {
                            displayAll(leftShoulderX, leftShoulderY, rightShoulderX, rightShoulderY,
                                lElbowX, lElbowY, rElbowX, rElbowY, lWristX, lWristY, rWristX, rWristY,
                                lHipX, lHipY, rHipX, rHipY, lAnkleX, lAnkleY, rAnkleX, rAnkleY,
                                lKneeX, lKneeY, rKneeX, rKneeY, noseX, noseY, leftEyeX, leftEyeY, rightEyeX, rightEyeY,
                                leftMouthX, leftMouthY, rightMouthX, rightMouthY, leftEarX, leftEarY, rightMouthX, rightMouthY)
                        }
                    }
                }
            }





        }catch (e: Exception){
            Log.d("MyCamera", "Object not detected")
            Toast.makeText(this, "Object not detected" , Toast.LENGTH_SHORT).show()
        }
    }

    // draw pose
    fun displayAll(
        lShoulderX: Float, lShoulderY: Float, rShoulderX: Float, rShoulderY: Float,
        lElbowX: Float, lElbowY: Float, rElbowX: Float, rElbowY: Float,
        lWristX: Float, lWristY: Float, rWristX: Float, rWristY: Float,
        lHipX: Float, lHipY: Float, rHipX: Float, rHipY: Float,
        lAnkleX: Float, lAnkleY: Float, rAnkleX: Float, rAnkleY: Float,
        lKneeX: Float, lKneeY: Float, rKneeX: Float, rKneeY: Float,
        noseX: Float, noseY: Float, lEyeX: Float, lEyeY: Float, rEyeX: Float, rEyeY: Float,
        lMouthX: Float, lMouthY: Float, rMouthX: Float, rMouthY: Float, lEarX: Float, lEarY: Float,
        rEarX: Float, rEarY: Float
    ){
        val paint = Paint()
        paint.color = Color.GREEN
        val strokeWidth = 5.0f
        paint.strokeWidth = strokeWidth

        val drawBitmap = Bitmap.createBitmap(
            resizedBitmap.width,
            resizedBitmap.height,
            resizedBitmap.config
        )

        val canvas = Canvas(drawBitmap)

        canvas.drawBitmap(resizedBitmap, 0f, 0f, null)

        canvas.drawLine(rShoulderX, rShoulderY, rElbowX, rElbowY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, rShoulderX, rShoulderY, paint)
        canvas.drawLine(rElbowX, rElbowY, rWristX, rWristY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lElbowX, lElbowY, paint)
        canvas.drawLine(lElbowX, lElbowY, lWristX, lWristY, paint)
        canvas.drawLine(rShoulderX, rShoulderY, rHipX, rHipY, paint)
        canvas.drawLine(lShoulderX, lShoulderY, lHipX, lHipY, paint)
        canvas.drawLine(lHipX, lHipY, rHipX, rHipY, paint)
        canvas.drawLine(rHipX, rHipY, rKneeX, rKneeY, paint)
        canvas.drawLine(lHipX, lHipY, lKneeX, lKneeY, paint)
        canvas.drawLine(rKneeX, rKneeY, rAnkleX, rAnkleY, paint)
        canvas.drawLine(lKneeX, lKneeY, lAnkleX, lAnkleY, paint)

        // draw points in image
        val paintPoint = Paint()
        paintPoint.color = Color.RED
        paintPoint.strokeWidth = 5.0f
        paintPoint.strokeCap = Paint.Cap.ROUND

        // draw points
        canvas.drawPoint(noseX, noseY, paintPoint)
        canvas.drawPoint(lEarX, lEarY, paintPoint)
        canvas.drawPoint(rEarX, rEarY, paintPoint)
        canvas.drawPoint(lMouthX, lMouthY, paintPoint)
        canvas.drawPoint(rMouthX, rMouthY, paintPoint)
        canvas.drawPoint(lShoulderX, lShoulderY, paintPoint)
        canvas.drawPoint(rShoulderX, rShoulderY, paintPoint)
        canvas.drawPoint(lElbowX, lElbowY, paintPoint)
        canvas.drawPoint(rElbowX, rElbowY, paintPoint)
        canvas.drawPoint(lHipX, lHipY, paintPoint)
        canvas.drawPoint(rHipX, rHipY, paintPoint)




        Log.d("MyCamera" , "Above intent")

        val scaleDownBitmap = Bitmap.createScaledBitmap(drawBitmap, drawBitmap.width/2, drawBitmap.height/2, false)

        val poseDetectedFile = saveImageToInternalStorage(scaleDownBitmap)

        val intent = Intent(this, PoseDetectActivity::class.java).apply {
            putExtra("URI", poseDetectedFile.toString())
            putExtra("Info" , angleText)

        }
        startActivity(intent)

    }

    fun saveImageToInternalStorage(bitmap: Bitmap) : Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("processed_images", Context.MODE_PRIVATE)

        file = File(file, FILENAME_FORMAT + "processed_image" + ".jpg")

        try{
            val stream = FileOutputStream(file)

            // compressing bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            // flush the stream
            stream.flush()
            // close the stream
            stream.close()
        }catch (e : IOException){
            e.printStackTrace()
        }

        return Uri.parse(file.absolutePath)
    }

    fun getAngle(
        firstPoint: PoseLandmark,
        midPoint: PoseLandmark,
        lastPoint: PoseLandmark
    ): Double {
        var result = Math.toDegrees(
            Math.atan2(
                (lastPoint.position.y - midPoint.position.y).toDouble(), (
                        lastPoint.position.x - midPoint.position.x).toDouble()
            )
                    - Math.atan2(
                (firstPoint.position.y - midPoint.position.y).toDouble(), (
                        firstPoint.position.x - midPoint.position.x).toDouble()
            )
        )
        result = Math.abs(result) // Angle should never be negative
        if (result > 180) {
            result = 360.0 - result // Always get the acute representation of the angle
        }
        return result
    }

    private fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.createSurfaceProvider())
                }

            // image capture builder
            imageCapture = ImageCapture.Builder().build()

            //image analyzer
//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also {
//                    it.setAnalyzer(cameraExecutor, PoseDetectionAnalyzer())
//                }

            // set back camera as default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {

                cameraProvider.unbindAll()
                // bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("MyCamera", "Binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

}