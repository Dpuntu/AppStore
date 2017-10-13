package com.seuic.app.store.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created on 2017/10/13.
 *
 * @author dpuntu
 *         流量统计数据库
 */
@Entity
public class DataUsageTable {
    @Id(autoincrement = true)
    private Long id;
    private String packageName;
    private long uidRxBytes;
    private long uidTxBytes;
    private int onceTime;

    @Generated(hash = 1203322258)
    public DataUsageTable(Long id, String packageName, long uidRxBytes,
                          long uidTxBytes, int onceTime) {
        this.id = id;
        this.packageName = packageName;
        this.uidRxBytes = uidRxBytes;
        this.uidTxBytes = uidTxBytes;
        this.onceTime = onceTime;
    }

    @Generated(hash = 1021156665)
    public DataUsageTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getUidRxBytes() {
        return this.uidRxBytes;
    }

    public void setUidRxBytes(long uidRxBytes) {
        this.uidRxBytes = uidRxBytes;
    }

    public long getUidTxBytes() {
        return this.uidTxBytes;
    }

    public void setUidTxBytes(long uidTxBytes) {
        this.uidTxBytes = uidTxBytes;
    }

    public int getOnceTime() {
        return this.onceTime;
    }

    public void setOnceTime(int onceTime) {
        this.onceTime = onceTime;
    }


}
