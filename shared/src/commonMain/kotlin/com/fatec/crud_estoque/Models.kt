package com.fatec.crud_estoque

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String? = null,
    val name: String,
    val description: String? = null,
    val sku: String,
    val category: String? = null,
    @SerialName("created_at")
    val createdAt: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class StockItem(
    val id: String? = null,
    @SerialName("product_id")
    val productId: String,
    val quantity: Int,
    @SerialName("unit_price")
    val unitPrice: Double,
    val location: String? = null,
    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class StockSummary(
    @SerialName("product_id")
    val productId: String,
    @SerialName("product_name")
    val productName: String,
    @SerialName("total_quantity")
    val totalQuantity: Int
)
