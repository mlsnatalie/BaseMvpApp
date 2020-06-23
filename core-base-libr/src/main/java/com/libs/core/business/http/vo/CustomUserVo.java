package com.libs.core.business.http.vo;

import java.io.Serializable;

public class CustomUserVo implements Serializable {

    /**
     * user_nicename :
     * nickname :
     * mobile : 1
     * head_portrait : https://qhimg3.jindashi.net/user_info/baracktocat.jpg
     * uid : 200000322
     * hx_pass : gR3aDnN2ZIiMkpG9QGPRnKc
     * user_level : 3
     * is_teacher : false
     * is_youke : 0
     * has_deal_acount : false
     * custom_service : {"uid":"5000116","nickname":"王妮娜","head_portrait":"https://qhimg3.jindashi.net/user_info/baracktocat.jpg"}
     * rong_token : gR3aDnN2ZIiMkpG9QGPRnKcWef2nyT/T/2x/GyCCgZ5uK1/Wye8d+b8he2aUAYp1D5/zCN7BsLj4gZ5u8pe3NDSfuHihdqWT
     */

    private String user_nicename;
    private String nickname;
    private String mobile;
    private String head_portrait;
    private String uid;
    private String hx_pass;
    private String hx_username;
    private String user_level;
    private String ukey;
    private boolean is_teacher;
    private int is_youke;
    private boolean has_deal_acount;
    private ServiceBean custom_service;
    private String rong_token;

    public String getUser_nicename() {
        return user_nicename;
    }

    public void setUser_nicename(String user_nicename) {
        this.user_nicename = user_nicename;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHead_portrait() {
        return head_portrait;
    }

    public void setHead_portrait(String head_portrait) {
        this.head_portrait = head_portrait;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getHx_pass() {
        return hx_pass;
    }

    public void setHx_pass(String hx_pass) {
        this.hx_pass = hx_pass;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public boolean isIs_teacher() {
        return is_teacher;
    }

    public void setIs_teacher(boolean is_teacher) {
        this.is_teacher = is_teacher;
    }

    public int getIs_youke() {
        return is_youke;
    }

    public void setIs_youke(int is_youke) {
        this.is_youke = is_youke;
    }

    public boolean isHas_deal_acount() {
        return has_deal_acount;
    }

    public void setHas_deal_acount(boolean has_deal_acount) {
        this.has_deal_acount = has_deal_acount;
    }

    public ServiceBean getCustom_service() {
        return custom_service;
    }

    public void setCustom_service(ServiceBean custom_service) {
        this.custom_service = custom_service;
    }

    public String getRong_token() {
        return rong_token;
    }

    public void setRong_token(String rong_token) {
        this.rong_token = rong_token;
    }

    public static class ServiceBean {

        /**
         * uid : 5000116
         * nickname : 王妮娜
         * head_portrait : https://qhimg3.jindashi.net/user_info/baracktocat.jpg
         */

        private String uid;
        private String nickname;
        private String head_portrait;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHead_portrait() {
            return head_portrait;
        }

        public void setHead_portrait(String head_portrait) {
            this.head_portrait = head_portrait;
        }
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }
}
