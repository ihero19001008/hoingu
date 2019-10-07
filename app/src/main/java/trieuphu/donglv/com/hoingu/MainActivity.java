package trieuphu.donglv.com.hoingu;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trieuphu.donglv.com.hoingu.activity.PlayGameActivity;
import trieuphu.donglv.com.hoingu.listener.OnTouchClickListener;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    AnimationDrawable ad;
    @BindView(R.id.btn_play_game)
    Button btnPlayGame;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;
    @BindView(R.id.btn_high)
    Button btnHigh;
    @BindView(R.id.btn_game_difference)
    Button btnGameDifference;
    @BindView(R.id.btn_add_quessiton)
    Button btnAddQuessiton;
    @BindView(R.id.adView)
    AdView adView;
    private InterstitialAd interstitialAd;
    public static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initAdView();
        initAdmob();
        initViews();
        initData();
    }

    private void initAdmob() {
        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        loadInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });
    }

    private void loadInterstitialAd() {

        AdRequest adRequest = new AdRequest.Builder()
//                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        interstitialAd.loadAd(adRequest);

    }

    private void initAdView() {
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    private void initData() {

    }

    private void initViews() {
        ad = (AnimationDrawable) imgIcon.getBackground();
        btnPlayGame.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count == 5 && interstitialAd.isLoaded()) {
                    count = 0;
                    interstitialAd.show();
                } else {
                    Intent intent = new Intent(MainActivity.this, PlayGameActivity.class);
                    startActivity(intent);
                }
            }
        }, 20, MainActivity.this));

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        ad.start();
    }

    @OnClick({R.id.btn_high, R.id.btn_game_difference, R.id.btn_add_quessiton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_high:
                onBackPressed();
                break;
            case R.id.btn_game_difference:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.dong.luong.ailatrieuphu"));
                startActivity(intent);
                break;
            case R.id.btn_add_quessiton:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/16nTt2Iqbewkllc24euq_zDeiwOmivgDN5tpPY-o4U24/edit"));
                startActivity(browserIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadInterstitialAd();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
