package com.superchat.retrofit.api;


import android.content.Context;
import android.support.annotation.NonNull;

import com.superchat.helper.UtilGlobal;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static ApiClient uniqInstance;
    private final String URL_LIVE = "http://api.voizd.com/";

    private VoizdInterface apiMazkara;

    public static synchronized ApiClient getInstance() {
        if (uniqInstance == null) {
            uniqInstance = new ApiClient();
        }
        return uniqInstance;
    }

    private void ApiClient(@NonNull final Context currContext, String CHANGE_BASE_URL) {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor headerInterceptor = new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    String ClientProperty = Utility.getApiHeader(currContext);
                    Request original = chain.request();

                    Request.Builder builder = original.newBuilder();
                    builder.header("ClientProperty", "" + ClientProperty)
                            .header("Content-Type", "multipart/form-data")
                            .method(original.method(), original.body());

                    String token = UtilGlobal.getGCMTOken(currContext);
                    if (token != null) {
                        builder.header("deviceId", "" + token);
                    }

                    String authparams = Utility.getAuthParams(currContext);
                    builder.header("authparams", "" + authparams);

                    Request request = builder.build();

                    return chain.proceed(request);
                }
            };

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    //.addInterceptor(headerInterceptor)
                    .addInterceptor(logging)
                    .build();
            //httpClient.networkInterceptors().add(headerInterceptor);

            String API_URL = URL_LIVE;
            if (CHANGE_BASE_URL != null && CHANGE_BASE_URL.trim().length() > 0) {
                API_URL = CHANGE_BASE_URL;
            }

            // <-- this is the important line!
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

            apiMazkara = retrofit.create(VoizdInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VoizdInterface getApi(Context currContext) {
        if (uniqInstance == null) {
            getInstance();
        }
        uniqInstance.ApiClient(currContext, null);

        return apiMazkara;
    }


    public VoizdInterface getApi(Context currContext, String CHANGE_BASE_URL) {
        if (uniqInstance == null) {
            getInstance();
        }
        uniqInstance.ApiClient(currContext, CHANGE_BASE_URL);

        return apiMazkara;
    }
}
