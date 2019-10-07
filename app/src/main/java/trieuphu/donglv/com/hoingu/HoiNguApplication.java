package trieuphu.donglv.com.hoingu;

import android.app.Application;

public class HoiNguApplication extends Application {
    private static HoiNguApplication instance;

    public static synchronized HoiNguApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }
}
