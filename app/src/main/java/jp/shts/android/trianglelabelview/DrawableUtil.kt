package jp.shts.android.trianglelabelview

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.TextView

class DrawableUtil(private val mTextView: TextView, l: OnDrawableListener?) {
    /**
     * TextView四周drawable的序号。
     * 0 left,  1 top, 2 right, 3 bottom
     */
    companion object {
        const val LEFT = 0
        const val RIGHT = 2
    }
    private var listener: OnDrawableListener? = null

    interface OnDrawableListener {
        fun onLeft(v: View?, left: Drawable?)
        fun onRight(v: View?, right: Drawable?)
    }

    @SuppressLint("ClickableViewAccessibility")
    private val mOnTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_UP -> if (listener != null) {
                val drawableLeft = mTextView.compoundDrawables[LEFT]
                if (drawableLeft != null && event.rawX <= mTextView.left + drawableLeft.bounds.width()) {
                    listener?.onLeft(v, drawableLeft)
                    return@OnTouchListener true
                }
                val drawableRight = mTextView.compoundDrawables[RIGHT]
                if (drawableRight != null && event.rawX >= mTextView.right - drawableRight.bounds.width()) {
                    listener?.onRight(v, drawableRight)
                    return@OnTouchListener true
                }
            }
        }
        false
    }

    init {
        mTextView.setOnTouchListener(mOnTouchListener)
        listener = l
    }
}