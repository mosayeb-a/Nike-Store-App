package com.example.nikestore.feature.checkout

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nikestore.R
import com.example.nikestore.common.EXTRA_KEY_ID
import com.example.nikestore.common.formatPrice
import com.example.nikestore.feature.home.HomeFragment
import com.example.nikestore.feature.main.MainActivity
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CheckOutActivity : AppCompatActivity() {
    val viewModel: CheckoutViewModel by viewModel {
        val uri: Uri? = intent.data
        if (uri != null)
            parametersOf(uri.getQueryParameter("order_id")!!.toInt())
        else
            parametersOf(intent.extras!!.getInt(EXTRA_KEY_ID))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        val orderPriceTv: TextView = findViewById(R.id.orderPriceTv)
        val orderStatusTv: TextView = findViewById(R.id.orderStatusTv)
        val purchaseStatusTv: TextView = findViewById(R.id.purchaseStatusTv)

        viewModel.checkoutLiveData.observe(this) {
            orderPriceTv.text = formatPrice(it.payable_price)
            orderStatusTv.text = it.payment_status
            purchaseStatusTv.text =
                if (it.purchase_success) "خرید با موفقیت انجام شد" else "خرید ناموفق"
        }

        val returnHomeBtn: MaterialButton = findViewById(R.id.returnHomeBtn)
        returnHomeBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }


    }
}