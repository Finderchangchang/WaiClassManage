package wai.clas.manage.ui;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.tsz.afinal.view.TitleBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import wai.clas.manage.BaseActivity;
import wai.clas.manage.R;

public class ImageActivity extends BaseActivity {
    String url = "";
    @Bind(R.id.toolbar)
    TitleBar toolbar;
    @Bind(R.id.img)
    ImageView img;

    @Override
    public int setLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void initViews() {
        toolbar.setLeftClick(() -> finish());
        url = getIntent().getStringExtra("url");
        Glide.with(this)
                .load(url)
                .error(R.mipmap.no_img)
                .into(img);
    }

    @Override
    public void initEvents() {

    }
}
