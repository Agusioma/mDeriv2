package com.tcreatesllc.mderiv.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TemporaryTokens::class, TransactionDetails::class], version = 4, exportSchema = false)
abstract class MDerivDatabase : RoomDatabase() {

    abstract fun contractDAO(): ContractDAO

    companion object{
        @Volatile
        private var DBINSTANCE: MDerivDatabase? = null

        fun getDatabase(context: Context): MDerivDatabase{
            return DBINSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MDerivDatabase::class.java,
                    "mderiv_database"
                ) .fallbackToDestructiveMigration()
                    .build()
                DBINSTANCE = instance
                return instance
            }
        }
    }

}