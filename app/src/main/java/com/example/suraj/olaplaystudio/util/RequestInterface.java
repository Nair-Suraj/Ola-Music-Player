package com.example.suraj.olaplaystudio.util;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by suraj on 16/12/17.
 */

public interface RequestInterface {
    @GET("studio")
    Call<ArrayList<MusicItem>> getJSon();
}
