package com.bluetree.jetpacksample.utils.http;

import android.text.TextUtils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {
    public static final String BASE_URL = "https://api.apiopen.top/";
    private static Retrofit rf1;
    private static BytePayApiService allApiInCompany;

    /**
     * 默认使用公司的域名
     * @return
     */
    public static Retrofit getMyRetrofit() {
        if (rf1 == null) {
            rf1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return rf1;
    }

    /**
     * 所有接口
     * @return
     */
    public static BytePayApiService getAllApiInCompany() {
        if (allApiInCompany == null) {
            allApiInCompany = getMyRetrofit().create(BytePayApiService.class);
        }
        return allApiInCompany;
    }


    /**
     * 创建自定义的
     * @param url
     * @return
     */
    public static Retrofit getMyRetrofit(String url) {
        if(TextUtils.isEmpty(url)) return getMyRetrofit();
        Retrofit rf = new Retrofit.Builder()
                .baseUrl(url)
                .build();
        return rf;
    }



    /*class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }
    interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo);
    }
    private void sample() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        GitHub github = retrofit.create(GitHub.class);

        // Create a call instance for looking up Retrofit contributors.
        Call<List<Contributor>> call = github.contributors("square", "retrofit");

        // Fetch and print a list of the contributors to the library.
        List<Contributor> contributors = call.execute().body();
    }
*/
}
