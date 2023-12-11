package com.Toffee.Wallet.restApi;
import static com.Toffee.Wallet.utils.Const.BU;

public interface WebApi {

    public interface Api{
         String BASE= BU;
         String API= "api/auth/";
         String Login =API+ "login";
         String Signup =API+ "signup";
         String Ap =API+ "api";
         String getPromoBanner =API+ "promoBanner";
         String getSubscriptions =API+ "subscriptions";
         String getPaymentGateway =API+ "payment_gateway";
         String refer_achivement =API+ "refer_achivement/{id}/{limit}";
         String getContest =API+ "getContest/{id}/{limit}";
         String social_links =API+ "social_links";
         String refer_scratch_card =API+ "refer_scratch_card/{id}/{limit}";

         String IMAGES=BASE +"images/";
         String LANGUAGE=API+ "getLanguage";
         String GAME_THUMB= BASE +"images/games/";
         String USERTHUMB= BASE+"images/user/";
         String HISTORY =API+"History/{id}";
         String REWARD_HISTORY =API+"rewardHistory/{id}";
         String Quiz_CATEGORY =API+"funQuizcat";
         String REWARD_BY_CATEGORY =API+"rewardCat";
         String REWARDS_BY_ID =API+"funrewards/{id}";



         String GLogin =API+ "gloin";
         String SIGNUP = API+ "signup";
         String verifyPay = API+ "verifyPay";
         String submit_dailyoffer = API+ "submit_dailyoffer";
         String submit_payment_proof = API+ "submit_payment_proof";
         String APPINFO =API+"appinfo";
         String CREATE_PROMO =API+"createPromo";
         String USER_COIN =API+"user_coin";
         String LEADERBOARD =API+"leaderboard/{type}";
         String getReferralHistory =API+"referral_History";
         String PROMO_REPORT =API+"promo_report";
         String WEB =API+"ofWeb";
         String getNative =API+"getNative";
         String GAMES =API+"funGame/{game_type}/{limit}";
         String FAQ =API+"faq/{type}";
         String supportTicket =API+"supportTicket";
         String create_support_ticket =API+"create_ticket";
         String PROMO_COUNT =API+"promo_count";
         String FETCH_OFFERWALLS =API+"funOfferwalls/{type}";
         String PROMO_APP =API+"promo_app";
         String PROMO_VIDEO =API+"promo_video";
         String PROMO_WEB =API+"promo_web";
         String DAILY_MISSION =API+"daily_mission/{id}";
         String VIDEOS =API+"ofVideo";
         String videowall =API+"videowall";
         String HOME_BANNER =API+"popupBanner/";
         String SLIDE_BANNER =API+"Homebanner";
         String ABOUT ="api/config";
         String PROMO_INFO =API+"promoinfo";
         String COLLECT_BONUS =API+"collect_bonus";
         String APPS =API+"ofCustom";
         String USER_NOTI =API+"user_noti/{id}";
         String GLOBAL_MSG =API+"global_msg/{id}";
         String dailyoffer =API+"dailyoffer";
         String APPS_PROGRESS =API+"ofCustomProgress";
         String RESET_PASSWORD ="api/reset_password";
         String VERIFY_EMAIL ="api/send_otp";
         String VERIFY_OTP ="api/verify_otp";
         String UPDATE_PASSWORD ="api/update_password";
         String UPDATE_PTOKEN =API+"updatetoken";

        String STORELIST =API+"coinstores";
        String UPDATE_TRANS =API+"update_trans";

    }
}
