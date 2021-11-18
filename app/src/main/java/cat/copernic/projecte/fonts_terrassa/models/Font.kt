package cat.copernic.projecte.fonts_terrassa.models

data class Font(val name: String, val lat: Double, val lon: Double, val type: Int) {
    var fontName: String? = null
    var fontLat: Double? = null
    var fontLon: Double? = null
    var fontType: Int? = null

    init {
        this.fontName = name
        this.fontLat = lat
        this.fontLon = lon
        this.fontType = type
    }

}