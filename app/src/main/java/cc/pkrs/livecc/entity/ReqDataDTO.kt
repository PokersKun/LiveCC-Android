package cc.pkrs.livecc.entity

import com.alibaba.fastjson.annotation.JSONField

class ReqDataDTO {
    @JSONField(name = "rid")
    var rid: String? = null

    @JSONField(name = "cid")
    var cid: String? = null
}