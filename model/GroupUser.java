package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-8
 * Time: 上午11:40
 * To change this template use File | Settings | File Templates.
 */
public class GroupUser extends  BaseModel {
    public static final String GROUP_USER_ID = "groupUserId";

    private Integer groupUserId;
    private User user;
    private Group group;

    public Integer getGroupUserId() {
        return groupUserId;
    }

    public void setGroupUserId(Integer groupUserId) {
        this.groupUserId = groupUserId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
