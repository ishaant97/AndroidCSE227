package com.example.androidcse227.Unit2.EndTermPractical

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View


class MyCanvasView(context: Context, attrs : AttributeSet? = null) : View(context,attrs){

    override fun onDraw(canvas : Canvas){
        super.onDraw(canvas)

        val paint = Paint()

        paint.color = Color.BLACK
        paint.strokeWidth = 8f

        //for line
//        canvas.drawLine(100f, 300f, 500f, 300f, paint)

        // for square
        canvas.drawLine(100f,200f,700f,200f,paint)
        canvas.drawLine(100f,200f,100f,800f,paint)
        canvas.drawLine(100f,800f,700f,800f,paint)
        canvas.drawLine(700f,200f,700f,800f,paint)

//        canvas.drawRect(100f,200f,700f,800f,paint)

        //for circle
        paint.color = Color.RED

        canvas.drawCircle(400f,1200f,200f,paint)

        //for triangle
        paint.color = Color.BLUE
        paint.strokeWidth = 10f

        canvas.drawLine(400f,1500f,200f,1800f,paint)
        canvas.drawLine(200f,1800f,600f,1800f,paint)
        canvas.drawLine(600f,1800f,400f,1500f,paint)


        //Semicircle
        paint.color=Color.MAGENTA
        paint.style=Paint.Style.STROKE
        paint.strokeWidth=8f
        canvas.drawArc(100f,1900f,700f,2500f,180f,180f,true,paint)
    }
}