package com.mjict.signboardsurvey.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.mjict.signboardsurvey.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Junseo on 2016-07-22.
 */
public class TimePickerDialog extends Dialog {

    private Button confirmButton;
    private DatePicker datePicker;
    private TimePicker timePicker;

    private ConfirmButtonOnClickListener clickListener;

    public TimePickerDialog(Context context) {
        super(context , android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
    }

    private int hour;
    private int min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_time_picker);

        datePicker = (DatePicker)this.findViewById(R.id.date_picker);
        timePicker = (TimePicker)this.findViewById(R.id.time_picker);
        confirmButton = (Button)this.findViewById(R.id.confirm_button);

        Calendar cal = Calendar.getInstance();
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                Calendar cal = Calendar.getInstance();
                cal.set(year, month, day, hour, min);
//                String text = String.format("입력된 시간: %d년 %d월 %d일 %d시 %d분", year, month, day, hour, min);
//                Log.d("junseo", text);
                if(clickListener != null)
                    clickListener.onClicked(cal.getTime());
            }
        });
    }

//    public void setConfirmButtonOnClickListener(View.OnClickListener listener) {
//        confirmButton.setOnClickListener(listener);
//    }

    public void setConfirmButtonOnClickListener(ConfirmButtonOnClickListener listener) {
        clickListener = listener;
    }

    public interface ConfirmButtonOnClickListener {
        public void onClicked(Date time);
    }
}
