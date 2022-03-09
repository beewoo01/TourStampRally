package com.sdin.tourstamprally.utill.ftp

import android.net.Uri
import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream

class FTPRepository {

    fun uploadReviewImage(filePath : ArrayList<Uri>) : Result<Boolean> {
        val ftpClient = FTPClient()
        try {
            ftpClient.apply {
                controlEncoding = "UTF-8"
                ftpClient.connect("coratest.kr", 21)
                ftpClient.login("bsrraon", "raon123!@")
                setFileType(FTP.BINARY_FILE_TYPE)
                enterLocalPassiveMode()
                bufferSize = 5 * 1024 * 1024
                changeWorkingDirectory("/tomcat/webapps/imagefile/bsr")
            }

            val resultMap = HashMap<Uri, Boolean>()

            for (uri in filePath) {
                uri.path?.let {
                    val uploadFile = File(it)
                    val fis = FileInputStream(uploadFile)
                    resultMap[uri] = ftpClient.storeFile(uploadFile.name, fis)

                } ?: run {
                    resultMap[uri] = false
                }

            }
            ftpClient.logout()
            ftpClient.disconnect()

            return if (resultMap.all { true }) {
                Result.success(true)
            }else {
                Result.success(false)
            }



        }catch (e: Exception) {
            return Result.failure(exception = e)
        }
    }
}