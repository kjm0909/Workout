package com.kurly.kjm.kurlytest

import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import kotlin.math.roundToInt

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("isRefreshing")
    fun bindSwipeRefresh(swipe: SwipeRefreshLayout, isRefreshing: Boolean) {
        swipe.isRefreshing = isRefreshing
    }

    @JvmStatic
    @BindingAdapter("onRefresh")
    fun SwipeRefreshLayout.setOnRefreshAction(onRefresh: () -> Unit) {
        setOnRefreshListener { onRefresh() }
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(view: ImageView, url: String?) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.ic_launcher_background)
            .into(view)
    }
    @JvmStatic
    @BindingAdapter("original", "discounted")
    fun setDiscountPercent(tv: TextView, original: Int?, discounted: Int?) { // 할인율 텍스트
        if (original != null && discounted != null && original > 0) {
            val percent = ((original - discounted) * 100.0f / original).roundToInt()
            tv.text = "$percent%"
        }
    }

    @JvmStatic
    @BindingAdapter("discountedPrice", "originalPrice")
    fun setPriceText(tv: TextView, discounted: Int?, original: Int?) { // 할인 가격 텍스트
        val price = discounted ?: original ?: 0
        val decimalFormat = DecimalFormat("###,###")
        val priceWithComma = decimalFormat.format(price)
        tv.text = "${priceWithComma}원"
    }

    @JvmStatic
    @BindingAdapter("originalFlagPrice")
    fun setOriginalPriceStrike(tv: TextView, original: Int?) { // 원래 가격 텍스트
        val decimalFormat = DecimalFormat("###,###")
        val priceWithComma = decimalFormat.format(original)
        tv.text = "${priceWithComma ?: 0}원"
        tv.paintFlags = tv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    @JvmStatic
    @BindingAdapter("visibleGoneIf")
    fun TextView.visibleGoneIf(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("visibleInvisibleIf")
    fun TextView.visibleInvisibleIf(visible: Boolean) {
        this.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("isFavorite")
    fun ImageView.setFavoriteIcon(isFav: Boolean) {
        setImageResource(
            if (isFav) R.drawable.ic_btn_heart_on
            else       R.drawable.ic_btn_heart_off
        )
    }
}