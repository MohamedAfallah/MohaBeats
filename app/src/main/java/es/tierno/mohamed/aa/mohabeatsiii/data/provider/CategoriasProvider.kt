package es.tierno.mohamed.aa.mohabeatsiii.data.provider

import es.tierno.mohamed.aa.mohabeatsiii.data.model.CategoriasModel

object CategoriasProvider {
    fun obtenerCategorias(): List<CategoriasModel> {
        return listOf(
            CategoriasModel(
                nombre = "Pop",
                urlImagen = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4DFF4081
            ),
            CategoriasModel(
                nombre = "Rock",
                urlImagen = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4D3F51B5
            ),
            CategoriasModel(
                nombre = "Jazz",
                urlImagen = "https://images.unsplash.com/photo-1464375117522-1311f9985e1a?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4D009688
            ),
            CategoriasModel(
                nombre = "Hip-Hop",
                urlImagen = "https://images.unsplash.com/photo-1511459134387-62a2f6b2d4aa?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4D795548
            ),
            CategoriasModel(
                nombre = "Clásica",
                urlImagen = "https://images.unsplash.com/photo-1504384308090-c894fdcc538d?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4D607D8B
            ),
            CategoriasModel(
                nombre = "Reggae",
                urlImagen = "https://images.unsplash.com/photo-1531058020387-3be344556be6?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4DCDDC39
            ),
            CategoriasModel(
                nombre = "Electrónica",
                urlImagen = "https://images.unsplash.com/photo-1508921912186-1d1a45ebb3c1?auto=format&fit=crop&w=800&q=80",
                colorSuperposicion = 0x4DFF5722
            )
        )
    }
}