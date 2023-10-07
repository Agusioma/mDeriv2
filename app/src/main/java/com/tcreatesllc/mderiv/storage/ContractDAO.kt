package com.tcreatesllc.mderiv.storage

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface ContractDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTempToken(temporaryTokens: TemporaryTokens)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionDetails(transactionDetails: TransactionDetails)

    @Query("SELECT * FROM transaction_details ORDER BY entry_tick_time DESC LIMIT 10")
    fun getRecentTenContracts(): Flow<List<TransactionDetails>>

    @Query("SELECT * FROM temporary_tokens WHERE login_id = :id")
    fun getAuthToken(id: Int): Flow<TemporaryTokens>

    @Query("SELECT * FROM transaction_details WHERE login_id = :id ORDER BY entry_tick_time")
    fun getAllContracts(id: Int): Flow<List<TransactionDetails>>

    @Query("UPDATE transaction_details SET current_amount = :amount, profit = :profit, status = :status WHERE contract_id = :id")
    suspend fun update(
        id: Int,
        amount: String,
        profit: String,
        status: String
    )

    @Query("UPDATE temporary_tokens SET auth_token = :newToken WHERE login_id = :id ")
    suspend fun updataToken(id: Int, newToken: String)

}