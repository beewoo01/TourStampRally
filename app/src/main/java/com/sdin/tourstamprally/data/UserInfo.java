package com.sdin.tourstamprally.data;




public class UserInfo {

    private static UserInfo singleton;

    //로그 설정
    public boolean DEBUG_MODE = true;







    public int accountidx;
    public String id;
    public String password;
    public String name;
    public String email;
    public String phone;
    public boolean agree1;
    public boolean agree2;
    public boolean agree3;


    public int historyidx;
    public int adminidx;
    public int useridx;
    public String adminid;
    public String userid;

    public String colorcode;
    public String createdate;






    public static UserInfo getInstance() {
        if (singleton == null) {
            singleton = new UserInfo();
        }

        return singleton;
    }


}
