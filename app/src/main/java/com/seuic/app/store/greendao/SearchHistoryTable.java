package com.seuic.app.store.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */
@Entity
public class SearchHistoryTable {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String appName;
    private String searchTime;

    @Generated(hash = 572456928)
    public SearchHistoryTable(Long id, String appName, String searchTime) {
        this.id = id;
        this.appName = appName;
        this.searchTime = searchTime;
    }

    @Generated(hash = 1689503406)
    public SearchHistoryTable() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSearchTime() {
        return this.searchTime;
    }

    public void setSearchTime(String searchTime) {
        this.searchTime = searchTime;
    }

}
