package wai.clas.manage.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/5/3 23:39
 * QQ群481606175
 */

public class UserModel extends BmobUser {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
