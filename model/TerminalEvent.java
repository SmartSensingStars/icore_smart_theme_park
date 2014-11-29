package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-21
 * Time: 下午12:11
 * To change this template use File | Settings | File Templates.
 */
public class TerminalEvent extends BaseModel {
    public static final String TERMINAL_EVENT_ID = "terminalEventId";
    public static final Integer TYPE_ONLINE = 1;
    public static final Integer TYPE_OFFLINE = 2;
    public static final String TYPE_ID = "typeId";

    public static final String TERMINAL_ID = "terminalId";

    private Integer terminalEventId;

    private Integer typeId;
    private String typeName;

    private Integer terminalId;

    public Integer getTerminalEventId() {
        return terminalEventId;
    }

    public void setTerminalEventId(Integer terminalEventId) {
        this.terminalEventId = terminalEventId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    public String getTypeName() {
        return this.typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
