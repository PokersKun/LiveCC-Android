package cc.pkrs.livecc.activity

import android.os.Bundle
import cc.pkrs.livecc.R
import cc.pkrs.livecc.data.Qos
import cc.pkrs.livecc.data.Topic
import cc.pkrs.livecc.entity.ReqDataDTO
import cc.pkrs.livecc.entity.ReqMsgDTO
import cc.pkrs.livecc.entity.RespMsgDTO
import cc.pkrs.livecc.utils.MQTTHelper
import com.alibaba.fastjson.JSON
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage


class PlayerActivity : BaseActivity() {
    private var player: ExoPlayer? = null
    private var mqttHelper: MQTTHelper? = null

    private val server = "tcp://192.168.1.8:1883"
    private var rid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        player = ExoPlayer.Builder(this).build()
        val styledPlayerView = findViewById<StyledPlayerView>(R.id.styledPlayerView)
        styledPlayerView.player = player

        rid = intent.getStringExtra("rid")
        initMqtt()
    }

    private fun initMqtt() {
        mqttHelper = MQTTHelper(this, server, "livecc", "203039")
        mqttHelper!!.connect(Topic.TOPIC_MSG, Qos.QOS_ZERO, true, object : MqttCallback {
            override fun connectionLost(cause: Throwable?) { }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
//                message?.payload?.let { ToastUtils.showShort(String(it)) }
                LogUtils.iTag("msg", message?.payload?.let { String(it) })
                val respMsg = JSON.parseObject(message?.payload?.let { String(it) }, RespMsgDTO::class.java)
                if (respMsg.code == 0) {
                    if (respMsg.type == "resp_get_url") {
                        if (respMsg.data?.url != "none") {
                            respMsg.data?.url?.let { playVideo(it) }
                            ToastUtils.showShort("播放链接获取成功")
                            getLiveDanmu()
                        } else {
                            ToastUtils.showShort("播放链接获取失败")
                        }
                    }
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) { }
        })
        val waitThread = object : Thread() {
            override fun run() {
                while(true) {
                    if (mqttHelper!!.is_connected() == true)
                        break;
                }
                getLiveUrl()
            }
        }
        waitThread.start()
    }

    private fun playVideo(url: String) {
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_M3U8)
            .build()
        player!!.setMediaItem(mediaItem)
        player!!.prepare()
        player!!.play()
    }

    private fun getLiveDanmu() {
        val reqMsg = ReqMsgDTO()
        reqMsg.code = 0
        reqMsg.type = "req_get_danmu"
        val data = ReqDataDTO()
        data.rid = rid
        data.cid = mqttHelper?.get_clientId()
        reqMsg.data = data
        mqttHelper?.publish(Topic.TOPIC_SEND, JSON.toJSONString(reqMsg), Qos.QOS_ZERO)
    }

    private fun getLiveUrl() {
        val reqMsg = ReqMsgDTO()
        reqMsg.code = 0
        reqMsg.type = "req_get_url"
        val data = ReqDataDTO()
        data.rid = rid
        data.cid = mqttHelper?.get_clientId()
        reqMsg.data = data
        mqttHelper?.publish(Topic.TOPIC_SEND, JSON.toJSONString(reqMsg), Qos.QOS_ZERO)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player!!.isPlaying)
            player!!.release()
        if (mqttHelper?.is_connected() == true)
            mqttHelper!!.disconnect()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}