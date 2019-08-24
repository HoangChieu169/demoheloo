package com.example.lamlairecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val  number =intent.getIntExtra("putDataToDetails", Int.MIN_VALUE)
        if (number !=Int.MIN_VALUE){
            editText.setText("$number")
        }
    }
    fun onClickFinish(view: View){
        var number=editText.text.toString().toIntOrNull()
        if (number == null){
            Toast.makeText(this,"hay nhap vao mot so nguyen", Toast.LENGTH_SHORT).show()
            return
        }
        intent.putExtra("Putback",number)
        setResult(0, intent)
        finish()


    }
}
