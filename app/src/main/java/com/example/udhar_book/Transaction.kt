package com.example.udhar_book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "transaction")
 data class Transaction(
 @PrimaryKey(autoGenerate = true) val id:Int,
   val label:String,
 val amount:Double,
val description:String): Serializable {

 }


