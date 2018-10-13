package br.com.cwi.cwiestacionamento.models

import android.net.Uri

class Pessoa {

    var displayName: String? = null
    var email: String? = null
    var photoURL: Uri? = null

    constructor(name: String, email: String, profileImage: Uri) {
        this.displayName = name
        this.email = email
        this.photoURL = profileImage
    }

    constructor() {}
}