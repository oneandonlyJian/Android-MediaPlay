package com.diggoods.api.only_one;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Create by  FengJianyi on 2019/5/20
 */
public interface ApiService {

    //请求下载视频地址
    @Headers("Cache-Control:public,max-age=120")
    @GET("searchMusic")
    Observable<MyBean> getQuery(@Query("name") String name);

}
