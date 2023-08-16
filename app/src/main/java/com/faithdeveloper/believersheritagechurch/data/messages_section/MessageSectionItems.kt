package com.faithdeveloper.believersheritagechurch.data.messages_section

data class MessageSectionItems(
    val imageLink: String,
    val title: String,
    val description: String
) {
    constructor() : this("", "", "")

}

