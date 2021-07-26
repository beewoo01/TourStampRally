package com.sdin.tourstamprally.api;



import com.google.gson.Gson;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface APIService {


    /* test
    @GET("_appV1/IF103.php")
    Call<IF103> getIF103(@Query("token") String token,
                         @Query("page") int page);
    */

//    /*ID 중복확인*/
//    @GET("confirm_id")
//    Call<ResponseBody> duplicateCheckUserId(@Query("user_id") String user_id);
//
//    /*로그인*/
//    @GET("login_insteacher")
//    Call<ResponseBody> userLogin(@Query("vteacher_id") String vteacher_id,
//                                 @Query("userpw") String userpw);
//

//
//    /*로그인*/
//    @GET("login_nurse")
//    Call<ResponseBody> nurseLogin(@Query("nurse_id") String userid,
//                                  @Query("userpw") String userpw);
//
//    /* 제공인력 정보 가져오기*/
//    @GET("insteacher_user_info")
//    Call<List<UserModel>> getUserInfo(@Query("vteacher_id") String vteacher_id);


    /*로그인*/
    @GET("login")
    Call<ResponseBody> Login(@Query("id") String id,
                             @Query("password") String password);

    /*로그인*/
    @GET("id_check")
    Call<ResponseBody> id_check(@Query("id") String id
    );

    /*로그인*/
    @GET("bsr_login")
    Call<UserModel> userLogin(@Query("user_phone") String user_phone,
                                 @Query("user_password") String user_password);

    /*로그인*/
    @GET("bsr_login_Exists")
    Call<String> userLoginExists(@Query("user_phone") String user_phone,
                                 @Query("user_password") String user_password);


    /*비번찾기 조회*/
    @GET("search_pw")
    Call<ResponseBody> SearchPw(@Query("id") String id,
                                @Query("phone") String phone,
                                @Query("name") String name);

    /*회원정보조회*/
    @GET("get_my_info")
    Call<List<UserModel>> getUserInfo(@Query("id") String id);


    /*측정기록 검색*/
    @GET("get_history_info")
    Call<List<UserModel>> get_history_info(@Query("id") String id);


    //유저 회원가입
    //@FormUrlEncoded
    @GET("bsr_join")
    Call<String> userSignUp(@Query("user_phone") String phone,
                                  @Query("user_password") String password,
                                  @Query("user_name") String name,
                                  @Query("user_email") String email,
                                  @Query("user_location") String location,
                                  @Query("agree1") int agree1,
                                  @Query("agree2") int agree2
    );


    //유저 회원가입
    //@FormUrlEncoded
    @GET("bsr_find_password")
    Call<String> find_Password(@Query("user_name") String phone,
                            @Query("user_phone") String password,
                            @Query("user_email") String name
    );


    /*//비밀번호 업데이트
    @FormUrlEncoded
    @POST("change_password")
    Call<ResponseBody> change_password(@Field("id") String id,
                                       @Field("name") String name,
                                       @Field("password") String password,
                                       @Field("phone") String phone

    );

    //색 측정값 넣기
    @FormUrlEncoded
    @POST("insert_history")
    Call<ResponseBody> historyInsert(@Field("adminidx") int adminidx,
                                     @Field("useridx") int useridx,
                                     @Field("adminid") String adminid,
                                     @Field("userid") String userid,
                                     @Field("colorcode") String colorcode

    );*/

    @GET("getTourist")
    Call<List<Tour_Spot>> getTour(@Query("userIdx") int idx);

    //메인 시작시 가져오는 Location 등 데이터
    //@FormUrlEncoded
    @GET("getTourist_OrderBy")
    Call<List<Tour_Spot>> getTourOrderBy(@Query("userIdx") int idx);

    @GET("getTourist_sortTag")
    Call<List<Tour_Spot>> getTourSortHashTag(@Query("userIdx") int idx);


    @GET("gabia_token")
    Call<String> getToken();

    @GET("phonenum_authorization")
    Call<String> getAutoNumber( @Query("user_phone") String phone, @Query("access_token") String token);

}