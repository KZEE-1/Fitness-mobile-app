package com.example.kfitness

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class Calculator : AppCompatActivity() {

    private lateinit var ageEditText: EditText
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var TextViewResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)


        // Set fullscreen programmatically
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        ageEditText = findViewById(R.id.editTextAge)
        heightEditText = findViewById(R.id.editTextHeight)
        weightEditText = findViewById(R.id.editTextWeight)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        TextViewResult = findViewById(R.id.textViewResult)

        val calculateButton: Button = findViewById(R.id.btnCalculate)
        calculateButton.setOnClickListener {
            if (validateFields()) {
                calculateCalories()
            }
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set the selected item to "Calculate" when the activity is created
        bottomNavigationView.menu.findItem(R.id.Calculate)?.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.Calculate -> {
                    true
                }

                R.id.Home -> {

                    val calculateIntent = Intent(this@Calculator, Homepage::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)

                    true
                }

                R.id.Workout1 -> {
                    val calculateIntent = Intent(this@Calculator, Workout::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Setting -> {
                    val calculateIntent = Intent(this@Calculator, Profile::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                R.id.Blog1 -> {
                    val calculateIntent = Intent(this@Calculator, Blog::class.java)
                    calculateIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(calculateIntent)
                    overridePendingTransition(0, 0)
                    true
                }

                else -> false
            }
        }
    }

    private fun validateFields(): Boolean {
        // Check if any of the fields are empty
        if (ageEditText.text.isBlank() || heightEditText.text.isBlank() || weightEditText.text.isBlank()
            || genderRadioGroup.checkedRadioButtonId == -1
        ) {
            TextViewResult.text = "Please fill in all fields."
            return false
        }

        // Check if the values are in the correct format
        return try {
            val age = ageEditText.text.toString().toInt()
            val heightInCm = heightEditText.text.toString().toDouble()
            val weightInKg = weightEditText.text.toString().toDouble()
            val genderId = genderRadioGroup.checkedRadioButtonId
            findViewById<RadioButton>(genderId).text.toString() // Just to ensure the gender is selected
            true
        } catch (e: NumberFormatException) {
            TextViewResult.text = "Please enter valid numbers."
            false
        }
    }

    private fun calculateCalories() {
        // Get user input
        val age = ageEditText.text.toString().toInt()
        val heightInCm = heightEditText.text.toString().toDouble()
        val weightInKg = weightEditText.text.toString().toDouble()
        val genderId = genderRadioGroup.checkedRadioButtonId
        val gender = findViewById<RadioButton>(genderId).text.toString()

        // Adjust the calculation based on your specific formula
        // For example, you might use the Harris-Benedict equation
        // Adjusted BMR = 66.47 + (13.75 * weightInKg) + (5.003 * heightInCm) - (6.75 * age)

        val bmr: Double = when (gender) {
            "Male" -> 66.47 + (13.75 * weightInKg) + (5.003 * heightInCm) - (6.75 * age)
            "Female" -> 665.09 + (9.563 * weightInKg) + (1.850 * heightInCm) - (4.676 * age)
            else -> 0.0
        }

        // For simplicity, let's say daily calories are BMR * activity factor (adjust as needed)
        val activityFactor = 1.2 // Modify based on the user's activity level

        val calories = (bmr * activityFactor).toInt()

        // Start the ResultActivity and pass the calculated result
        val intent = Intent(this@Calculator, Result::class.java)
        intent.putExtra("CALCULATED_RESULT", calories)
        startActivity(intent)



    }
}
