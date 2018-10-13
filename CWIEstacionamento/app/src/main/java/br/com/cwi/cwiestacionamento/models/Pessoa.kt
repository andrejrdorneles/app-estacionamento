package br.com.cwi.cwiestacionamento.models

import android.net.Uri

class Pessoa {

    var displayName: String? = null
    var email: String? = null
    var photoURL: Uri? = null
    var actualPassword: String? = null
    var newPassword: String? = null

    constructor(name: String, email: String, profileImage: Uri, actualPassword : String, newPassword : String) {
        this.displayName = name
        this.email = email
        this.photoURL = profileImage
        this.actualPassword = actualPassword
        this.newPassword = newPassword
    }

    constructor() {}
}