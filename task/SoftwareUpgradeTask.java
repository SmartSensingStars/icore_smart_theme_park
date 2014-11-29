package com.larcloud.task;

//import com.larcloud.work.nwwork.M2MObject;
import com.larcloud.model.Task;
import com.larcloud.model.Terminal;
import com.larcloud.model.TerminalInstruction;
import com.larcloud.model.TerminalVersion;
import com.larcloud.service.TerminalVersionService;
import com.larcloud.service.exception.BaseException;
import com.larcloud.util.ConfigurationSupport;
import com.larcloud.util.ParamMap;
//import com.larcloud.work.CloudServer;
import org.apache.log4j.Logger;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-8
 * Time: 上午12:32
 * To change this template use File | Settings | File Templates.
 */
public class SoftwareUpgradeTask extends BaseTask {

    private Logger logger = Logger.getLogger(SoftwareUpgradeTask.class.getName());

    @Resource(name="TerminalVersionServiceImpl")
    private TerminalVersionService terminalVersionService;



    @Override
    public void runForOneTerminal(Task task,Terminal terminal, ParamMap params){
//        M2MObject m2mObject = new M2MObject();
//        m2mObject.setCommid(new byte[] { 0x00, 0x0C });
//        m2mObject.setNw_id(terminal.getSerialNo().getBytes());
//        logger.debug("terminal.getTxNo() ====  " + terminal.getTxNo()+ "    terminal.getSerialNo() ==== "+  terminal.getSerialNo());
//        m2mObject.setSeria(params.getInteger(TerminalInstruction.TX_NO));
//
//        String dataJson=params.getString(TerminalInstruction.DATA_JSON);
//
//        ParamMap paramsTV=ParamMap.fromJson(dataJson);
//
//        TerminalVersion tv= terminalVersionService.get(paramsTV);
//        if (tv==null){
//            throw new BaseException(BaseException.PARAMETER_ERROR);
//        }
//        String ftpUser= ConfigurationSupport.read("root.properties", "FTP.USER");
//        String ftpPassword= ConfigurationSupport.read("root.properties", "FTP.PASS");
//        String url = tv.getFileUrl()
//                +tv.getFileName()
//                + "?USER=" + ftpUser
//                + "&PASS=" + ftpPassword
//                + "&FILE_SIZE="+ tv.getFileSize()
//                + "&VER_DSC="+ tv.getCode()
//                + "&MD5="+ tv.getFileMd5()
//                //+ nwType.getFileMd5() + "&TRY_NUM=" + ConfigurationSupport.read("root.properties", "FTP.TRY_NUM");
//                + "&TRY_NUM=3";
//        m2mObject.setOutmsg(url.getBytes());
//
//        CloudServer.sendToTerminalByTcp(m2mObject);
//
//       /* Integer msgStatus = 0;
//        int i = 0;
//        while (i < 60) {
//            msgStatus = CloudHandleInterface.getMsgStatusByPushKey(pushKey);
//            if (msgStatus == 1 || msgStatus == 2) {
//                try {
//                    new Thread().sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                i++;
//            } else {
//                break;
//            }
//        }
//        if (msgStatus!= 4) {
//            throw new BaseException(BaseException.MESSAGE_SERVER_ERROR);
//        }*/
    }
}
