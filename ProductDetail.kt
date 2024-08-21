package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView

class ProductDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_detail)

        // Intent로부터 데이터 받아오기
        val productName = intent.getStringExtra("productName")
        val productPoints = intent.getIntExtra("productPoints", 0)
        val productImageResId = intent.getIntExtra("productImageResId", 0)

        // 뷰에 데이터 설정
        val imageView = findViewById<ImageView>(R.id.imageViewProduct)
        val textViewName = findViewById<TextView>(R.id.textViewProductName)
        val textViewPoints = findViewById<TextView>(R.id.textViewProductPoints)

        imageView.setImageResource(productImageResId)
        textViewName.text = productName
        textViewPoints.text = "$productPoints Points"
    }
}
