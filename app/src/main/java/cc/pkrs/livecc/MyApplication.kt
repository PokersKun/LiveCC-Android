package cc.pkrs.livecc

import android.app.Activity
import android.app.Application
import cc.pkrs.livecc.MyApplication
import java.util.*

class MyApplication private constructor() : Application() {
    private val activities: MutableList<Activity> = LinkedList()
    fun exit() {
        for (activity in activities) {
            activity.finish()
        }
        System.exit(0)
    }

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }

    companion object {
        var instance: MyApplication? = null
            get() {
                if (field == null) {
                    field = MyApplication()
                }
                return field
            }
            private set
    }
}