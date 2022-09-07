package cc.pkrs.livecc.entity

import com.alibaba.fastjson.annotation.JSONField

class DanmuDTO {
    @JSONField(name = "name")
    var name: String? = null

    @JSONField(name = "content")
    var content: String? = null
}