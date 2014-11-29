package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-14
 * Time: 下午5:36
 * To change this template use File | Settings | File Templates.
 */
public class SysConfig extends BaseModel {
    private Integer version;

    private Integer terminalInstructionAckTimeOut;
    private Integer terminalInstructionUpgradeTimeOut;
    private Integer terminalInstructionSendInterval;

    public Integer getTerminalInstructionAckTimeOut() {
        return terminalInstructionAckTimeOut;
    }

    public void setTerminalInstructionAckTimeOut(Integer terminalInstructionAckTimeOut) {
        this.terminalInstructionAckTimeOut = terminalInstructionAckTimeOut;
    }

    public Integer getTerminalInstructionUpgradeTimeOut() {
        return terminalInstructionUpgradeTimeOut;
    }

    public void setTerminalInstructionUpgradeTimeOut(Integer terminalInstructionUpgradeTimeOut) {
        this.terminalInstructionUpgradeTimeOut = terminalInstructionUpgradeTimeOut;
    }

    public Integer getTerminalInstructionSendInterval() {
        return terminalInstructionSendInterval;
    }

    public void setTerminalInstructionSendInterval(Integer terminalInstructionSendInterval) {
        this.terminalInstructionSendInterval = terminalInstructionSendInterval;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
