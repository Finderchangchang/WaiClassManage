package wai.clas.manage.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by Finder丶畅畅 on 2017/5/5 23:45
 * QQ群481606175
 */

public class SCModel extends BmobObject {
    UserModel user;//账户是谁
    TotalClass subj;//课程

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public TotalClass getSubj() {
        return subj;
    }

    public void setSubj(TotalClass subj) {
        this.subj = subj;
    }
}
