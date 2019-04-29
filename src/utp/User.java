package utp;

import javax.management.relation.Role;

public class User {
    private long userId;
    private String userPassword;
    private String userLogin;
    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userAccountType;

    public User() {
    }

    public User(long userId, String userPassword, String userLogin, String userFirstName, String userLastName, String userEmail, String userAccountType) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userLogin = userLogin;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userAccountType = userAccountType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAccountType() {
        return userAccountType;
    }

    public void setUserAccountType(String userAccountType) {
        this.userAccountType = userAccountType;
    }

    @Override
    public String toString() {
        return userFirstName + " " + userLastName;
    }
}
