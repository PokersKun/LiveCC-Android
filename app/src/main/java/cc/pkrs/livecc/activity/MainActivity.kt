package cc.pkrs.livecc.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cc.pkrs.livecc.MyApplication
import cc.pkrs.livecc.R
import cc.pkrs.livecc.fragment.RoomListFragment
import com.blankj.utilcode.util.KeyboardUtils
import java.lang.reflect.Field
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.instance?.addActivity(this)
        closeDetectedProblemApiDialog()
        supportFragmentManager.beginTransaction().add(android.R.id.content, RoomListFragment()).commit();
    }

    override fun onBackPressed() {
        val fragmentManager: Fragment? =  supportFragmentManager.findFragmentById(android.R.id.content)
        if (fragmentManager != null && fragmentManager is RoomListFragment) {
            MyApplication.instance?.exit()
        } else {
            KeyboardUtils.hideSoftInput(this)
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_to_left, R.anim.fragment_exit_to_left)
                .replace(android.R.id.content, RoomListFragment()).commit()
        }
    }

    @SuppressLint("SoonBlockedPrivateApi", "DiscouragedPrivateApi")
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
            val activityThread: Any = method.invoke(null) as Any
            val hiddenApiWarning: Field =
                clsActivityThread.getDeclaredField("mHiddenApiWarningShown")
            hiddenApiWarning.isAccessible = true
            hiddenApiWarning.setBoolean(activityThread, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}