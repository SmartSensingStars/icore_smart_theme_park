package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-1-4
 * Time: 下午1:09
 * To change this template use File | Settings | File Templates.
 */
public class TerminalData extends BaseModel {
    public static final String TERMINAL_DATA_ID = "terminalDataId";
    public static final Integer TYPE_MOBILE_DATA_TRAFFIC = 1;
    public static final Integer TYPE_RFID_READER = 21;
    public static final String TYPE_ID = "typeId";
    public static final String VALUE_DOUBLE = "valueDouble";
    public static final String VALUE_STRING = "valueString";
    public static final String VALUE_INTEGER = "valueInteger";
    public static final String TERMINAL_ID = "terminalId";

    private Integer terminalDataId;

    private Integer typeId;
    private String valueDouble;
    private String valueString;

    private Integer terminalId;

    public Integer getTerminalDataId() {
        return terminalDataId;
    }

    public void setTerminalDataId(Integer terminalDataId) {
        this.terminalDataId = terminalDataId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getValueDouble() {
        return valueDouble;
    }

    public void setValueDouble(String valueDouble) {
        this.valueDouble = valueDouble;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Integer getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }
}
