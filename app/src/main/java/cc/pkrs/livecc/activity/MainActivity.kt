package cc.pkrs.livecc.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.annotation.RequiresApi
import cc.pkrs.livecc.R

class MainActivity : BaseActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val etRID = findViewById<EditText>(R.id.etRID)
        findViewById<View>(R.id.btnPlay).setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("rid", etRID.text.toString())
            startActivity(intent)
        }
    }
}