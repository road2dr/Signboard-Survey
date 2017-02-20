package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.model.ui.SignInputData;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.widget.SimpleSpinner;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-07-20.
 */
public class SignViewPagerAdapter extends PagerAdapter {
    private Context context;

    private SparseArray<PageView> pageViews = new SparseArray<PageView>();

    private LayoutInflater inflater;
    private ArrayList<SignInputData> signs;
//    private SparseArray<Bitmap> signBitmaps = new SparseArray<>();
//    private SparseArray<Bitmap> demolishBitmaps = new SparseArray<>();

    private GestureDetector mGestureDetector;

//    private TimePickerDialog timePickerDialog;

    private ArrayList<SimpleSpinner.SpinnerItem> typeSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> statsSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> lightSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> reviewSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> installSideSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> uniquenessSpinnerItems = new ArrayList<>();
    private ArrayList<SimpleSpinner.SpinnerItem> resultSpinnerItems = new ArrayList<>();

    private ConfirmButtonOnClickListener confirmButtonOnClickListener;
    private AddSignImageButtonOnClickListener addSignImageButtonOnClickListener;
    private SignImageOnClickListener signImageOnClickListener;
    private DemolitionImageOnClickListener demolitionImageOnClickListener;
    private DateTextOnClickListener dateTextOnClickListener;
    private AddDemolitionImageButtonOnClickListener addDemolitionImageButtonOnClickListener;
    //    private SizeEditTextOnClickListener sizeEditTextOnClickListener;
    private ContentTextFocusChangedListener contentTextFocusChangedListener;
    private AutoJudgementButtonOnClickListener autoJudgementButtonClickListener;

//    private InputDataChangeListener changeListener;

    public SignViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
        signs = new ArrayList<>();

//        timePickerDialog = new TimePickerDialog(c);
//        timePickerDialog.create();
//        mGestureDetector = new GestureDetector(c, new YScrollDetector());
    }

//    public void setStatusSettings(Setting[] settings) {
//        statusSettings = settings;
//    }
//
//    public void setTypeSettings(Setting[] settings) {
//        typeSettings = settings;
//    }
//
//    public void setLightSettings(Setting[] settings) {
//        lightSettings = settings;
//    }
//
//    public void setResultSettings(Setting[] settings) {
//        resultSettings = settings;
//    }

//    public void setTargetShop(Shop shop) {
//        targetShop = shop;
//    }

    public void add(SignInputData data) {
        Log.d("junseo", "add");
        signs.add(data);
        count = signs.size();
        notifyDataSetChanged();
    }

    public void setConfirmButtonOnClickListener(ConfirmButtonOnClickListener listener) {
        confirmButtonOnClickListener = listener;
    }

    public void setAddImageButtonOnClickListener(AddSignImageButtonOnClickListener listener) {
        addSignImageButtonOnClickListener = listener;
    }

    public void setSignImageOnClickListener(SignImageOnClickListener listener) {
        signImageOnClickListener = listener;
    }

//    public void setSignInputDataChangeListener(InputDataChangeListener listener) {
//        changeListener = listener;
//    }

    public void setDateTextOnClickListener(DateTextOnClickListener listener) {
        dateTextOnClickListener = listener;
    }

    public void setDemolitionImageOnClickListener(DemolitionImageOnClickListener listener) {
        demolitionImageOnClickListener = listener;
    }

    public void setAddDemolitionImageButtonOnClickListener(AddDemolitionImageButtonOnClickListener listener) {
        addDemolitionImageButtonOnClickListener = listener;
    }

    public void setContentTextFocusChangedListener(ContentTextFocusChangedListener listener) {
        contentTextFocusChangedListener = listener;
    }

    public void setAutoJudgementButtonOnClickListener(AutoJudgementButtonOnClickListener listener) {
        autoJudgementButtonClickListener = listener;
    }

    public void setInputData(int page, SignInputData data) {
        if(page < 0 || page >= signs.size())
            return;

        signs.set(page, data);
        refreshPage(page);
    }

//    private ArrayList<Object> typeSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> statsSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> lightSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> reviewSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> installSideSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> uniquenessSpinnerItems = new ArrayList<>();
//    private ArrayList<Object> resultSpinnerItems = new ArrayList<>();

    public void addToTypeSpinner(SimpleSpinner.SpinnerItem item) {
        typeSpinnerItems.add(item);
    }

    public void addToStatsSpinner(SimpleSpinner.SpinnerItem item) {
        statsSpinnerItems.add(item);
    }

    public void addToLightSpinner(SimpleSpinner.SpinnerItem item) {
        lightSpinnerItems.add(item);
    }

    public void addToReviewSpinner(SimpleSpinner.SpinnerItem item) {
        reviewSpinnerItems.add(item);
    }

    public void addToInstallSideSpinner(SimpleSpinner.SpinnerItem item) {
        installSideSpinnerItems.add(item);
    }

    public void addToUniquenessSpinner(SimpleSpinner.SpinnerItem item) {
        uniquenessSpinnerItems.add(item);
    }

    public void addToResultSpinner(SimpleSpinner.SpinnerItem item) {
        resultSpinnerItems.add(item);
    }

    public SignInputData getCurrentInputData(int page) {
        if(page < 0 || page >= signs.size())
            return null;

        PageView pageView = pageViews.get(page);
        SignInputData data = null;
        if(pageView == null) {
            data = signs.get(page);
        } else {
            SignInputData original = signs.get(page);
            data = pageView.generateInputData();
//            data.signImagePath = original.signImagePath;  ////
//            data.demolishImagePath = original.demolishImagePath;
        }

        return data;
    }

    public void setSignWidthText(int page, String text) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.width = text;
        } else {
            SignInputData data = pageView.generateInputData();
            data.width = text;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public void setSignLengthText(int page, String text) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.length = text;
        } else {
            SignInputData data = pageView.generateInputData();
            data.length = text;
            signs.set(page, data);
        }

        refreshPage(page);
    }

//    public void setSignImage(int page, Bitmap image) {
//        Log.d("junseo", "setSignImage: "+page+" "+image);
//        if(page < 0 || page >= signs.size())
//            return;
//
////        View view = pageViews.get(page);
////        if(view == null)
////            return;
////
////        PageView pageView = new PageView(view, page);
////        SignInputData data = pageView.generateInputData();
//
//        SignInputData data = signs.get(page);
//        data.image = image;
//        signs.set(page, data);
//
//    }

    public void setSignImage(int page, String path) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.signImagePath = path;
        } else {
            SignInputData data = pageView.generateInputData();
            data.signImagePath = path;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public void setDemolishImage(int page, String path) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.demolishImagePath = path;
        } else {
            SignInputData data = pageView.generateInputData();
            data.demolishImagePath                 = path;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public void setContentText(int page, String text) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.content = text;
        } else {
            SignInputData data = pageView.generateInputData();
            data.content = text;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public void setDateText(int page, String text) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.demolishDate = text;
        } else {
            SignInputData data = pageView.generateInputData();
            data.demolishDate = text;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public void setResultSpinnerSelection(int page, Object id) {
        if(page < 0 || page >= signs.size())
            return;

        PageView pageView = pageViews.get(page);
        if(pageView == null) {
            SignInputData data = signs.get(page);
            data.inspectionResult = id;
        } else {
            SignInputData data = pageView.generateInputData();
            data.inspectionResult = id;
            signs.set(page, data);
        }

        refreshPage(page);
    }

    public String getSignImagePath(int page) {
        if(page < 0 || page >= signs.size())
            return null;

        return signs.get(page).signImagePath;
    }

    public String getDemolishImagePath(int page) {
        if(page < 0 || page >= signs.size())
            return null;

        return signs.get(page).demolishImagePath;
    }

//    public void setVisibleAddDemolishImageButton(int page) {
//        if(page < 0 || page >= signs.size())
//            return;
//
//        View view = pageViews.get(page);
//
//        if(view == null)
//            return;
//
//        PageView pageView = new PageView(view, page);
//        pageView.setVisibleAddDemolishImageButton();
//    }

    private int count = 0;
    @Override
    public int getCount() {

        return count;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        Log.d("junseo", "instantiateItem: "+position);
        View view = null;

        view = inflater.inflate(R.layout.view_page_sign_information, null);

        SignInputData data = signs.get(position);
        PageView pageView = new PageView(view, position);

        pageView.initSpinners();

        pageView.updateToUI(data);

        container.addView(view);

        pageViews.put(position, pageView);

        return view;
    }

    @Override
    public void destroyItem(View pager, int position, Object o) {
        Log.d("junseo", "destroyItem page: "+position);
        View view = (View) o;

        PageView pageView = new PageView(view, position);

        // 이전 상태를 기억
        SignInputData before = signs.get(position);
        SignInputData data = pageView.generateInputData();
        data.signImagePath = before.signImagePath;      // 다른것들은 상태로 그대로 저장 되지만 이미지 경로는 저장이 안되서 이렇게 따로 저장 해야함
        data.demolishImagePath = before.demolishImagePath;
        signs.set(position, data);

        ((ViewPager)pager).removeView(view);

        pageViews.remove(position);
        view = null;

        count = signs.size();
    }

    public void refreshPage(int page) {
        PageView pageView = pageViews.get(page);
        SignInputData data = signs.get(page);
        if(pageView == null || data == null)
            return;

        pageView.updateToUI(data);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }

    @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    @Override public Parcelable saveState() { return null; }
    @Override public void startUpdate(View arg0) {}
    @Override public void finishUpdate(View arg0) {}



    private class PageView {
        View parent;

        Button autoJudgementButton;
        ImageButton addSignImageButton;
        ImageView signImageView;
        TextView demolitionDateTextView;
        SimpleSpinner typeSpinner;
        EditText contentEditText;
        RadioGroup statusRadioGroup;
        RadioButton normalRadioButton;
        RadioButton demolitionRadioButton;
        RadioButton demolitionExpectedRadioButton;
        ViewGroup normalLayout;
        ViewGroup demolitionLayout;
        EditText widthEditText;
        EditText lengthEditText;
        EditText heightEditText;
        CheckBox frontCheckBox;
        CheckBox intersectionCheckBox;
        CheckBox frontBackRoadCheckBox;
        SimpleSpinner lightSpinner;
        EditText placedFloorEditText;
        EditText totalFloorEditText;
        CheckBox roadCollisionCheckBox;
        EditText collisionWidthEditText;
        EditText collisionLengthEditText;
        SimpleSpinner resultSpinner;
        ImageButton addDemolishedImageButton;
        ImageView demolishedImageView;
        Button confirmButton;
        ProgressBar signImageProgressBar;
        ProgressBar demolishImageProgressBar;

        //
        SimpleSpinner reviewSpinner;
        SimpleSpinner installSideSpinner;
        SimpleSpinner uniquenessSpinner;
        EditText memoEditText;

        String signImagePath = "";
        String demolishImagePath = "";

        private int pageIndex;

        // TODO 여기서 findView 할게 아니라 init 함수를 만들어 옮기고 holder 패턴 처럼 작동 하게...
        // TODO 속도 저하가 꽤 있는데 캐싱 같은 방법을 검토 해볼 것
        // findView 로 인한 속도 저하가 꽤 있다.
        public PageView(View view, final int position) {
            parent = view;
            pageIndex = position;

            init();
        }

        private void init() {
            autoJudgementButton = (Button)parent.findViewById(R.id.auto_judgement_button);
            addSignImageButton = (ImageButton)parent.findViewById(R.id.add_sign_image_button);
            signImageView = (ImageView)parent.findViewById(R.id.sign_image_view);
            demolitionDateTextView = (TextView)parent.findViewById(R.id.demolition_date_text_view);
            typeSpinner = (SimpleSpinner)parent.findViewById(R.id.type_spinner);
            contentEditText = (EditText)parent.findViewById(R.id.content_edit_text);
            statusRadioGroup = (RadioGroup)parent.findViewById(R.id.status_radio_group);
            normalRadioButton = (RadioButton)parent.findViewById(R.id.normal_radio_button);
            demolitionRadioButton = (RadioButton)parent.findViewById(R.id.demolition_radio_button);
            demolitionExpectedRadioButton = (RadioButton)parent.findViewById(R.id.demolition_expected_radio_button);
            normalLayout = (ViewGroup)parent.findViewById(R.id.layout_for_normal);
            demolitionLayout = (ViewGroup)parent.findViewById(R.id.layout_for_demolition);
            widthEditText = (EditText)parent.findViewById(R.id.width_edit_text);
            lengthEditText = (EditText)parent.findViewById(R.id.length_edit_text);
            heightEditText = (EditText)parent.findViewById(R.id.height_edit_text);
            frontCheckBox = (CheckBox)parent.findViewById(R.id.front_check_box);
            intersectionCheckBox = (CheckBox)parent.findViewById(R.id.intersection_check_box);
            frontBackRoadCheckBox = (CheckBox)parent.findViewById(R.id.front_back_road_check_box);
            lightSpinner = (SimpleSpinner)parent.findViewById(R.id.light_spinner);
            placedFloorEditText = (EditText)parent.findViewById(R.id.placed_floor_edit_text);
            totalFloorEditText = (EditText)parent.findViewById(R.id.total_floor_edit_text);
            roadCollisionCheckBox = (CheckBox)parent.findViewById(R.id.road_collision_check_box);
            collisionWidthEditText = (EditText)parent.findViewById(R.id.collision_width_edit_text);
            collisionLengthEditText = (EditText)parent.findViewById(R.id.collision_length_edit_text);
            resultSpinner = (SimpleSpinner)parent.findViewById(R.id.inspection_result_spinner);
            addDemolishedImageButton = (ImageButton)parent.findViewById(R.id.add_demolition_image_button);
            demolishedImageView = (ImageView)parent.findViewById(R.id.demolition_image_view);
            signImageProgressBar = (ProgressBar)parent.findViewById(R.id.sign_image_progress);
            demolishImageProgressBar = (ProgressBar)parent.findViewById(R.id.demolish_image_progress);
            reviewSpinner = (SimpleSpinner)parent.findViewById(R.id.review_spinner);
            installSideSpinner = (SimpleSpinner)parent.findViewById(R.id.installed_side_spinner);
            uniquenessSpinner = (SimpleSpinner)parent.findViewById(R.id.uniqueness_spinner);
            memoEditText = (EditText)parent.findViewById(R.id.memo_edit_text);

            confirmButton = (Button)parent.findViewById(R.id.confirm_button);

            statusRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  // TODO 얘는 여기보다 handler 쪽으로 이동 하는게 낫겠지?
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.demolition_radio_button) {
                        normalLayout.setVisibility(View.INVISIBLE);
                        demolitionLayout.setVisibility(View.VISIBLE);
                    } else {
                        normalLayout.setVisibility(View.VISIBLE);
                        demolitionLayout.setVisibility(View.INVISIBLE);
                    }
                }
            });

            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(confirmButtonOnClickListener != null) {
                        SignInputData data = generateInputData();
                        //
//                        String signImagePath = signs.get(pageIndex).signImagePath;
//                        String demolishImagePath = signs.get(pageIndex).demolishImagePath;
//                        data.signImagePath = signImagePath;
//                        data.demolishImagePath = demolishImagePath;

                        confirmButtonOnClickListener.onClick(pageIndex, data);
                    }
                }
            });

            addSignImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addSignImageButtonOnClickListener != null)
                        addSignImageButtonOnClickListener.onClick(pageIndex);
                }
            });

            signImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(signImageOnClickListener != null)
                        signImageOnClickListener.onClick(pageIndex);
                }
            });

            signImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(signImageOnClickListener != null)
                        signImageOnClickListener.onLongClick(pageIndex);
                    return false;
                }
            });

            addDemolishedImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addDemolitionImageButtonOnClickListener != null)
                        addDemolitionImageButtonOnClickListener.onClick(pageIndex);
                }
            });

            demolitionDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dateTextOnClickListener != null)
                        dateTextOnClickListener.onClick(pageIndex);
                }
            });

            demolishedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(demolitionImageOnClickListener != null)
                        demolitionImageOnClickListener.onClick(pageIndex);
                }
            });

            demolishedImageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(demolitionImageOnClickListener != null)
                        demolitionImageOnClickListener.onLongClick(pageIndex);

                    return false;
                }
            });

            autoJudgementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(autoJudgementButtonClickListener != null) {
                        SignInputData data = generateInputData();
                        autoJudgementButtonClickListener.onClick(pageIndex, data);
                    }
                }
            });

//            widthEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if(hasFocus)
//                        if(sizeEditTextOnClickListener != null)
//                        sizeEditTextOnClickListener.onClick(pageIndex);
//                }
//            });

//            widthEditText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(sizeEditTextOnClickListener != null)
//                        sizeEditTextOnClickListener.onClick(pageIndex);
//                }
//            });

            contentEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(contentTextFocusChangedListener != null)
                        contentTextFocusChangedListener.onFocusChange(pageIndex, hasFocus);
                }
            });
        }


        public void initSpinners() {
            Log.d("junseo", "initSpinners");
            for(int i=0; i<typeSpinnerItems.size(); i++)
                typeSpinner.addSpinnerData(typeSpinnerItems.get(i));
            for(int i=0; i<lightSpinnerItems.size(); i++)
                lightSpinner.addSpinnerData(lightSpinnerItems.get(i));
            for(int i=0; i<reviewSpinnerItems.size(); i++)
                reviewSpinner.addSpinnerData(reviewSpinnerItems.get(i));
            for(int i=0; i<installSideSpinnerItems.size(); i++)
                installSideSpinner.addSpinnerData(installSideSpinnerItems.get(i));
            for(int i=0; i<uniquenessSpinnerItems.size(); i++)
                uniquenessSpinner.addSpinnerData(uniquenessSpinnerItems.get(i));
            for(int i=0; i<resultSpinnerItems.size(); i++)
                resultSpinner.addSpinnerData(resultSpinnerItems.get(i));

            for(int i=0; i<statusRadioGroup.getChildCount(); i++) {
                View child = statusRadioGroup.getChildAt(i);
                if(child.getId() == R.id.normal_radio_button)
                    child.setTag("4");
                else if(child.getId() == R.id.demolition_radio_button)
                    child.setTag("2");
                else if(child.getId() == R.id.demolition_expected_radio_button)
                    child.setTag("3");
                else
                    child.setTag("4");
            }
        }

        private boolean firstTimeChange = true;
//        private void dataChanged() {
//
//            SignInputData inputData = generateInputData();
//            if(changeListener != null)
//                changeListener.onChanged(pageIndex, inputData);
//        }

//        private void setSignImage(Bitmap image) {
//            signImageView.setImageBitmap(image);
//
//            Log.d("junseo", "Page view- setSignImage: "+pageIndex+" "+image);
//            if(image != null) {
//                addSignImageButton.setVisibility(View.INVISIBLE);
//                signImageView.setVisibility(View.VISIBLE);
//            } else {
//                addSignImageButton.setVisibility(View.VISIBLE);
//                signImageView.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        private void setDemolishImage(Bitmap image) {
//            Log.d("junseo", "setDemolishImage: "+pageIndex+" "+image);
//            demolishedImageView.setImageBitmap(image);
//
//            if(image != null) {
//                addDemolishedImageButton.setVisibility(View.INVISIBLE);
//                demolishedImageView.setVisibility(View.VISIBLE);
//            } else {
//                addDemolishedImageButton.setVisibility(View.VISIBLE);
//                demolishedImageView.setVisibility(View.INVISIBLE);
//            }
//        }

        public void showSignImageProgressBar() {
            addSignImageButton.setVisibility(View.INVISIBLE);
            signImageView.setVisibility(View.INVISIBLE);
            signImageProgressBar.setVisibility(View.VISIBLE);
        }

        public void setSignImage(Bitmap image) {
            addSignImageButton.setVisibility(View.INVISIBLE);
            signImageView.setVisibility(View.VISIBLE);
            signImageProgressBar.setVisibility(View.INVISIBLE);

            signImageView.setImageBitmap(image);
        }

        public void setSignImage(int resId) {
            addSignImageButton.setVisibility(View.INVISIBLE);
            signImageView.setVisibility(View.VISIBLE);
            signImageProgressBar.setVisibility(View.INVISIBLE);

            signImageView.setImageResource(resId);
        }

        public void setVisibleAddSignImageButton() {
            addSignImageButton.setVisibility(View.VISIBLE);
            signImageView.setVisibility(View.INVISIBLE);
            signImageProgressBar.setVisibility(View.INVISIBLE);
        }

        public void showDemolishImageProgressBar() {
            addDemolishedImageButton.setVisibility(View.INVISIBLE);
            demolishedImageView.setVisibility(View.INVISIBLE);
            demolishImageProgressBar.setVisibility(View.VISIBLE);
        }

        public void setDemolishImage(Bitmap image) {
            addDemolishedImageButton.setVisibility(View.INVISIBLE);
            demolishedImageView.setVisibility(View.VISIBLE);
            demolishImageProgressBar.setVisibility(View.INVISIBLE);

            demolishedImageView.setImageBitmap(image);
        }

        public void setDemolishImage(int resId) {
            addDemolishedImageButton.setVisibility(View.INVISIBLE);
            demolishedImageView.setVisibility(View.VISIBLE);
            demolishImageProgressBar.setVisibility(View.INVISIBLE);

            demolishedImageView.setImageResource(resId);
        }

        private void setVisibleAddDemolishImageButton() {
            addDemolishedImageButton.setVisibility(View.VISIBLE);
            demolishedImageView.setVisibility(View.INVISIBLE);
            demolishImageProgressBar.setVisibility(View.INVISIBLE);
        }

        private void updateToUI(SignInputData data) {
            if(data != null) {
                typeSpinner.setSpinnerSelection(data.signType);
                contentEditText.setText(data.content);
                heightEditText.setText(data.height);
                frontCheckBox.setChecked(data.isFront);
                intersectionCheckBox.setChecked(data.isIntersection);
                frontBackRoadCheckBox.setChecked(data.isFrontBack);
                lightSpinner.setSpinnerSelection(data.lightType);
                placedFloorEditText.setText(String.valueOf(data.placedFloor));
                totalFloorEditText.setText(String.valueOf(data.totalFloor));
                roadCollisionCheckBox.setChecked(data.isCollision);
                collisionWidthEditText.setText(data.collisionWidth);
                collisionLengthEditText.setText(data.collisionLength);
                resultSpinner.setSpinnerSelection(data.inspectionResult);
                widthEditText.setText(data.width);
                lengthEditText.setText(data.length);
                demolitionDateTextView.setText(data.demolishDate);

                signImagePath = data.signImagePath;
                demolishImagePath = data.demolishImagePath;

                signImageProgressBar.setVisibility(View.INVISIBLE);
                if(signImagePath == null || signImagePath.equals("")) {
                    setVisibleAddSignImageButton();
                } else {
                    addSignImageButton.setVisibility(View.INVISIBLE);
                    signImageView.setVisibility(View.VISIBLE);
                    signImageProgressBar.setVisibility(View.INVISIBLE);
                    startLoadImage(signImagePath, signImageView, signImageProgressBar);
                }

                demolishImageProgressBar.setVisibility(View.INVISIBLE);
                if(demolishImagePath == null || demolishImagePath.equals("")) {
                    setVisibleAddDemolishImageButton();
                } else {
                    addDemolishedImageButton.setVisibility(View.INVISIBLE);
                    demolishedImageView.setVisibility(View.VISIBLE);
                    demolishImageProgressBar.setVisibility(View.INVISIBLE);
                    startLoadImage(demolishImagePath, demolishedImageView, demolishImageProgressBar);
                }

//                inspectionResultWaitProgressBar.setVisibility(View.INVISIBLE);
//                inspectionResultLayout.setVisibility(View.VISIBLE);

                for(int i=0; i<statusRadioGroup.getChildCount(); i++) {
                    View child = statusRadioGroup.getChildAt(i);
                    Object tag = child.getTag();
                    if(tag != null ) {
                        if(tag.equals(data.signStats)) {
                            ((RadioButton)child).setChecked(true);
                        }
                    }
                }

                if(statusRadioGroup.getCheckedRadioButtonId() == -1)
                    normalRadioButton.setChecked(true);

                //
                reviewSpinner.setSpinnerSelection(data.reviewStats);
                installSideSpinner.setSpinnerSelection(data.installSide);
                uniquenessSpinner.setSpinnerSelection(data.uniqueness);
                memoEditText.setText(data.memo);

                //
            }
        }



        public SignInputData generateInputData() {
            String signImagePath = this.signImagePath;
            String content = contentEditText.getText().toString();
            Object signType = typeSpinner.getSelectedDataId();
            int checkedStatusId = statusRadioGroup.getCheckedRadioButtonId();
            Object signStats = statusRadioGroup.findViewById(checkedStatusId).getTag();
            String width = widthEditText.getText().toString();
            String length = lengthEditText.getText().toString();
            Object lightType = lightSpinner.getSelectedDataId();
            String placedFloor = placedFloorEditText.getText().toString();
            String totalFloor = totalFloorEditText.getText().toString();
            String height = heightEditText.getText().toString();
            boolean isFront = frontCheckBox.isChecked();
            boolean isIntersection = intersectionCheckBox.isChecked();
            boolean isFrontBack = frontBackRoadCheckBox.isChecked();
            boolean isCollision = roadCollisionCheckBox.isChecked();
            String collisionWidth = collisionWidthEditText.getText().toString();
            String collisionLength = collisionLengthEditText.getText().toString();
//            Object reviewStats = reviewSpinner.getSelectedData();     // TODO ////
//            Object installSide = installSideSpinner.getSelectedData();
//            Object uniqueness = uniquenessSpinner.getSelectedData();
//            String memo = memoTextView.getText().toString();

            Object reviewStats = reviewSpinner.getSelectedDataId();
            Object installSide = installSideSpinner.getSelectedDataId();
            Object uniqueness = uniquenessSpinner.getSelectedDataId();
            String memo = memoEditText.getText().toString();

            String demolishImagePath = this.demolishImagePath;
            String demolishDate = demolitionDateTextView.getText().toString();
            Object inspectionResult = resultSpinner.getSelectedDataId();

            SignInputData inputData = new SignInputData(signImagePath, content, signType, signStats, width,
                    length, lightType, placedFloor, totalFloor, height, isFront, isIntersection, isFrontBack,
                    isCollision, collisionWidth, collisionLength, reviewStats, installSide, uniqueness,
                    memo, demolishImagePath, demolishDate, inspectionResult);


            return inputData;
        }

        private void startLoadImage(String path, final ImageView imageView, final View loadingView) {
            LoadImageTask task = new LoadImageTask();
            task.setSampleSize(8);
            task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
                @Override
                public void onTaskStart() {
                    imageView.setVisibility(View.INVISIBLE);
                    loadingView.setVisibility(View.VISIBLE);
                }
                @Override
                public void onTaskProgressUpdate(IndexBitmap... values) {
                    imageView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.INVISIBLE);
                    if(values[0].image != null)
                        imageView.setImageBitmap(values[0].image);
                    else {
                        imageView.setImageResource(R.drawable.ic_no_image);
                    }
                }

                @Override
                public void onTaskFinished(Boolean aBoolean) {

                }
            });
            task.execute(path);

        }
    }



    public interface ConfirmButtonOnClickListener {
        public void onClick(int index, SignInputData data);
    }

    public interface AddSignImageButtonOnClickListener {
        public void onClick(int index);
    }

    public interface SignImageOnClickListener {
        public void onClick(int index);
        public void onLongClick(int index);
    }

    public interface InputDataChangeListener {
        public void onChanged(int index, SignInputData inputData);
    }

    public interface AddDemolitionImageButtonOnClickListener {
        public void onClick(int index);
    }

    public interface DemolitionImageOnClickListener {
        public void onClick(int index);
        public void onLongClick(int index);
    }

    public interface DateTextOnClickListener {
        public void onClick(int index);
    }

    public interface AutoJudgementButtonOnClickListener {
        public void onClick(int page, SignInputData inputData);
    }

//    public interface SizeEditTextOnClickListener {
//        public void onClick(int index);
//    }

    public interface ContentTextFocusChangedListener {
        public void onFocusChange(int page, boolean hasFocus);
    }

//    public static class InputData {
//        public int typeCode;
//        public String content;
//        public int statusCode;
//        public String width;
//        public String length;
//        public String height;
//        public boolean isFront;
//        public boolean isIntersection;
//        public int lightCode;
//        public String placedFloor;
//        public String totalFloor;
//        public String collisionWidth;
//        public String collisionLength;
//        public int resultCode;
//
//        public InputData(int typeCode, String content, int statusCode, String width, String length,
//                         String height, boolean isFront, boolean isIntersection, int lightCode,
//                         String placedFloor, String totalFloor, String collisionWidth,
//                         String collisionLength, int resultCode) {
//            this.typeCode = typeCode;
//            this.content = content;
//            this.statusCode = statusCode;
//            this.width = width;
//            this.length = length;
//            this.height = height;
//            this.isFront = isFront;
//            this.isIntersection = isIntersection;
//            this.lightCode = lightCode;
//            this.placedFloor = placedFloor;
//            this.totalFloor = totalFloor;
//            this.collisionWidth = collisionWidth;
//            this.collisionLength = collisionLength;
//            this.resultCode = resultCode;
//        }
//    }

//    public static class Setting2 extends Setting {
//
//        public Setting2(Setting s) {
//            super();
//            super.
//        }
//    }
}
