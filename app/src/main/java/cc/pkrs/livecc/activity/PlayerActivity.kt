package cc.pkrs.livecc.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cc.pkrs.livecc.R
import cc.pkrs.livecc.danmaku.DefaultRenderer
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
import com.kuaishou.akdanmaku.DanmakuConfig
import com.kuaishou.akdanmaku.data.DanmakuItemData
import com.kuaishou.akdanmaku.render.SimpleRenderer
import com.kuaishou.akdanmaku.render.TypedDanmakuRenderer
import com.kuaishou.akdanmaku.ui.DanmakuPlayer
import com.kuaishou.akdanmaku.ui.DanmakuView
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import kotlin.random.Random

class PlayerActivity : AppCompatActivity() {
    private var player: ExoPlayer? = null
    private var danmakuPlayer: DanmakuPlayer? = null
    private val danmakuView by lazy { findViewById<DanmakuView>(R.id.danmakuView) }
    private val simpleRenderer = SimpleRenderer()
    private val renderer by lazy {
        TypedDanmakuRenderer(
            simpleRenderer,
            DanmakuItemData.DANMAKU_STYLE_NONE to DefaultRenderer()
        )
    }
    private var config = DanmakuConfig().apply {
        textSizeScale = 2.0f
    }

    private var mqttHelper: MQTTHelper? = null
    private val server = "tcp://192.168.1.8:1883"
    private var rid: String? = null
    private var node: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        player = ExoPlayer.Builder(this).build()
        val styledPlayerView = findViewById<StyledPlayerView>(R.id.styledPlayerView)
        styledPlayerView.player = player

        danmakuPlayer = DanmakuPlayer(renderer).also {
            it.bindView(danmakuView)
        }

        rid = intent.getStringExtra("rid")
        node = intent.getStringExtra("node")
        initMqtt()
    }

    private fun initMqtt() {
        mqttHelper = MQTTHelper(this, server, "livecc", "203039")
        mqttHelper!!.connect(Topic.TOPIC_MSG, Qos.QOS_ZERO, true, object : MqttCallback {
            override fun connectionLost(cause: Throwable?) { }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
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
                    else if (respMsg.type == "resp_get_danmu") {
                        if (respMsg.data?.danmu?.content != "" && respMsg.data?.danmu?.content != "欢迎来到直播间") {
                            val danmaku = respMsg.data?.danmu?.content?.let {
                                DanmakuItemData(
                                    Random.nextLong(),
                                    danmakuPlayer?.getCurrentTimeMs()!! + 500,
                                    it,
                                    DanmakuItemData.DANMAKU_MODE_ROLLING,
                                    25,
                                    Color.WHITE,
                                    9,
                                    DanmakuItemData.DANMAKU_STYLE_NONE,
                                    9
                                )
                            }
                            if (danmaku != null) {
                                danmakuPlayer?.send(danmaku)
                            }
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
        reqMsg.node = node
        val data = ReqDataDTO()
        data.rid = rid
        data.cid = mqttHelper?.get_clientId()
        reqMsg.data = data
        mqttHelper?.publish(Topic.TOPIC_SEND, JSON.toJSONString(reqMsg), Qos.QOS_ZERO)
        danmakuPlayer?.start(config)
    }

    private fun getLiveUrl() {
        val reqMsg = ReqMsgDTO()
        reqMsg.code = 0
        reqMsg.type = "req_get_url"
        reqMsg.node = node
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
        danmakuPlayer?.release()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}