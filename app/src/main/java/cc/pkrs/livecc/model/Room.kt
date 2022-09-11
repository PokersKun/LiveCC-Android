package cc.pkrs.livecc.model

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

class Room : LitePalSupport() {
    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var rid: String? = null

    @Column(nullable = false)
    var node: String? = null
}