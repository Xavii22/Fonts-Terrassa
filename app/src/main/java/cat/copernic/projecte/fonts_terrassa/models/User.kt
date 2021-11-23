package cat.copernic.projecte.fonts_terrassa.models

data class User(val email: String) {
    var useremail: String? = null

    init {
        this.useremail = email
    }

}