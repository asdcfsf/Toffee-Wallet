package com.Toffee.Wallet.restApi;

import static com.Toffee.Wallet.utils.Const.A;
import static com.Toffee.Wallet.utils.Const.AB;
import static com.Toffee.Wallet.utils.Const.BU;
import static com.Toffee.Wallet.utils.Const.randomCode;

import android.content.Context;
import android.util.Log;

import com.Toffee.Wallet.App;

import com.Toffee.Wallet.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public  static Retrofit retrofit;
    private static final String CACHE_CONTROL = "Cache-Control";

    public static Retrofit restAdapter(Context activity){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        if(retrofit==null){
            OkHttpClient.Builder oktHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new NetworkConnectionInterceptor(activity));

            if(BuildConfig.DEBUG){
                oktHttpClient.addInterceptor(logging);
            }

            oktHttpClient
                    .addInterceptor(chain -> {
                        Request request;
                        request  = chain.request().newBuilder()
                                .header("Accept", "application/json")
                                .addHeader(A ,AB+ randomCode)
                                .build();
                        return chain.proceed(request);
                    })
                    .addNetworkInterceptor( provideCacheInterceptor() )
                    .cache( provideCache() )
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            Gson gson= new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();

                retrofit= new Retrofit.Builder()
                        .baseUrl(BU)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(oktHttpClient.build())
                        .build();
            }

//        if(vpn()){
//            Toast.makeText(activity, "Do not Use VPN", Toast.LENGTH_SHORT).show();
//            return null;
//        }else {
            return retrofit;
//        }
    }

    private static Cache provideCache ()
    {
        Cache cache = null;
        try
        {
            cache = new Cache( new File( App.getInstance().getCacheDir(), "rewarda-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e)
        {
            Log.e( "e", "Could not create Cache!" );
        }
        return cache;
    }

    public static Interceptor provideCacheInterceptor ()
    {
        return chain -> {
            Response response = chain.proceed( chain.request() );
            // re-write response header to force use of cache
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge( 2, TimeUnit.SECONDS )
                    .build();
            return response.newBuilder()
                    .header( CACHE_CONTROL, cacheControl.toString() )
                    .build();
        };
    }


}
