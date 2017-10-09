package com.seuic.app.store.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created on 2017/9/23.
 *
 * @author dpuntu
 */
@Entity
public class DownloadTaskTable {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String taskId;
    private String downloadUrl;
    private String savePath;
    private String fileName;
    private long loadedLength;
    private long totalSize;

    @Generated(hash = 819303366)
    public DownloadTaskTable(Long id, String taskId, String downloadUrl,
                             String savePath, String fileName, long loadedLength, long totalSize) {
        this.id = id;
        this.taskId = taskId;
        this.downloadUrl = downloadUrl;
        this.savePath = savePath;
        this.fileName = fileName;
        this.loadedLength = loadedLength;
        this.totalSize = totalSize;
    }

    @Generated(hash = 525850253)
    public DownloadTaskTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSavePath() {
        return this.savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLoadedLength() {
        return this.loadedLength;
    }

    public void setLoadedLength(long loadedLength) {
        this.loadedLength = loadedLength;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

}
