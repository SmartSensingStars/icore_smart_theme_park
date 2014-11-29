package com.larcloud.model;

import com.larcloud.service.exception.BaseException;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 12-9-19
 * Time: 下午5:27
 * To change this template use File | Settings | File Templates.
 */
public class User extends BaseModel {
    public static final String LOGGED_IN_USER = "loggedInUser";
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String ACCESS_KEY = "accessKey";
    public static final String HEART_BEAT_AT = "heartBeatAt";
    public static final String ONLINE_STATUS = "onlineStatus";
    public static final String CLIENT_ID = "clientId";
    public static final String EMAIL = "email";
    public static final String GROUP_ID__IN = "groupId__IN";

    public static User getLoggedInUser(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        User loginUser = (User) session.getAttribute(User.LOGGED_IN_USER);
        if (loginUser==null){
            throw new BaseException(BaseException.LOGIN_ERROR);
        }
        return loginUser;
    }

    public static void setLoggedInUser(User user){
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute(User.LOGGED_IN_USER, user);
    }
	
    private Integer userId;
    private String username;
    private String password;
    private String accessKey;
    private String phoneNumber;
    private Date hearBeatAt;
    private Integer onlineStatus;
	private String name;
    private String email;
    private Group group;
    private Role role;


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }


    public Date getHearBeatAt() {
        return hearBeatAt;
    }

    public void setHearBeatAt(Date hearBeatAt) {
        this.hearBeatAt = hearBeatAt;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
