package es.tierno.mohamed.aa.mohabeatsiii.core

import android.content.Context
import androidx.room.Room
import es.tierno.mohamed.aa.mohabeatsiii.data.db.MohaBeatsDataBase

object DataBaseCreator {
    private const val DATABASE_NAME = "moha_beats_database"

    fun createDatabase(context: Context): MohaBeatsDataBase {
        return Room.databaseBuilder(
            context.applicationContext,
            MohaBeatsDataBase::class.java,
            DATABASE_NAME
        ).build()
    }
}