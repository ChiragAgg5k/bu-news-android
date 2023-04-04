package com.chiragagg5k.bu_news_android;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiragagg5k.bu_news_android.objects.EventsObject;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    ImageView backBtn;
    DatabaseReference eventsRef;
    Button selectDateBtn, submitBtn;
    TextView selectedDateTV;
    EditText eventTitle, eventDescription;
    long date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        backBtn = findViewById(R.id.back_button_add_event);
        selectDateBtn = findViewById(R.id.select_date_button);
        submitBtn = findViewById(R.id.submit_button);
        selectedDateTV = findViewById(R.id.selected_date_text_view);
        eventTitle = findViewById(R.id.event_heading_edit_text);
        eventDescription = findViewById(R.id.event_description_edit_text);

        eventsRef = FirebaseDatabase.getInstance().getReference("events");
        date = getIntent().getLongExtra("date", 0);

        backBtn.setOnClickListener(v -> finish());

        selectDateBtn.setOnClickListener(v -> {
            // on below line we are getting
            // the instance of our calendar.
            final Calendar c = Calendar.getInstance();

            // on below line we are getting
            // our day, month and year.
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // on below line we are creating a variable for date picker dialog.
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    // on below line we are passing context.
                    AddEventActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        selectedDateTV.setVisibility(View.VISIBLE);
                        selectedDateTV.setText(String.format(Locale.US, "%d-%d-%d", dayOfMonth, monthOfYear + 1, year1));

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, monthOfYear, dayOfMonth);
                        date = calendar.getTimeInMillis();
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        submitBtn.setOnClickListener(v -> {
            if (date == 0) {
                selectedDateTV.setVisibility(View.VISIBLE);
                selectedDateTV.setText(R.string.select_date_label);
                return;
            }

            String title = eventTitle.getText().toString();
            String description = eventDescription.getText().toString();

            if (title.isEmpty()) {
                eventTitle.setError("Please enter a title");
                return;
            }

            if (description.isEmpty()) {
                eventDescription.setError("Please enter a description");
                return;
            }

            String id = eventsRef.push().getKey();
            EventsObject eventsObject = new EventsObject(title, description, date);

            assert id != null;
            eventsRef.child(id).setValue(eventsObject);
            finish();
        });
    }
}

