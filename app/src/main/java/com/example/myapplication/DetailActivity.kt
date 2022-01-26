package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail2.*
import kotlinx.android.synthetic.main.cat_item.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val CAT_FACT_TEXT = "cat_fact_text"

        fun openDetailActivity(context: Context, catFactText: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(CAT_FACT_TEXT, catFactText)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail2)
        setupActionBar()
        setText()
    }
    private fun setupActionBar (){
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title="Detail"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private  fun setText (){
        val text = intent?.extras?.getString(CAT_FACT_TEXT)
        textViewDetail.text= text

    }
}