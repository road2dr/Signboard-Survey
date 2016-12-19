package com.mjict.signboardsurvey.activity;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.mjict.signboardsurvey.R;

import java.util.List;

/**
 * 
 * 
 * @author Junseo
 *
 */
public class CameraActivity extends SABaseActivity implements SurfaceHolder.Callback {
//	public static final int 
	
	private Camera camera;
	
	private SurfaceView cameraPreview;	
	
	private ImageButton shootButton;
	private SeekBar zoomSeekBar;
	
	private int lastOrientation;
//	private int lastAngle;
	
	private onPictureTakenListener pirctureListener;
	
	OrientationEventListener orientEventListener;
	
	private float focalLength = -1;
	private float horizontalAngle = -1;
	private float verticalAngle = -1;

	private int lastButtonDegree = 0;


	@Override
	protected int getContentLayout() {
		return R.layout.activity_camera;
	}

	@Override
	protected void init() {
		super.init();
		this.hideToolBar();
		this.disableNavigation();

		initCamera();
		
		cameraPreview = (SurfaceView)this.findViewById(R.id.cameraPreview);
		SurfaceHolder holder = cameraPreview.getHolder();
		holder.setSizeFromLayout();
		holder.addCallback(this);
		
		cameraPreview.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				previewClicked();
			}
		});
		
		orientEventListener = new OrientationCheckListener(this);
        if (orientEventListener.canDetectOrientation() == true) 
        	orientEventListener.enable();

        lastOrientation = 2;
//        lastAngle = 0;
		
		shootButton = (ImageButton)this.findViewById(R.id.shootButton);
		
		zoomSeekBar = (SeekBar)this.findViewById(R.id.zoomSeekBar);
		zoomSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				Parameters param = camera.getParameters();
				int max = param.getMaxZoom();
				
				int zoom = progress* max / 100;
				param.set("zoom", zoom);
				camera.setParameters(param);
			}
		});


	}

	public void setShootButtonOnClickListener(OnClickListener listener) {
		shootButton.setOnClickListener(listener);
	}


	@Override
	protected void onResume() {
		super.onResume();

		if (orientEventListener != null && orientEventListener.canDetectOrientation() == true)
			orientEventListener.enable();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if(orientEventListener != null)
			orientEventListener.disable();
	}

	public void stopCamera() {
		camera.stopPreview();
	}
	
	public void takePicture(onPictureTakenListener listener) {
//		Log.d("junseo", "take picture lastOrientation: "+lastOrientation);
//		final int orientation = (lastOrientation == 0 || lastOrientation == 2) ? 1 : 0;
		
		pirctureListener = listener;
		shootButton.setEnabled(false);
		focusEnabled = false;
		camera.takePicture(new ShutterCallback() {
			
			@Override
			public void onShutter() {
				Log.d("junseo", "onShutter");
//				camera.stopPreview();
			}
		}, new PictureCallback() {			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
				Log.d("junseo", "onPictureTaken - raw");
//				if(data != null) {
//					Bitmap pic = BitmapFactory.decodeByteArray(data, 0, data.length);
////					capturedPictureView.setImageBitmap(pic);
//				}
				
//				camera.stopPreview();
			}
		}, new PictureCallback() {
			
			@Override
			public void onPictureTaken(byte[] data, Camera camera) {
//				Log.d("junseo", "camera orientation: "+camera.)
				
				Camera.CameraInfo info = new Camera.CameraInfo();
				   Camera.getCameraInfo(0, info);

				   Log.d("junseo", "onPictureTaken orientation: "+lastOrientation);
				
				if(data != null) {
//					Bitmap pic = BitmapFactory.decodeByteArray(data, 0, data.length);				

					if(pirctureListener != null)
						pirctureListener.ontPictureTaken(lastOrientation, data);
//					Matrix rotateMatrix = new Matrix();
//					rotateMatrix.postRotate(0); //-360~360
//
//					Bitmap sideInversionImg = Bitmap.createBitmap(pic, 0, 0,
//							pic.getWidth(), pic.getHeight(), rotateMatrix, false);
//					
//					Bitmap temp = sideInversionImg.copy(Config.ARGB_8888, true);
					
					
					
				}
				focusEnabled = true;
				shootButton.setEnabled(true);
			}
		});
	}
	
	public interface onPictureTakenListener {
		public void ontPictureTaken(int orientation, byte[] data);
	}
	
	public float getCameraFocalLength() {
		return focalLength;
	}
	
	public float getCameraHorizontalAngle() {
		return horizontalAngle;
	}
	
	public float getCameraVeticalAngle() {
		return verticalAngle;
	}
	
	public float getCurrentZoom() {
		int max = camera.getParameters().getMaxZoom();
		int cur = camera.getParameters().getZoom();
		
		List<Integer> zoomRatios = camera.getParameters().getZoomRatios();
//		for(int i=0; i<zr.size(); i++)
//			Log.d("junseo", "gg: "+zr.get(i));
//		
//		Log.d("junseo", "max: "+max+" cur: "+cur);
		
//		float value = 0;
//		if(cur != 0)
//			value = (float)max / (float)cur;
		
		float value = 0;
		if(cur < zoomRatios.size())
			value = (float)zoomRatios.get(cur) / 100;
		
		return value;
	}
	
	private void initCamera() {
		camera = Camera.open(0);
		
		Parameters params = camera.getParameters();
//		params = findSize(params);

		camera.setParameters(params);
		
		List<Size> picSizes = params.getSupportedPictureSizes();

		for(int i=0; i<picSizes.size(); i++) {
			Size s = picSizes.get(i);
			Log.d("junseo", "size: "+s.width+", "+s.height);
		}
//		Log.d("junseo", "������ ũ���");
		List<Size> preSizes = params.getSupportedPreviewSizes();
		for(int i=0; i<preSizes.size(); i++) {
			Size s = preSizes.get(i);
//			Log.d("junseo", "size: "+s.width+", "+s.height);
		}
		
		focalLength = params.getFocalLength();
		horizontalAngle = params.getHorizontalViewAngle();
		verticalAngle = params.getVerticalViewAngle();
		
//		String flatten = params.flatten();
		
		

//		Log.d("junseo", "flatten: "+flatten);
		

//		camera.setDisplayOrientation(90); // landscape
	}

	private boolean focusEnabled = true;
	private void previewClicked() {
		if(focusEnabled == false)
			return;

		// TODO 여기서  Process: com.mjict.signboardsurvey, PID: 28501
//		java.lang.RuntimeException: autoFocus failed 에러 발생 나중에 체크해봐
		camera.autoFocus(new AutoFocusCallback() {			
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				
			}
		});
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("junseo", "SignMesureActivity-surfaceCreated");
		if(camera == null)
			return;
		
		try {
			Parameters params = camera.getParameters();
			params.set("orientation", "landscape");
			camera.setDisplayOrientation(90);
			params.setRotation(0);
			params.setPictureSize(1920, 1080);
			params.setPreviewSize(1920, 1080);

//			params.set("orientation", "portrait");
//			params.setRotation(270);
			
			camera.setParameters(params);
			
			camera.setPreviewDisplay(cameraPreview.getHolder());
			camera.startPreview();
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d("junseo", "SignMesureActivity-surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("junseo", "SignMesureActivity-surfaceDestroyed");
		if (camera != null) {
			camera.stopPreview();
			camera.release();
			camera = null;
        }
	}
	
	private int calculateOrientation(int degree) {
		int orientation = -1;
		if(degree >= 45 && degree < 135)
			orientation = 0;
		else if(degree >= 135 && degree < 225)
			orientation = 1;
		else if(degree >= 225 && degree < 315)
			orientation = 2;
		else
			orientation = 3;
		
		return orientation;
	}
	
	private int calculateDirection(int last, int cur) {
		final int[][] orientationGraph = {
				{0, +1, 0, -1},
				{-1, 0, +1, 0},
				{0, -1, 0, +1},
				{+1, 0, -1, 0}
		};
		
		return orientationGraph[last][cur];
	}
	
	private class OrientationCheckListener extends OrientationEventListener {

		public OrientationCheckListener(Context context) {
			super(context);
		}

		@Override
		public void onOrientationChanged(int degree) {
			final int orientation = calculateOrientation(degree);
			if(orientation == lastOrientation)
				return;
			
			final int direction = calculateDirection(lastOrientation, orientation);
			
//			Log.d("junseo", "last orientation: "+lastOrientation+" current orientation: "+orientation);
			
//			int fromDegree = getDegree(lastOrientation);
//			int toDegree = getDegree(lastOrientation, orientation);
			
			int[] degrees = getDegree(lastOrientation, orientation);
			
//			Log.d("junseo", "from degree: "+degrees[0]+" toDegree: "+degrees[1]);
			
			RotateAnimation rAnim = new RotateAnimation(degrees[0], degrees[1], Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
			rAnim.setDuration(450);
			rAnim.setFillEnabled(true);
			rAnim.setFillAfter(true);
			
			rAnim.setAnimationListener(new AnimationListener() {				
				@Override
				public void onAnimationStart(Animation animation) {
				}				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}				
				@Override
				public void onAnimationEnd(Animation animation) {
					Log.d("junseo", "end of animation");
//					float cr =  shootButton.getRotation();
//					Log.d("junseo", "last rotation: "+cr);
//					
//					int rotation = 0;
//					Log.d("junseo", "orientation: "+lastOrientation);
//					
//					if(direction >=0 )
//						rotation = 270;
//					else
//						rotation = 90;
					
//					switch(orientation) {
//					case 0:		// 45 ~ 135
//						rotation = 0;
//						break;
//					case 1:		// 135 ~ 225
//						rotation = 90;		
//						break;
//					case 2:		// 225 ~ 315
//						rotation = 180;		
//						break;
//					case 3:		// 315 ~ 45
//						rotation = 270;
//						break;
//					}
//					Log.d("junseo", "set to rotation: "+rotation);
//					shootButton.setRotation(rotation);
				}
			});

			shootButton.startAnimation(rAnim);
			
//			lastButtonDegree = toDegree;
			
//			shootButton.
			
//			lastAngle = degree;
			
			lastOrientation = orientation;			

				
		}		
	}
	
	private int[] getDegree(int last, int cur) {
		final int[][][] degrees = {
				{{0, 0}, {180, -270}, {180, 0}, {180, 90}},
				{{270, 180}, {0, 0}, {-270, 0}, {270, 90}},
				{{0, 180}, {0, 270}, {0, 0}, {0, -90}},
				{{90, -180}, {90, 270}, {90, 0}, {0, 0}}
		};
		
		return degrees[last][cur];
		
//		final int[][] degrees = {
//				{0, 90, 0, 270},
//				{180, 90, 0, 0},
//				{180, 90, 0, 270},
//				{180, 0, 0, 270}
//		};
//		
//		return degrees[last][cur];
		
		
	}
	
	
}
