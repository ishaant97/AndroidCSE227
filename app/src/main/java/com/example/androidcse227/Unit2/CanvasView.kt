package com.example.androidcse227.Unit2

import android.content.Context
import android.graphics.*
import android.view.View

class CanvasView(context : Context) : View(context) {
    private val paint: Paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.isAntiAlias = true
        paint.strokeWidth = 8f
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLUE

        // 1. Triangle
        val path = Path()
        path.moveTo(300f, 200f)  // Top
        path.lineTo(200f, 400f)  // Bottom left
        path.lineTo(400f, 400f)  // Bottom right
        path.close()
        canvas.drawPath(path, paint)

        // 2. Hello Android text
        paint.style = Paint.Style.FILL
        paint.color = Color.RED
        paint.textSize = 70f
        canvas.drawText("Hello Android", 100f, 600f, paint)

        // 3. Smiley face
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f
        canvas.drawCircle(700f, 400f, 150f, paint) // Face
        canvas.drawCircle(650f, 350f, 20f, paint) // Left eye
        canvas.drawCircle(750f, 350f, 20f, paint) // Right eye
        val smilePath = Path()
        smilePath.addArc(600f, 350f, 800f, 500f, 20f, 140f)
        canvas.drawPath(smilePath, paint)

        // 4. Android Logo (outline only)
        paint.color = Color.GREEN
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE

        val centerX = 1100f
        val centerY = 500f

        // Head (semi-circle)
        val headRect = RectF(centerX - 150f, centerY - 300f, centerX + 150f, centerY)
        canvas.drawArc(headRect, 180f, 180f, false, paint)

        // Body (rectangle)
        canvas.drawRect(centerX - 150f, centerY, centerX + 150f, centerY + 300f, paint)

        // Arms
        canvas.drawRect(centerX - 220f, centerY, centerX - 150f, centerY + 250f, paint) // Left
        canvas.drawRect(centerX + 150f, centerY, centerX + 220f, centerY + 250f, paint) // Right

        // Legs
        canvas.drawRect(centerX - 100f, centerY + 300f, centerX - 40f, centerY + 450f, paint) // Left
        canvas.drawRect(centerX + 40f, centerY + 300f, centerX + 100f, centerY + 450f, paint) // Right

        // Antennae
        canvas.drawLine(centerX - 100f, centerY - 280f, centerX - 150f, centerY - 400f, paint) // Left
        canvas.drawLine(centerX + 100f, centerY - 280f, centerX + 150f, centerY - 400f, paint) // Right

        // Eyes
        canvas.drawCircle(centerX - 50f, centerY - 150f, 15f, paint)
        canvas.drawCircle(centerX + 50f, centerY - 150f, 15f, paint)

        // Text inside logo
        paint.style = Paint.Style.FILL
        paint.textSize = 60f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("Humanoid", centerX, centerY + 150f, paint)


        // === Satellite ===
        paint.color = Color.DKGRAY
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f

        val satCx = 600f   // Satellite center X
        val satCy = 1000f  // Satellite center Y

// Main body
        val satBody = RectF(satCx - 100f, satCy - 120f, satCx + 100f, satCy + 120f)
        canvas.drawRect(satBody, paint)

// Solar panel left
        val satLeftPanel = RectF(satCx - 350f, satCy - 80f, satCx - 100f, satCy + 80f)
        canvas.drawRect(satLeftPanel, paint)

// Solar panel right
        val satRightPanel = RectF(satCx + 100f, satCy - 80f, satCx + 350f, satCy + 80f)
        canvas.drawRect(satRightPanel, paint)

// Stripes on solar panels
        for (i in 1..3) {
            canvas.drawLine(satCx - 350f, satCy - 80f + i * 40f, satCx - 100f, satCy - 80f + i * 40f, paint)
            canvas.drawLine(satCx + 100f, satCy - 80f + i * 40f, satCx + 350f, satCy - 80f + i * 40f, paint)
        }

// Antenna dish
        val satDishCenterX = satCx
        val satDishCenterY = satCy + 150f
        canvas.drawCircle(satDishCenterX, satDishCenterY, 50f, paint)

// Antenna stick
        canvas.drawLine(satCx, satCy + 120f, satDishCenterX, satDishCenterY - 50f, paint)

// Signal waves
        val satSignal = Path()
        satSignal.addArc(RectF(satDishCenterX - 80f, satDishCenterY - 80f, satDishCenterX + 80f, satDishCenterY + 80f), 220f, 80f)
        satSignal.addArc(RectF(satDishCenterX - 120f, satDishCenterY - 120f, satDishCenterX + 120f, satDishCenterY + 120f), 220f, 80f)
        canvas.drawPath(satSignal, paint)

    }
}
