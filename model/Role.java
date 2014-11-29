package com.larcloud.model;

import com.larcloud.annotation.Permission;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 13-6-8
 * Time: 上午11:31
 * To change this template use File | Settings | File Templates.
 */
public class Role extends  BaseModel {
    public static final String ROLE_ID = "roleId";
    public static final String NAME = "name";
    public static final String LABEL = "label";
    public static final String PERMISSION = "permission";
    private Integer roleId;
    private String name;
    private String label;
    private String permission;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;


    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        if (roleId==1){
            List<String> strs=new ArrayList<String>();
            for (Permission.Item item:Permission.Item.values()){
                strs.add(item.toString());
            }
            permission= StringUtils.join(strs,",");

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
