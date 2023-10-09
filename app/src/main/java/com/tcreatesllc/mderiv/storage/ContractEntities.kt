package com.tcreatesllc.mderiv.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "temporary_tokens")
data class TemporaryTokens(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name = "login_id", index = true) var userTradeAccountNo: String?,
    @ColumnInfo(name = "auth_token") var userTradeAuthToken: String?
)

@Entity(tableName = "transaction_details")
data class TransactionDetails(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name = "login_id") var loginID: String?,
    @ColumnInfo(name = "contract_id") var contractID: String?,
    @ColumnInfo(name = "display_name") var marketName: String?,
    @ColumnInfo(name = "contract_type") var contractType: String?,
    @ColumnInfo(name = "buy_price") var buyPrice: String?,
    @ColumnInfo(name = "stop_loss") var stopLoss: String?,
    @ColumnInfo(name = "take_profit") var takeProfit: String?,
    @ColumnInfo(name = "current_amount") var indicativeAmt: String?,
    @ColumnInfo(name = "multiplier") var multiplierChosen: String?,
    @ColumnInfo(name = "entry_spot") var entrySpot: String?,
    @ColumnInfo(name = "profit") var profitOrLoss: String?,
    @ColumnInfo(name = "entry_tick_time") var tickSpotEntryTime: String?,
    @ColumnInfo(name = "status") var contractStatusOpenOrClosed: String?,
    @ColumnInfo(name = "underlying") var symbolName: String?
)

