package cc.lijingbo.androidstudy.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import cc.lijingbo.androidstudy.R;
import cc.lijingbo.androidstudy.ui.adapter.RxListenerAdapter;
import cc.lijingbo.androidstudy.ui.model.bean.CityBean;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 滑动到底部，触发相关工作 监听实现吸顶效果
 */
public class RvListenerActivity extends AppCompatActivity {

    private static final String TAG = "RvListenerActivity";

    List<MultiItemEntity> allList = new ArrayList<>();

    ArrayList<MultiItemEntity> promotionsList = new ArrayList<>();

    ArrayList<MultiItemEntity> nearByList = new ArrayList<>();

    private SmartRefreshLayout refreshLayout;
    private RecyclerView rvList;
    private RxListenerAdapter mAdapter;
    private RelativeLayout headerView;
    private LinearLayoutManager layoutManager;
    private int mCurrentPosition = 0;
    private int fontListSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_listener);
        initView();
        fakeNetworkfresh();
    }

    private void initView() {
        refreshLayout = findViewById(R.id.refresh_layout);
        rvList = findViewById(R.id.play_card_list);
        headerView = findViewById(R.id.header_title_layout);

        headerView.setVisibility(View.GONE);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                fakeNetworkfresh();
            }
        });
        refreshLayout.setEnableLoadMore(false);
        initRecycerView();
    }

    int distanceY;
    int state;

    private void initRecycerView() {
        rvList.setHasFixedSize(true);
        rvList.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new
                LinearLayoutManager(this);
        rvList.setLayoutManager(layoutManager);
        mAdapter = new RxListenerAdapter(allList);
        rvList.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(false);
        rvList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isDataAll) {
                    return;
                }
                if (!recyclerView.canScrollVertically(1)) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        initFooter();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isDataAll) {
                    return;
                }
                distanceY = dy;
                Log.e(TAG, "dx:" + dx + ",distanceY:" + distanceY);
                mCurrentPosition = layoutManager.findFirstVisibleItemPosition();
                if (mCurrentPosition >= fontListSize + mAdapter.getHeaderLayoutCount()) {
                    headerView.setVisibility(View.VISIBLE);
                } else {
                    headerView.setVisibility(View.GONE);
                }
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (!recyclerView.canScrollVertically(1)) {
                    if (mAdapter.getFooterLayoutCount() == 0) {

                    } else {
                        if (recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_DRAGGING && dy > 0) {
                            Intent intent = new Intent(RvListenerActivity.this, NaviActivity.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    if (dy < 0 && lastVisibleItemPosition
                            < allList.size() + mAdapter.getHeaderLayoutCount()  - 1) {
                        removeFooter();
                    }
                }
            }
        });
        initHeader();
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeFooter();
        rvList.scrollToPosition(0);
    }


    private void initFooter() {
        if (mAdapter.getFooterLayoutCount() > 0) {
            return;
        }
        View footer = LayoutInflater.from(RvListenerActivity.this)
                .inflate(R.layout.item_play_card_footer, rvList, false);
        mAdapter.addFooterView(footer);
        Log.e(TAG, "添加 footer");
    }

    private void removeFooter() {
        mAdapter.removeAllFooterView();
    }

    private void initHeader() {
        View locationView = LayoutInflater.from(this).inflate(R.layout.item_play_card_1, rvList, false);
        mAdapter.addHeaderView(locationView);
        View locationView2 = LayoutInflater.from(this).inflate(R.layout.item_play_card_2, rvList, false);
        mAdapter.addHeaderView(locationView2);
    }

    boolean isDataAll = false;

    private void fakeNetworkfresh() {
        isDataAll = false;
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        CityBean cityBean = new CityBean();
                        cityBean.setItemType(RxListenerAdapter.TYPE_HEADER_PROMOTION);
                        promotionsList.add(cityBean);
                        for (int i = 0; i < 3; i++) {
                            cityBean = new CityBean();
                            cityBean.setName("北京" + i);
                            cityBean.setItemType(RxListenerAdapter.TYPE_LEVEL_0);
                            promotionsList.add(cityBean);
                        }
                        fontListSize = promotionsList.size();
                        cityBean = new CityBean();
                        cityBean.setItemType(RxListenerAdapter.TYPE_HEADER_NEARBY);
                        nearByList.add(cityBean);
                        for (int i = 0; i < 5; i++) {
                            cityBean = new CityBean();
                            cityBean.setName("南京" + i);
                            cityBean.setItemType(RxListenerAdapter.TYPE_LEVEL_0);
                            nearByList.add(cityBean);
                        }
                        cityBean = new CityBean();
                        cityBean.setItemType(RxListenerAdapter.TYPE_FOOTER_NEAYRBY);
                        nearByList.add(cityBean);
                        refreshLayout.finishRefresh();
                        allList.clear();
                        allList.addAll(promotionsList);
                        allList.addAll(nearByList);
                        mAdapter.notifyDataSetChanged();
                        promotionsList.clear();
                        nearByList.clear();
                        isDataAll = true;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


}
