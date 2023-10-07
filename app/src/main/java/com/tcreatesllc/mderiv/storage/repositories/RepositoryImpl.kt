package com.tcreatesllc.mderiv.storage.repositories

import com.tcreatesllc.mderiv.storage.ContractDAO
import com.tcreatesllc.mderiv.storage.TemporaryTokens
import com.tcreatesllc.mderiv.storage.TransactionDetails
import kotlinx.coroutines.flow.Flow

class RepositoryImpl(private val contractDAO: ContractDAO): ContractsRepository {
    override suspend fun insertTempToken(temporaryTokens: TemporaryTokens) {
        contractDAO.insertTempToken(temporaryTokens)
    }

    override suspend fun insertTransactionDetails(transactionDetails: TransactionDetails) {
        contractDAO.insertTransactionDetails(transactionDetails)
    }

    override fun getRecentTenContracts(): Flow<List<TransactionDetails>> {
        return contractDAO.getRecentTenContracts()
    }

    override fun getAuthToken(id: Int): Flow<TemporaryTokens> {
        return contractDAO.getAuthToken(id)
    }

    override fun getAllContracts(id: Int): Flow<List<TransactionDetails>> {
        return contractDAO.getAllContracts(id)
    }

    override fun getContractCount(id: Int): Flow<Int> {
       return contractDAO.getContractCount(id)
    }

    override suspend fun update(id: Int, amount: String, profit: String, status: String) {
        contractDAO.update(id, amount, profit, status)
    }

    override suspend fun updateToken(id: Int, newToken: String) {
        contractDAO.updateToken(id, newToken)
    }

}