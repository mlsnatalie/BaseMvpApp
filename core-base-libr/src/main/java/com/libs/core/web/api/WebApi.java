package com.libs.core.web.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WebApi {

    /**
     * 获取公告链接
     */
    @GET("/api/openapi.php/TouziF10Service.getAnnoContent")
    Observable<ResponseBody> getAnnounmtLink(@Query("announmtid") String announmtid);
}
