package com.example.multichoice_app.communiti;

public class APIUtills {
    public static  final String baseURL ="http://ec2-54-151-187-248.ap-southeast-1.compute.amazonaws.com/multiplechoice_webserver/";
//    public static  final String baseURL ="https://androidthunder2021.000webhostapp.com/";
    //  dấu  / ở cuối là lấy file ở folder gốc retrofit_sinhvien

    // nhập và gửi dữ liệu đi
    public static DataClient getData(){
        return  RetrofitClient.getClient(baseURL).create(DataClient.class);
    }
}
