package cc.pkrs.livecc.entity

import com.alibaba.fastjson.annotation.JSONField

class RespDataDTO {
    @JSONField(name = "url")
    var url: String? = null

    @JSONField(name = "danmu")
    var danmu: DanmuDTO? = null
}