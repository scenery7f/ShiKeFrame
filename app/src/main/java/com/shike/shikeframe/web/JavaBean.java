package com.shike.shikeframe.web;

/**
 * Created by snoopy on 2018/8/1.
 */

public class JavaBean {

    /**
     * msg : 成功！
     * code : 0
     * data : {"password":"8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92","createtime":1531031374,"phone":"13363307219","nick_name":"13363307219","id":1,"token":"AaGuXpQQzyPVDOFUmPWzWm3P5mHIpHFX"}
     */

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * password : 8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92
         * createtime : 1531031374
         * phone : 13363307219
         * nick_name : 13363307219
         * id : 1
         * token : AaGuXpQQzyPVDOFUmPWzWm3P5mHIpHFX
         */

        private String password;
        private int createtime;
        private String phone;
        private String nick_name;
        private int id;
        private String token;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getCreatetime() {
            return createtime;
        }

        public void setCreatetime(int createtime) {
            this.createtime = createtime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getNick_name() {
            return nick_name;
        }

        public void setNick_name(String nick_name) {
            this.nick_name = nick_name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
