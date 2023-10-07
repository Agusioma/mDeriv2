package com.tcreatesllc.mderiv.storage

import android.content.Context
import com.tcreatesllc.mderiv.storage.repositories.ContractsRepository
import com.tcreatesllc.mderiv.storage.repositories.RepositoryImpl

interface AppContainer {
    val contractsRepository: ContractsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val contractsRepository: ContractsRepository by lazy {
        RepositoryImpl(MDerivDatabase.getDatabase(context).contractDAO())
    }

}