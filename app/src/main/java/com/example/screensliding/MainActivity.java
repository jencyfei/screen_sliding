package com.example.screensliding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1;
    private Button startServiceBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startServiceBtn = findViewById(R.id.start_service_btn);
        startServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Settings.canDrawOverlays(MainActivity.this)) {
                    requestOverlayPermission();
                } else {
                    startFloatingWindowService();
                }
            }
        });
    }

    private void requestOverlayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startFloatingWindowService();
            } else {
                Toast.makeText(this, "需要悬浮窗权限来显示控制按钮", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startFloatingWindowService() {
        Intent intent = new Intent(MainActivity.this, FloatingWindowService.class);
        startService(intent);
        finish();
    }
}