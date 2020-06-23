package com.libs.core.business.vo;

public class WdLikeVo {

    /**
     * cmd : wd_like
     * room_id : “直播室id”
     * msg_id : 888
     * result : {"user":{"uid":"\u201cuid\u201d","nickname":"\u201c昵称\u201d","icon":"头像"}}
     */

    private String cmd;
    private String room_id;
    private int msg_id;
    private ResultBean result;

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

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * user : {"uid":"\u201cuid\u201d","nickname":"\u201c昵称\u201d","icon":"头像"}
         */

        private UserBean user;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * uid : “uid”
             * nickname : “昵称”
             * icon : 头像
             */

            private String uid;
            private String nickname;
            private String icon;

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
        }
    }

    public WdLikeVo() {
    }


}
