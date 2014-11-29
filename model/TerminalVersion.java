package com.larcloud.model;

/**
 * Created with IntelliJ IDEA.
 * User: spectrext
 * Date: 14-3-4
 * Time: 上午9:13
 * To change this template use File | Settings | File Templates.
 */
public class TerminalVersion extends BaseModel {
    public static final String TERMINAL_VERSION_ID = "terminalVersionId";
    public static final String NAME = "versionName";
    public static final String FILEURL = "fileUrl";

    public static final String CODE = "code";

    private Integer terminalVersionId;
    private String name;
    private String code;
    private String hardwareName;
    private String softwareName;
    private String fileName;
    private String fileSize;
    private String fileUrl;
    private String fileMd5;
    private String comment;
    private Integer deleted;

    public Integer getTerminalVersionId() {
        return terminalVersionId;
    }

    public void setTerminalVersionId(Integer terminalVersionId) {
        this.terminalVersionId = terminalVersionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHardwareName() {
        return hardwareName;
    }

    public void setHardwareName(String hardwareName) {
        this.hardwareName = hardwareName;
    }

    public String getSoftwareName() {
        return softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        deleted = deleted;
    }
}
