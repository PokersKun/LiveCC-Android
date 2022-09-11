package cc.pkrs.livecc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.pkrs.livecc.R
import cc.pkrs.livecc.activity.PlayerActivity
import cc.pkrs.livecc.model.Room


class MyAdapter internal constructor(private val dataList: List<Room>?, private val context: Context?) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = dataList!![position]
        holder.tvName.text = room.name
        holder.tvNode.text = room.node
        holder.tvRID.text = "RID: ${room.rid}"
        holder.itemView.setOnClickListener() {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("rid", room.rid)
            intent.putExtra("node", room.node)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView
        val tvNode: TextView
        val tvRID: TextView

        init {
            tvName = itemView.findViewById(R.id.tvName)
            tvNode = itemView.findViewById(R.id.tvNode)
            tvRID = itemView.findViewById(R.id.tvRID)
        }
    }
}