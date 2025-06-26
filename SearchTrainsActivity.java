package com.example.railwaymad;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchTrainsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private TextView tvSelectedDate;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_trains);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dbHelper = new DBHelper(this);

        // UI Elements
        AutoCompleteTextView source = findViewById(R.id.source);
        AutoCompleteTextView dest = findViewById(R.id.dest);
        Spinner trainSpinner = findViewById(R.id.train_spinner);
        ImageButton date = findViewById(R.id.Date);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        Button search = findViewById(R.id.search);
        Button backHome = findViewById(R.id.btnBackHome);
        Button nextBookTicket = findViewById(R.id.btnNextBookTicket);

        // Populate Station and Train Lists
        ArrayList<String> stationList = dbHelper.getAllStations();
        ArrayList<String> trainList = dbHelper.getAllTrains();

        // Set Adapters for AutoCompleteTextViews
        ArrayAdapter<String> stationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, stationList);
        source.setAdapter(stationAdapter);
        dest.setAdapter(stationAdapter);

        // Set Adapter for Spinner
        ArrayAdapter<String> trainAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trainList);
        trainAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trainSpinner.setAdapter(trainAdapter);

        // Date Picker
        date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        selectedMonth += 1; // Months are 0-indexed
                        selectedDate = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                        tvSelectedDate.setText("Date: " + selectedDate);
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Search Button
        search.setOnClickListener(v -> {
            String src = source.getText().toString();
            String destination = dest.getText().toString();
            String selectedTrain = trainSpinner.getSelectedItem() != null ? trainSpinner.getSelectedItem().toString() : "";

            if (src.isEmpty() || destination.isEmpty() || selectedTrain.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields and select a date", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Searching for:\nTrain: " + selectedTrain + "\nFrom: " + src + "\nTo: " + destination + "\nDate: " + selectedDate, Toast.LENGTH_LONG).show();
            }
        });

        // Back to Home Button
        backHome.setOnClickListener(v -> {
            Intent intent = new Intent(SearchTrainsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Next to Book Ticket Page Button
        nextBookTicket.setOnClickListener(v -> {
            String selectedTrain = trainSpinner.getSelectedItem() != null ? trainSpinner.getSelectedItem().toString() : "";

            if (selectedTrain.isEmpty()) {
                Toast.makeText(this, "Please select a train before proceeding", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(SearchTrainsActivity.this, BookTicketActivity.class);
                intent.putExtra("train", selectedTrain); // Pass only the train name
                startActivity(intent);
            }
        });
    }
}

