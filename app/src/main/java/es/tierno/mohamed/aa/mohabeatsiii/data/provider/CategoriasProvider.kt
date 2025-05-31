package es.tierno.mohamed.aa.mohabeatsiii.data.provider

import es.tierno.mohamed.aa.mohabeatsiii.data.model.CategoriasModel

object CategoriasProvider {
    fun obtenerCategorias(): List<CategoriasModel> {
        return listOf(
            CategoriasModel(
                nombre = "Pop",
                urlImagen = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4DFF4081,
                cod = "14"
            ),
            CategoriasModel(
                nombre = "Rock",
                urlImagen = "https://tse2.mm.bing.net/th?id=OIP.Zd5S2R405-3JVYw7nGY5FAHaEo&pid=Api&P=0&h=180",
                colorSuperposicion = 0x4D3F51B5,
                cod = "21"
            ),
            CategoriasModel(
                nombre = "Jazz",
                urlImagen = "https://tse1.mm.bing.net/th?id=OIP.ev_XOOlo-Lwg5ErYN8QLAwAAAA&pid=Api&P=0&h=180",
                colorSuperposicion = 0x4D009688,
                cod = "11"
            ),
            CategoriasModel(
                nombre = "Hip-Hop",
                urlImagen = "https://www.thoughtco.com/thmb/N6-A7tCAizM2xlQBbPXFLWkEMXE=/1883x1801/filters:no_upscale():max_bytes(150000):strip_icc()/2pac-58b8d3125f9b58af5c8e5e12.jpg",
                colorSuperposicion = 0x4D795548,
                cod = "18"
            ),
            CategoriasModel(
                nombre = "Clásica",
                urlImagen = "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4D607D8B,
                cod = "5"
            ),
            CategoriasModel(
                nombre = "Reggae",
                urlImagen = "https://tse2.mm.bing.net/th?id=OIP.6fSDPIFrjfa6WO8Kf7UfcgHaFj&pid=Api&P=0&h=180",
                colorSuperposicion = 0x4DCDDC39,
                cod = "24"
            ),
            CategoriasModel(
                nombre = "Electrónica",
                urlImagen = "https://tse1.mm.bing.net/th?id=OIP.3-CAdx76LcFJKiEH6vVJbgHaE8&pid=Api&P=0&h=180",
                colorSuperposicion = 0x4DFF5722,
                cod = "7"
            )
        )
    }
}