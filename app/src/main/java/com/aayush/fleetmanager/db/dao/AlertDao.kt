package com.aayush.fleetmanager.db.dao

import android.os.ParcelUuid
import androidx.room.Dao
import androidx.room.Query
import com.aayush.fleetmanager.model.Alert
import com.aayush.fleetmanager.util.android.base.BaseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class AlertDao: BaseDao<Alert> {
    @Query("SELECT * FROM alert")
    abstract fun getAllAlerts(): Flow<List<Alert>>

    @Query("SELECT COUNT(*) FROM alert")
    abstract fun getAlertCount(): Flow<Int>

    @Query("DELETE FROM alert WHERE id = :id")
    abstract suspend fun deleteAlertById(id: ParcelUuid)

    @Query("DELETE FROM alert")
    abstract suspend fun deleteAll()

    fun getAllAlertsDistinctUntilChanged(): Flow<List<Alert>> = getAllAlerts().distinctUntilChanged()

    fun getAlertCountDistinctUntilChanged(): Flow<Int> = getAlertCount().distinctUntilChanged()
}