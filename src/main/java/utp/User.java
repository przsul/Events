package utp;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.DatatypeConverter;

@Entity
@Table(name = "users")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long userId;
	
	@Column(name = "password", nullable = false)
    private String userPassword;
	
	@Column(name = "login", nullable = false)
    private String userLogin;
	
	@Column(name = "firstname", nullable = false)
    private String userFirstName;
	
	@Column(name = "lastname", nullable = false)
    private String userLastName;
	
	@Column(name = "email", nullable = false)
    private String userEmail;
	
	@Column(name = "type", nullable = false)
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

    public void setUserPassword(String userPassword) throws NoSuchAlgorithmException {
    	MessageDigest md = MessageDigest.getInstance("MD5");
    	
    	md.update(userPassword.getBytes());
    	
    	byte[] digest = md.digest();
    	String hash = DatatypeConverter.printHexBinary(digest).toLowerCase();
    	
        this.userPassword = hash;
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
