package com.Toffee.Wallet.utils;

import static com.ads.androidsdk.sdk.util.Securee.getDotCount;
import static com.ads.androidsdk.sdk.util.Securee.isDevMode;
import static com.ads.androidsdk.sdk.util.Tool.vpn;
import static com.Toffee.Wallet.utils.Const.REFER_CODE;
import static com.Toffee.Wallet.utils.Const.nativeAdded;
import static com.Toffee.Wallet.utils.Const.nativeList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AlertDialog;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.ads.androidsdk.sdk.util.Em;
import com.ads.androidsdk.sdk.util.InputValidation;
import com.ads.androidsdk.sdk.util.Securee;
import com.Toffee.Wallet.BuildConfig;
import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.DefListResp;
import com.Toffee.Wallet.databinding.LayoutAlertBinding;
import com.Toffee.Wallet.databinding.LayoutCollectBonusBinding;
import com.Toffee.Wallet.databinding.LayoutGlobalMsgBinding;
import com.Toffee.Wallet.databinding.LayoutProgressLoadingBinding;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import de.mateware.snacky.Snacky;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;


public class Fun {

	public static boolean isConnected(Context ctx) {
		ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo nInfo = cm.getActiveNetworkInfo();

		boolean isConnected = nInfo != null && nInfo.isConnectedOrConnecting();
		if(isConnected){
			return true;
		}else {
			return false;
		}

	}

	public static String coolNumberFormat(long count) {
		if (count < 1000) return "" + count;
		int exp = (int) (Math.log(count) / Math.log(1000));
		DecimalFormat format = new DecimalFormat("0.#");
		String value = format.format(count / Math.pow(1000, exp));
		return String.format("%s%c", value, "kMBTPE".charAt(exp - 1));
	}

	public static String formatNumber(String myNum) {
		char[] str = myNum.toCharArray();
		int numCommas = str.length / 3;
		char[] formattedStr = new char[str.length + numCommas];

		for(int i = str.length - 1, j = formattedStr.length - 1, cnt = 0; i >= 0 && j >=0 ;) {
			if(cnt != 0 && cnt % 3 == 0 && j > 0) {
				formattedStr[j] = ',';
				j--;
			}

			formattedStr[j] = str[i];
			i--;
			j--;
			cnt++;
		}
		return String.valueOf(formattedStr);
	}


	public static String deviceId(Context ctx) {
		String android_id = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
		String deviceId = md5(android_id).toUpperCase();
		return deviceId;
	}

	private static int getRandomSalt() {
		Random random = new Random();
		return random.nextInt(900);
	}

	public static void log(String msg){
		if(BuildConfig.DEBUG){
			Timber.w(msg);
		}
	}

	public static String md5(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes());
			byte messageDigest[] = digest.digest();
			StringBuilder hexString = new StringBuilder();
			for (int i=0; i<messageDigest.length; i++)
				hexString.append(String.format("%02x", messageDigest[i]));
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static androidx.appcompat.app.AlertDialog loading(Context context) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(LayoutInflater.from(context).inflate(R.layout.layout_loading, null)).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}
		return dialog;
	}

	public static androidx.appcompat.app.AlertDialog Alerts(Context context) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(LayoutInflater.from(context).inflate(R.layout.layout_alert, null)).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}

		return dialog;
	}

	public static androidx.appcompat.app.AlertDialog Alerts(Context context, LayoutAlertBinding lytAlert) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(lytAlert.getRoot()).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}
		return dialog;
	}

	public static androidx.appcompat.app.AlertDialog GlobalMsg(Context context, LayoutGlobalMsgBinding lyt) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(lyt.getRoot()).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}
		return dialog;
	}

	public static androidx.appcompat.app.AlertDialog addToWallet(Context context, LayoutCollectBonusBinding lyt) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(lyt.getRoot()).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}
		return dialog;
	}

	public static  String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		if(finalTimerString.startsWith("0:") || finalTimerString.startsWith("00:")){
			return finalTimerString+" Sec";
		}
		return finalTimerString+" Min";
	}


	public static androidx.appcompat.app.AlertDialog loadingProgress(Context context) {
		androidx.appcompat.app.AlertDialog dialog = new AlertDialog.Builder(context).setView(LayoutInflater.from(context).inflate(R.layout.layout_progress_loading, null)).create();
		dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
		dialog.getWindow().setWindowAnimations(R.style.Dialoganimation);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		Window w = dialog.getWindow();
		if (w != null) {
			w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			w.setNavigationBarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
		}
		return dialog;
	}


	public static void hideKeyboard(View view, Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void alert(Activity context,String message) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("Info")
				.setMessage(message)
				.setNegativeButton("ok", (dialog1, which) -> dialog1.dismiss())
				.create();
		dialog.show();
	}

	public static void alert2(Activity context,String title,String message) {
		AlertDialog dialog = new AlertDialog.Builder(context)
				.setTitle("Info")
				.setMessage(message)
				.setCancelable(false)
				.create();
		dialog.show();
	}

	@SuppressLint("SetTextI18n")


	public static String encryption(String coded){
		byte[] encodeValue = Base64.decode(coded.getBytes(), Base64.DEFAULT);
		return new String(encodeValue);
	}


	public static String encrypt_code(String coded){
		byte[] encodeValue = Base64.encode(coded.getBytes(), Base64.DEFAULT);
		return new String(encodeValue);
	}


	public static long timeStringtoMilis(String time) {
		long milis = 0;

		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sd.parse(time);
			milis = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return milis;
	}

	public static String getFormatedDate(String date_str) {
		if (date_str != null && !date_str.trim().equals("")) {
			SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm");
			try {
				String newStr = newFormat.format(oldFormat.parse(date_str));
				return newStr;
			} catch (ParseException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public static String getFormatedDateSimple(String date_str) {
		if (date_str != null && !date_str.trim().equals("")) {
			SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
			try {
				String newStr = newFormat.format(oldFormat.parse(date_str));
				return newStr;
			} catch (ParseException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public static String getFormatedDateWithoutTimestamp(String date_str) {
		if (date_str != null && !date_str.trim().equals("")) {
			SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
			try {
				String newStr = newFormat.format(oldFormat.parse(date_str));
				return newStr;
			} catch (ParseException e) {
				return "";
			}
		} else {
			return "";
		}
	}

	public static void launchCustomTabs(Activity activity, String url) {
		CustomTabsIntent.Builder customIntent = new CustomTabsIntent.Builder();
		customIntent.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
		customIntent.setExitAnimations(activity,R.anim.exit,R.anim.enter);
		customIntent.setStartAnimations(activity,R.anim.enter,R.anim.exit);
		customIntent.setUrlBarHidingEnabled(true);
		customIntent.build().launchUrl(activity, Uri.parse(url));


	}

	public static String js(Activity a,String type , String userid, String reward, String tag,String id){
		JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new JsonObject());
		jsObj.addProperty("type",type);
		jsObj.addProperty("id",id);
		jsObj.addProperty("im",encrypt_code(userid));
		jsObj.addProperty("reward",reward);
		jsObj.addProperty("tag",tag);
		try {
			return InputValidation.parse(a,encrypt_code(jsObj.toString()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static String d(Activity a,String name, String email, String password, String person_id, String type, String p_token){
		JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new JsonObject());
			jsObj.addProperty("name",name);
			jsObj.addProperty("email",email);
			jsObj.addProperty("password",password);
			jsObj.addProperty("person_id",person_id);
			jsObj.addProperty("p_token",p_token);
			jsObj.addProperty("type",type);
		try {
			return InputValidation.parse(a,encrypt_code(jsObj.toString()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String sec(Activity a,String photo,boolean checkapp){
		JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new JsonObject());
		jsObj.addProperty("profile",photo);
		jsObj.addProperty("root", Securee.checkRoot(a));
		jsObj.addProperty("vpn",vpn());
		jsObj.addProperty("clone",checkapp);
		jsObj.addProperty("devMode",isDevMode(a));
		jsObj.addProperty("termux",Securee.termux(a));
		jsObj.addProperty("ref",REFER_CODE);
		jsObj.addProperty("emu", Em.Em(a));
		jsObj.addProperty("token",Fun.deviceId(a));

		try {
			return InputValidation.parse(a,encrypt_code(jsObj.toString()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public static boolean checkApp(String path) {
		int count = getDotCount(path);
		return count > 3;
	}

	public static String generateRandomString(String resp,String refid) {
		String rep=refid.trim();
		return resp.replace(rep,"");
	}

	public static void ClaimBonus(Activity ctx, String msg) {
		MediaPlayer mMediaPlayer = MediaPlayer.create(ctx, R.raw.coin_sound);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setLooping(false);
		mMediaPlayer.start();

		Snacky.builder()
				.setIcon(R.drawable.ic_small_coin)
				.setActivity(ctx)
				.setText(msg)
				.setTextColor(ctx.getResources().getColor(R.color.white))
				.setTextSize(18f)
				.setDuration(6000)
				.success()
				.show();

		if(!mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer=null;
		}
	}

	public static void msgError(Activity ctx, String msg) {
//		if(App.isDebugMode){
//			Fun.alert(ctx,msg);
//		}
		Snacky.builder()
				.setActivity(ctx)
				.setText(msg)
				.setTextColor(ctx.getResources().getColor(R.color.white))
				.setTextSize(18f)
				.setDuration(3000)
				.warning()
				.show();
	}

	public static void msgSuccess(Activity ctx, String msg) {
		Snacky.builder()
				.setActivity(ctx)
				.setText(msg)
				.setTextColor(ctx.getResources().getColor(R.color.white))
				.setTextSize(18f)
				.setDuration(6000)
				.success()
				.show();
	}

    public static void statusbar(Activity activity) {
		activity.getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

	public static void loadNative(Activity activity) {
		if(nativeAdded){
			try {
				Objects.requireNonNull(ApiClient.restAdapter(activity)).create(ApiInterface.class).getNative().enqueue(new Callback<List<DefListResp>>() {
					@Override
					public void onResponse(Call<List<DefListResp>> call, Response<List<DefListResp>> response) {
						if(response.isSuccessful() && response.body().size()!=0){
							nativeList.addAll(response.body());
							nativeAdded=false;
						}
					}

					@Override
					public void onFailure(Call<List<DefListResp>> call, Throwable t) {

					}
				});
			}catch (Exception e){}
		}
	}
}
