package cc.pkrs.livecc.danmaku

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.graphics.withTranslation
import com.kuaishou.akdanmaku.DanmakuConfig
import com.kuaishou.akdanmaku.data.DanmakuItem
import com.kuaishou.akdanmaku.render.SimpleRenderer
import com.kuaishou.akdanmaku.ui.DanmakuDisplayer
import com.kuaishou.akdanmaku.utils.Size

class DefaultRenderer() : SimpleRenderer() {

    val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = UP_BACKGROUND
    }
    val rect = RectF()

    override fun measure(
        item: DanmakuItem,
        displayer: DanmakuDisplayer,
        config: DanmakuConfig
    ): Size {
        val contentSize = super.measure(item, displayer, config)
        return Size(
            contentSize.width + UP_PADDING_HORIZONTAL * 2 + UP_SPACE_LOGO_TEXT,
            contentSize.height + UP_PADDING_VERTICAL * 2
        )
    }

    override fun draw(
        item: DanmakuItem,
        canvas: Canvas,
        displayer: DanmakuDisplayer,
        config: DanmakuConfig
    ) {
        rect.set(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
        val radius = canvas.height * 0.5f
        canvas.drawRoundRect(rect, radius, radius, backgroundPaint)
        canvas.withTranslation(
            (UP_PADDING_HORIZONTAL + UP_SPACE_LOGO_TEXT).toFloat(),
            UP_PADDING_VERTICAL.toFloat()
        ) {
            super.draw(item, canvas, displayer, config)
        }
    }

    companion object {
        private val UP_BACKGROUND = Color.argb(0, 0, 0, 0)
        private const val UP_SPACE_LOGO_TEXT = 8
        private const val UP_PADDING_VERTICAL = 12
        private const val UP_PADDING_HORIZONTAL = 26

    }
}