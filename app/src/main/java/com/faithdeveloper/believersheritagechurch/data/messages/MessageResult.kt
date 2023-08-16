package com.faithdeveloper.believersheritagechurch.data.messages

import com.google.firebase.firestore.DocumentSnapshot

data class MessageResult(
    val key:DocumentSnapshot?,
    val messages:List<Message>
)
