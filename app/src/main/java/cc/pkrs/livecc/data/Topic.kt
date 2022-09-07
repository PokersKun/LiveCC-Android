package cc.pkrs.livecc.data

/**
 *
 * @Description:     java类作用描述
 * @Author:         作者名
 * @CreateDate:     2022/1/27 10:15
 * @UpdateUser:     更新者：
 * @UpdateDate:     2022/1/27 10:15
 * @UpdateRemark:   更新说明：
 */
enum class Topic{
    TOPIC_MSG{
        override fun value():String{
            return "/live/cc/server"
        }
    },
    TOPIC_SEND{
        override fun value():String{
            return "/live/cc/client/"
        }
    };
    abstract fun value(): String
}