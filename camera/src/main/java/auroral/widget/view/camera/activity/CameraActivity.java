package auroral.widget.view.camera.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.auroral.camera.R;
import auroral.widget.view.camera.listener.CameraListener;
import auroral.widget.view.camera.listener.ClickListener;
import auroral.widget.view.camera.listener.ErrorListener;
import auroral.widget.view.camera.util.FileUtils;
import auroral.widget.view.camera.view.CameraView;

import java.io.File;


public class CameraActivity extends AppCompatActivity {
    private CameraView mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.came_activity_camera);
        mCameraView = findViewById(R.id.cv_camera);
        //设置视频保存路径
        mCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().getPath() + File.separator + "ACamera");
        mCameraView.setFeatures(CameraView.BUTTON_STATE_ONLY_CAPTURE);
        mCameraView.setMediaQuality(CameraView.MEDIA_QUALITY_MIDDLE);
        mCameraView.hideRightIcon();
        mCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                Intent intent = new Intent();
                setResult(100, intent);
                finish();
            }

            @Override
            public void AudioPermissionError() {
            }
        });
        //JCameraView监听
        mCameraView.setCameraLisenter(new CameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                String path = FileUtils.saveBitmap("ACamera", bitmap);
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(98, intent);
                finish();
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                Intent intent = new Intent();
                intent.putExtra("path", url);
                setResult(99, intent);
                finish();
            }
        });

        mCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CameraActivity.this.finish();
            }
        });
        mCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(CameraActivity.this, "Right", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //全屏显示
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mCameraView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.onPause();
    }
}
