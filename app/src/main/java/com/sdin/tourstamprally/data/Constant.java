package com.sdin.tourstamprally.data;


import android.util.Log;


public class Constant {

    /*FirstCare Server URL*/
    //public static final String SERVER_URL = "http://raon-soft.com/palette/";
    //public static final String SERVER_URL = "http://192.168.1.18:8080/project/";
    //public static final String SERVER_URL = "http://192.168.219.111:8005/project/";
    /*로컬 용*/
    public static final String SERVER_URL = "http://raon-soft.com/base_project_brs/";
    /*실제 업로드용*/
    //  http://raon-soft.com/firstcare_mobile/
    //  http://112.173.80.179:8080/firstcare_mobile/
    //  http://192.168.1.9:8080/firstcare_mobile/
    //  http://localhost:8080/firstcare_mobile/

    /*FirstCare Teacher Profile URL*/
//    public static final String TEACHER_PROFILE_IMG_URL = "https://firstcare.kr/_public/uploadFiles/vteacher_data/";

    /*도로명 주소 검색 파일 URL - 수정 필요*/
//    public static final String SEARCH_ADDRESS_URL = "http://raon-soft.com/firstcare_mobile/search_addr_android/";

    /*퍼미션*/
    public static int ACCESS_FINE_LOCATION_STAT = 1;
    public static int ACCESS_BACKGROUND_LOCATION_STAT = 1;
    public static int ACCESS_CAMERA = 1;

    public static final int PROGRESS_COUNT = 3;

    /*위치 정보 받아오기 실패 했을 때, 기본 위경도 값 (사용안함)*/
//    public static final double DefaultLatitude = 35.1750569;
//    public static final double DefaultLongitude = 129.1250885;

    /*이용약관 파일*/
    public static final String FILE_TERM_OF_USE = "fc_termofuse.txt";
    public static final String FILE_PRIVACY = "fc_privacy.txt";


    /*로그 찍기, DEBUG_MODE = true 일때만 로그 찍힘*/
    public static void LOG(String tag, String text) {
        if (UserInfo.getInstance().DEBUG_MODE) {
            Log.e(tag, text);
        }
    }


}