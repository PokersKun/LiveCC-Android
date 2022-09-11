package cc.pkrs.livecc.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.pkrs.livecc.R
import cc.pkrs.livecc.activity.MainActivity
import cc.pkrs.livecc.adapter.MyAdapter
import cc.pkrs.livecc.model.Room
import org.litepal.LitePal
import org.litepal.extension.findAll

class RoomListFragment : Fragment() {
    private lateinit var cvAdd : CardView
    private lateinit var cvExit : CardView
    private lateinit var recyclerView : RecyclerView
    private lateinit var loadingView : LinearLayout
    private lateinit var handler : Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_list, container, false)
        cvAdd = view.findViewById(R.id.cvAdd)
        cvExit = view.findViewById(R.id.cvExit)
        recyclerView = view.findViewById(R.id.recyclerView)
        loadingView = view.findViewById(R.id.loadingView)
        val data = LitePal.findAll<Room>()
        val adapter = MyAdapter(data, (activity as MainActivity))
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
        return view
    }

    private fun delayForView() {
        recyclerView.visibility = View.INVISIBLE
        loadingView.visibility = View.VISIBLE
        handler.postDelayed(showRecyclerView, 2000)
    }

    private val showRecyclerView : () -> Unit = {
        loadingView.visibility = View.INVISIBLE
        recyclerView.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cvAdd.setOnClickListener() {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_enter_to_right, R.anim.fragment_exit_to_right)
                .replace(android.R.id.content, RoomAddFragment()).commit()
        }
        cvExit.setOnClickListener() {
            (activity as MainActivity).onBackPressed()
        }
        handler = Handler()
        delayForView()
    }

    override fun onResume() {
        super.onResume()
        delayForView()
    }

}