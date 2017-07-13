package com.fmx.dpuntu.mvp;

import com.fmx.dpuntu.utils.AppInfo;

import java.util.ArrayList;

/**
 * Created on 2017/7/13.
 *
 * @author dpuntu
 */

public class MainContact {
    public interface Presenter {
        ArrayList<AppInfo> getAppsList();

        void getDownLoadAppsList();
    }

    public interface View {
        ArrayList<AppInfo> getAppsList();
        void getDownLoadAppsList();
    }
}
