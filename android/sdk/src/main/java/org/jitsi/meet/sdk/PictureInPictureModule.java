package org.jitsi.meet.sdk;

import android.app.Activity;
import android.app.PictureInPictureParams;
import android.os.Build;
import android.util.Log;
import android.util.Rational;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class PictureInPictureModule extends ReactContextBaseJavaModule {
    private final static String TAG = "PictureInPicture";

    static boolean isPictureInPictureSupported() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public PictureInPictureModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * Enters Picture-in-Picture (mode) for the current {@link Activity}.
     * Supported on Android API >= 26 (Oreo) only.
     *
     * @throws IllegalStateException if {@link #isPictureInPictureSupported()}
     * returns {@code false} or if {@link #getCurrentActivity()} returns
     * {@code null}.
     * @throws RuntimeException if
     * {@link Activity#enterPictureInPictureMode(PictureInPictureParams)} fails.
     * That method can also throw a {@link RuntimeException} in various cases,
     * including when the activity is not visible (paused or stopped), if the
     * screen is locked or if the user has an activity pinned.
     */
    public void enterPictureInPicture() {
        if (!isPictureInPictureSupported()) {
            throw new IllegalStateException("Picture-in-Picture not supported");
        }

        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            throw new IllegalStateException("No current Activity!");
        }

        Log.d(TAG, "Entering Picture-in-Picture");

        PictureInPictureParams.Builder builder
            = new PictureInPictureParams.Builder()
                .setAspectRatio(new Rational(1, 1));

        // https://developer.android.com/reference/android/app/Activity.html#enterPictureInPictureMode(android.app.PictureInPictureParams)
        //
        // The system may disallow entering picture-in-picture in various cases,
        // including when the activity is not visible, if the screen is locked
        // or if the user has an activity pinned.
        if (!currentActivity.enterPictureInPictureMode(builder.build())) {
            throw new RuntimeException("Failed to enter Picture-in-Picture");
        }
    }

    /**
     * Enters Picture-in-Picture (mode) for the current {@link Activity}.
     * Supported on Android API >= 26 (Oreo) only.
     *
     * @param promise a {@code Promise} which will resolve with a {@code null}
     * value upon success, and an {@link Exception} otherwise.
     */
    @ReactMethod
    public void enterPictureInPicture(Promise promise) {
        try {
            enterPictureInPicture();
            promise.resolve(null);
        } catch (RuntimeException re) {
            promise.reject(re);
        }
    }

    @Override
    public String getName() {
        return TAG;
    }
}
