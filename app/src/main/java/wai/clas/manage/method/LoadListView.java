package wai.clas.manage.method;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import wai.clas.manage.R;

/**
 * Created by Administrator on 2017/3/1.
 */

public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    private View footer;// 底部布局
    int totalItemCount;// 总数量
    int lastVisibieItem;// 最后一个可见的item;
    boolean isLoading;// 判断变量
    IloadListener iLoadListener;// 接口变量

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadListView(Context context) {
        super(context);
        initView(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // listview加载底部布局
    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.footermore, null);
        // 设置隐藏底部布局
        footer.findViewById(R.id.footer_layout).setVisibility(View.GONE);
        load_tv = (TextView) footer.findViewById(R.id.load_tv);
        pb = (ProgressBar) footer.findViewById(R.id.pb);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    /**
     * @param pageSize  当前页面显示数量
     * @param pageIndex 页面码
     */
    public void getIndex(int pageIndex, int pageSize, int total) {
        if (pageIndex == 1) {
            closeLoading();
        }
        if (total <= pageIndex * pageSize) {
            loadComplete();//关闭加载更多
        } else {
            closeLoading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (totalItemCount == lastVisibieItem && scrollState == SCROLL_STATE_IDLE) {
            LinearLayout foot = (LinearLayout) footer.findViewById(R.id.footer_layout);
            foot.setVisibility(View.VISIBLE);

            if (!load_tv.getText().toString().trim().equals("------已经到底了------")) {
                if (!isLoading) {
                    isLoading = true;
                    // 加载更多（获取接口）
                    iLoadListener.onLoad(footer);
                }
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.lastVisibieItem = firstVisibleItem + visibleItemCount;
        this.totalItemCount = totalItemCount;
    }

    public void setInterface(IloadListener iLoadListener) {
        this.iLoadListener = iLoadListener;
    }

    // 加载更多数据的回调接口
    public interface IloadListener {
        void onLoad(View view);
    }

    public void closeLoading() {
        if (load_tv != null) {
            footer.findViewById(R.id.footer_layout).setVisibility(View.GONE);
            load_tv.setText("正在加载，请稍等。。。");
            isLoading = false;
        }
    }

    TextView load_tv;
    ProgressBar pb;

    // 加载完成通知隐藏
    public void loadComplete() {
        isLoading = false;
        footer.findViewById(R.id.footer_layout).setVisibility(View.VISIBLE);

        load_tv.setText("------已经到底了------");
        pb.setVisibility(GONE);
    }
}

