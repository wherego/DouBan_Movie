package cn.flyexp.douban_movie.presenter;

import android.util.Log;

import java.util.ArrayList;

import cn.flyexp.douban_movie.base.BasePresenter;
import cn.flyexp.douban_movie.model.MovieModel;
import cn.flyexp.douban_movie.net.NetWork;
import cn.flyexp.douban_movie.presenter.ipresenter.ITop250Presenter;
import cn.flyexp.douban_movie.view.iview.ITop250View;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Won on 2017/3/12.
 */

public class Top250Presenter extends BasePresenter<ITop250View> implements ITop250Presenter{

    private static final String TAG = "Top250Presenter";

    private int start = 0;

    private Observer<MovieModel> observer = new Observer<MovieModel>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "onError: ", e);
            mView.showError();
        }

        @Override
        public void onNext(MovieModel movieModel) {
            if (movieModel.getSubjects().size() > 0) {
                mView.showComplete((ArrayList<?>) movieModel.getSubjects());
                start = start + movieModel.getSubjects().size();
            } else {
                mView.showEmpty();
            }

        }
    };

    @Override
    public void loadingData() {
        mView.showLoading();
        subscription = NetWork.getDouBanApi()
                .getTop250(start, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    protected void onDestroy() {
        unsubscribe();
    }
}
