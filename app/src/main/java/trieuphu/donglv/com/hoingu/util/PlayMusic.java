package trieuphu.donglv.com.hoingu.util;

import android.content.Context;
import android.media.MediaPlayer;

import trieuphu.donglv.com.hoingu.R;

public class PlayMusic {
    public static MediaPlayer mp;

    public static void playCorrect(Context context) {
        mp = MediaPlayer.create(context, R.raw.correct);
        mp.start();
    }

    public static void playUhOh(Context context) {
        mp = MediaPlayer.create(context, R.raw.uh_oh);
        mp.start();
    }

    public static void playSad(Context context) {
        mp = MediaPlayer.create(context, R.raw.sad_trombone);
        mp.start();
    }
}
