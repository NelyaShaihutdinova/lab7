package builders;

import java.io.Serializable;

public class Auntification implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userAction;
    private String nickname;
    private String password;

    public Auntification(String nickname, String password, String userAction) {
        this.nickname = nickname;
        this.password = password;
        this.userAction = userAction;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
