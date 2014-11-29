package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-5-1
 * Time: 下午12:38
 * To change this template use File | Settings | File Templates.
 */
public class ProgramDownload extends BaseModel {
    public static final String PROGRAM_DOWNLOAD_ID = "programDownloadId";
    public static final String STATUS = "status";
    public static final Integer STATUS_START = 1;
    public static final Integer STATUS_ERROR = 4;
    public static final Integer STATUS_OK = 8;
    public static final String FILE_PATH = "filePath";
    public static final String ORDER_ID = "orderId";


    private Integer programDownloadId;
    private Program program;
    private Integer status;
    private String statusName;
    private String filePath;
    private Order order;
    private int orderId;

    public Integer getProgramDownloadId() {
        return programDownloadId;
    }

    public void setProgramDownloadId(Integer programDownloadId) {
        this.programDownloadId = programDownloadId;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (status==STATUS_START)    {
            statusName="进行";
        }
        if (status==STATUS_OK)    {
            statusName="完成";
        }
        if (status==STATUS_ERROR)    {
            statusName="错误";
        }

    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
