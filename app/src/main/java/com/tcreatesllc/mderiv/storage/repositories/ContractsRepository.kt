package com.tcreatesllc.mderiv.storage.repositories

import com.tcreatesllc.mderiv.storage.TemporaryTokens
import com.tcreatesllc.mderiv.storage.TransactionDetails
import kotlinx.coroutines.flow.Flow

interface ContractsRepository {
    suspend fun insertTempToken(temporaryTokens: TemporaryTokens)
    suspend fun insertTransactionDetails(transactionDetails: TransactionDetails)
    fun getRecentTenContracts(id: String?): Flow<List<TransactionDetails>>
    fun getAuthToken(id: String?): Flow<TemporaryTokens>
    fun getAllContracts(id: String?): Flow<List<TransactionDetails>>

    fun getContractDetails(id: String?): Flow<List<TransactionDetails>>
    fun getContractCount(id: String?): Flow<Int>
    suspend fun update(
        id: String?,
        amount: String?,
        profit: String?,
        status: String?,
        entry_spot: String?
    )
   fun clearAuthTable()
}