package com.tcreatesllc.mderiv.storage.repositories

import com.tcreatesllc.mderiv.storage.TemporaryTokens
import com.tcreatesllc.mderiv.storage.TransactionDetails
import kotlinx.coroutines.flow.Flow

interface ContractsRepository {
    suspend fun insertTempToken(temporaryTokens: TemporaryTokens)
    suspend fun insertTransactionDetails(transactionDetails: TransactionDetails)
    fun getRecentTenContracts(): Flow<List<TransactionDetails>>
    fun getAuthToken(id: Int): Flow<TemporaryTokens>
    fun getAllContracts(id: Int): Flow<List<TransactionDetails>>
    fun getContractCount(id: Int): Flow<Int>
    suspend fun update(
        id: Int,
        amount: String,
        profit: String,
        status: String
    )
    suspend fun updateToken(
        id: Int,
        newToken: String
    )
}