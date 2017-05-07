package wai.clas.manage.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Finder丶畅畅 on 2017/5/3 21:58
 * QQ群481606175
 */

public class AskModel extends BmobObject {
    UserModel user;
    UserModel q_user;
    Question question;
    String content;

    public UserModel getQ_user() {
        return q_user;
    }

    public void setQ_user(UserModel q_user) {
        this.q_user = q_user;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
