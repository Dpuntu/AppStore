package com.seuic.app.store.ui.presenter;

import com.seuic.app.store.bean.RecycleObject;
import com.seuic.app.store.bean.RecycleSearchBean;
import com.seuic.app.store.bean.RecycleTitleMoreBean;
import com.seuic.app.store.bean.RecycleViewType;
import com.seuic.app.store.bean.response.AppDetailReceive;
import com.seuic.app.store.greendao.GreenDaoManager;
import com.seuic.app.store.greendao.SearchHistoryTable;
import com.seuic.app.store.net.ApiManager;
import com.seuic.app.store.ui.contact.SearchContact;
import com.seuic.app.store.utils.AndroidUtils;
import com.seuic.app.store.utils.Loger;
import com.seuic.app.store.utils.RxUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/9/17.
 *
 * @author dpuntu
 */

public class SearchPresenter implements SearchContact.Presenter {
    private SearchContact.View mView;
    private List<RecycleObject> recycleObjects;

    public SearchPresenter(SearchContact.View view) {
        mView = view;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void queryHistorySearch(String searchText) {
        List<SearchHistoryTable> searchHistoryTables = GreenDaoManager.getInstance().querySearchHistory();
        recycleObjects = new ArrayList<>();
        if (searchHistoryTables != null && searchHistoryTables.size() > 0) {
            recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_TITLE,
                                                 new RecycleTitleMoreBean("搜索历史", false, "")));
            for (SearchHistoryTable historyTable : searchHistoryTables) {
                recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA,
                                                     new RecycleSearchBean(historyTable.getAppName(),
                                                                           true,
                                                                           historyTable.getSearchTime())));
            }
        }
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .searchAppsNote(searchText))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSearchNoteObserver("AppSearchNote"));
    }

    private class AppSearchNoteObserver extends RxUtils.ResponseObserver<List<String>> {

        AppSearchNoteObserver(String observeName) {
            super(observeName);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onSuccess(List<String> strings) {
            recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_TITLE,
                                                 new RecycleTitleMoreBean("推荐搜索", false, "")));
            if (strings != null && strings.size() > 0) {
                for (String str : strings) {
                    recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA,
                                                         new RecycleSearchBean(str, false,
                                                                               AndroidUtils.systemTime())));
                }
            }
            mView.updateHistoryRecycleView(recycleObjects);
        }

        @Override
        public void onError(String errorMsg) {
            Loger.e("错误提示:" + errorMsg);
            mView.updateHistoryRecycleView(recycleObjects);
        }
    }


    @Override
    public void searchApps(String appName) {
        GreenDaoManager.getInstance().insertSearchHistoryTableDao(appName, AndroidUtils.systemTime());
        RxUtils.onErrorInterceptor(
                ApiManager.getInstance()
                        .getService()
                        .searchApps(appName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppSearchObserver("AppSearch"));
    }

    private class AppSearchObserver extends RxUtils.ResponseObserver<List<AppDetailReceive>> {
        AppSearchObserver(String observeName) {
            super(observeName);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onSuccess(List<AppDetailReceive> appDetailReceives) {
            List<RecycleObject> recycleObjects = new ArrayList<>();
            if (appDetailReceives != null && appDetailReceives.size() > 0) {
                for (AppDetailReceive appDetailReceive : appDetailReceives) {
                    recycleObjects.add(new RecycleObject(RecycleViewType.RECYCEL_DATA, appDetailReceive));
                }
            }
            mView.updateSearchRecycleView(recycleObjects);
        }

        @Override
        public void onError(String errorMsg) {
            Loger.e("错误提示:" + errorMsg);
        }
    }
}
