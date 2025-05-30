package com.kurly.kjm.kurlytest.data

import com.google.gson.annotations.SerializedName

data class SectionProductResponse (
    @SerializedName("data")
    var sectionProductData: ArrayList<SectionProductData>,
)

data class SectionProductData (
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("image")
    var image: String? = null,
    @SerializedName("originalPrice")
    var originalPrice: Int = 0,
    @SerializedName("discountedPrice") // 할인이 없으면 내려오지 않음
    var discountedPrice: Int? = null,
    @SerializedName("isSoldOut")
    var isSoldOut: Boolean? = null,
    var isTextVisible: Boolean = false,
    var isFavorite: Boolean = false
)

data class SectionWithProducts(
    val section: SectionData,
    val items: List<SectionProductData>
)