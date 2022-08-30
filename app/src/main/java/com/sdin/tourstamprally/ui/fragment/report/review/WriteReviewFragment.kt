package com.sdin.tourstamprally.ui.fragment.report.review

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.databinding.FragmentWriteReviewBinding
import com.sdin.tourstamprally.model.ReviewImg
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.ui.dialog.ReviewBottomSheetDialog
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.utill.ftp.FTPRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

class WriteReviewFragment : BaseFragment() {

    private var binding: FragmentWriteReviewBinding? = null
    private val args: WriteReviewFragmentArgs by navArgs()
    private lateinit var reviewWriter: ReviewWriter
    private var review_idx: Int = 0

    private val reviewImgList = mutableListOf<ReviewImg>()
    private val originImgList = mutableListOf<ReviewImg>()
    private val saveFilePath = arrayListOf<Uri>()

    private val imageFilterViewArray by lazy {
        arrayOf(
            R.id.second_item_view,
            R.id.third_item_view,
            R.id.four_item_view,
            R.id.five_item_view,
            R.id.first_imgFilter_view
        )
    }

    private val groupArr by lazy {
        arrayOf(
            R.id.second_picture_group,
            R.id.third_picture_group,
            R.id.four_picture_group,
            R.id.five_picture_group,
            R.id.first_picture_group
        )
    }

    private var saveFileName: String? = null
    //private var saveFilePath: ArrayList<Uri?> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_write_review, container, false)
        binding?.fragment = this@WriteReviewFragment
        reviewWriter = args.writerModel
        getReviewImgData()

        return binding?.root

    }

    private fun getReviewImgData() {
        if (!reviewWriter.isFirst) {
            review_idx = reviewWriter.review_idx

            apiService.selectReviewImg(reviewWriter.review_idx)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<ReviewImg>>() {
                    override fun onSuccess(result: List<ReviewImg>) {
                        if (result.isNotEmpty()) {
                            originImgList.addAll(result)
                            reviewImgList.addAll(result)

                            setReviewImageList()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setReviewImageList() {
        binding?.addPictureTxv?.text = "사진 ${reviewImgList.size}/5"

        for (i in 0 until reviewImgList.count()) {
            binding?.root?.findViewById<Group>(groupArr[i])?.visibility = View.VISIBLE
            val imageFilterView =
                binding?.root?.findViewById<ImageFilterView>(imageFilterViewArray[i])

            imageFilterView?.let {
                Glide.with(it.context)
                    .load("http://coratest.kr/imagefile/bsr/" + reviewImgList[i].review_img_url)
                    .placeholder(R.drawable.sample_bg)
                    .into(it)
            }
            reviewImgList[i].img = reviewImgList[i].review_img_url

        }
        setLastPictureItem()
    }

    private fun setLastPictureItem() {
        with(binding!!) {

            if (reviewImgList.size == 5) {
                Glide.with(addPictureImv.context)
                    .load(R.drawable.review_camera_white)
                    .into(addPictureImv)

                addPictureTxv.setTextColor(ContextCompat.getColor(addPictureTxv.context, R.color.white))
            }else {
                Glide.with(addPictureImv.context)
                    .load(R.drawable.review_camara_black)
                    .into(addPictureImv)

                addPictureTxv.setTextColor(ContextCompat.getColor(addPictureTxv.context, R.color.black))
            }

        }
    }

    @SuppressLint("SetTextI18n")
    fun onImageDeleteClick(view: View, tag: Int) {

        val subImageFilterView =
            binding?.root?.findViewById<ImageFilterView>(imageFilterViewArray[tag])
        if (tag.toString() == subImageFilterView?.tag.toString()) {
            val group = binding?.root?.findViewById<Group>(groupArr[tag])
            group?.visibility = View.INVISIBLE
            subImageFilterView?.setImageResource(0)
            reviewImgList.removeAt(tag)

        }
        binding?.addPictureTxv?.text = "사진 ${reviewImgList.size}/5"



        for (i in 0 until reviewImgList.count()) {
            val imageFilterView =
                binding?.root?.findViewById<ImageFilterView>(imageFilterViewArray[i])
            imageFilterView?.let {
                if (reviewImgList[i].img is Bitmap?) {

                    Glide.with(it.context)
                        .load(reviewImgList[i].img)
                        .into(it)
                } else {
                    Glide.with(it.context)
                        .load("http://coratest.kr/imagefile/bsr/" + reviewImgList[i].img)
                        .into(it)
                }

                val group = binding?.root?.findViewById<Group>(groupArr[i])
                group?.visibility = View.VISIBLE

            }
        }

        if (reviewImgList.size > 0) {
            for (i in 0 until groupArr.count()) {
                if (i > reviewImgList.count() -1) {
                    val removeImageFilterView =
                        binding?.root?.findViewById<ImageFilterView>(imageFilterViewArray[i])
                    removeImageFilterView?.setImageResource(0)
                    val group = binding?.root?.findViewById<Group>(groupArr[i])
                    group?.visibility = View.INVISIBLE
                }
            }
        }

        setLastPictureItem()
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        binding?.apply {
            titleTxv.text = reviewWriter.spotName
            if (!reviewWriter.isFirst) {
                reviewEdt.setText(reviewWriter.review_contents)
                ratingbar.rating = reviewWriter.review_score
            }

            firstItemView.setOnClickListener {
                if (reviewImgList.size < 6) {
                    permissionCheck()
                } else {
                    showToast("이미지는 최대 5개 까지 등록 가능합니다.")
                }
            }

        }
    }

    private fun permissionCheck() {
        requestMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

        )
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { isGrant ->
            if (isGrant.all { permission -> permission.value }) {
                ReviewBottomSheetDialog {
                    if (it == 0) { //사진
                        requestCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
                    } else if (it == 1) { //겔러리
                        requestGallery.launch(Intent(Intent.ACTION_PICK).apply {
                            type = "image/*"
                            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                        })
                    }
                }.show(requireActivity().supportFragmentManager, "")
            } else {
                Toast.makeText(requireContext(), "이미지 등록을 위한 권한을 허용해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    private val requestCamera: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val bitmap: Bitmap = result.data?.extras?.get("data") as Bitmap?
            ?: return@registerForActivityResult

        saveBitmapToJpeg(bitmap)
    }


    private val requestGallery: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val url: Uri? = result.data?.data



        var bitmap: Bitmap? = null
        bitmap = url?.let {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            } else {
                /*val source = ImageDecoder.createSource(requireContext().contentResolver, it)
                ImageDecoder.decodeBitmap(source)*/

                ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(requireContext().contentResolver, it)
                ) { decoder: ImageDecoder, _: ImageDecoder.ImageInfo?, _: ImageDecoder.Source? ->
                    decoder.isMutableRequired = true
                    decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                }
            }
        }

        bitmap?.let {
            saveBitmapToJpeg(bitmap = it)
            //getCacheImage()
        }


    }

    @SuppressLint("SetTextI18n")
    private fun saveBitmapToJpeg(bitmap: Bitmap) {
        val storage = requireContext().cacheDir
        if (reviewImgList.size > 5) {
            return
        }


        saveFileName = System.currentTimeMillis().toString() + ".jpg"
        val tempFile = File(storage, saveFileName)
        try {
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()

            reviewImgList.add(
                ReviewImg(
                    0,
                    reviewWriter.review_idx,
                    saveFileName,
                    bitmap,
                    saveFileName
                )
            )
            binding?.addPictureTxv?.text = "사진 ${reviewImgList.size}/5"

            val imageFilterView =
                binding?.root?.findViewById<ImageFilterView>(imageFilterViewArray[reviewImgList.size - 1])
            val group =
                binding?.root?.findViewById<Group>(groupArr[reviewImgList.size - 1])
            group?.let {
                it.visibility = View.VISIBLE
                imageFilterView?.let { imgFilterView ->
                    Glide.with(imgFilterView.context).load(bitmap).into(imgFilterView)
                }
            }

            setLastPictureItem()


        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun getCacheImage() {


        val file = File(requireContext().cacheDir.toString())

        for (tempFile in file.listFiles()) {

            for (model in reviewImgList) {

                if (model.img is Bitmap? && model.imgName != null) {
                    if (model.imgName == tempFile.name) {
                        saveFilePath.add(tempFile.absolutePath.toUri())
                    }
                }
            }
        }

        if (!saveFilePath.isNullOrEmpty()) {
            sendFTP(saveFilePath)
        } else {
            if (originImgList.size > reviewImgList.size) {
                deleteImages2()
                //deleteImages()
                //findNavController().popBackStack()
            }
        }
    }

    private fun sendFTP(pathArr: ArrayList<Uri>) {
        val ftpScope = CoroutineScope(Dispatchers.IO).async {
            val result: Result<Boolean> = FTPRepository().uploadReviewImage(pathArr)
            result.onSuccess { result ->
                if (result) {
                    Log.wtf("result", "isSuccess true")
                    imageDBSave()

                } else {

                    Log.wtf("result", "isSuccess false")
                }

            }

            result.onFailure {
                Log.wtf("result.onFailure", it)
            }

        }
        ftpScope.start()

    }

    private fun imageDBSave() {
        if (!saveFilePath.isNullOrEmpty()) {
            if (reviewWriter.isFirst) {
                insertImages()
            } else {

                deleteImages()
                if (reviewImgList.size > 0) {
                    insertImages()
                }

            }
        }
    }


    private fun insertImages() {
        val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val imgListToGson = gson.toJson(reviewImgList)
        apiService.insertReviewImg(imgListToGson, review_idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {

                    showToast("리뷰 업데이트에 성공하였습니다.")
                    findNavController().popBackStack()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun deleteImages2() {
        val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val imgListToGson = gson.toJson(originImgList)

        apiService.deleteReviewImgs(imgListToGson)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {
                    showToast("리뷰 업데이트에 성공하였습니다.")
                    findNavController().popBackStack()

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    private fun deleteImages() {
        val gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val imgListToGson = gson.toJson(originImgList)

        apiService.deleteReviewImgs(imgListToGson)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {
                    showToast("리뷰 업데이트에 성공하였습니다.")

                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }

    fun sendReview() {

        val reviewScore = binding?.ratingbar?.rating
        val reviewContents = binding?.reviewEdt?.text.toString()

        if (reviewScore != null) {

            if (reviewContents.trim() != "") {

                if (reviewWriter.isFirst) {
                    insertReview(reviewScore, reviewContents)


                } else {

                    updateReview(reviewScore, reviewContents)

                }


            } else {
                showToast("리뷰를 작성해 주세요")
            }
        }
    }

    private fun updateReview(reviewScore: Float, reviewContent: String) {

        apiService.updateReview(reviewScore, reviewContent, reviewWriter.review_idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(t: Int) {
                    if (t > 0) {
                        checkChangeImages()
                    }
                }

                override fun onError(e: Throwable) {
                    showToast("리뷰 업데이트에 실패하였습니다.")
                }

            })
    }

    private fun insertReview(reviewScore: Float, reviewContent: String) {


        if (TextUtils.isEmpty(reviewContent)) {
            showToast("리뷰를 작성해 주세요")
            return
        }

        apiService.insert_writeReview(
            Utils.User_Idx,
            reviewWriter.spotIdx,
            reviewScore,
            reviewContent
        ).enqueue(object : Callback<Int> {
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if (response.isSuccessful) {
                    try {
                        val result = response.body()
                        if (result != null) {
                            if (result > 0) {
                                review_idx = result
                                checkChangeImages()
                            } else {
                                showToast("리뷰 등록에 실패하였습니다.")
                            }
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    showToast("리뷰 등록에 실패하였습니다.")
                }
            }

            override fun onFailure(call: Call<Int>, t: Throwable) {
                showToast("리뷰 등록에 실패하였습니다.")
                t.printStackTrace()
            }

        })
    }

    private fun checkChangeImages() {
        if (!originImgList.containsAll(reviewImgList)
            || !reviewImgList.containsAll(originImgList)
        ) {

            getCacheImage()

        } else {
            showToast("리뷰 업데이트에 성공하였습니다.")
        }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


}