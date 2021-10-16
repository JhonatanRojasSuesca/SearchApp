package com.jhonatanrojas.searchapp.data.modelLocal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by JHONATAN ROJAS on 15/10/2021.
 */
@Entity(tableName = "cartTable")
data class CartProductModel(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String
)