package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-8
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class UserRole extends  BaseModel {
    public static final String USER_ROLE_ID = "userRoleId";

    private Integer userRoleId;
    private User user;
    private Role role;

    public Integer getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
