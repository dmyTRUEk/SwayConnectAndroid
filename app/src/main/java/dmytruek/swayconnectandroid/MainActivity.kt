package dmytruek.swayconnectandroid

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var rootLayout: FrameLayout

    private val client = OkHttpClient()

    private var isFirstOnScrollOccurred: Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(event: MotionEvent): Boolean {
                Log.d("TAG", "onDown: ")
                return true // don't return false here or else none of the other gestures will work
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                Log.i("TAG", "onSingleTapConfirmed: ")
                sendEvent(client, "http://192.168.0.103:8000/mouse_click_primary")
                return true
            }

            override fun onLongPress(event: MotionEvent) {
                Log.i("TAG", "onLongPress: ")
                sendEvent(client, "http://192.168.0.103:8000/mouse_click_secondary")
            }

            override fun onDoubleTap(event: MotionEvent): Boolean {
                Log.i("TAG", "onDoubleTap: ")
                sendEvent(client, "http://192.168.0.103:8000/mouse_click_primary")
                Timer().schedule(10){
                    sendEvent(client, "http://192.168.0.103:8000/mouse_click_primary")
                }
                return true
            }

            override fun onScroll(event1: MotionEvent, event2: MotionEvent, dx: Float, dy: Float): Boolean {
                Log.i("TAG", "onScroll: ")
                if (isFirstOnScrollOccurred) {
                    fun Float.interpolate(): Float = this.pow(1.2f)
                    val dx = dx.sign * abs(dx).interpolate()
                    val dy = dy.sign * abs(dy).interpolate()
                    sendEvent(client, "http://192.168.0.103:8000/mouse_move/${-dx.roundToInt()}/${-dy.roundToInt()}")
                }
                else {
                    isFirstOnScrollOccurred = true
                }
                return true
            }

            override fun onFling(event1: MotionEvent, event2: MotionEvent, vx: Float, vy: Float): Boolean {
                Log.d("TAG", "onFling: ")
                isFirstOnScrollOccurred = false
                return true
            }
        })

        rootLayout = findViewById(R.id.root_layout)
        rootLayout.setOnTouchListener { _: View, event: MotionEvent ->
            gestureDetector.onTouchEvent(event)
        }
    }

}
