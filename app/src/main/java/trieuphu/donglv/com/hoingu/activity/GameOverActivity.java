package trieuphu.donglv.com.hoingu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trieuphu.donglv.com.hoingu.MainActivity;
import trieuphu.donglv.com.hoingu.R;
import trieuphu.donglv.com.hoingu.util.Const;
import trieuphu.donglv.com.hoingu.util.SharePreferenceUtils;

public class GameOverActivity extends AppCompatActivity {

    @BindView(R.id.lbl_noti)
    Button lblNoti;
    @BindView(R.id.lbl_dapan)
    TextView lblDapan;
    @BindView(R.id.lbl_score)
    TextView lblScore;
    @BindView(R.id.lbl_highscore)
    TextView lblHighscore;
    @BindView(R.id.btn_watch)
    Button btnWatch;
    @BindView(R.id.btn_play_again)
    Button btnPlayAgain;
    @BindView(R.id.lose)
    LinearLayout lose;
    @BindView(R.id.adView)
    AdView adView;
    private String explain;
    private int countPass = 0;
    private InterstitialAd interstitialAd;
    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        ButterKnife.bind(this);
        initAdmob();
        initAdmobFull();
        initViews();
        initData();
    }

    private void initAdmob() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    private void initAdmobFull() {
        interstitialAd = new InterstitialAd(GameOverActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        loadInterstitialAd();
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Intent intent = new Intent(GameOverActivity.this, PlayGameActivity.class);
                startActivity(intent);
                finish();
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

    private void initData() {
        explain = getIntent().getStringExtra(Const.KEY_EXPLAIN);
        countPass = getIntent().getIntExtra(Const.KEY_COUNT_PASS, 0);
        if (explain.isEmpty() || explain.equals("") || explain.trim().length() == 0 || explain == null || explain.equals("null") || explain.equals("    ")) {
            lblDapan.setText("Rất tiếc bạn đã quá đen");
        } else
            lblDapan.setText(String.format("Đáp án%s", explain));
        lblScore.setText(String.format("Điểm: %d", (countPass)));
        if (countPass > SharePreferenceUtils.getIntData(GameOverActivity.this, Const.HIGH_SCORE)) {
            SharePreferenceUtils.insertIntData(GameOverActivity.this, Const.HIGH_SCORE, countPass);
            lblHighscore.setText("Điểm cao nhất " + countPass);
        } else {
            lblHighscore.setText("Điểm cao nhất " + SharePreferenceUtils.getIntData(GameOverActivity.this, Const.HIGH_SCORE));
        }
    }

    private void initViews() {
    }

    @OnClick({R.id.btn_play_again, R.id.btn_watch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_watch:
                break;
            case R.id.btn_play_again:
                count++;
                if (count == 2 && interstitialAd.isLoaded()) {
                    count = 0;
                    interstitialAd.show();
                } else {
                    Intent intent = new Intent(GameOverActivity.this, PlayGameActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
        }
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
