package com.xiling.play.bean

data class HomeLiveBean(
    var indexHot: IndexHot,
    var indexHotList: List<IndexHotX>
)

data class IndexHot(
    var imgUrl: String,
    var roomId: Double
)

data class IndexHotX(
    var imgUrl: String,
    var roomId: Double
)