# 🧮 Calculadora Android

Una calculadora Android moderna construida con **Jetpack Compose** que ofrece dos modos de uso: una calculadora básica en modo vertical y una calculadora científica en modo horizontal.

---

## ✨ Características

### Modo vertical (portrait)
- Operaciones aritméticas básicas: suma, resta, multiplicación y división
- Soporte para paréntesis
- Botón **C** para borrar el último carácter
- Botón **AC** para limpiar toda la expresión
- Vista previa del resultado en tiempo real

### Modo horizontal (landscape)
- Todo lo anterior, más funciones científicas:
  - Trigonometría: `sin`, `cos`, `tan`
  - Logaritmos: `ln`, `log`
  - Raíz cuadrada: `√`
  - Potencias: `^`
  - Factorial: `!`
- Diseño dividido en dos paneles: funciones avanzadas a la izquierda, teclado numérico a la derecha

---

## 🛠️ Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| Kotlin | Lenguaje principal |
| Jetpack Compose | Interfaz de usuario declarativa |
| ViewModel + LiveData | Gestión del estado |
| Rhino (Mozilla JS Engine) | Evaluación de expresiones matemáticas |
| Material 3 | Componentes visuales y temas |

---

## 📁 Estructura del proyecto

```
com.example.calculator/
├── MainActivity.kt          # Punto de entrada; detecta orientación y lanza el layout correcto
├── CalculatorUI.kt          # Composables: Calculator (vertical) y HorizontalCalculator (horizontal)
└── CalculatorViewModel.kt   # Lógica de negocio y evaluación de expresiones
```

---

## 🚀 Cómo ejecutar

1. Clona el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/calculator-android.git
   ```
2. Ábrelo en **Android Studio** (versión Flamingo o superior recomendada).
3. Sincroniza las dependencias de Gradle.
4. Ejecuta la app en un emulador o dispositivo físico con Android 8.0+ (API 26+).

---

## 🎨 Colores de los botones

| Color | Botones |
|---|---|
| 🔴 Rojo | `C`, `AC` |
| ⚫ Gris | Funciones científicas y paréntesis |
| 🟠 Naranja | Operadores aritméticos (`+`, `-`, `*`, `/`, `=`) |
| 🩵 Cian | Dígitos (`0`–`9`) y punto decimal |

---

## ⚙️ Lógica de evaluación

Las expresiones matemáticas se evalúan usando el motor **Rhino** (JavaScript embebido). Antes de ejecutar la evaluación:

- Las funciones como `sin`, `cos`, `ln`, `log` se traducen a sus equivalentes de `Math` en JavaScript.
- Los paréntesis abiertos se cierran automáticamente si es necesario.
- Las potencias (`^`) se procesan directamente con `kotlin.math.pow`.

---

## 📄 Licencia

Este proyecto está disponible bajo la licencia **MIT**. Consulta el archivo `LICENSE` para más detalles.
