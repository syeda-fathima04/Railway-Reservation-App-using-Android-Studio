package com.example.railwaymad;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class BookTicketActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "booking_confirmation";
    private Button btBookTicket, btViewTickets, btBackToHome;
    private AutoCompleteTextView autoCompleteTrain;
    private EditText etPassengers, etname, etemail, etphone;
    private TextView tvSelectedDate, tvConfirmation;
    private ImageButton btnDate;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btViewTickets = findViewById(R.id.btnViewTickets);
        btBackToHome = findViewById(R.id.btnBackToHome);
        autoCompleteTrain = findViewById(R.id.autoCompleteTrain);
        etPassengers = findViewById(R.id.etPassengers);
        etname = findViewById(R.id.etName);
        etemail = findViewById(R.id.etEmail);
        etphone = findViewById(R.id.etPhone);
        btnDate = findViewById(R.id.btnDate);
        btBookTicket=findViewById(R.id.btnBookTicket);


        // Retrieve the train name from the intent
        String trainName = getIntent().getStringExtra("train");
        if (trainName != null) {
            autoCompleteTrain.setText(trainName);
        }

        // Create notification channel (for Android 8.0 and above)
        createNotificationChannel();

        // Set listener for Date Picker Button
        btnDate.setOnClickListener(v -> showDatePickerDialog());

        // Set listener for Book Ticket Button
        btBookTicket.setOnClickListener(v -> bookTicket());

        // Navigate to View Tickets Page
        btViewTickets.setOnClickListener(v -> {
            Intent intent = new Intent(BookTicketActivity.this, ViewTicketActivity.class);
            startActivity(intent);
        });

        // Navigate to Home Page
        btBackToHome.setOnClickListener(v -> {
            Intent intent = new Intent(BookTicketActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                }, year, month, day);
        datePickerDialog.show();
    }

    private void bookTicket() {
        String train = autoCompleteTrain.getText().toString();
        String passengers = etPassengers.getText().toString();
        String name = etname.getText().toString();
        String email = etemail.getText().toString();
        String phone = etphone.getText().toString();

        if (train.isEmpty() || passengers.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || selectedDate == null) {
            // Show a toast for missing fields
            Toast.makeText(this, "\"Please fill all fields.\"", Toast.LENGTH_SHORT).show();
        } else {
            // Create a confirmation message
            String confirmationMessage = "Ticket Booked Successfully!\n" +
                    "Train: " + train + "\n" +
                    "Date: " + selectedDate + "\n" +
                    "Passengers: " + passengers + "\n" +
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Phone: " + phone;

            Intent intent = new Intent(BookTicketActivity.this, ViewTicketActivity.class);
            intent.putExtra("train", train);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("passengers", passengers);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("phone", phone);
            startActivity(intent);


            // Send Notification
            sendNotification(confirmationMessage);

            // Show a toast for success
            Toast.makeText(this, "Your ticket has been booked successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNotification(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.train_600nw_104147417)
                .setContentTitle("Booking Confirmation")
                .setContentText("Your ticket has been booked successfully!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Booking Confirmation";
            String description = "Notifications for ticket booking confirmation.";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}