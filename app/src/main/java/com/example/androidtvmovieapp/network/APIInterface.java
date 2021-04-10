package com.example.androidtvmovieapp.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {
    String API_DOMAIN = "https://api.themoviedb.org/3/";
    String MOVIE = "trending/movie/";
    String POSTER_URL = "http://image.tmdb.org/t/p/w500";
    String BACKDROP_URL = "http://image.tmdb.org/t/p/w1280";
    String VIDEO_URL = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/";


    @GET(API_DOMAIN + MOVIE + "week?")
    public Call<ResponseBody> getMovieData(@Query("api_key") String api_key,
                                           @Query("page") String pageNo);
}
