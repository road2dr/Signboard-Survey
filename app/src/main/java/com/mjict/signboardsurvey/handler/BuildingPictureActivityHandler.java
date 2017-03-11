package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.CameraActivity;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.DeleteBuildingPictureTask;
import com.mjict.signboardsurvey.task.InsertBuildingPictureTask;
import com.mjict.signboardsurvey.task.LoadImageTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-07-26.
 */
public class BuildingPictureActivityHandler extends SABaseActivityHandler {


    private PictureActivity activity;
    private ArrayList<BuildingPicture> buildingPictures = null;
    private Building building = null;
    private int imageIndex = -1;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (PictureActivity)getActivity();

        // register listener
        activity.setNextButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked();
            }
        });

        activity.setPreviousButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousButtonClicked();
            }
        });

        activity.setDeleteButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButtonClicked();
            }
        });

        activity.setAddPictureButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPictureButtonClicked();
            }
        });

        // init
        Intent intent = activity.getIntent();
        buildingPictures = (ArrayList<BuildingPicture>) intent.getSerializableExtra(MJConstants.BUILDING_PICTURE_LIST);
        building = (Building)intent.getSerializableExtra(MJConstants.BUILDING);
        imageIndex = intent.getIntExtra(MJConstants.INDEX, 0);

        if(building == null) {
            // TODO 에러
            activity.finish();
            return;
        }

        if(buildingPictures == null) {
            // TODO 사진 목록을 불러와야 하겠지
            activity.finish();
            return;
        }

        // do first job
        if(buildingPictures.size() <=0) {
            updatePageText();
            activity.setImage(R.drawable.ic_building);
        } else {
            updatePageText();
            startToLoadImage();
        }

//        startToLoadBuildingPictures();
    }

    @Override
    public void onBackPressed() {
        Intent responseIntent = new Intent();
        responseIntent.putExtra(MJConstants.BUILDING_PICTURE_LIST, buildingPictures);
        activity.setResult(Activity.RESULT_OK, responseIntent);

        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MJConstants.INDEX, imageIndex);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MJConstants.REQUEST_TAKE_AND_SAVE) {
            if(resultCode == Activity.RESULT_OK) {
                Boolean result = data.getBooleanExtra(MJConstants.RESPONSE, false);
                if(result == false) {
                    // TODO 사진 저장 실패
                } else {
                    String path = data.getStringExtra(MJConstants.PATH);
                    startToSavePictureInformation(path);
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addPictureButtonClicked() {
        // 건물 사진 파일 이름 만듬
        String time = Utilities.getCurrentTimeAsString();
        int hash = Math.abs((int)Utilities.hash(time));
        String dir = SyncConfiguration.getDirectoryForBuildingPicture(false);
        final String fileName = String.format("building_%d_%d.jpg", building.getId(), hash);
        String path = dir + fileName;

        Intent intent = new Intent(activity, CameraActivity.class);
        intent.putExtra(HANDLER_CLASS, CameraActivityHandler.class);
        intent.putExtra(MJConstants.PATH, path);

        activity.startActivityForResult(intent, MJConstants.REQUEST_TAKE_AND_SAVE);

    }

    private void previousButtonClicked() {
        if(buildingPictures == null)
            return;

        imageIndex--;
        if(imageIndex < 0)
            imageIndex = buildingPictures.size()-1;

        updatePageText();
        startToLoadImage();
    }

    private void nextButtonClicked() {
        if(buildingPictures == null)
            return;

        imageIndex++;
        if(imageIndex >= buildingPictures.size())
            imageIndex = 0;

        updatePageText();
        startToLoadImage();
    }

    private void deleteButtonClicked() {
        if(buildingPictures == null || buildingPictures.size() <= 0)
            return;

        if(buildingPictures.size() <= 0)
            return;

        final BuildingPicture pic = buildingPictures.get(imageIndex);
        if(pic.isSynchronized() == true) {
            Toast.makeText(activity, R.string.cannot_delete_that_sign_information, Toast.LENGTH_SHORT).show();
            return;
        }

        DeleteBuildingPictureTask task = new DeleteBuildingPictureTask(activity.getBaseContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.deleteing);
            }
            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();

                int msg = result ? R.string.succeeded_to_delete : R.string.failed_to_delete;
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();

                buildingPictures.remove(pic);
                if(imageIndex >= buildingPictures.size())
                    imageIndex = 0;

                updatePageText();
                startToLoadImage();
            }
        });
        task.execute(pic);
    }

//    private void startToLoadBuildingPictures() {
//        LoadBuildingPictureTask task = new LoadBuildingPictureTask(activity.getApplicationContext());
//        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<BuildingPicture>>() {
//            @Override
//            public void onTaskStart() {
//                activity.showWaitingDialog(R.string.loading_building_picture_list);
//            }
//            @Override
//            public void onTaskFinished(List<BuildingPicture> pictures) {
//                activity.hideWaitingDialog();
//                if(pictures == null) {
//                    activity.setImage(R.drawable.img_buil_fail);
//                }
//
//                buildingPictures = new ArrayList<BuildingPicture>(pictures);
//
//                if(buildingPictures.size() <=0) {
//                    updatePageText();
//                    activity.setImage(R.drawable.img_buil_not);
//                } else {
//                    updatePageText();
//                    startToLoadImage();
//                }
//            }
//        });
//        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, buildingId);
//    }

    private void startToLoadImage() {
        if(imageIndex < 0 || imageIndex >= buildingPictures.size())
            imageIndex = 0;

        if(buildingPictures.size() <= 0)
            return;

        BuildingPicture pic = buildingPictures.get(imageIndex);
        String path = SyncConfiguration.getDirectoryForBuildingPicture(pic.isSynchronized())+pic.getPath();
        LoadImageTask task = new LoadImageTask();
        task.setSampleSize(1);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showLoadingView();
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... image) {
                if(image != null)
                    activity.setImage(image[0].image);
            }
            @Override
            public void onTaskFinished(Boolean aBoolean) {
            }
        });

        task.execute(path);
    }

    private void startToSavePictureInformation(String path) {
        String fileName = path.substring(path.lastIndexOf("/")+1);

        final BuildingPicture bp = new BuildingPicture(0, building.getId(), fileName, false);
        InsertBuildingPictureTask task = new InsertBuildingPictureTask(activity.getApplicationContext());
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<Long, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }
            @Override
            public void onTaskProgressUpdate(Long... values) {
                if(values != null && values.length > 0) {
                    long id = values[0];
                    bp.setId(id);
                    buildingPictures.add(bp);

                    imageIndex = buildingPictures.size()-1;

                    startToLoadImage();

                    updatePageText();
                }
            }

            @Override
            public void onTaskFinished(Boolean result) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(bp);
    }

    private void updatePageText() {
        String pageText = null;
        if(buildingPictures.size() <= 0)
            pageText = "0/0";
        else
            pageText = (imageIndex+1)+"/"+buildingPictures.size();

        activity.setPageText(pageText);
    }
}
