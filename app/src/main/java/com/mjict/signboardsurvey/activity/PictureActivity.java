package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.TouchImageView;


/**
 * Created by Junseo on 2016-07-26.
 */
public class PictureActivity extends SABaseActivity {

    private ImageButton addPictureButton;
    private ImageButton deleteButton;
    private TouchImageView buildingImageView;
    private ViewGroup loadingLayout;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private TextView pageNumTextView;
    private TextView noImageTextView;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_picture;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.show_image);


        addPictureButton = (ImageButton)this.findViewById(R.id.addPictureButton);
        deleteButton = (ImageButton)this.findViewById(R.id.deleteButton);
        nextButton = (ImageButton)this.findViewById(R.id.nextButton);
        previousButton = (ImageButton)this.findViewById(R.id.previousButton);
        pageNumTextView = (TextView)this.findViewById(R.id.pageNumTextView);
        noImageTextView = (TextView)this.findViewById(R.id.noImageTextView);
        loadingLayout = (ViewGroup)this.findViewById(R.id.loading_image_layout);

        buildingImageView = (TouchImageView)this.findViewById(R.id.buildingImageView);
        buildingImageView.setOnTouchListener(new ListView.OnTouchListener() {    // 리스트뷰 스크롤 안되는 버그 픽스
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return false;
            }
        });

    }

    public void hideButtonLayout() {
        previousButton.setVisibility(View.INVISIBLE);
        nextButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        addPictureButton.setVisibility(View.INVISIBLE);
    }

    public void setAddPictureButtonOnClickListener(View.OnClickListener listener) {
        addPictureButton.setOnClickListener(listener);
    }

    public void setDeleteButtonOnClickListener(View.OnClickListener listener) {
        deleteButton.setOnClickListener(listener);
    }

    public void setNextButtonOnClickListener(View.OnClickListener listener) {
        nextButton.setOnClickListener(listener);
    }

    public void setPreviousButtonOnClickListener(View.OnClickListener listener) {
        previousButton.setOnClickListener(listener);
    }

    public void setImage(Bitmap image) {
        loadingLayout.setVisibility(View.INVISIBLE);
        buildingImageView.setVisibility(View.VISIBLE);

        if(image == null)
            buildingImageView.setImageResource(R.drawable.ic_no_image);
        else
            buildingImageView.setImageBitmap(image);

        buildingImageView.setZoom(1);
    }

    public void setImage(int resId) {
        loadingLayout.setVisibility(View.INVISIBLE);
        buildingImageView.setVisibility(View.VISIBLE);

        buildingImageView.setImageResource(resId);
        buildingImageView.setZoom(1);
    }

    public void showLoadingView() {
        loadingLayout.setVisibility(View.VISIBLE);
        buildingImageView.setVisibility(View.INVISIBLE);
    }

    public void setPageText(String text) {
        pageNumTextView.setText(text);
    }


}
