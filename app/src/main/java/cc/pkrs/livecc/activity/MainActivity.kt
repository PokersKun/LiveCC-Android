package cc.pkrs.livecc.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import cc.pkrs.livecc.R
import java.lang.reflect.Field;
import java.lang.reflect.Method;


class MainActivity : AppCompatActivity() {
    private var node = "cc"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        closeDetectedProblemApiDialog()
        setContentView(R.layout.activity_main)
        val etRID = findViewById<EditText>(R.id.etRID)
        val rgNode = findViewById<RadioGroup>(R.id.rgNode)

        rgNode.setOnCheckedChangeListener { _, i ->
            if (i == R.id.rbCC)
                node = "cc"
            else if (i == R.id.rbBiliBili)
                node = "bilibili"
        }

        findViewById<View>(R.id.btnPlay).setOnClickListener {
            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra("rid", etRID.text.toString())
            intent.putExtra("node", node)
            startActivity(intent)
        }
    }

    @SuppressLint("SoonBlockedPrivateApi")
    private fun closeDetectedProblemApiDialog() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            return
        }
        try {
            @SuppressLint("PrivateApi") val clsPkgParser =
                Class.forName("android.content.pm.PackageParser\$Package")
            val constructor = clsPkgParser.getDeclaredConstructor(String::class.java)
            constructor.isAccessible = true
            @SuppressLint("PrivateApi") val clsActivityThread =
                Class.forName("android.app.ActivityThread")
            val method: Method = clsActivityThread.getDeclaredMethod("currentActivityThread")
            method.isAccessible = true
            val activityThread: Any = method.invoke(null)
            val hiddenApiWarning: Field =
                clsActivityThread.getDeclaredField("mHiddenApiWarningShown")
            hiddenApiWarning.isAccessible = true
            hiddenApiWarning.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}