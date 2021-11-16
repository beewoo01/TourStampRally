package com.sdin.tourstamprally.utill;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FtpThread extends Thread{
    private String export;
    private Context context;
    private Uri imgeUri;
    private String fileName;
    boolean isSuccess = false;

    public FtpThread(Uri imgeUri, Context context, String fileName){
        this.imgeUri = imgeUri;
        this.context = context;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.setControlEncoding("UTF-8");

            //"http://coratest.kr/imagefile/bsr/"
            //ftpClient.connect("zzipbbong.cafe24.com", 21);
            ftpClient.connect("coratest.kr", 21);
            ftpClient.login("bsrraon", "raon123!@");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setBufferSize(5 * 1024 * 1024);
            int reply = ftpClient.getReplyCode();


            //세팅후에 서버로부터 연결 여부를 받아와 전송 할 것인지를 판단 한다.
            if (!FTPReply.isPositiveCompletion(reply)) {
                Log.e("FTP 접속 실패 ", "asdfa");
                ftpClient.disconnect();

            } else {
                Log.e("FTP 접속 성공 ", "asdfa");
                ftpClient.enterLocalPassiveMode(); // important!
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory("/tomcat/webapps/imagefile/bsr");

                // Will return "image:x*"
                String wholeID = DocumentsContract.getDocumentId(imgeUri);
                String id = wholeID.split(":")[1];
                String[] column = {MediaStore.Images.Media.DATA};
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = context.getContentResolver().
                        query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                column, sel, new String[]{id}, null);

                String filePath = "";

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }

                cursor.close();

                File uploadFile = new File(filePath);
                FileInputStream fis;

                fis = new FileInputStream(uploadFile);



                isSuccess = ftpClient.storeFile(fileName, fis);

                if (isSuccess) {


                    Log.d("FTP 파일 업로드 성공", "성공 성공!!");
                } else {
                    Log.e("FTP 파일 업로드 실패", "실패ㅜㅜ");
                }

                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

}
