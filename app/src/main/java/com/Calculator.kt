package com.example.calculator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import android.content.res.Configuration

import androidx.compose.ui.platform.LocalConfiguration



// Note: Assumes CalculatorViewModel is available in this package

/**
 * Standard button list for the basic vertical calculator.
 */
// Обычный вертикальный калькулятор
val buttonList = listOf(
    "C", "(", ")", "/",
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "AC", "0", ".", "="
)

// ЛЕВАЯ часть в горизонтальном (только функции, без цифр)
val advancedLeftButtons = listOf(
    "C","ln", "log",
    "sin","cos","tan",
    "√","^", "!",
    "AC", "(", ")"
)

// ПРАВАЯ часть в горизонтальном (цифры + операторы)
val basicRightButtons = listOf(
    "7", "8", "9", "*",
    "4", "5", "6", "+",
    "1", "2", "3", "-",
    "0", ".", "=", "/"
)

/**
 * Determines the color of a calculator button based on its label.
 * This unified function handles both basic and scientific buttons.
 */
fun getButtonColor(btn: String): Color {
    if (btn == "C" || btn == "AC")
        return Color(0xFFBE2419) // Red: Clear/All Clear

    // Dark Gray: Utility/Function buttons (including parentheses)
    if (btn == "(" || btn == ")" || btn == "ln" || btn == "log" ||
        btn == "sin" || btn == "cos" || btn == "tan" || btn == "√" ||
        btn == "^" || btn == "!"
    )
        return Color.Gray

    // Orange: Operators/Equals
    if (btn == "/" || btn == "*" || btn == "+" || btn == "-" || btn == "=")
        return Color(0xFFFF9800)

    // Cyan: Number buttons
    return Color(0xFF00C8C9)
}

@Composable
fun CalculatorButton(btn: String, onClick: () -> Unit) {
    val configuration = LocalConfiguration.current
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // отступы вокруг кнопки
    val padding = if (isLandscape) 2.dp else 8.dp

    // ФИКСИРОВАННЫЙ размер круга
    val buttonSize = if (isLandscape) 48.dp else 80.dp
    // 48.dp в ландшафте даёт 4 ряда, которые точно влазят

    val fontSize = if (isLandscape) 13.sp else 18.sp

    Box(
        modifier = Modifier.padding(padding),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.size(buttonSize),   // <– главный параметр
            shape = CircleShape,
            contentColor = Color.White,
            containerColor = getButtonColor(btn)
        ) {
            Text(
                text = btn,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun HorizontalCalculator(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel
) {
    val equationText by viewModel.equationText.observeAsState("")
    val resultText by viewModel.resultText.observeAsState("0")

    Column(
        modifier = modifier
            .fillMaxSize()
           //.padding(10.dp)
            .padding(
                start = 24.dp,
                end   = 34.dp,
                top   = 20.dp,
                bottom = 32.dp   //  добавили место под жестовую панель
    )
    ) {
        // ----- ДИСПЛЕЙ (сверху на всю ширину) -----
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(5f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = equationText,
                style = TextStyle(fontSize = 20.sp, textAlign = TextAlign.End),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = resultText,
                style = TextStyle(
                    fontSize = 32.sp,
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ----- НИЖНЯЯ ЧАСТЬ: ДВЕ СЕТКИ В РЯДУ -----
        Row(
            modifier = Modifier.weight(7.5f)
        ) {

            // Левая НАУЧНАЯ часть
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    //userScrollEnabled=false
            ) {
                items(advancedLeftButtons) { btn ->
                    CalculatorButton(btn = btn) {
                        viewModel.onButtonClick(btn)
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Правая БАЗОВАЯ часть (цифры)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    //userScrollEnabled=false
            ) {
                items(basicRightButtons) { btn ->
                    CalculatorButton(btn = btn) {
                        viewModel.onButtonClick(btn)
                    }
                }
            }
        }
    }
}

@Composable
fun Calculator(modifier: Modifier = Modifier, viewModel: CalculatorViewModel) {

    val equationText by viewModel.equationText.observeAsState("")
    val resultText by viewModel.resultText.observeAsState("0")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                top = 64.dp,      // отступ от статус-бара
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.End
    ) {
        // Блок дисплея (верхняя часть)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.30f),            // 30% экрана под текст
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom   // ← здесь всё ОК
        ) {
            Text(
                text = equationText,
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = resultText,
                style = TextStyle(
                    fontSize = 36.sp,
                    textAlign = TextAlign.End
                ),
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Сетка кнопок (нижняя часть)
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.weight(0.70f)   // 70% экрана отдадим кнопкам
        ) {
            items(buttonList) { btn ->
                CalculatorButton(btn = btn) {
                    viewModel.onButtonClick(btn)
                }
            }
        }
    }


}