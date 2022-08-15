package com.sdin.tourstamprally

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {

    private var currentPhotoPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dispatchTakePictureIntent()
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            var photoFile: File? = null
            photoFile = createImageFile()
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    requireContext(),
                    "com.sdin.tourstamprally.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(takePictureIntent)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val file: File = File(currentPhotoPath)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            saveMediaToStorage(bitmap)
        }else {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveMediaToStorage(bitmap: Bitmap) {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(currentPhotoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
            findNavController().popBackStack()
        }
        val orientation = exif!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val bmRotated: Bitmap? = rotateBitmap(bitmap, orientation)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "jpeg_" + timeStamp + "_"
        var outputStream: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            contentValues.put(MediaStore.Images.Media.WIDTH, bmRotated?.width)
            contentValues.put(MediaStore.Images.Media.HEIGHT, bmRotated?.height)
            val imageUri =
                requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                try {
                    outputStream = requireActivity().contentResolver.openOutputStream(imageUri)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File(storageDir, imageFileName)
            try {
                outputStream = FileOutputStream(image)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        if (outputStream != null) {
            bmRotated?.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //bitmap.recycle();
            Toast.makeText(requireContext(), "저장 성공", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        } else {
            Toast.makeText(requireContext(), "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }



    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
            )
            currentPhotoPath = image.absolutePath
            image
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


        // Save a file: path for use with ACTION_VIEW intents
    }



}