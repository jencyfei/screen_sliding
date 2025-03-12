package com.example.screensliding;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;

public class AutoScrollService extends AccessibilityService {
    private static final long SCROLL_DELAY = 2000; // 滑动间隔时间
    private static final long AUTO_STOP_DELAY = 180000; // 3分钟后自动停止
    private Handler handler;
    private boolean isScrolling = false;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 不需要处理任何辅助功能事件
    }

    @Override
    public void onInterrupt() {
        stopScrolling();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        startScrolling();
        // 3分钟后自动停止
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopScrolling();
                stopSelf();
            }
        }, AUTO_STOP_DELAY);
    }

    private void startScrolling() {
        if (isScrolling) return;
        isScrolling = true;
        performScroll();
    }

    private void stopScrolling() {
        isScrolling = false;
        handler.removeCallbacksAndMessages(null);
    }

    private void performScroll() {
        if (!isScrolling) return;

        Path path = new Path();
        path.moveTo(500, 1000);
        path.lineTo(500, 500);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 500));

        dispatchGesture(builder.build(), new GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                if (isScrolling) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            performScroll();
                        }
                    }, SCROLL_DELAY);
                }
            }
        }, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScrolling();
    }
}