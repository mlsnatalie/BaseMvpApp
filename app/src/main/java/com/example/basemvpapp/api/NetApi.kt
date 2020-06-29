package com.example.basemvpapp.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * @author: amos
 * @date: 2019/12/25 13:54
 * @description:
 */
interface NetApi {
//    /**
//     * 用户提交答案
//     *
//     * @param params
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/bonus/receive")
//    fun openGiftBox(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<String?>?>?
//
//    /**
//     * 用户提交答案
//     *
//     * @param params
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/process/commitAnswer")
//    fun sendUserAnswer(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<LiveSendAnswerResultBean?>?>?
//
//    /**
//     * 获取直播信息
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/activity/info")
//    fun getLiveInfo(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<LiveActivityInfo?>?>?
//
//    /**
//     * 获取复活卡信息
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/resurrect/getCardInfo")
//    fun queryCardInfo(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<CardInfo?>?>?
//
//    /**
//     * 通过邀请码领取复活卡
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/resurrect/bindCard")
//    fun  //    Observable<HttpResultVo> getCardByCode(@Field("channelType") String channelCode, @Field("userId") String userId, @Field("cardCode") String code);
//            getCardByCode(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<*>?>?
//
//    /**
//     * 微信注册成功领取复活卡(微信授权成功使用)
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/resurrect/getCard")
//    fun  //    Observable<HttpResultVo> getCardByAuth(@Field("channelType") String channelCode, @Field("userId") String userId, @Field("cardType") String cardType);
//            getCardByAuth(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<*>?>?
//
//    /**
//     * 获取排行榜总榜信息
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/rank/allList")
//    fun  //    Observable<HttpResultVo<List<LiveRankBean>>> getRankList(@Field("channelType") String channelCode);
//            getRankList(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<List<LiveRankBean?>?>?>?
//
//    /**
//     * 微信授权成功绑定用户openId
//     *
//     * @param token         token
//     * @param openid        openId
//     * @param sourceChannel openId来源：1-小程序 2-服务号
//     * @param wxHeadImgUrl  微信头像
//     * @param wxNickName    微信昵称
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/CasManager/index/bindWxOpenId")
//    fun authBindOpenId(
//        @Field("token") token: String?,
//        @Field("openId") openid: String?,
//        @Field("sourceChannel") sourceChannel: String?,
//        @Field("wxHeadImgUrl") wxHeadImgUrl: String?,
//        @Field("wxNickName") wxNickName: String?
//    ): Observable<HttpResultVo<*>?>?
//
//    /**
//     * 获取用户订阅状态（直播提醒）
//     *
//     * @param token
//     * @param actId
//     * @return
//     */
//    @GET("/api/user/status")
//    fun getLiveSubStatus(
//        @Query("ukey") token: String?,
//        @Query("activity_id") actId: String?
//    ): Observable<HttpResultVo<LiveSubscribeStatus?>?>?
//
//    /**
//     * 用户订阅直播提醒
//     *
//     * @param token
//     * @param actId
//     * @param time
//     * @return
//     */
//    @GET("/api/user/subscribe")
//    fun subLive(
//        @Query("ukey") token: String?,
//        @Query("activity_id") actId: String?,
//        @Query("time") time: String?
//    ): Observable<HttpResultVo<*>?>?
//
//    /**
//     * 获取直播的在线活动ID
//     *
//     * @return
//     */
//    @get:GET("/api/answer/current")
//    val liveActId: Observable<HttpResultVo<Any?>?>?
//
//    /**
//     * 获取用户直播钱包信息
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/bonus/info")
//    fun  //    Observable<HttpResultVo<WalletInfo>> getWalletInfo(@Field("ukey") String ukey, @Field("channelType") String channelType);
//            getWalletInfo(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<WalletInfo?>?>?
//
//    /**
//     * 获取用户提现记录列表
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/bonus/withdrawList")
//    fun  //    Observable<HttpResultVo<CashRecordBean>> getCashList(@Field("ukey") String ukey,
//    //                                                         @Field("channelType") String channelType,
//    //                                                         @Field("current") int pageIndex,
//    //                                                         @Field("pageSize") int pageSize);
//            getCashList(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<CashRecordBean?>?>?
//
//    /**
//     * 获取推广二维码
//     *
//     * @param params
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/promote/userCode")
//    fun getPromoteCode(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<String?>?>?
//
//    /**
//     * 获取直播答题复活卡数量
//     */
//    @FormUrlEncoded
//    @POST("/resurrect/getCardInfo")
//    fun getResurgenceCard(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<ResurgenceCardBean?>?>?
//
//    /**
//     * 直播室-用户聊天-发送消息
//     */
//    @FormUrlEncoded
//    @POST("/live-api/room/send")
//    fun sendLiveMessage(
//        @Field("sessionId") sessionId: String?,
//        @Field("content") content: String?,
//        @Field("atCustomerMessageId") atCustomerMessageId: String?
//    ): Observable<ResponseBody?>?
//
//    /**
//     * 获取用户直播权限查询，可参与答题和不可参与答题
//     */
//    @FormUrlEncoded
//    @POST("/process/userAuthCheck")
//    fun getUserAuthCheck(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<UserAuthCheckBean?>?>?
//
//    /**
//     * 用户发起提现
//     *
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/bonus/withdraw")
//    fun takeCash(@FieldMap params: Map<String?, String?>?): Observable<HttpResultVo<*>?>?
//
//    /**
//     * 获取直播状态查
//     *
//     * @param sourceId
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("/live-api/room/getRoomBySource")
//    fun getLiveStatus(@Field("sourceId") sourceId: String?): Observable<LiveStatusBean?>?
//
//    /**
//     * 获取用户注册信息
//     *
//     * @return
//     */
//    @GET("/api/user/info")
//    fun getRegisterInfo(@Query("ukey") token: String?): Observable<HttpResultVo<RegisterUserInfo?>?>?
//
//    /**
//     * 直播室获取历史聊天记录
//     *
//     * @return
//     */
//    @GET("/live-api/room/queryMsg")
//    fun getHistoryChat(
//        @Query("sessionId") sessionId: String?,
//        @Query("pageSize") pageSize: Int
//    ): Observable<HttpResultVo<List<HistoryChatData?>?>?>?

    /**
     * 天气预报数据
     */
    @GET("/data/2.5/forecast/daily")
    fun getWeather(@Query("APPID") APPID: String?, @Query("zip") zip: String?): Observable<ResponseBody>
}