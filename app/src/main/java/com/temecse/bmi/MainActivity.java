package com.temecse.bmi;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private EditText ageInput, feetInput, inchesInput, weightInput;
    private RadioGroup genderGroup;
    private TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi);

        initializeViews();
        setupCalculateButton();
    }

    private void initializeViews() {
        ageInput = findViewById(R.id.age);
        feetInput = findViewById(R.id.feet);
        inchesInput = findViewById(R.id.inc);
        weightInput = findViewById(R.id.weight);
        genderGroup = findViewById(R.id.genderGroup);
        resultText = findViewById(R.id.result);
    }

    private void setupCalculateButton() {
        Button calculateButton = findViewById(R.id.cal);
        calculateButton.setOnClickListener(v -> calculateAndDisplayBMI());
    }

    private void calculateAndDisplayBMI() {
        try {
            if (!validateInputs()) return;

            double bmi = calculateBMI();
            displayBMIResult(bmi);

        } catch (NumberFormatException e) {
            showToast("Please enter valid numbers");
        } catch (Exception e) {
            showToast("Error calculating BMI");
        }
    }

    private boolean validateInputs() {
        if (genderGroup.getCheckedRadioButtonId() == -1) {
            showToast("Please select your gender");
            return false;
        }

        if (ageInput.getText().toString().isEmpty()) {
            showToast("Please enter your age");
            return false;
        }

        if (feetInput.getText().toString().isEmpty() || inchesInput.getText().toString().isEmpty()) {
            showToast("Please enter your height");
            return false;
        }

        if (weightInput.getText().toString().isEmpty()) {
            showToast("Please enter your weight");
            return false;
        }

        return true;
    }

    private double calculateBMI() {
        int feet = Integer.parseInt(feetInput.getText().toString());
        int inches = Integer.parseInt(inchesInput.getText().toString());
        double weight = Double.parseDouble(weightInput.getText().toString());

        // Convert height to meters
        double heightInMeters = ((feet * 12) + inches) * 0.0254;
        return weight / (heightInMeters * heightInMeters);
    }

    private void displayBMIResult(double bmi) {
        int age = Integer.parseInt(ageInput.getText().toString());
        boolean isMale = genderGroup.getCheckedRadioButtonId() == R.id.male;

        String formattedBMI = new DecimalFormat("0.00").format(bmi);
        String interpretation = interpretBMI(bmi, age, isMale);

        resultText.setText(String.format("BMI: %s\n%s", formattedBMI, interpretation));
    }

    private String interpretBMI(double bmi, int age, boolean isMale) {
        String genderTerm = age < 18 ? (isMale ? "Boy" : "Girl") : (isMale ? "Sir" : "Madam");

        if (age < 18) {
            // Pediatric BMI interpretation
            if (bmi < 18.5) return genderTerm + ", you may be underweight for your age. Consult a pediatrician.";
            if (bmi <= 25.5) return genderTerm + ", your weight is healthy for your age!";
            return genderTerm + ", you may be overweight for your age. Consult a pediatrician.";
        }
        else if (age <= 65) {
            // Adult BMI interpretation
            if (bmi < 18.5) return "You're underweight. Consider nutritional counseling.";
            if (bmi <= 24.9) return "You're at a healthy weight. Maintain your lifestyle!";
            if (bmi <= 29.9) return "You're overweight. Consider more exercise and diet changes.";
            return "You're obese. Please consult a doctor for health advice.";
        }
        else {
            // Elderly BMI interpretation (slightly higher thresholds)
            if (bmi < 22) return genderTerm + ", you may be underweight. Ensure proper nutrition.";
            if (bmi <= 27) return genderTerm + ", your weight is healthy for your age.";
            return genderTerm + ", you may be overweight. Consult your doctor about healthy aging.";
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}