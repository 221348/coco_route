package com.example.myapplication

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompanyDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.company_detail)

        // 전달된 데이터 받기
        val companyName = intent.getStringExtra("companyName")

        // TextView와 ImageView를 찾아서 기업 이름과 소개를 설정
        val companyNameTextView = findViewById<TextView>(R.id.companyNameTextView)
        val companyDescriptionTextView = findViewById<TextView>(R.id.companyDescriptionTextView)
        val companyLogo = findViewById<ImageView>(R.id.companyLogo)
        val carbonFootprintGraph = findViewById<ImageView>(R.id.carbonFootprintGraph)

        // 기업에 따라 다른 정보를 설정
        when (companyName) {
            "삼성전자" -> {
                companyNameTextView.text = "삼성전자"
                companyDescriptionTextView.text = "삼성전자는 세계적인 전자기업으로 혁신을 이끌고 있습니다."
                companyLogo.setImageResource(R.drawable.ss2)
                carbonFootprintGraph.setImageResource(R.drawable.graph1) // 삼성전자 그래프
            }
            "SK" -> {
                companyNameTextView.text = "SK"
                companyDescriptionTextView.text = "SK는 다양한 산업에 걸쳐 혁신을 추구하는 기업입니다."
                companyLogo.setImageResource(R.drawable.sk)
                carbonFootprintGraph.setImageResource(R.drawable.graph2) // SK 그래프
            }
            "현대자동차" -> {
                companyNameTextView.text = "현대자동차"
                companyDescriptionTextView.text = "현대자동차는 세계적인 자동차 제조업체입니다."
                companyLogo.setImageResource(R.drawable.hyundai)
                carbonFootprintGraph.setImageResource(R.drawable.graph3) // 현대자동차 그래프
            }
            "LG" -> {
                companyNameTextView.text = "LG"
                companyDescriptionTextView.text = "LG는 다양한 전자 제품과 화학 제품을 제조하는 글로벌 기업입니다."
                companyLogo.setImageResource(R.drawable.lg)
                carbonFootprintGraph.setImageResource(R.drawable.graph4) // LG 그래프
            }
            "롯데" -> {
                companyNameTextView.text = "롯데"
                companyDescriptionTextView.text = "롯데는 다양한 분야에서 활동하는 대기업입니다."
                companyLogo.setImageResource(R.drawable.lotte)
                carbonFootprintGraph.setImageResource(R.drawable.graph5) // 롯데 그래프
            }
            "포스코" -> {
                companyNameTextView.text = "포스코"
                companyDescriptionTextView.text = "포스코는 세계적인 철강 기업입니다."
                companyLogo.setImageResource(R.drawable.posco)
                carbonFootprintGraph.setImageResource(R.drawable.graph6) // 포스코 그래프
            }
            "한화" -> {
                companyNameTextView.text = "한화"
                companyDescriptionTextView.text = "한화는 방산, 에너지, 금융 등 다양한 분야에 진출해 있는 기업입니다."
                companyLogo.setImageResource(R.drawable.hanwha)
                carbonFootprintGraph.setImageResource(R.drawable.graph7) // 한화 그래프
            }
            "현대중공업" -> {
                companyNameTextView.text = "현대중공업"
                companyDescriptionTextView.text = "현대중공업은 세계적인 조선업체입니다."
                companyLogo.setImageResource(R.drawable.hyundai2)
                carbonFootprintGraph.setImageResource(R.drawable.graph8) // 현대중공업 그래프
            }
            else -> {
                // 기업 정보가 없을 경우 그래프 이미지 초기화
                carbonFootprintGraph.setImageResource(0)
            }
        }
    }
}
