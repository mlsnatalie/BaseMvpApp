package com.libs.core.business.vo;

import java.io.Serializable;

public class ChatVo implements Serializable {


    /**
     * cmd : wd_chat
     * room_id : 18080641684720
     * result : {"message":"123","user":{"uid":"123456789","nickname":"xxxxxb","icon":"aaa","userType":1}}
     * time : 1542625424007
     */

    private String cmd;
    private String room_id;
    private ResultBean result;
    private String time;
    private String authCode;
    private String userId;

    public ChatVo() {
    }

    public ChatVo(String cmd, String room_id, ResultBean result, String time, String authCode) {
        this.cmd = cmd;
        this.room_id = room_id;
        this.result = result;
        this.time = time;
        this.authCode = authCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class ResultBean {
        /**
         * message : 123
         * user : {"uid":"123456789","nickname":"xxxxxb","icon":"aaa","userType":1}
         */

        private String seed;
        private String message;
        private UserBean user;
        private int persons;
        private String sendUserId;

        public int getPersons() {
            return persons;
        }

        public void setPersons(int persons) {
            this.persons = persons;
        }

        public String getSeed() {
            return seed;
        }

        public void setSeed(String seed) {
            this.seed = seed;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSendUserId() {
            return sendUserId;
        }

        public void setSendUserId(String sendUserId) {
            this.sendUserId = sendUserId;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : 123456789
             * nickname : xxxxxb
             * icon : aaa
             * userType : 1
             */

            private String uid;
            private String nickname;
            private String icon;
            private String userType;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }
        }
    }
}
