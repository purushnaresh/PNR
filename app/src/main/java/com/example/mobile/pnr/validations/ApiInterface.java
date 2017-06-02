package com.example.mobile.pnr.validations;
/**
 * Created by pnr on 06/02/2017.
 */
import com.example.mobile.pnr.Models.AddValue;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Mobtech-02 on 6/2/2017 5:01 PM.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("AddValue.ashx ")
     Call<AddValue> addCountry(@Field("ValueName") String userId, @Field("ValueID") String ValueID,
                               @Field("mode") int mode);

}
