package com.seuic.app.store.bean;

import java.io.Serializable;

/**
 * Created on 2017/9/22.
 *
 * @author dpuntu
 */

public class ScreenShotBean implements Serializable {
    private String shotName;

    public ScreenShotBean(String shotName) {
        this.shotName = shotName;
    }

    public String getShotName() {
        return shotName;
    }

    public void setShotName(String shotName) {
        this.shotName = shotName;
    }
}
