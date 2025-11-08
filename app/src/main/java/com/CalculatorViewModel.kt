package com.example.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.mozilla.javascript.Context
import kotlin.math.pow
import org.mozilla.javascript.Scriptable



class CalculatorViewModel: ViewModel() {
    private val _equationText = MutableLiveData("")
    val equationText : LiveData<String> = _equationText

    private val _resultText = MutableLiveData("0")
    val resultText : LiveData<String> = _resultText

    /**
     * Handles button clicks, updating the equation and calculating the result.
     */
    fun onButtonClick(btn: String) {
        Log.i("Clicked Button", btn)

        val currentEquation = _equationText.value ?: ""

        // 1️⃣ AC — очистить всё
        if (btn == "AC") {
            _equationText.value = ""
            _resultText.value = "0"
            return
        }

        // 2️⃣ C — удалить последний символ
        if (btn == "C") {
            if (currentEquation.isNotEmpty()) {
                _equationText.value = currentEquation.dropLast(1)
            }
            return
        }

        // 3️⃣ "=" — СЧИТАЕМ выражение
        if (btn == "=") {
            if (currentEquation.isBlank()) return

            val balanced = balanceParentheses(currentEquation)
            val result = calculateResult(balanced)

            _resultText.value = result

            if (result != "Error") {
                _equationText.value = result
            }
            return
        }

        // 4️⃣ Остальные кнопки — просто добавляем в строку
        var equationToAppend = btn
        when (btn) {
            "sin", "cos", "tan", "log", "ln" -> equationToAppend = "$btn("
            "√" -> equationToAppend = "Math.sqrt("
            "^" -> equationToAppend = "^"
            "!" -> equationToAppend = "!"
        }

        _equationText.value = currentEquation + equationToAppend
    }
    private fun balanceParentheses(expr: String): String {
        var opened = 0
        for (ch in expr) {
            if (ch == '(') opened++
            if (ch == ')') opened--
        }
        return if (opened > 0) {
            expr + ")".repeat(opened)
        } else {
            expr
        }
    }


    /**
     * Evaluates the mathematical equation using the Rhino JavaScript engine.
     * Includes replacements for scientific functions.
     */
    fun calculateResult(equation : String) : String {
        // --- отдельная обработка степени a^b ---
        if (equation.contains("^")) {
            return try {
                val parts = equation.split("^")
                if (parts.size == 2) {
                    val base = parts[0].toDouble()
                    val exponent = parts[1].toDouble()
                    var value = base.pow(exponent)

                    // форматируем красиво, как у тебя в конце
                    var result = value.toString()
                    if (result.endsWith(".0")) {
                        result = result.substringBefore(".")
                    } else {
                        result = "%.6f".format(value)
                            .trimEnd('0')
                            .trimEnd('.')
                    }
                    result
                } else {
                    "Error"
                }
            } catch (e: Exception) {
                "Error"
            }
        }

        // Prepare the equation: Replace custom symbols/functions with their JavaScript equivalents
        val jsEquation = equation.replace("ln", "Math.log")
            .replace("log", "Math.log10") // Assuming 'log' is base 10
            .replace("sin", "Math.sin")
            .replace("cos", "Math.cos")
            .replace("tan", "Math.tan")
            //.replace("√", "Math.sqrt")
            .replace("!", "") // Factorial symbol removed to prevent parsing errors

        val context : Context = Context.enter()
        context.optimizationLevel = -1
        val scriptable : Scriptable = context.initStandardObjects()

        var finalResult = try {
            context.evaluateString(scriptable, jsEquation, "Javascript", 1, null).toString()
        } catch (e: Exception) {
            Log.e("CalculatorViewModel", "Error evaluating expression: $e")
            "Error"
        } finally {
            Context.exit()
        }

        // Clean up the result formatting
        if(finalResult.endsWith(".0")){
            finalResult = finalResult.substringBefore(".")
        } else if (finalResult != "Error") {
            // Limit decimal places for cleaner display
            finalResult = finalResult.toDoubleOrNull()?.let {
                "%.6f".format(it).trimEnd('0').trimEnd('.')
            } ?: finalResult
        }

        return finalResult


    }
}