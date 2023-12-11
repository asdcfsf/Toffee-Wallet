package com.Toffee.Wallet.restApi;

import com.Toffee.Wallet.Responsemodel.ContestResp;
import com.Toffee.Wallet.Responsemodel.AppsResp;
import com.Toffee.Wallet.Responsemodel.BannerResp;
import com.Toffee.Wallet.Responsemodel.CoinStoreResponse;
import com.Toffee.Wallet.Responsemodel.DefResp;
import com.Toffee.Wallet.Responsemodel.FaqResp;
import com.Toffee.Wallet.Responsemodel.GameResp;
import com.Toffee.Wallet.Responsemodel.HistoryResp;
import com.Toffee.Wallet.Responsemodel.LeaderboardResp;
import com.Toffee.Wallet.Responsemodel.LoginResp;
import com.Toffee.Wallet.Responsemodel.MissionResponse;
import com.Toffee.Wallet.Responsemodel.OfferwallResp;
import com.Toffee.Wallet.Responsemodel.RewardCatResp;
import com.Toffee.Wallet.Responsemodel.RewardResp;
import com.Toffee.Wallet.Responsemodel.SettingResp;
import com.Toffee.Wallet.Responsemodel.SubscriptionResp;
import com.Toffee.Wallet.Responsemodel.SupportResp;
import com.Toffee.Wallet.Responsemodel.TaskResp;
import com.Toffee.Wallet.Responsemodel.VideowallResp;
import com.Toffee.Wallet.Responsemodel.DefListResp;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(WebApi.Api.Login)
    Call<LoginResp> Login(@Field("data") String data, @Field("secure") String secure);

    @FormUrlEncoded
    @POST(WebApi.Api.Signup)
    Call<LoginResp> Signup(@Field("data") String data, @Field("secure") String secure);

    @FormUrlEncoded
    @POST(WebApi.Api.Ap)
    Call<DefResp> api(@Field("data") String data);

    @FormUrlEncoded
    @POST(WebApi.Api.Ap)
    Call<List<TaskResp>> apiPromo(@Field("data") String data);

    @FormUrlEncoded
    @POST(WebApi.Api.getPromoBanner)
    Call<BannerResp> promoBanner(@Field("f") String d);

    @GET(WebApi.Api.getSubscriptions)
    Call<SubscriptionResp> getSubscriptions();

    @GET(WebApi.Api.getPaymentGateway)
    Call<List<DefListResp>> getPaymentGateway();

    @GET(WebApi.Api.refer_achivement)
    Call<ContestResp> getReferAchivement(@Path("id") String id, @Path("limit") int limit);

    @GET(WebApi.Api.getContest)
    Call<ContestResp> getContest(@Path("id") String id, @Path("limit") int limit);

    @GET(WebApi.Api.social_links)
    Call<List<DefListResp>> getSocialLink();

    @GET(WebApi.Api.refer_scratch_card)
    Call<DefResp> getReferScratch(@Path("id") String id, @Path("limit") int limit);

    @GET(WebApi.Api.WEB)
    Call<List<TaskResp>> getWebLink();

    @GET(WebApi.Api.getNative)
    Call<List<DefListResp>> getNative();

    @GET(WebApi.Api.VIDEOS)
    Call<List<TaskResp>> apivideo();

    @GET(WebApi.Api.videowall)
    Call<VideowallResp> videowall();

    @GET(WebApi.Api.GAMES)
    Call<GameResp> funGame(@Path("game_type") String game_type, @Path("limit") String limit);

    @GET(WebApi.Api.APPS)
    Call<List<AppsResp>> cpaoffers();

    @GET(WebApi.Api.APPS_PROGRESS)
    Call<List<AppsResp>> cpaprogress();


    @GET(WebApi.Api.HISTORY)
    Call<List<HistoryResp>> History(@Path("id") String id);

    @GET(WebApi.Api.REWARD_HISTORY)
    Call<List<HistoryResp>> RewardHistory(@Path("id") String id);

    @GET(WebApi.Api.REWARD_BY_CATEGORY)
    Call<List<RewardCatResp>> getRewardCategory();

    @GET(WebApi.Api.REWARDS_BY_ID)
    Call<RewardResp> getRewardbyID(@Path("id") String id);

    @GET(WebApi.Api.HOME_BANNER)
    Call<BannerResp> PromoBanner();

    @GET
    Call<ResponseBody> getApi(@Url String url);

    @GET(WebApi.Api.FAQ)
    Call<List<FaqResp>> Faqs(@Path("type") String type);

    @GET(WebApi.Api.supportTicket)
    Call<List<SupportResp>> supportTicket();

    @FormUrlEncoded
    @POST(WebApi.Api.create_support_ticket)
    Call<DefResp> createSupportTicket(@Field("email") String email,
                                      @Field("subject") String subject,
                                      @Field("message") String message);
    //old

    @GET(WebApi.Api.APPS)
    Call<AppsResp> getOffers();

    @GET(WebApi.Api.USER_NOTI)
    Call<List<DefListResp>> getNoti(@Path("id") String userid);

    @GET(WebApi.Api.GLOBAL_MSG)
    Call<List<DefListResp>> getGlobalMsg(@Path("id") String userid);

    @GET(WebApi.Api.dailyoffer)
    Call<List<TaskResp>> getDailyoffer();

    @GET(WebApi.Api.DAILY_MISSION)
    Call<MissionResponse> dailyMission(@Path("id") String id);

    @FormUrlEncoded
    @POST(WebApi.Api.VERIFY_OTP)
    Call<DefResp> Verify_OTP(@Field("otp") String otp,
                             @Field("email") String email);

    @POST(WebApi.Api.VERIFY_EMAIL)
    Call<DefResp> VerifyEmail(@Query("email") String email);

    @POST(WebApi.Api.RESET_PASSWORD)
    Call<DefResp> ResetPass(@Query("email") String email,
                            @Query("type") String type);

    @FormUrlEncoded
    @POST(WebApi.Api.UPDATE_PASSWORD)
    Call<DefResp> UpdatePass(@Field("email") String email,
                             @Field("password") String password,
                             @Field("otp") String otp);


    @GET(WebApi.Api.FETCH_OFFERWALLS)
    Call<OfferwallResp> Fetch_offerwalls(@Path("type") String type);

    @GET(WebApi.Api.STORELIST)
    Call<CoinStoreResponse> StoreList();

    @FormUrlEncoded
    @POST(WebApi.Api.ABOUT)
    Call<SettingResp> getSetting(@Field("field") String f);

    @GET(WebApi.Api.LEADERBOARD)
    Call<LeaderboardResp> getLeaderbord(@Path("type") String type);

    @GET(WebApi.Api.LANGUAGE)
    Call<List<DefListResp>> getLanguage();

    @GET(WebApi.Api.getReferralHistory)
    Call<LeaderboardResp> getReferralHistory();


    @FormUrlEncoded
    @POST(WebApi.Api.CREATE_PROMO)
    Call<DefResp> createPromo(@Field("id") String id,
                              @Field("type") String type,
                              @Field("title") String title,
                              @Field("url") String url,
                              @Field("thumb") String thumb,
                              @Field("limit") String limit);

    @Multipart
    @POST(WebApi.Api.submit_dailyoffer)
    Call<DefResp> submitWithAttach(@Part MultipartBody.Part part,
                                   @Query("link") String link,
                                   @Query("id") String id);

    @Multipart
    @POST(WebApi.Api.submit_payment_proof)
    Call<DefResp> submitPayment(@Part MultipartBody.Part part,
                                @Query("model_id") String model_id,
                                @Query("model_type") String model_type,
                                @Query("trans_id") String trans_id,
                                @Query("type") String type,
                                @Query("id") String id);


    @POST(WebApi.Api.submit_dailyoffer)
    Call<DefResp> submit(@Query("link") String link,
                         @Query("id") String id);

    @FormUrlEncoded
    @POST(WebApi.Api.verifyPay)
    Call<DefResp> verifyPay(@Field("data") String data);

}
