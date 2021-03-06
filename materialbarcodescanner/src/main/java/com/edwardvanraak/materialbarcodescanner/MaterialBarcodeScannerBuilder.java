package com.edwardvanraak.materialbarcodescanner;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Camera;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.greenrobot.eventbus.EventBus;

public class MaterialBarcodeScannerBuilder {

    protected Activity mActivity;
    protected ViewGroup mRootView;

    protected CameraSource mCameraSource;

    protected BarcodeDetector mBarcodeDetector;

    protected boolean mUsed = false; //used to check if a builder is only used

    protected int mFacing = CameraSource.CAMERA_FACING_BACK;
    protected boolean mAutoFocusEnabled = false;

    protected MaterialBarcodeScanner.OnResultListener onResultListener;

    protected int mTrackerColor = Color.parseColor("#F44336"); //Material Red 500

    protected boolean mBleepEnabled = false;

    protected boolean mFlashEnabledByDefault = false;

    protected String mText = "";
    /**
     * Default constructor
     */
    public MaterialBarcodeScannerBuilder() {

    }

    /**
     * Called immediately after a barcode was scanned
     * @param onResultListener
     */
    public MaterialBarcodeScannerBuilder withResultListener(@NonNull MaterialBarcodeScanner.OnResultListener onResultListener){
        this.onResultListener = onResultListener;
        return this;
    }

    /**
     * Construct a MaterialBarcodeScannerBuilder by passing the activity to use for the generation
     *
     * @param activity current activity which will contain the drawer
     */
    public MaterialBarcodeScannerBuilder(@NonNull Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.mActivity = activity;
    }

    /**
     * Sets the activity which will be used as the parent of the MaterialBarcodeScanner activity
     * @param activity current activity which will contain the MaterialBarcodeScanner
     */
    public MaterialBarcodeScannerBuilder withActivity(@NonNull Activity activity) {
        this.mRootView = (ViewGroup) activity.findViewById(android.R.id.content);
        this.mActivity = activity;
        return this;
    }

    /**
     * Makes the barcode scanner use the camera facing back
     */
    public MaterialBarcodeScannerBuilder withBackfacingCamera(){
        mFacing = CameraSource.CAMERA_FACING_BACK;
        return this;
    }

    /**
     * Makes the barcode scanner use camera facing front
     */
    public MaterialBarcodeScannerBuilder withFrontfacingCamera(){
        mFacing = CameraSource.CAMERA_FACING_FRONT;
        return this;
    }

    /**
     * Either CameraSource.CAMERA_FACING_FRONT or CameraSource.CAMERA_FACING_BACK
     * @param cameraFacing
     */
    public MaterialBarcodeScannerBuilder withCameraFacing(int cameraFacing){
        mFacing = cameraFacing;
        return this;
    }

    /**
     * Enables or disables auto focusing on the camera
     */
    public MaterialBarcodeScannerBuilder withEnableAutoFocus(boolean enabled){
        mAutoFocusEnabled = enabled;
        return this;
    }

    /**
     * Sets the tracker color used by the barcode scanner, By default this is Material Red 500 (#F44336).
     * @param color
     */
    public MaterialBarcodeScannerBuilder withTrackerColor(int color){
        mTrackerColor = color;
        return this;
    }

    /**
     * Enables or disables a bleep sound whenever a barcode is scanned
     */
    public MaterialBarcodeScannerBuilder withBleepEnabled(boolean enabled){
        mBleepEnabled = enabled;
        return this;
    }

    /**
     * Shows a text message at the top of the barcode scanner
     */
    public MaterialBarcodeScannerBuilder withText(String text){
        mText = text;
        return this;
    }

    /**
     * Shows a text message at the top of the barcode scanner
     */
    public MaterialBarcodeScannerBuilder withFlashLightEnabledByDefault(){
        mFlashEnabledByDefault = true;
        return this;
    }

    /**
     * Build a ready to use MaterialBarcodeScanner
     *
     * @return A ready to use MaterialBarcodeScanner
     */
    public MaterialBarcodeScanner build() {
        if (mUsed) {
            throw new RuntimeException("You must not reuse a MaterialBarcodeScanner builder");
        }
        if (mActivity == null) {
            throw new RuntimeException("Please pass an activity to the MaterialBarcodeScannerBuilder");
        }
        mUsed = true;
        buildMobileVisionBarcodeDetector();
        MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScanner(this);
        materialBarcodeScanner.setOnResultListener(onResultListener);
        return materialBarcodeScanner;
    }

    /**
     * Build a barcode scanner using the Mobile Vision Barcode API
     */
    private void buildMobileVisionBarcodeDetector() {
        String focusMode = Camera.Parameters.FOCUS_MODE_FIXED;
        if(mAutoFocusEnabled){
            focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;
        }
        mBarcodeDetector = new BarcodeDetector.Builder(mActivity).build();
        mCameraSource = new CameraSource.Builder(mActivity, mBarcodeDetector)
                .setFacing(mFacing)
                .setFlashMode(mFlashEnabledByDefault ? Camera.Parameters.FLASH_MODE_TORCH : null)
                .setFocusMode(focusMode)
                .build();
    }

    /**
     * Get the activity associated with this builder
     * @return
     */
    public Activity getActivity() {
        return mActivity;
    }

    /**
     * Get the barcode detector associated with this builder
     * @return
     */
    public BarcodeDetector getBarcodeDetector() {
        return mBarcodeDetector;
    }

    /**
     * Get the camera source associated with this builder
     * @return
     */
    public CameraSource getCameraSource() {
        return mCameraSource;
    }


    /**
     * Get the tracker color associated with this builder
     * @return
     */
    public int getTrackerColor() {
        return mTrackerColor;
    }

    /**
     * Get the text associated with this builder
     * @return
     */
    public String getText() {
        return mText;
    }

    /**
     * Get the bleep enabled value associated with this builder
     * @return
     */
    public boolean isBleepEnabled() {
        return mBleepEnabled;
    }

    /**
     * Get the flash enabled by default value associated with this builder
     * @return
     */
    public boolean isFlashEnabledByDefault() {
        return mFlashEnabledByDefault;
    }

    public void clean() {
        mActivity = null;
    }
}
