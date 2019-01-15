package com.auroral.camera.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.auroral.camera.activity.CameraActivity;

import java.io.File;

/**
 * Created by Auroral 2018/11/28 11:23
 */
public class CameraUtils {

    private Activity mActivity;
    private final int GET_REQUEST = 1;
    private CameraListener mCameraListener;
    private PermissionsUtils mPermissionsUtils;

    private CameraUtils(Activity activity) {
        mActivity = activity;
        mPermissionsUtils = PermissionsUtils.getInstance(mActivity, GET_REQUEST);
    }

    public static CameraUtils getInstance(Activity activity) {
        return new CameraUtils(activity);
    }

    public void startCamera() {
        if (mPermissionsUtils.checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA)) {
            openCamera();
        }
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_REQUEST) {
            String path;
            if (resultCode == 98) {
                path = data.getStringExtra("path");
                updateSystemImageDatabase(path);
                mCameraListener.cameraResult(path, MediaType.PICTURE);
            }
            if (resultCode == 99) {
                path = data.getStringExtra("path");
                updateSystemImageDatabase(path);
                mCameraListener.cameraResult(path, MediaType.VIDEO);
            }
            if (resultCode == 100) {
                mCameraListener.errorResult("请检查相机权限,没有权限", ErrorType.PERMISSIONS);
            }
        }
    }

    public void requestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (mPermissionsUtils.requestPermissionsResult(requestCode, grantResults)) {
            openCamera();
        } else {
            mCameraListener.errorResult("请检查相机权限,没有权限", ErrorType.PERMISSIONS);
        }
    }

    public void setCameraListener(CameraListener cameraListener) {
        mCameraListener = cameraListener;
    }

    public interface CameraListener {
        void errorResult(String msg, ErrorType type);

        void cameraResult(String path, MediaType type);
    }

    /**
     * media type.
     */
    public enum MediaType {
        /**
         * picture
         */
        PICTURE,
        /**
         * video
         */
        VIDEO
    }

    /**
     * error type.
     */
    public enum ErrorType {
        PERMISSIONS
    }

    private void openCamera() {
        mActivity.startActivityForResult(new Intent(mActivity, CameraActivity.class), GET_REQUEST);
    }

    /**
     * update system image database.
     *
     * @param path path
     */
    private void updateSystemImageDatabase(String path) {
        Uri localUri = Uri.fromFile(new File(path));
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        mActivity.sendBroadcast(localIntent);
    }
}
