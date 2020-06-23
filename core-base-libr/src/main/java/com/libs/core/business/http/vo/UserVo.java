package com.libs.core.business.http.vo;

import java.io.Serializable;

/**
 * 用户信息模型
 */
public class UserVo implements Serializable {

    private String id;
    private String token;
    private String email;
    private String mobile;
    private String tradeNo;
    private String serverType;
    private String nickName;
    private String userName;
    private String headImgUrl;
    private String tgt;
    private String cascookie;
    private String score;
    private boolean isBindingWb;
    private boolean isBindingWx;
    private boolean isBindingQq;
    private boolean hasPasswd;
    //下列为新增字段
    private String createTime;
    private String channelId;
    private String wxNickName;
    private String wxHeadImgUrl;
    private String mpOpenId;
    private String pbOpenId;


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getWxHeadImgUrl() {
        return wxHeadImgUrl;
    }

    public void setWxHeadImgUrl(String wxHeadImgUrl) {
        this.wxHeadImgUrl = wxHeadImgUrl;
    }

    public String getMpOpenId() {
        return mpOpenId;
    }

    public void setMpOpenId(String mpOpenId) {
        this.mpOpenId = mpOpenId;
    }

    public String getPbOpenId() {
        return pbOpenId;
    }

    public void setPbOpenId(String pbOpenId) {
        this.pbOpenId = pbOpenId;
    }


    // 登录类型
    private int loginType;


    /**
     * 是否需要调用复活卡接口  //liveanswer 调用复活卡接口
     *
     * @return
     */
    public boolean isRequestReviveInterface() {
        return "liveanswer".equals(channelId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    public String getCascookie() {
        return cascookie;
    }

    public void setCascookie(String cascookie) {
        this.cascookie = cascookie;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isBindingWb() {
        return isBindingWb;
    }

    public void setBindingWb(boolean bindingWb) {
        isBindingWb = bindingWb;
    }

    public boolean isBindingWx() {
        return isBindingWx;
    }

    public void setBindingWx(boolean bindingWx) {
        isBindingWx = bindingWx;
    }

    public boolean isBindingQq() {
        return isBindingQq;
    }

    public void setBindingQq(boolean bindingQq) {
        isBindingQq = bindingQq;
    }

    public boolean isHasPasswd() {
        return hasPasswd;
    }

    public void setHasPasswd(boolean hasPasswd) {
        this.hasPasswd = hasPasswd;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

}
