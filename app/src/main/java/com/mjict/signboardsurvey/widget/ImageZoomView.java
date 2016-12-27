package com.mjict.signboardsurvey.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class ImageZoomView extends View {
	private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	private Paint borderPaint;
	private Paint centerPaint;
	
	private Paint textPaint;
	
	private Bitmap image;

	private int panx;
	private int pany;
	
	private int zoom;
	
	private Rect srcRect;
	private Rect destRect;
	
	public ImageZoomView(Context context) {
		super(context);
		init();
	}
	
	public ImageZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		zoom = 4;
		borderPaint = new Paint();
		borderPaint.setColor(Color.GREEN);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(3);
		
		centerPaint = new Paint();
		centerPaint.setColor(0x80ffff00);
		centerPaint.setStyle(Paint.Style.STROKE);
		centerPaint.setStrokeWidth(2);
		
		textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(25);
//		this.setBackgroundColor(0x99ff0000);
//		panx = 200;
//		pany = 200;
		
		srcRect = new Rect();
		destRect = new Rect();
	}
	
	public void setImage(Bitmap image) {
		this.image = image;
		
		invalidate();
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
		
		invalidate();
	}
	
	public void setZoomPosition(int x, int y) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		int xmax = w - (w/zoom)/2;
		int xmin = (w/zoom)/2;
		int ymax = h - (h/zoom)/2;
		int ymin = (h/zoom)/2;
		
		panx = (x > xmax || x < xmin) ? panx : x;
		pany = (y > ymax || y < ymin) ? pany : y;
		
//		panx = (x>image.getWidth()||x<0)? panx : x;
//		pany = (y>image.getHeight()||y<0)? pany : y;
		
//		Log.d("junseo", "ZoomView - zoom position: "+panx+", "+pany);
		
		invalidate();
	}
	
	public int getCurrentPositionX() {
		return panx;
	}
	
	public int getCurrentPositionY() {
		return pany;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
//		Log.d("junseo", "ImageZoomView - onDraw");
		if(image == null)
			return;
		
		int vw = getWidth();
		int vh = getHeight();
		int bw = image.getWidth();
		int bh = image.getHeight();
		
		// �̹����� ��� �κ��� �׷���� ���� ���ؾ���.
		// ũ��� ��Ʈ�� ũ��/zoom
		///////////////////////////
		// TODO ���� �� ���� ���߿� ���� �ؾ� �ҵ�.
		// ���� ���� ũ�⸦ ����� ���� ��� �Ǿ����.
		srcRect.left = panx - (bw/zoom)/2;
		srcRect.right = panx + (bw/zoom)/2;
		srcRect.top = pany - (bh/zoom)/2;
		srcRect.bottom = pany + (bh/zoom)/2;
		
//		Log.d("junseo", "ImageZoomView - onDraw3: "+srcRect.left+", "+srcRect.right+", "+srcRect.top+", "+srcRect.bottom);
//		Log.d("junseo", "ImageZoomView - src ũ�� "+(srcRect.right-srcRect.left)+", "+(srcRect.bottom-srcRect.top));
		
		destRect.left = 0; 
		destRect.top = 0; 
		destRect.right = vw; 
		destRect.bottom = vh;

		int cw = (vw)/2;
		int ch = (vh)/2;
		
		canvas.drawBitmap(image, srcRect, destRect, mPaint);
		canvas.drawRect(destRect, borderPaint);
		canvas.drawLine(cw, 0, cw, canvas.getHeight(), centerPaint);
		canvas.drawLine(0, ch, canvas.getWidth(), ch, centerPaint);
		
		String temp = "�ܺ� ��ġ: "+panx+", "+pany;
		canvas.drawText(temp, 10, 30, textPaint);
		
//		invalidate();
	}
}
