package com.example.madproject;



import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    private Switch swNot;
    private EditText editTextDietaryRestrictions;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE);
        swNot =findViewById(R.id.switchNotifications);
        editTextDietaryRestrictions = findViewById(R.id.editTextDietaryRestrictions);
        loadSettings();
        swNot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveNotificationSetting(isChecked);
            // Implement notification logic based on isChecked
        });
    }

    private void loadSettings() {

        boolean areNotificationsEnabled = sharedPreferences.getBoolean("notifications", true);
        String dietaryRestrictions = sharedPreferences.getString("dietary", "");


        swNot.setChecked(areNotificationsEnabled);
        editTextDietaryRestrictions.setText(dietaryRestrictions);
    }



    private void saveNotificationSetting(boolean isEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("notifications", isEnabled);
        editor.apply();
    }

    public void onSaveSettingsClick(View view) {
        String dietaryRestrictions = editTextDietaryRestrictions.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dietary", dietaryRestrictions);
        editor.apply();
        finish(); // Close settings activity
    }
}