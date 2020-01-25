package com.firstteamout.farmershopping.`interface`

import com.firstteamout.farmershopping.POKO.*
import retrofit2.Call
import retrofit2.http.*

interface FarmerClient {

    @FormUrlEncoded
    @POST("/api/register")
    fun registerUser(@Field("uid") uid: String, @Field("name") name: String, @Field("email") email: String, @Field("password") password: String) : Call<ResponseMessage>

    @FormUrlEncoded
    @POST("/api/check-reward")
    fun validateReferralCode(@Field("uid") uid: String, @Field("referral_code") referral_code: String) : Call<ResponseMessage>

    @GET("/api/GetAllServices")
    fun getAllServices(): Call<Service>

    @GET("/api/topUsers")
    fun getTopUsersList(): Call<Users>

    @GET("/api/user/{uid}")
    fun getSingleUser(@Path("uid") uid: String): Call<data>

    @GET("/api/store")
    fun getAllStoresProduct() : Call<Products>

    @FormUrlEncoded
    @POST("/api/checkServicePurchased")
    fun checkServicePurchased(@Field("service_id") serviceid: String, @Field("uid") uid:String) : Call<ResponseMessage>

    @FormUrlEncoded
    @POST("/api/getSingleService")
    fun getSingleService(@Field("service_id") service_id: String): Call<SingleService>

    @FormUrlEncoded
    @POST("/api/redeemService")
    fun redeemService(@Field("service_id") service_id: String, @Field("uid") uid: String): Call<ResponseFlagMessage>

    @FormUrlEncoded
    @POST("/api/product")
    fun getSingleProduct(@Field("id")id: String): Call<SingleProduct>

    @FormUrlEncoded
    @POST("/api/buyProduct")
    fun buyProduct(@Field("uid") uid: String, @Field("product_id") product_id: String, @Field("product_price")product_price: Int): Call<ResponseData>
}