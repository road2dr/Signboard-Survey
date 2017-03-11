package com.mjict.signboardsurvey.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.ImageZoomView;
import com.mjict.signboardsurvey.widget.JoystickView;
import com.mjict.signboardsurvey.widget.RetangleDrawImageView;


public class SignMeasureActivity extends SABaseActivity implements JoystickView.OnJoystickMoveListener,
        RetangleDrawImageView.VertexOnMoveChangedListener, RetangleDrawImageView.SizeSettledListener {

    private JoystickView joystick;
    private RetangleDrawImageView imageView;
    private ImageZoomView zoomView;

    private Button measureButton;
    private Button doneButton;
    private Button cancelButton;

    private TextView widthTextView;
    private TextView heightTextView;
    private TextView resultTextView;

    private EditText distanceEditText;

    private Configuration oldConfig;

    private Bitmap image;

//	private boolean imageViewPositionSetted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_measure;
    }

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();

        imageView = (RetangleDrawImageView)this.findViewById(R.id.image_view);
//        imageView.setVertexDrawingEnabled(true);

//        imageView.setImageResource(R.drawable.sign_2);
		imageView.setVertexMoveListener(this);
		imageView.setSizeSettledListener(this);

		joystick = (JoystickView)this.findViewById(R.id.joystick_view);
		joystick.setOnJoystickMoveListener(this, 100);

        zoomView = (ImageZoomView)this.findViewById(R.id.image_zoom_view);
        zoomView.setZoom(6);

        measureButton = (Button)this.findViewById(R.id.measure_button);
        doneButton = (Button)this.findViewById(R.id.done_button);
        cancelButton = (Button)this.findViewById(R.id.cancel_button);
//        closeButton = (Button)this.findViewById(R.id.close_button);

        widthTextView = (TextView)this.findViewById(R.id.width_text_view);
        heightTextView = (TextView)this.findViewById(R.id.height_text_view);
        resultTextView = (TextView)this.findViewById(R.id.result_text_view);

        distanceEditText = (EditText)this.findViewById(R.id.distance_edit_text);

        oldConfig = getResources().getConfiguration();


    }


    public void setImage(Bitmap image) {
        this.image = image;
        imageView.setImageBitmap(image);
//        imageView.setZoom(1);
        zoomView.setImage(image);

        int w = imageView.getWidth();
        int h = imageView.getHeight();

        Point[] pos = new Point[4];
        pos[0] = new Point(w/2-120, h/2-120);
        pos[1] = new Point(w/2+120, h/2-120);
        pos[2] = new Point(w/2+120, h/2+120);
        pos[3] = new Point(w/2-120, h/2+120);

		imageView.setVertexSize(35);
		imageView.setVertexPosition(pos);
		imageView.setLineWidth(4);

        int zoom = zoomView.getZoom();
        int paddingX = (image.getWidth()/zoom)/2;
        int paddingY = (image.getHeight()/zoom)/2;
        imageView.setTouchPaddingX(paddingX);
        imageView.setTouchPaddingY(paddingY);

//		Point bp = findBitmapPosition(pos[0].x, pos[0].y);
//		zoomView.setZoomPosition(bp.x, bp.y);
//		zoomView.setZoom(0.99f);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setMeasureButtonOnClickListener(OnClickListener listener) {
        measureButton.setOnClickListener(listener);
    }

    public void setDoneButtonOnClickListener(OnClickListener listener) {
        doneButton.setOnClickListener(listener);
    }

    public void setCancelButtonOnClickListener(OnClickListener listener) {
        cancelButton.setOnClickListener(listener);
    }

//    public void setCloseButtonOnClickListener(OnClickListener listener) {
//        closeButton.setOnClickListener(listener);
//    }

    public void setWidthText(String text) {
        widthTextView.setText(text);
    }

    public void setHeightText(String text) {
        heightTextView.setText(text);
    }

    public void setResultText(String text) {
        resultTextView.setText(text);
    }

    public String getInputtedDistance() {
        return distanceEditText.getText().toString();
    }

    public void clearImageViewVertex() {

    }

    /**
     * 네점의 위치를 비트맵(픽셀의 위치) 기준으로 반환.
     * @return
     */
    public Point[] getVertexPositionFromBitmap() {
        Point[] pos = imageView.getVertexPosition();

		Point[] bPosition = new Point[pos.length];
		for(int i=0; i<pos.length; i++) {
			Point p = pos[i];
			if(p == null)
				return null;

			bPosition[i] = findBitmapPosition(p.x, p.y);
		}

        return bPosition;
    }

    @Override
    public void onValueChanged(int angle, int power, int direction) {   // JoystickView - onValueChanged
        if(image == null)
            return;

        int curX = zoomView.getCurrentPositionX();
        int curY = zoomView.getCurrentPositionY();

        int[] tickValues = getTickValue(direction);
//        int tx = (power>50) ? tickValues[0]*2 : tickValues[0];
//        int ty = (power>50) ? tickValues[1]*2 : tickValues[1];
        // 조이스틱 움직임 줄임
        int tx = tickValues[0];
        int ty = tickValues[1];

        if( tx == 0 && ty == 0)
            return;
        ///////////////

		Point ip = imageView.getSelectedPosition();
		int mx = ip.x+tx;
		int my = ip.y+ty;

        Point zp = findBitmapPosition(mx, my);
        int w = image.getWidth();
        int h = image.getHeight();
        int zoom = zoomView.getZoom();

        int minx = (w/zoom)/2;
        int maxx = image.getWidth() - (w/zoom)/2;
        int miny = (h/zoom)/2;
        int maxy = image.getHeight() - (h/zoom)/2;

        if(zp.x < minx || zp.x >= maxx)
            return;
        if(zp.y < miny || zp.y >= maxy)
            return;

        /////////////////////////////////////

		imageView.setSelecteVertexPosition(mx, my);
        zoomView.setZoomPosition(zp.x, zp.y);

		// temp
		String temp = tx+", "+ty+" 만큼 뷰에서 이동, 줌뷰 에서는 "+(zp.x-curX)+", "+(zp.y-curY)+" 만큼 이동";
		imageView.setTempText(temp);

    }

	@Override
	public void onMove(int index, int x, int y) {
		Point p = findBitmapPosition(x, y);
		zoomView.setZoomPosition(p.x, p.y);
	}

	@Override
	public void onSelectedVertexChanged(int x, int y) {
		Point p = findBitmapPosition(x, y);
		zoomView.setZoomPosition(p.x, p.y);
	}

    /**
     * 이미지뷰 x, y 해당하는 곳의 비트맵의 위치를 구함.
     *
     * @param x
     * @param y
     * @return
     */
    private Point findBitmapPosition(int x, int y) {
        Point p = new Point();

        float[] eventXY = new float[] {x, y};

        Matrix invertMatrix = new Matrix();
        imageView.getImageMatrix().invert(invertMatrix);

        invertMatrix.mapPoints(eventXY);
        p.x = Integer.valueOf((int)eventXY[0]);
        p.y = Integer.valueOf((int)eventXY[1]);

        return p;
    }

    private int[] getTickValue(int direction) {
        int tx = 0;
        int ty = 0;
        int[] value = new int[2];
        switch(direction) {
            case 1:
                tx = 2;
                ty = -1;
                break;
            case 2:
                tx = 1;
                ty = -2;
                break;
            case 3:
                tx = -1;
                ty = -2;
                break;
            case 4:
                tx = -2;
                ty = -1;
                break;
            case 5:
                tx = -2;
                ty = 1;
                break;
            case 6:
                tx = -1;
                ty = 2;
                break;
            case 7:
                tx = 1;
                ty = 2;
                break;
            case 8:
                tx = 2;
                ty = 1;
                break;
            default:
                tx = 0;
                ty = 0;
        }
        value[0] = tx;
        value[1] = ty;
        return value;
    }

    @Override
    public void onSizeSettled() {

    }
}
