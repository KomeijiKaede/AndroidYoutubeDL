package net.teamfruit.androidyoutubedl

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    private var job: Deferred<Unit>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if(editText.text != null) {
                val url = editText.text.toString()
                job = async {
                    runTask(url)
                }
            }
        }
    }
    private suspend fun runTask(inputURL:String) {
        async(UI) {
            textView.text = "start"
        }.await()
        launch {
            textView.text = statusCheck(inputURL)
        }.join()
    }
}

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


    }
}