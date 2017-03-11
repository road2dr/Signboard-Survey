package com.mjict.signboardsurvey.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class RetangleDrawImageView extends ImageView {
//	private Point firstVertex;
//	private Point secondVertex;
//	private Point thirdVertex;
//	private Point fourthVertex;
	
	private VertexOnMoveChangedListener moveListener;
	private SizeSettledListener sizeSettledListener;
	private Point[] vertexes;
	private Rect[] vertexRects;

	private int vertexSize;
	private int lineWidth;
	
	private Paint linePaint;
	private Paint vertexPaint;
	private Paint seletedVertexPaint;
	
	// temp
	private Paint textPaint;
	private String tempText;
	//
	
	private int selectedVertex;
	private boolean vertexPressed;
	
	private int imageWidth;
	private int imageHeight;

	private int touchPaddingX;
	private int touchPaddingY;
	
	public RetangleDrawImageView(Context context) {
		super(context);
		init();
	}
	
	public RetangleDrawImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
//		int cw = getWidth()/2;
//		int ch = getHeight()/2;
//		
//		firstVertex = new Point(cw-100, ch-50);
//		secondVertex = new Point(cw-100, ch+50);
//		thirdVertex = new Point(cw+100, ch-50);
//		fourthVertex = new Point(cw+100, ch+50);
		
		Log.d("junseo", "init");
		
		vertexes = new Point[4];
		//
		vertexes[0] = new Point();
		vertexes[1] = new Point();
		vertexes[2] = new Point();
		vertexes[3] = new Point();
		
		vertexRects = new Rect[4];
		
		vertexSize = 0;
		lineWidth = 0;
		
		selectedVertex = 0;
		
		linePaint = new Paint();
		linePaint.setColor(0x88ff0000);
		linePaint.setStrokeWidth(lineWidth);
		
		vertexPaint = new Paint();
		vertexPaint.setColor(0x88ffff00);
		vertexPaint.setStrokeWidth(vertexSize);
		
		seletedVertexPaint = new Paint();
		seletedVertexPaint.setColor(0x88ff0000);
		seletedVertexPaint.setStrokeWidth(vertexSize);
		
		textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(35);
		
		vertexPressed = false;
		
		imageWidth = 0;
		imageHeight = 0;

		touchPaddingX = 0;
		touchPaddingY = 0;
	}

	@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {			
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			if(sizeSettledListener != null)
				sizeSettledListener.onSizeSettled();
		}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		imageWidth = bm.getWidth();
		imageHeight = bm.getHeight();
		
		Log.d("junseo", "�̹��� ũ��: "+imageWidth+"*"+imageHeight);
		super.setImageBitmap(bm);
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}

	public void setTouchPaddingX(int padding) {
		touchPaddingX = padding;
	}

	public void setTouchPaddingY(int padding) {
		touchPaddingY = padding;
	}
	
	public void setVertexPosition(Point[] pos) {		
		if(pos.length != 4)
			return;
		

		Log.d("junseo", "setVertexPosition");
				
		vertexes[0] = pos[0];
		vertexes[1] = pos[1];
		vertexes[2] = pos[2];
		vertexes[3] = pos[3];
		
		int k = vertexSize/2+65;	// 클릭 하시 쉽도록 영역을 살짝 크게 잡음
		
		for(int i=0; i<4; i++)
			vertexRects[i] = new Rect(pos[i].x-k, pos[i].y-k , pos[i].x+k, pos[i].y+k);

	}
	
	public Point[] getVertexPosition() {
		return vertexes;
	}
	
	public void setVertexSize(int size) {
		vertexSize = size;
		vertexPaint.setStrokeWidth(vertexSize);
		seletedVertexPaint.setStrokeWidth(vertexSize);
	}
	
	public void setLineWidth(int size) {
		lineWidth = size;
		linePaint.setStrokeWidth(lineWidth);
	}
	
	public void setVertexMoveListener(VertexOnMoveChangedListener listener) {
		moveListener = listener;
	}
	
	public void setSelecteVertexPosition(int x, int y) {
		if(selectedVertex != -1) {
			vertexes[selectedVertex].x = x;
			vertexes[selectedVertex].y = y;
			
			int k = vertexSize/2+10;	// ��ġ�ϰ� ���� ��¦ ũ�� ����
			vertexRects[selectedVertex] = new Rect(x-k, y-k , x+k, y+k);
		}
		invalidate();
	}
	
	public Point getSelectedPosition() {
		return (selectedVertex != -1) ? vertexes[selectedVertex] : null;
	}
	
	public void setSizeSettledListener(SizeSettledListener listener) {
		sizeSettledListener = listener;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int)event.getX();
		int y = (int)event.getY();
		
		Log.d("junseo", "RetangleDrawImageView - onTouchEvent: "+x+", "+y);
		switch(action) {
		case MotionEvent.ACTION_DOWN:
			int seleceted = checkTouch(x, y);
			if(seleceted != -1) {
				selectedVertex = seleceted;
				vertexPressed = true;
				
				if(getDrawable() != null)
					moveListener.onSelectedVertexChanged(x, y);
				
			}
			break;
		case MotionEvent.ACTION_UP:
			vertexPressed = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if(selectedVertex != -1 && vertexPressed == true) {

				float[] eventXY = new float[] {x, y};
				Matrix invertMatrix = new Matrix();
				getImageMatrix().invert(invertMatrix);
				invertMatrix.mapPoints(eventXY);
				int tx = Integer.valueOf((int)eventXY[0]);
				int ty = Integer.valueOf((int)eventXY[1]);

				if(tx < touchPaddingX || tx >= (imageWidth-touchPaddingX) || ty < touchPaddingY || ty >= (imageHeight-touchPaddingY)) {

				} else {

					vertexes[selectedVertex].x = x;
					vertexes[selectedVertex].y = y;

					int k = vertexSize / 2;
					vertexRects[selectedVertex].left = vertexes[selectedVertex].x - k;
					vertexRects[selectedVertex].top = vertexes[selectedVertex].y - k;
					vertexRects[selectedVertex].right = vertexes[selectedVertex].x + k;
					vertexRects[selectedVertex].bottom = vertexes[selectedVertex].y + k;

					if (getDrawable() != null)
						moveListener.onMove(selectedVertex, x, y);
				}
			}
			break;
		}
		invalidate();
		
		return true;
	}
	
	private int checkTouch(int x, int y) {
		int check = -1;
		for(int i=0; i<4; i++) {
			if(vertexRects[i].contains(x, y)) {
				check = i;
				break;
			}
		}		
		
		return check;
	}
	
	private Point getImagePosition(int x, int y) {
		Point p = new Point();
		float xsop = imageWidth/getWidth();
		float ysop = imageHeight/getHeight();
		p.x = (int)(x*xsop);
		p.y = (int)(y*ysop);
		
		return p;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawLine(vertexes[0].x, vertexes[0].y, vertexes[1].x, vertexes[1].y, linePaint);
		canvas.drawLine(vertexes[1].x, vertexes[1].y, vertexes[2].x, vertexes[2].y, linePaint);
		canvas.drawLine(vertexes[2].x, vertexes[2].y, vertexes[3].x, vertexes[3].y, linePaint);
		canvas.drawLine(vertexes[3].x, vertexes[3].y, vertexes[0].x, vertexes[0].y, linePaint);
		
		canvas.drawCircle(vertexes[0].x, vertexes[0].y, vertexSize, vertexPaint);
		canvas.drawCircle(vertexes[1].x, vertexes[1].y, vertexSize, vertexPaint);
		canvas.drawCircle(vertexes[2].x, vertexes[2].y, vertexSize, vertexPaint);
		canvas.drawCircle(vertexes[3].x, vertexes[3].y, vertexSize, vertexPaint);

		
		if(selectedVertex != -1) {
			canvas.drawCircle(vertexes[selectedVertex].x, vertexes[selectedVertex].y, vertexSize, seletedVertexPaint);
			
			// temp
			String temp = "현재 점의 위치: "+vertexes[selectedVertex].x+", "+vertexes[selectedVertex].y;
			canvas.drawText(temp, 10, 40, textPaint);
			if(tempText != null)
				canvas.drawText(tempText, 10, 70, textPaint);

		}
	}
	
	public interface VertexOnMoveChangedListener {
		public void onMove(int index, int x, int y);
		public void onSelectedVertexChanged(int x, int y);
	}
	
	public interface SizeSettledListener {
		public void onSizeSettled();
	}
	
	// temp
	public void setTempText(String text) {
		tempText = text;
	}
}
