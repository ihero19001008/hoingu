package trieuphu.donglv.com.hoingu.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import trieuphu.donglv.com.hoingu.HoiNguApplication;
import trieuphu.donglv.com.hoingu.R;
import trieuphu.donglv.com.hoingu.listener.OnTouchClickListener;
import trieuphu.donglv.com.hoingu.model.Question;
import trieuphu.donglv.com.hoingu.util.Const;
import trieuphu.donglv.com.hoingu.util.DatabaseManager;
import trieuphu.donglv.com.hoingu.util.PlayMusic;
import trieuphu.donglv.com.hoingu.util.SharePreferenceUtils;

public class PlayGameActivity extends AppCompatActivity implements RewardedVideoAdListener {

    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.btn_a)
    Button btnA;
    @BindView(R.id.btn_b)
    Button btnB;
    @BindView(R.id.btn_c)
    Button btnC;
    @BindView(R.id.btn_d)
    Button btnD;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_sound)
    ImageView imgSound;
    @BindView(R.id.img_high)
    ImageView imgHigh;
    @BindView(R.id.img_video)
    ImageView imgVideo;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.tv_quesstion)
    TextView tvQuesstion;
    @BindView(R.id.tv_number_quesstion)
    TextView tvNumberQuesstion;
    @BindView(R.id.tv_bad)
    TextView tvBad;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;
    @BindView(R.id.adView)
    AdView adView;
    private DatabaseManager databaseManager;
    private ArrayList<Question> questions;
    private Question question;
    private int countPass = 0;
    private int countLive = 3;
    private boolean isSound = true;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean isComplete = false;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        ButterKnife.bind(this);
        initAdview();
        initWatchVideo();
        initViews();
        initData();
    }

    private void initWatchVideo() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getResources().getString(R.string.app_id_video),//use this id for testing
                new AdRequest.Builder().build());
    }

    private void initAdview() {
        MobileAds.initialize(this, getResources().getString(R.string.app_id));
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
    }

    private void initData() {
    }

    private void initViews() {
        countLive = getIntent().getIntExtra(Const.KEY_COUNT_LIVE, 3);
        countPass = getIntent().getIntExtra(Const.KEY_COUNT_PASS, 0);
        isSound = getIntent().getBooleanExtra(Const.SHARE_SOUND, true);
        if (isSound) {
            imgSound.setImageResource(R.mipmap.ic_on);
        } else {
            imgSound.setImageResource(R.mipmap.ic_off);
        }
        questions = new ArrayList<>();
        databaseManager = new DatabaseManager(this);
        question = databaseManager.getOneQuestion();
        questions = databaseManager.getListQuestion();
        tvNumberQuesstion.setText("Câu: " + (countPass + 1) + "/" + questions.size());

        if (question.getNickName().equals("null") ||
                question.getNickName().isEmpty()) {
            tvAuthor.setText("Của: " + "Sơn Tùng");
        }
        tvAuthor.setText("Của: " + question.getNickName());
        tvQuesstion.setText("Câu " + (countPass + 1) + ": " + question.getQuestion());
        btnA.setText(question.getAnswerA());
        btnB.setText(question.getAnswerB());
        btnC.setText(question.getAnswerC());
        btnD.setText(question.getAnswerD());
        setOnClickButton();
        setOnClickNavigationBar();
    }

    private void setOnClickNavigationBar() {
        imgBack.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, 20, PlayGameActivity.this));

    }

    private void setOnClickButton() {
        btnA.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.getAnswerTrue().equals("a")) {
                    lose();
                } else {
                    win();
                }
            }
        }, 20, this));
        btnB.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.getAnswerTrue().equals("b")) {
                    lose();
                } else {
                    win();
                }
            }
        }, 20, this));
        btnC.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.getAnswerTrue().equals("c")) {
                    lose();
                } else {
                    win();
                }
            }
        }, 20, this));
        btnD.setOnTouchListener(new OnTouchClickListener(new OnTouchClickListener.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!question.getAnswerTrue().equals("d")) {
                    lose();
                } else {
                    win();
                }
            }
        }, 20, this));
    }

    private void win() {
        if (isSound) {
            PlayMusic.playCorrect(HoiNguApplication.getInstance());
        }
        countPass++;
        Intent intent = new Intent(PlayGameActivity.this, ExplainActivity.class);
        intent.putExtra(Const.KEY_EXPLAIN, question.getGiaiThich());
        intent.putExtra(Const.KEY_COUNT_LIVE, countLive);
        intent.putExtra(Const.KEY_COUNT_PASS, countPass);
        intent.putExtra(Const.SHARE_SOUND, isSound);
        startActivity(intent);
        finish();
    }

    private void lose() {
        countLive--;
        if (countLive == 0) {
            Intent intent = new Intent(PlayGameActivity.this, GameOverActivity.class);
            intent.putExtra(Const.KEY_EXPLAIN, question.getGiaiThich());
            intent.putExtra(Const.KEY_COUNT_PASS, countPass);
            if (isSound) {
                PlayMusic.playSad(HoiNguApplication.getInstance());
            }
            startActivity(intent);
            finish();
        } else {
            if (isSound)
                PlayMusic.playUhOh(HoiNguApplication.getInstance());
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
            tvBad.setText("Ngu: " + countLive);
            tvBad.startAnimation(anim);
        }
    }

    @OnClick({R.id.img_sound, R.id.img_high, R.id.img_video, R.id.img_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_sound:
                if (isSound) {
                    imgSound.setImageResource(R.mipmap.ic_off);
                    isSound = false;
                } else {
                    imgSound.setImageResource(R.mipmap.ic_on);
                    isSound = true;
                }

                break;
            case R.id.img_high:
                final Dialog dialog = new Dialog(PlayGameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_high_score);
                TextView textViewHighScore = dialog.findViewById(R.id.tv_high);
                int highScore = SharePreferenceUtils.getIntData(PlayGameActivity.this, Const.HIGH_SCORE);
                textViewHighScore.setText(String.format("%s Câu", String.valueOf(highScore)));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;

                dialog.getWindow().setAttributes(lp);
                Button buttonNext = dialog.findViewById(R.id.btn_play_next);
                buttonNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.img_video:
                final Dialog dialog1 = new Dialog(PlayGameActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(false);
                dialog1.setContentView(R.layout.dialog_watch_video);

                WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
                lp1.copyFrom(dialog1.getWindow().getAttributes());
                lp1.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp1.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp1.gravity = Gravity.CENTER;

                dialog1.getWindow().setAttributes(lp1);
                Button buttonWatch = dialog1.findViewById(R.id.btn_watch);
                Button buttonCancel = dialog1.findViewById(R.id.btn_cacel);
                buttonWatch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        } else {
                            Toast.makeText(PlayGameActivity.this, "Hiện tại không có video nhận thưởng nào!", Toast.LENGTH_SHORT).show();
                        }
                        dialog1.dismiss();
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            case R.id.img_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Ứng dụng sử dụng tốt lắm: https://play.google.com/store/apps/details?id=com.dong.luong.hoingu");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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
        if (adView != null) {
            adView.resume();
        }
        if (isComplete == true) {
            tvBad.setText("Ngu: " + countLive);
            isComplete = false;
        } else {
            tvBad.setText("Ngu: " + countLive);
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Log.d("luongvandong", "on rewarded video Ad loaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.d("luongvandong", "on rewarded video Ad opened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.d("luongvandong", "on rewarded video Ad Started");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Log.d("luongvandong", "on rewarded video Ad Closed");
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Log.d("luongvandong", "on rewarded video Ad Rewared");
        isComplete = true;
        countLive = countLive + 1;
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.d("luongvandong", "on rewarded video Ad left application");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
}
