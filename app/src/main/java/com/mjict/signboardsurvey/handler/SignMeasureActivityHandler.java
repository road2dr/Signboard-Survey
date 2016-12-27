package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.SignMeasureActivity;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2016-07-25.
 */
public class SignMeasureActivityHandler extends SABaseActivityHandler {
    public static final int REQUEST_MEASURE = 64785;

//    public static final String IMAGE_PATH = "image_path";
//    public static final String FOCAL_LENGTH = "focal_length";
//    public static final String HORIZONTAL_ANGLE = "horizontal_angle";
//    public static final String VERTICAL_ANGLE = "vertical_angle";
//    public static final String ZOOM = "zoom";

//    public static final String SIZE_X = "size_x";
//    public static final String SIZE_Y = "size_y";

    private SignMeasureActivity activity;

    private float horizontalAngle = -1f;
    private float verticalAngle = -1f;
    private float zoom = -1f;;

    private int responseCode;
    private Intent responseIntent;

    private float sizeXForResponse = -1f;
    private float sizeYForResponse = -1f;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // TODO 화면 돌아가도 기존 위치를 유지하도록 수정
        activity = (SignMeasureActivity)getActivity();

        Intent intent = activity.getIntent();
        String targetImage = intent.getStringExtra(MJConstants.PATH);
        if(targetImage == null) {
            // TODO exit
        }

        activity.setMeasureButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesureButtonClicked();
            }
        });

        activity.setCancelButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonClicked();
            }
        });

        activity.setCloseButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeButtonClicked();
            }
        });

        activity.setApplyButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyhButtonClicked();
            }
        });

        responseCode = Activity.RESULT_CANCELED;
        responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.PATH, targetImage);

        horizontalAngle = intent.getFloatExtra(MJConstants.HORIZONTAL_ANGLE, -1);
        verticalAngle = intent.getFloatExtra(MJConstants.VERTICAL_ANGLE, -1);

        zoom = intent.getFloatExtra(MJConstants.CAMERA_ZOOM, -1);

        ImageBitmapLoadTask task = new ImageBitmapLoadTask(activity);
        task.execute(targetImage);
    }

    private void cancelButtonClicked() {
        responseCode = Activity.RESULT_CANCELED;
        sizeXForResponse = -1;
        sizeYForResponse = -1;


        activity.setWidthText("");
        activity.setHeightText("");
        activity.setResultText("");
        activity.clearImageViewVertex();
    }

    public void closeButtonClicked() {
        activity.setResult(responseCode, responseIntent);
        activity.finish();
    }

    public void applyhButtonClicked() {
        responseCode = Activity.RESULT_OK;
        responseIntent.putExtra(MJConstants.SIZE_X, sizeXForResponse);
        responseIntent.putExtra(MJConstants.SIZE_Y, sizeYForResponse);


        activity.setResult(responseCode, responseIntent);
        // TODO 토스트 하나 띄워?
    }

    private void mesureButtonClicked() {
        int bWidth = activity.getImage().getWidth();
        int bHeight = activity.getImage().getHeight();

        Log.d("junseo", "비트맵: "+bWidth+"*"+bHeight);

        String dis = activity.getInputtedDistance();
        if(dis.equals("")) {
            Toast.makeText(activity, R.string.input_distance, Toast.LENGTH_SHORT).show();
            return;
        }

        double  distance = -1;
        float measurementErrorFactor = SyncConfiguration.getLazerDistanceMeasurementErrorFactor();;
        try {
            distance = Double.valueOf(dis);
        }catch(Exception e) {
            Toast.makeText(activity, R.string.wrong_distance_value, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        // 레이저 센서의 값 보정
        distance = distance * measurementErrorFactor;

        Point[] pos = activity.getVertexPositionFromBitmap();

        if(pos == null || pos.length != 4) {
            Toast.makeText(activity, R.string.area_not_selected, Toast.LENGTH_SHORT).show();
            return;
        }

        // 네 점의 센터를 구하고
        int cx = (pos[0].x + pos[1].x +pos[2].x + pos[3].x) / 4;
        int cy = (pos[0].y + pos[1].y +pos[2].y + pos[3].y) / 4;

        Point p1 = null;
        Point p2 = null;
        Point p3 = null;
        Point p4 = null;	// 각각 왼쪽위, 오른쪽위, 왼쪽아래, 오른쪽아래 점을 나타냄.

        // 각 점이 위치한 사분면 구함
        for(int i=0; i<4; i++) {

            if(pos[i].x <= cx) { // 반으로 갈라 왼쪽
                if(pos[i].y <cy)
                    p1 = pos[i];	// 왼쪽 위
                else
                    p3 = pos[i];	// 왼쪽 아래
            } else {	// 반으로 갈라 오른쪽
                if(pos[i].y <cy)
                    p2 = pos[i];	// 오른쪽 위
                else
                    p4 = pos[i];	// 오른쪽 아래
            }
        }

        if(p1 == null || p2 == null || p3 == null || p4 == null) {
            Toast.makeText(activity, R.string.select_vertex_correctly, Toast.LENGTH_SHORT).show();
            return;
        }

        double HORIZONTAL_ANGLE = -1;
        double VERTICAL_ANGLE = -1;
        if(bWidth > bHeight) {		// 가로 사진
            HORIZONTAL_ANGLE = horizontalAngle;
            VERTICAL_ANGLE = verticalAngle;
        } else {					// 세로 사진
            HORIZONTAL_ANGLE = verticalAngle;
            VERTICAL_ANGLE = horizontalAngle;
        }

        // 사진상의 픽셀 거리
        int ud = (int) getDistance(p1.x, p1.y, p2.x, p2.y);
        int bd = (int) getDistance(p3.x, p3.y, p4.x, p4.y);
        Log.d("junseo", "가로윗변길이: "+ud+"픽셀 가로밑변길이: "+bd+"픽셀");

        // 줌 값 보정
        if(zoom != -1 && zoom != 0f) {
            ud = (int) ((float)ud / zoom);
            bd = (int) ((float)bd/ zoom);
        }

        double radianX = (HORIZONTAL_ANGLE/2)*Math.PI/180;
        double radianY = (VERTICAL_ANGLE/2)*Math.PI/180;

        double totalX = (Math.tan(radianX) * distance)*2;
        double totalY = (Math.tan(radianY) * distance)*2;
        Log.d("junseo", "사진 실제 총 길이: "+totalX+"*"+totalY);

        // 실제 거리 계산
        double real_bd = totalX * ((double)bd/bWidth);
        double real_ud = totalX * ((double)ud/bWidth);
        Log.d("junseo", "실제가로윗변길이: "+real_ud+" 실제가로밑변길이: "+real_bd+"");

        double b = (distance * real_bd) / real_ud;
        Log.d("junseo", "b 카메라와 윗변 꼭지점 까지의 실제거리: "+b);

        // 각도 구하기
        int h = (int)getDistance(p1.x, p1.y, p3.x, p3.y);
        h = (int) ((float)h / zoom);	// 줌 값 보정
        double real_h = totalX * ((double)h/bWidth);
        Log.d("junseo", "h(세로) 의 사진상 거리: "+h+"픽셀, 실제거리: "+real_h);

        double angle = (real_h/distance) * (180/Math.PI);
        Log.d("junseo", "각도: "+angle);

        // 실제 세로 길이
        double c = Math.sqrt( (distance*distance) + (b*b) - 2*distance*b*Math.cos(angle*Math.PI/180) );
        Log.d("junseo", "실제 세로: "+c);

        double area = c * real_bd;

        activity.setWidthText(String.format("%.3f", real_bd));
        activity.setHeightText(String.format("%.3f", c));
        activity.setResultText(String.format("%.3f", area));

        sizeXForResponse = (float) real_bd;
        sizeYForResponse = (float) c;
    }

    private double getDistance(int ax, int ay, int bx, int by) {
        return Math.sqrt((ax-bx)*(ax-bx)+(ay-by)*(ay-by));
    }

    @Override
    public void onActivityDestroy() {
        Bitmap image = activity.getImage();
        if( image != null)
            image.recycle();

        super.onActivityDestroy();
    }


    private class ImageBitmapLoadTask extends AsyncTask<String, Integer, Bitmap> {
        private Context context;

        public ImageBitmapLoadTask(Context context) {
            this.context = context;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String path = params[0];

            Bitmap bitmap = Utilities.loadImage(path, 1);
            // TODO bitmap == null 예외 처리 액티비티 종료

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            activity.setImage(result);

            super.onPostExecute(result);
        }
    }
}
