package com.fmx.dpuntu.ui.fragment.loaclapp;

import com.fmx.dpuntu.utils.AppInfo;

import java.util.ArrayList;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class LocalAppContract {
    public interface Presenter {
        void initAppList();
    }

    public interface View {
        ArrayList<AppInfo> getAppsList();
    }
}
