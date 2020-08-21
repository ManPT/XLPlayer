package com.xiling.play.bean

data class ShopDetailsBean(
    var anchorSharesRevenue: Int,
    var categoryId: Int,
    var categoryName: String,
    var content: String,
    var coverImg: String,
    var freeShipping: Int,
    var imgUrl: String,
    var imgUrls: List<String>,
    var intro: String,
    var labelName: String,
    var normalSharesRevenue: Int,
    var originalPrice: Double,
    var saleCount: Double,
    var salePrice: Int,
    var shotName: String,
    var skuId: Int,
    var skuName: String,
    var spuCode: String,
    var spuId: Int,
    var spuName: String,
    var stock: Int
)