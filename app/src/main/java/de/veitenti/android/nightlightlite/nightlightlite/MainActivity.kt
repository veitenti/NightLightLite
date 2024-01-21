package de.veitenti.android.nightlightlite

import android.graphics.Point
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import de.veitenti.android.nightlightlite.ui.theme.NightLightLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        makeAppFullScreen(window)
        keepDisplayOn(window)
        setBrightnessToMax(window)

        setContent {
            NightLightLiteTheme {
                val color = remember { mutableStateOf(getDefaultColor()) }
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            val dim = getDisplayDimensions(windowManager)
                            detectTapGestures(
                                onTap = {
                                    color.value =
                                        offsetToColor(it.x, it.y, dim.x.toFloat(), dim.y.toFloat())
                                },
                                onDoubleTap = { setBrightnessToMax(window) }
                            )
                        }, color = color.value
                ) { }
            }
        }
    }
}

fun keepDisplayOn(window: Window) {
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

fun makeAppFullScreen(window: Window) {
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
}

fun setBrightnessToMax(window: Window) {
    val layout = window.attributes
    layout.screenBrightness = 1f
    window.attributes = layout
}

fun getDisplayDimensions(windowManager: WindowManager): Point {
    val size = Point()
    windowManager.defaultDisplay.getSize(size)
    return size
}

fun offsetToColor(x: Float, y: Float, maxX: Float, maxY: Float): Color {
    return Color.hsv(y * 360f / maxY, x / maxX, 1f, 1f)
}

fun getDefaultColor(): Color {
    return Color.hsv(
        (Math.random() * 360).toFloat(),
        Math.random().toFloat(), 1f, 1f

    )
}
