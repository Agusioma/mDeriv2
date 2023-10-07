package com.tcreatesllc.mderiv.storage.repositories

import com.tcreatesllc.mderiv.storage.ContractDAO
import com.tcreatesllc.mderiv.storage.TemporaryTokens
import com.tcreatesllc.mderiv.storage.TransactionDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RepositoryImpl(private val contractDAO: ContractDAO): ContractsRepository {
    override suspend fun insertTempToken(temporaryTokens: TemporaryTokens) {
        withContext(Dispatchers.IO){
            contractDAO.insertTempToken(temporaryTokens)
        }

    }

    override suspend fun insertTransactionDetails(transactionDetails: TransactionDetails) {
        withContext(Dispatchers.IO) {
            contractDAO.insertTransactionDetails(transactionDetails)
        }
    }

    override fun getRecentTenContracts(id: String?): Flow<List<TransactionDetails>> {
      /*  database.cardioDao().getLatestAvg().map {
            it?:0.0
        }*/
        return contractDAO.getRecentTenContracts(id).map{
           it
        }
        //return contractDAO.getRecentTenContracts(id)
    }

    override fun getAuthToken(id: String?): Flow<TemporaryTokens> {
        return contractDAO.getAuthToken(id)
    }

    override fun getAllContracts(id: String?): Flow<List<TransactionDetails>> {
        return contractDAO.getAllContracts(id)
    }

    override fun getContractCount(id: String?): Flow<Int> {
       return contractDAO.getContractCount(id)
    }

    override suspend fun update(id: String?, amount: String?, profit: String?, status: String?) {
        withContext(Dispatchers.IO) {
            contractDAO.update(id, amount, profit, status)
        }
    }

    override suspend fun updateToken(id: String?, newToken: String?) {
        withContext(Dispatchers.IO) {
            contractDAO.updateToken(id, newToken)
        }
    }

}