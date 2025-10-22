package com.example.androidcse227.Unit2

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class NameCanvasView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.WHITE) // background

        var x = 100f
        val y = 200f
        val gap = 100f

        // ====== I ======
        paint.color = Color.RED
        canvas.drawLine(x, y - 100, x, y + 100, paint)
        x += gap

        // ====== S ======
        paint.color = Color.YELLOW
        canvas.drawLine(x, y - 100, x + 60, y - 100, paint)
        canvas.drawLine(x, y, x + 60, y, paint)
        canvas.drawLine(x, y + 100, x + 60, y + 100, paint)
        canvas.drawLine(x, y - 100, x, y, paint)
        canvas.drawLine(x + 60, y, x + 60, y + 100, paint)
        x += gap + 60

        // ====== H ======
        paint.color = Color.GREEN
        canvas.drawLine(x, y - 100, x, y + 100, paint)
        canvas.drawLine(x + 60, y - 100, x + 60, y + 100, paint)
        canvas.drawLine(x, y, x + 60, y, paint)
        x += gap + 60

        // ====== A ======
        paint.color = Color.CYAN
        canvas.drawLine(x, y + 100, x + 30, y - 100, paint)
        canvas.drawLine(x + 30, y - 100, x + 60, y + 100, paint)
        canvas.drawLine(x + 15, y, x + 45, y, paint)
        x += gap + 60

        // ====== A ======
        paint.color = Color.MAGENTA
        canvas.drawLine(x, y + 100, x + 30, y - 100, paint)
        canvas.drawLine(x + 30, y - 100, x + 60, y + 100, paint)
        canvas.drawLine(x + 15, y, x + 45, y, paint)
        x += gap + 60

        // ====== N ======
        paint.color = Color.BLUE
        canvas.drawLine(x, y + 100, x, y - 100, paint)
        canvas.drawLine(x, y - 100, x + 60, y + 100, paint)
        canvas.drawLine(x + 60, y + 100, x + 60, y - 100, paint)
        x += gap + 60

        // ====== T ======
        paint.color = Color.LTGRAY
        canvas.drawLine(x, y - 100, x + 60, y - 100, paint)
        canvas.drawLine(x + 30, y - 100, x + 30, y + 100, paint)
    }
}
