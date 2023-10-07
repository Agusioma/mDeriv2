package com.tcreatesllc.mderiv

import android.app.Application
import com.tcreatesllc.mderiv.storage.AppContainer
import com.tcreatesllc.mderiv.storage.AppDataContainer
import com.tcreatesllc.mderiv.storage.MDerivDatabase
import com.tcreatesllc.mderiv.storage.repositories.ContractsRepository
import com.tcreatesllc.mderiv.storage.repositories.RepositoryImpl

class MainApplication: Application() {
   lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}

