package es.tierno.mohamed.aa.mohabeatsiii.data.provider

import es.tierno.mohamed.aa.mohabeatsiii.data.db.entidades.MusicaEntity

//Provider para cargar los datos a la base de datos
object MusicaProvider {
    val canciones = listOf(
        MusicaEntity(id = 1,
            nombre = "Blinding Lights",
            artista = "The Weeknd",
            url = "https://1.bp.blogspot.com/-c9aB5F7bkk0/Xuwwzg3CsQI/AAAAAAAACyo/cBnPTbyvNaMuhedE1XLGfDpKk3ImkmT0ACK4BGAsYHg/s600/R-14752961-1580918235-5355.jpeg.jpg"),
        MusicaEntity(id = 2,
            nombre = "Shape of You",
            artista = "Ed Sheeran",
            url = "https://www.lahiguera.net/musicalia/artistas/ed_sheeran/disco/8124/tema/14856/ed_sheeran_shape_of_you-portada.jpg"),
        MusicaEntity(id = 3,
            nombre = "Believer",
            artista = "Imagine Dragons",
            url = "https://www.lahiguera.net/musicalia/artistas/imagine_dragons/disco/8369/tema/15042/imagine_dragons_believer-portada.jpg"),
        MusicaEntity(id = 4,
            nombre = "Dance Monkey",
            artista = "Tones and I",
            url = "https://i.pinimg.com/originals/8a/db/4e/8adb4e89b9dd875f30d1aaae747690d1.jpg"),
        MusicaEntity(id = 5,
            nombre = "Someone Like You",
            artista = "Adele",
            url = "https://www.playhousewhitleybay.co.uk/images/lg_20190214155847_450.jpg")
    )
}
