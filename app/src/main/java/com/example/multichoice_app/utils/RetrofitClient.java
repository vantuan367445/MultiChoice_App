package com.example.multichoice_app.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    public static  Retrofit getClient(String baseUrl){
        OkHttpClient builder = new OkHttpClient.Builder()
                .connectTimeout(7000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
        // thoi gian doc du lieu trong 5s // dinh dang kieu  MILLISECONDS
                .writeTimeout(5000,TimeUnit.MILLISECONDS)

                .retryOnConnectionFailure(true)
                .build();
        // nếu như connection bị trục chặc thì nó sẽ khôi phục kết nối đó và thử lại
        Gson gson = new GsonBuilder().setLenient().create();

        // khởi tạo 1 cái URL
        // kiểm soát cài đặt builder ở trên

        return new Retrofit.Builder()
                .baseUrl(baseUrl) // khởi tạo 1 cái URL
                .client(builder) // kiểm soát cài đặt builder ở trên
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
}
