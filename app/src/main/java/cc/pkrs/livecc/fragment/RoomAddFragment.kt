package cc.pkrs.livecc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import cc.pkrs.livecc.R
import cc.pkrs.livecc.activity.MainActivity
import cc.pkrs.livecc.model.Room
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.ToastUtils

class RoomAddFragment : Fragment() {
    private lateinit var etName : EditText
    private lateinit var etRID : EditText
    private lateinit var rgNode : RadioGroup
    private lateinit var btnSave : Button
    private var node = "cc"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_room_add, container, false)
        etName = view.findViewById(R.id.etName)
        etRID = view.findViewById(R.id.etRID)
        rgNode = view.findViewById(R.id.rgNode)
        btnSave = view.findViewById(R.id.btnSave)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSave.setOnClickListener {
            if (!StringUtils.isEmpty(etName.text) && !StringUtils.isEmpty(etRID.text)) {
                ToastUtils.showShort("${etName.text}|${etRID.text}|$node")
                val room = Room()
                room.name = etName.text.toString()
                room.rid = etRID.text.toString()
                room.node = node
                room.save()
                ToastUtils.showShort("房间保存成功")
                (activity as MainActivity).onBackPressed()
            } else {
                ToastUtils.showShort("请输入房间参数")
            }
        }
        rgNode.setOnCheckedChangeListener { _, i ->
            if (i == R.id.rbCC)
                node = "cc"
            else if (i == R.id.rbBiliBili)
                node = "bilibili"
        }
    }
}