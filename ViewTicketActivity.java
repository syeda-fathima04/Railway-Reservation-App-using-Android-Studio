package com.example.railwaymad;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class ViewTicketActivity extends AppCompatActivity {
    private TextView tvBookingDetails;
    private Button btnBackHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setContentView(R.layout.activity_view_ticket);

        // Initialize views
        tvBookingDetails = findViewById(R.id.tvBookingDetails);
        btnBackHome = findViewById(R.id.btnBackHome);

        // Retrieve data passed from the previous activity
        Intent intent = getIntent();
        String trainDetails = intent.getStringExtra("train");
        String travelDate = intent.getStringExtra("selectedDate");
        String passengers = intent.getStringExtra("passengers");
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone");

        // Combine all details into a formatted string
        String bookingDetails = "Ticket Confirmation\n\n" +
                "Train: " + trainDetails + "\n" +
                "Date: " + travelDate + "\n" +
                "Passengers: " + passengers + "\n" +
                "Name: " + name + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone;

        // Display the booking details in the TextView
        tvBookingDetails.setText(bookingDetails);

        // Set OnClickListener for the Back to Home button
        btnBackHome.setOnClickListener(v -> {
            Toast.makeText(ViewTicketActivity.this, "Returning to Home", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(ViewTicketActivity.this, MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish(); // Close the current activity
        });
    }
}