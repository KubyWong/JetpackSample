package com.bluetree.jetpacksample.utils.http;

import com.bluetree.jetpacksample.bean.SamMusicResponeBean;
import com.bluetree.jetpacksample.bean.SamUserBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * 接口类
 * 参数定义参考资料：
 * https://www.jianshu.com/p/308f3c54abdd/
 * http://square.github.io/retrofit/
 */
public interface BytePayApiService {

//    免费接口https://www.jianshu.com/p/e6f072839282
    @GET("users/{user}/repos")
    Call<List<SamUserBean>> listRepos(@Path("user") String user);

    /**
     * ?page=1&count=2&type=video
     * @param params
     * @return
     */
    @GET("/getJoke")
    Call<ResponseBody> getSimpleString(@QueryMap Map<String, String> params);

    /**
     * retrofit方式回调
     * https://api.apiopen.top/searchMusic?name=你好
     * @return
     */
    @GET("/searchMusic")
    Call<ResponseBody> searchMusic(@Query("name") String name);

    /**
     * rxjava的方式回调
     * @param name
     * @return
     */
    @GET("/searchMusic")
    Observable<Result<ResponseBody>> searchMusicRx(@Query("name") String name);

//    @GET("getSongPoetry")//获取宋词接口
//    Observable<Result<MusicBean>> getSongPoerty(@Query("page") int page, @Query("count") int count);

    @GET("getSongPoetry")//获取宋词接口
    Observable<Result<ResponseBody>> getSongPoerty(@Query("page") int page, @Query("count") int count);

    @GET("getTangPoetry")
    Observable<Result<ResponseBody>> getTangPoerty(@Query("page") int page, @Query("count") int count);


    /**
     * ?page=1&count=2&type=video
     * @param params
     * @return
     */
    @GET("/getJoke")
    Call<SamMusicResponeBean> getVideoBean(@QueryMap Map<String, String> params);
}
