package com.fmx.dpuntu.mvp;

import android.content.Intent;

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

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getDownLoadAppsList();
    }

    public interface View {
        ArrayList<AppInfo> getAppsList();

        void getDownLoadAppsList();
    }
}
