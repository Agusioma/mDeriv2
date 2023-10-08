package com.tcreatesllc.mderiv.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTempToken(temporaryTokens: TemporaryTokens)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransactionDetails(transactionDetails: TransactionDetails)

    @Query("SELECT * FROM transaction_details WHERE login_id = :id ORDER BY entry_tick_time DESC LIMIT 10")
    fun getRecentTenContracts(id: String?): Flow<List<TransactionDetails>>

    @Query("SELECT * FROM temporary_tokens WHERE login_id = :id")
    fun getAuthToken(id: String?): Flow<TemporaryTokens>

    @Query("SELECT * FROM transaction_details WHERE login_id = :id ORDER BY entry_tick_time")
    fun getAllContracts(id: String?): Flow<List<TransactionDetails>>

    @Query("SELECT * FROM transaction_details WHERE contract_id = :id")
    fun getContractDetails(id: String?): Flow<List<TransactionDetails>>

    @Query("SELECT COUNT(contract_id) AS CHECKER FROM transaction_details WHERE login_id = :id")
    fun getContractCount(id: String?): Flow<Int>

    @Query("UPDATE transaction_details SET current_amount = :amount, profit = :profit, status = :status, entry_spot=:entry_spot  WHERE contract_id = :id")
    fun update(
        id: String?,
        amount: String?,
        profit: String?,
        status: String?,
        entry_spot: String?
    )

    @Query("UPDATE temporary_tokens SET auth_token = :newToken WHERE login_id = :id ")
    fun updateToken(
        id: String?,
        newToken: String?
    )

}