package com.kurly.kjm.kurlytest.data

import com.google.gson.annotations.SerializedName

data class SectionResponse (
    @SerializedName("data")
    var sectionData: ArrayList<SectionData>,
    @SerializedName("paging")
    var paging: Paging? = Paging()
)

data class SectionData (
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("type") // vertical, horizontal, grid (3*2 리스트 제공 후 6개 짤라서)
    var type: String? = "vertical",
    @SerializedName("url")
    var url: String? = null
)

data class Paging (
    @SerializedName("next_page") // 다음 페이지가 없으면 내려오지 않음
    var nextPage: Int? = null
)