package br.infnet.dk_tp1.domain

import com.google.firebase.firestore.DocumentId

data class MyFirestoreUser(
    @DocumentId
    val idUser:String? = null,
    val name:String? = null,
    val email:String? = null,
)