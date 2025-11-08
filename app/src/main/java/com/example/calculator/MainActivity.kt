package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.ViewModelProvider
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // создаём экземпляр ViewModel (сохраняется при повороте экрана)
        val calculatorViewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

       // enableEdgeToEdge()

        setContent {
            CalculatorTheme {
                // Смотрим, какая сейчас ориентация экрана
                val configuration = LocalConfiguration.current

                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    // 🌄 ГОРИЗОНТАЛЬНЫЙ РЕЖИМ → профессиональный калькулятор
                    HorizontalCalculator(viewModel = calculatorViewModel)
                } else {
                    // 📱 ВЕРТИКАЛЬНЫЙ РЕЖИМ → базовый калькулятор
                    Calculator(viewModel = calculatorViewModel)
                }
            }
        }
    }
}
