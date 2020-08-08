package com.smartechnologies.smartchat.models

data class Chat(
    val sender: String,
    val receiver: String,
    val message: String,
    val timeStamp: String
)