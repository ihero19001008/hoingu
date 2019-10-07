package trieuphu.donglv.com.hoingu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import trieuphu.donglv.com.hoingu.R;
import trieuphu.donglv.com.hoingu.util.Const;

public class ExplainActivity extends AppCompatActivity {

    @BindView(R.id.btn_explain)
    Button btnExplain;
    @BindView(R.id.btn_play_next)
    Button btnPlayNext;
    @BindView(R.id.adView)
    AdView adView;
    private String explain;
    private int countLive = 0;
    private int countPass = 0;
    private boolean isSound = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explain);
        ButterKnife.bind(this);
        initAdmob();
        initViews();
        initData();
    }

    private void initAdmob() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    private void initData() {
    }

    private void initViews() {
        explain = getIntent().getStringExtra(Const.KEY_EXPLAIN);
        countLive = getIntent().getIntExtra(Const.KEY_COUNT_LIVE, 0);
        countPass = getIntent().getIntExtra(Const.KEY_COUNT_PASS, 0);
        isSound = getIntent().getBooleanExtra(Const.SHARE_SOUND, true);
        if (explain.isEmpty() || explain.equals("") || explain.trim().length() == 0 || explain == null || explain.equals("null") || explain.equals("    ")) {
            btnExplain.setText("Con cái nhà ai mà giỏi thế");
        } else
            btnExplain.setText(explain);

        btnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExplainActivity.this, PlayGameActivity.class);
                intent.putExtra(Const.KEY_COUNT_LIVE, countLive);
                intent.putExtra(Const.KEY_COUNT_PASS, countPass);
                intent.putExtra(Const.SHARE_SOUND, isSound);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
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
