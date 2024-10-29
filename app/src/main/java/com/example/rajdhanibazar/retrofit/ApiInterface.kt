package com.example.rajdhanibazar.retrofit


import com.example.rajdhanibazar.data.AddBid
import com.example.rajdhanibazar.data.Balance
import com.example.rajdhanibazar.data.BidResponse
import com.example.rajdhanibazar.data.FetchUser
import com.example.rajdhanibazar.data.FetchUserResponse
import com.example.rajdhanibazar.data.FullSangamData
import com.example.rajdhanibazar.data.FullSangamResponse
import com.example.rajdhanibazar.data.Gali
import com.example.rajdhanibazar.data.GaliResponse
import com.example.rajdhanibazar.data.GameRate
import com.example.rajdhanibazar.data.GameResult
import com.example.rajdhanibazar.data.HalfSangamData
import com.example.rajdhanibazar.data.HalfSangamResponse
import com.example.rajdhanibazar.data.LoginRequest
import com.example.rajdhanibazar.data.LoginResponse
import com.example.rajdhanibazar.data.Market
import com.example.rajdhanibazar.data.SignUpRequest
import com.example.rajdhanibazar.data.SignUpResponse
import com.example.rajdhanibazar.data.StarlineGamesResponse
import com.example.rajdhanibazar.data.Withdraw
import com.example.rajdhanibazar.data.WithdrawResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @POST("signup")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignUpResponse>

    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("bid_history/store")
    fun postBid(@Body bidRequest: AddBid): Call<BidResponse>


    @POST("update-balance/{phoneNumber}")
    fun updateBalance(
        @Path("phoneNumber") phoneNumber: String,
        @Body balanceUpdate: Balance
    ): Call<ResponseBody>
    @POST("forgot-password")
    fun forgotPassword(@Query("email") email: String): Call<ResponseBody>

    @POST("withdraw_request")
    fun postWithdrawRequest(@Body withdraw: Withdraw): Call<WithdrawResponse>

    @POST("store_fullsangam")
    fun storeFullSangam(@Body requestData: FullSangamData): Call<FullSangamResponse>

    @POST("store_halfsangam")
    fun storeHalfSangam(@Body halfSangamRequest: HalfSangamData): Call<HalfSangamResponse>
    @GET("declear-result")
    fun getResults(): Call<List<GameResult>>

    @GET("game_types")
    fun getGameRate(): Call<List<GameRate>>

    @GET("rajdhani-users")
    fun getUsers(): Call<FetchUserResponse>

    @GET("market")
    fun getMarkets(): Call<List<Market>>

    @GET("gali-disawar")
    fun getGaliGames(): Call<GaliResponse>

    @GET("starline-games")
    fun getStarlineGames(): Call<StarlineGamesResponse>
}