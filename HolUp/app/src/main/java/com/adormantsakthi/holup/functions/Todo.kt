package com.adormantsakthi.holup.functions

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var title: String,
    var isCompleted: Boolean,
    var importance: String, // will be low, med, high
    var createdAt: Date
)