package com.fmx.dpuntu.download;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public enum DownloadState {

    STATE_DOWNLOADING(0),

    STATE_FINISH(1),

    STATE_START(2),

    STATE_PAUSE(3),

    STATE_ERROR(4),

    STATE_DEFAULT(5);

    private int state;

    public int getState() {
        return state;
    }

    DownloadState(int state) {
        this.state = state;
    }

}
