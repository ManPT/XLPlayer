package com.xiling.play.bean

data class ShopListBean(
    var code: Int,
    var msg: Any,
    var rows: List<Row>,
    var total: Int,
    var totalPage: Int
)

data class Row(
    var anchorSharesRevenue: Int,
    var categoryId: Int,
    var categoryName: String,
    var coverImg: String,
    var intro: String,
    var labelName: String,
    var normalSharesRevenue: Int,
    var originalPrice: Double,
    var saleCount: Int,
    var salePrice: Double,
    var shotName: String,
    var skuName: String,
    var spuCode: String,
    var spuId: Int,
    var spuName: String,
    var stock: Int
)