package com.larcloud.action;

import com.larcloud.model.Task;
import com.larcloud.model.TerminalInstruction;
import com.larcloud.service.CameraGroupService;
import com.larcloud.service.TaskService;
import com.larcloud.util.ParamItem;
import com.larcloud.util.ParamMap;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-7
 * Time: 下午8:22
 * To change this template use File | Settings | File Templates.
 */
@Controller
@Namespace(value = "/cameraGroup")
public class CameraGroupAction extends BaseAction {
    private Logger logger = Logger.getLogger(CameraGroupAction.class.getName());
    @Resource(name = "CameraGroupServiceImpl")
    CameraGroupService mainService;


    @Resource(name = "TaskServiceImpl")
    TaskService taskService;

}
