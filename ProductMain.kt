package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class ProductMain : AppCompatActivity() {

    private lateinit var textViewPoints: TextView
    private lateinit var buttonProductOfTheDay: Button
    private lateinit var buttonRecommendedProducts: Button
    private lateinit var editTextSearch: EditText
    private lateinit var layoutSearchResults: LinearLayout

    private var points = 200
    private val products = listOf(
        "포도", "소금빵", "휴지", "책"
    )

    private val productDetails = mapOf(
        "포도" to Pair(100, R.drawable.product_image_1),
        "소금빵" to Pair(150, R.drawable.product_image_2),
        "휴지" to Pair(200, R.drawable.product_image_3),
        "책" to Pair(300, R.drawable.product_image_4)
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_main)

        // 뷰 초기화
        textViewPoints = findViewById(R.id.textViewPoints)
        buttonProductOfTheDay = findViewById(R.id.buttonProductOfTheDay)
        buttonRecommendedProducts = findViewById(R.id.buttonRecommendedProducts)
        editTextSearch = findViewById(R.id.editTextSearch)
        layoutSearchResults = findViewById(R.id.layoutSearchResults)
        val buttonSearch: Button = findViewById(R.id.buttonSearch)

        // 사용자 포인트 설정
        textViewPoints.text = "마이 코코: $points"

        // 버튼 클릭 이벤트 설정
        buttonSearch.setOnClickListener { searchProducts() }
        buttonProductOfTheDay.setOnClickListener { showProductOfTheDay() }
        buttonRecommendedProducts.setOnClickListener { showRecommendedProduct() }

        // 상품 클릭 이벤트 설정
        findViewById<LinearLayout>(R.id.layoutGrape).setOnClickListener {
            showProductDetails("포도")
        }
        findViewById<LinearLayout>(R.id.layoutSaltBread).setOnClickListener {
            showProductDetails("소금빵")
        }
    }

    private fun showProductOfTheDay() {
        val productOfTheDay = getProductOfTheDay(products)
        buttonProductOfTheDay.text = "오늘의 상품: $productOfTheDay"
        // 선택된 오늘의 상품 세부 페이지로 이동
        showProductDetails(productOfTheDay)
    }

    private fun showRecommendedProduct() {
        val recommendedProduct = getRecommendedProduct(products)
        buttonRecommendedProducts.text = "추천 상품: $recommendedProduct"
        // 추천된 상품의 세부 페이지로 이동
        showProductDetails(recommendedProduct)
    }

    private fun getProductOfTheDay(products: List<String>): String {
        val random = Random()
        return products[random.nextInt(products.size)]
    }

    private fun getRecommendedProduct(products: List<String>): String {
        val random = Random()
        return products[random.nextInt(products.size)]
    }

    private fun searchProducts() {
        val searchTerm = editTextSearch.text.toString().lowercase()
        layoutSearchResults.removeAllViews()
        for (product in products) {
            if (product.lowercase().contains(searchTerm)) {
                val textView = TextView(this).apply {
                    text = product
                    textSize = 18f
                    setTextColor(getColor(android.R.color.black))
                    setOnClickListener {
                        showProductDetails(product)
                    }
                }
                layoutSearchResults.addView(textView)
            }
        }
    }

    private fun showProductDetails(productName: String) {
        val productInfo = productDetails[productName]
        if (productInfo != null) {
            val (productPoints, productImageResId) = productInfo
            val intent = Intent(this, ProductDetail::class.java).apply {
                putExtra("productName", productName)
                putExtra("productPoints", productPoints)
                putExtra("productImageResId", productImageResId)
            }
            startActivity(intent)
        }
    }
}
