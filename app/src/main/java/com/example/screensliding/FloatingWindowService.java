package com.example.screensliding;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class FloatingWindowService extends Service {
    private WindowManager windowManager;
    private View floatingView;
    private WindowManager.LayoutParams params;
    private float initialX, initialY;
    private float initialTouchX, initialTouchY;
    private boolean isLongPress = false;
    private boolean isScrolling = false;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFloatingWindow();
    }

    private void initializeFloatingWindow() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        floatingView = LayoutInflater.from(this).inflate(R.layout.float_window, null);

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        params.gravity = Gravity.TOP | Gravity.START;

        Button controlButton = floatingView.findViewById(R.id.control_button);
        setupTouchListener(controlButton);

        windowManager.addView(floatingView, params);
    }

    private void setupTouchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            private long startTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startTime = System.currentTimeMillis();
                        initialX = params.x;
                        initialY = params.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;

                    case MotionEvent.ACTION_UP:
                        long endTime = System.currentTimeMillis();
                        if (endTime - startTime > 500) { // 长按检测
                            stopSelf();
                        } else if (!isLongPress && Math.abs(event.getRawX() - initialTouchX) < 5
                                && Math.abs(event.getRawY() - initialTouchY) < 5) {
                            toggleScrolling();
                        }
                        isLongPress = false;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        if (!isLongPress && System.currentTimeMillis() - startTime > 500) {
                            isLongPress = true;
                        }
                        if (!isLongPress) {
                            params.x = (int) (initialX + (event.getRawX() - initialTouchX));
                            params.y = (int) (initialY + (event.getRawY() - initialTouchY));
                            windowManager.updateViewLayout(floatingView, params);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    private void toggleScrolling() {
        isScrolling = !isScrolling;
        if (isScrolling) {
            Intent intent = new Intent(this, AutoScrollService.class);
            startService(intent);
        } else {
            stopService(new Intent(this, AutoScrollService.class));
        }
        updateButtonAppearance();
    }

    private void updateButtonAppearance() {
        Button controlButton = floatingView.findViewById(R.id.control_button);
        controlButton.setBackgroundResource(isScrolling ? 
            R.drawable.button_active : R.drawable.button_inactive);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null && windowManager != null) {
            windowManager.removeView(floatingView);
        }
        stopService(new Intent(this, AutoScrollService.class));
    }
}