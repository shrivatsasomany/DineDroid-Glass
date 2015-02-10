package com.main.dinedroid.glassactivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.jzplusplus.glasswificonnect.R;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
/* Import ZBar Class files */
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity
{
	final static String TAG = "DineDroidGlass";

	private Camera mCamera;
	private CameraPreview mPreview;
	private Handler autoFocusHandler;
	private String text;

	Button scanButton;
	FrameLayout preview;

	ImageScanner scanner;

	private boolean previewing = true;

	static {
		System.loadLibrary("iconv");
	} 

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		autoFocusHandler = new Handler();

		//For some reason, right after launching from the "ok, glass" menu the camera is locked
		//Try 3 times to grab the camera, with a short delay in between.
		for(int i=0; i < 3; i++)
		{
			mCamera = getCameraInstance();
			if(mCamera != null) break;

			//Toast.makeText(this, "Couldn't lock camera, trying again in 1 second", Toast.LENGTH_SHORT).show();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(mCamera == null)
		{
			Toast.makeText(this, "Camera cannot be locked", Toast.LENGTH_SHORT).show();
			finish();
		}

		/* Instance barcode scanner */
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
		preview = (FrameLayout)findViewById(R.id.cameraPreview);
		preview.addView(mPreview);
		Toast.makeText(getApplicationContext(), "Please Scan your QR Code", Toast.LENGTH_LONG).show();
	}

	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		try {
			c = Camera.open();
			Log.d(TAG, "getCamera = " + c);
		} catch (Exception e){
			Log.d(TAG, e.toString());
		}
		return c;
	}

	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCB);
		}
	};

	PreviewCallback previewCb = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					text = sym.getData();
					Log.d("QR Result", text);
					String[] split_result = text.split("\\|\\|");
					Intent i = new Intent(MainActivity.this, CardViewActivity.class);
					i.putExtra("qr_result", split_result);
					startActivity(i);
					finish();
					break;
				}
			}
		}
	};



	// Mimic continuous auto-focusing
	AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};





}
