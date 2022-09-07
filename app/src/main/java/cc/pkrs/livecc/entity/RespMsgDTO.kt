package cc.pkrs.livecc.entity

import com.alibaba.fastjson.annotation.JSONField

class RespMsgDTO {
    @JSONField(name = "code")
    var code: Int? = null

    @JSONField(name = "type")
    var type: String? = null

    @JSONField(name = "data")
    var data: RespDataDTO? = null
}