package com.zzh.findit.mode;

/**
 * Created by 腾翔信息 on 2017/8/31.
 */

public class MemberMode {
    private String result;
    private String message;
    private MemberData data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MemberData getData() {
        return data;
    }

    public void setData(MemberData data) {
        this.data = data;
    }

    public class MemberData{
        private MineMemberinfo member;

        public MineMemberinfo getMember() {
            return member;
        }

        public void setMember(MineMemberinfo member) {
            this.member = member;
        }

        public class MineMemberinfo{
            private String account_security_level;
            private String state;
            private String member_id;
            private String lv_id;
            private String uname;
            private String regtime;
            private String name;
            private String sex;
            private String mobile;
            private String point;
            private String lastlogin;
            private String logincount;
            private String mp;
            private String nickname;
            private String midentity;
            private String face;
            private String email;

            public String getAccount_security_level() {
                return account_security_level;
            }

            public void setAccount_security_level(String account_security_level) {
                this.account_security_level = account_security_level;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getLv_id() {
                return lv_id;
            }

            public void setLv_id(String lv_id) {
                this.lv_id = lv_id;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getRegtime() {
                return regtime;
            }

            public void setRegtime(String regtime) {
                this.regtime = regtime;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getPoint() {
                return point;
            }

            public void setPoint(String point) {
                this.point = point;
            }

            public String getLastlogin() {
                return lastlogin;
            }

            public void setLastlogin(String lastlogin) {
                this.lastlogin = lastlogin;
            }

            public String getLogincount() {
                return logincount;
            }

            public void setLogincount(String logincount) {
                this.logincount = logincount;
            }

            public String getMp() {
                return mp;
            }

            public void setMp(String mp) {
                this.mp = mp;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getMidentity() {
                return midentity;
            }

            public void setMidentity(String midentity) {
                this.midentity = midentity;
            }

            public String getFace() {
                return face;
            }

            public void setFace(String face) {
                this.face = face;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }


    }
}
