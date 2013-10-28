package src.com.example.opticalflow;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.highgui.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements CvCameraViewListener2 {
	protected Mat recent;
	protected Mat prev;
	
	public static int MAX_CORNERS = 500;
	private CameraBridgeViewBase   mOpenCvCameraView;
	//private VideoCapture videoCapture = new VideoCapture();
	 private static final String TAG = "OCVSample::Activity";
     private Mat mRgba;
     private Mat mGray;
     private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
         @Override
         public void onManagerConnected(int status) {
             switch (status) {
                 case LoaderCallbackInterface.SUCCESS:
                 {
                     Log.i(TAG, "OpenCV loaded successfully");
                     mOpenCvCameraView.enableView();
                 } break;
                 default:
                 {
                     super.onManagerConnected(status);
                 } break;
             }
         }
     };
     
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_surface_view);
		mOpenCvCameraView.setCvCameraViewListener(this);
	}
	
	@Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
	
	 @Override
	    public void onResume()
	    {
	        super.onResume();
	       
			OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	    }
	 @Override
	 public void onDestroy() {
	        super.onDestroy();
	        if (mOpenCvCameraView != null)
	            mOpenCvCameraView.disableView();
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCameraViewStarted(int width, int height) {
		
		
	}

	@Override
	public void onCameraViewStopped() {
		
	}

	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		if(prev != null){
			recent = inputFrame.gray(); 
		}else{
			prev = recent;
			recent = inputFrame.gray();
			Mat flow = new Mat();
			
			Video.calcOpticalFlowSF(prev, recent, flow, 3, 2, 4);
			
			
			//Video.calcOpticalFlowPyrLK(prev, recent, prevPts, nextPts, status, err, winSize, maxLevel);
			
		}
		
		
		
		return inputFrame.gray();
	}
	
}
