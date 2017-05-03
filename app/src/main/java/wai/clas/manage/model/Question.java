package wai.clas.manage.model;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/5/3 21:56
 * QQ群481606175
 */

public class Question extends BmobObject implements Serializable{
    BmobUser user;
    String title;
    String content;
    String img1;
    String img2;
    TotalClass clas;

    public BmobUser getUser() {
        return user;
    }

    public void setUser(BmobUser user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public TotalClass getClas() {
        return clas;
    }

    public void setClas(TotalClass clas) {
        this.clas = clas;
    }
}
