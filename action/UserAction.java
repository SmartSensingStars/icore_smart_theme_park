package com.larcloud.action;

import com.larcloud.annotation.Permission;
import com.larcloud.model.BaseModel;
import com.larcloud.service.UserService;
import com.larcloud.util.ParamMap;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Date;

@Results({
        @Result(name = "getuser", location = "/WEB-INF/content/jsp/sysContrlo/UserView.jsp"),
        @Result(name = "getListMenu", location = "/WEB-INF/content/jsp/sysContrlo/simpleData.jsp"),
        @Result(name = "getZTree", location = "/WEB-INF/content/jsp/sysContrlo/orgMenu.jsp"),
        @Result(name = "upUserView" ,location = "/WEB-INF/content/jsp/sysContrlo/UpUserView.jsp"),
        @Result(name = "upUserPwdView", location = "/WEB-INF/content/jsp/sysContrlo/UpUserPwd.jsp"),
        @Result(name ="searchView",location ="/WEB-INF/content/jsp/sysContrlo/SearchSys.jsp"),
        @Result(name ="getTerminalList",location = "/WEB-INF/content/jsp/sysContrlo/TerminalList.jsp"),
        @Result(name = "tree", location = "/WEB-INF/content/jsp/sysContrlo/userTree.jsp"),
        @Result(name = "upUserView" ,location = "/WEB-INF/content/jsp/sysContrlo/UpUserView.jsp"),
        @Result(name = "addUserView",location = "/WEB-INF/content/jsp/sysContrlo/addUser.jsp"),
        @Result(name = "userList",location = "/WEB-INF/content/jsp/sysContrlo/userList.jsp"),
        @Result(name = "userLifePage",location = "/WEB-INF/content/jsp/nw/User.jsp"),
        @Result(name = "selectTree",location = "/WEB-INF/content/jsp/sysContrlo/selectTree.jsp"),
        @Result(name="loging",location="/WEB-INF/content/jsp/Login.jsp")


})

@SuppressWarnings("serial")
@Controller
@Namespace(value = "/user")
public class UserAction extends BaseAction{
    private Logger logger = Logger.getLogger(UserAction.class.getName());
    @Resource(name = "UserServiceImpl")
    UserService mainService;



    private final String GETUSER = "getuser";
    private final String GETLISTMENU = "getListMenu";
    private final String GETZTREE = "getZTree";
    // 各大菜单使用
    private final String TREE = "tree";
    private final String SELECTTREE = "selectTree";
    private final String UPUSERVIEW = "upUserView";
    private final String UPUSERPWDVIEW = "upUserPwdView";
    private final String SEARCHVIEW = "searchView";
    private final String SEARCHOPEAR = "searchOpear";
    private final String ADDUSER = "addUser";
    private final String ADDUSERVIEW = "addUserView";
    private final String USERLIST = "userList";
    private final String USERLIFEPAGE = "userLifePage";
    public final String LOGING = "loging";

    @Action(value = "add")
    @Permission({Permission.Item.USER_ADD})
    public void add() {
        params.put(BaseModel.CREATED_AT,new Date());
        ParamMap newObj=mainService.add(params);
        writer.writeOK(newObj);
    }

    @Action(value = "delete")
    @Permission({Permission.Item.USER_DELETE})
    public void delete() {

        mainService.delete(params);
        writer.writeOK();
    }
    @Action(value = "update")
//    @Permission({Permission.Item.USER_UPDATE})
    public void update() {
        params.put(BaseModel.UPDATED_AT,new Date());
        mainService.update(params);
        writer.writeOK();
    }

    @Action(value = "login")
    public void login() {
        Object responseData = mainService.login(params);
        writer.writeOK(responseData);
    }

    @Action(value = "get")
    public void get()
    {
        writer.writeOK(mainService.get(params));
    }

    @Action(value = "list")
    public void list()
    {
        Object resp=mainService.list(params);
        writer.writeOK(resp);
    }

    @Action(value = "selectTree")
    public String selectTree(){
        /*Sys_User s=null;
          Object o  = ServletActionContext.getRequest().getSession().getAttribute("LoginUserBean");
          if(o!=null){
               s = (Sys_User)o;
          }*/
        //List<UserBean> alist = userControlServiceImpl.getTreeByStid(User.getLoggedInUser().getStid());
        //jsonArray = JSONArray.fromObject(alist);
        return SELECTTREE;
    }
}
