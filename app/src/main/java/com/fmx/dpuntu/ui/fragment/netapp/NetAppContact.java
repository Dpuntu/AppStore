package com.fmx.dpuntu.ui.fragment.netapp;

import android.content.Intent;

/**
 * Created on 2017/7/26.
 *
 * @author dpuntu
 */

public class NetAppContact {
    public interface Presenter {
        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getDownLoadAppsList();
    }

    public interface View {
        void getDownLoadAppsList();
    }
}
