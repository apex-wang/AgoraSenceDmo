package io.agora.rtckit.open.config

/**初始化*/
data class RtcInitConfig constructor(var appId: String? = null)

/**频道配置*/
data class RtcChannelConfig constructor(
    var appToken: String,
    var roomId: String,
    var userId: Int,
    var audioEnabled: Boolean = true,
    var broadcaster: Boolean = true,
)