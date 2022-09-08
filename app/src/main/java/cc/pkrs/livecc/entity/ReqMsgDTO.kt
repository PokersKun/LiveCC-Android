package cc.pkrs.livecc.entity

import com.alibaba.fastjson.annotation.JSONField

class ReqMsgDTO {
    @JSONField(name = "code")
    var code: Int? = null

    @JSONField(name = "type")
    var type: String? = null

    @JSONField(name = "node")
    var node: String? = null

    @JSONField(name = "data")
    var data: ReqDataDTO? = null
}