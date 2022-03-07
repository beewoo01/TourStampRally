package com.sdin.tourstamprally.api;



import com.google.gson.Gson;
import com.sdin.tourstamprally.model.AllReviewDTO;
import com.sdin.tourstamprally.model.AllReviewModel;
import com.sdin.tourstamprally.model.CouponModel;
import com.sdin.tourstamprally.model.HashTagModel;
import com.sdin.tourstamprally.model.InterestModel;
import com.sdin.tourstamprally.model.Location;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.Notice;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.RallyMapModel;
import com.sdin.tourstamprally.model.ReveiwCommentsDC;
import com.sdin.tourstamprally.model.ReviewDetailDC;
import com.sdin.tourstamprally.model.StoreModel;
import com.sdin.tourstamprally.model.StoreSubDTO;
import com.sdin.tourstamprally.model.TopFourLocationModel;
import com.sdin.tourstamprally.model.TourTagModel;
import com.sdin.tourstamprally.model.TourTagModelDC;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.Tour_Spot2;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.model.TouristSpotPointDC;
import com.sdin.tourstamprally.model.TouristSpotPointImg;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.model.VisitCountModel;
import com.sdin.tourstamprally.model.history_spotModel2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;


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
    Call<ResponseBody> id_check(@Query("id") String id);

    /*로그인*/
    @GET("bsr_login")
    Call<UserModel> userLogin(@Query("user_phone") String user_phone,
                                 @Query("user_password") String user_password);

    /*로그인*/
    @GET("bsr_login_Exists")
    Call<String> userLoginExists(@Query("user_phone") String user_phone,
                                 @Query("user_password") String user_password);

    /*로그인*/
    @GET("bsr_phone_Exists")
    Call<String> userPhoneExists(@Query("user_phone") String user_phone);


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
                                  @Query("authorityIdx") int authorityIdx
    );

    //유저 회원가입
    //@FormUrlEncoded
    @GET("bsr_find_password")
    Call<String> find_Password(@Query("user_name") String phone,
                            @Query("user_phone") String password,
                            @Query("user_email") String name
    );


    @GET("user_update")
    Call<String> user_update(@Query("user_idx") int user_idx,
                             @Query("user_phone") String phone,
                             @Query("user_password") String password,
                             @Query("user_name") String name,
                             @Query("user_email") String email,
                             @Query("user_location") String location,
                             @Query("user_profile") String profile
    );

    @GET("update_user_psw")
    Call<Integer> update_user_psw(@Query("user_name") String name,
                                 @Query("user_phone") String phone,
                                 @Query("user_email") String email,
                                 @Query("user_password") String password
    );

    @GET("insert_multiple_tourspot_deaps")
    Single<Integer> multipleInserDeaps(@Query("user_idx") int user_idx,
                                       @Query("location_idx") int location_idx);

    @GET("delete_multiple_tourspot_deaps")
    Single<Integer> multipleDelDeaps(@Query("user_idx") int user_idx,
                                         @Query("location_idx") int location_idx);

    /*@GET("insert_multiple_tourspot_deaps")
    Call<Integer> multipleInserDeaps(@Query("user_idx") int user_idx,
                                @Query("location_idx") int location_idx);*/

    /*@GET("delete_multiple_tourspot_deaps")
    Call<Integer> multipleDelDeaps(@Query("user_idx") int user_idx,
                                @Query("location_idx") int location_idx);*/

    @GET("getTourist")
    Call<List<Tour_Spot>> getTour(@Query("userIdx") int idx);

    @GET("getFourLocations")
    Call<List<TopFourLocationModel>> getFourLocations(@Query("userIdx") int idx);

    @GET("getHashTag")
    Call<List<TourTagModelDC>> getHashTag();

    //메인 시작시 가져오는 Location 등 데이터
    //@FormUrlEncoded
    @GET("getTourist_OrderBy")
    Call<List<Tour_Spot>> getTourOrderBy(@Query("userIdx") int idx);

    @GET("getTourist_location_for_spot")
    Call<List<RallyMapModel>> getTourLocation_for_spot(@Query("user_idx") int user_idx,
                                                       @Query("location_idx") int location_idx);

    @GET("getTourist_spotpoint")
    Call<List<TouristSpotPointDC>> getTourLocation_spotpoint(@QueryMap Map<String, Integer> map);

    @GET("getTourist_sortTag")
    Call<List<Tour_Spot>> getTourSortHashTag(@Query("userIdx") int idx);

    @GET("getTour_participants")
    Call<List<Map<String, Integer>>> getTourParticipants();

    @GET("getNotice")
    Call<List<Notice>> getNotice();

    @GET("select_interest")
    Call<List<Tour_Spot2>> getSelect_interest(@Query("user_idx") int user_idx);

    @GET("getAllInterest")
    Call<List<InterestModel>> getAllInterest(@Query("user_idx") int user_idx);

    @GET("insert_intest")
    Call<Integer> insert_intest(@Query("interest_user_idx") int user_idx, @Query("interest_touristspot_idx") int touristspot_idx);

    @GET("select_interest_status")
    Call<Integer> select_interest_status(@Query("interest_user_idx") int user_idx, @Query("interest_touristspot_idx") int touristspot_idx);

    @GET("getLocations")
    Call<List<Location>> getLocations();

    @GET("delete_interest")
    Call<Integer> remove_intest(@Query("interest_idx") int interest_idx,
                                @Query("ts_type") int ts_type);


    @GET("gabia_token")
    Call<String> getToken();

    @GET("phonenum_authorization")
    Call<String> getAutoNumber( @Query("user_phone") String phone, @Query("access_token") String token);

    @GET("getdistance")
    Call<TouristSpotPoint> getDistance( @Query("tag_info") String taggin_info);

    @GET("check_in")
    Call<HashMap<String, Integer>> check_in(@Query("taggin_info") String taggin_info, @Query("userIdx") String userIdx, @Query("user_phone") String user_phone);

    @GET("select_success_data")
    Call<RallyMapDTO> select_success_data( @Query("touristspotpoint_idx") String touristspotpoint_idx);

    @GET("select_history_spot")
    Call<List<history_spotModel2>> getHistorySpot(@Query("user_idx") int user_idx);

    @GET("select_visit_count")
    Call<List<VisitCountModel>> getVisitCount(@Query("user_idx") int user_idx);


    @GET("insert_writeReview")
    Call<Integer> insert_writeReview(@Query("review_user_user_idx") int user_idx,
                                     @Query("review_touristspot_touristspot_idx") int touristspot_idx,
                                     @Query("review_score") float review_score,
                                     @Query("review_contents") String review_contents
    );

    @GET("select_all_review")
    Single<List<AllReviewModel>> select_all_review();

    @GET("select_review_detail")
    Single<ReviewDetailDC> select_review_detail(@Query("review_idx") int review_idx,
                                                @Query("user_idx") int user_idx);

    @GET("select_review_comments")
    Single<List<ReveiwCommentsDC>> select_review_comments(@Query("review_idx") int review_idx);

    @GET("inordel_review_interest")
    Single<Integer> inordel_review_interest(@Query("review_interest_review_idx") int review_idx,
                                            @Query("review_interes_user_idx") int user_idx);


    @GET("insert_review_comment")
    Single<Integer> insert_review_comment(@Query("review_comment_review_idx") int review_idx,
                                          @Query("review_comment_user_idx") int user_idx,
                                          @Query("review_comment_content") String comment);

    @GET("userwithdrawal")
    Single<Integer> userwithdrawal(@Query("user_idx") int user_idx);

    @GET("deleteReview")
    Single<Integer> deleteReview(@Query("review_idx") int review_idx);

    @GET("updateReview")
    Single<Integer> updateReview(
            @Query("review_score") float review_score,
            @Query("review_contents") String review_contents,
            @Query("review_idx") int review_idx
    );

    @GET("selectCoupon")
    Single<CouponModel> selectCoupon(
            @Query("coupon_touristspot_idx") int coupon_touristspot_idx,
            @Query("coupon_user_idx") int coupon_user_idx
    );

    @GET("selectAllStore")
    Single<List<StoreModel>> selectAllStore();

    @GET("selectStoreDetail")
    Single<StoreSubDTO> selectStoreDetail(
            @Query("store_idx") int store_idx
    );

    @GET("selectTourSpotPointImages")
    Single<List<TouristSpotPointImg>> selectTourSpotPointImages(
            @Query("touristspotpoint_idx") int touristspotpoint_idx
    );

}