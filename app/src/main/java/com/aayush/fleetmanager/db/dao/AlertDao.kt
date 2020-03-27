package com.aayush.fleetmanager.db.dao

import android.os.ParcelUuid
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.aayush.fleetmanager.model.Alert

@Dao
interface AlertDao: BaseDao<Alert> {
    @Query("SELECT * FROM alert")
    fun getAllAlerts(): LiveData<List<Alert>>

    @Query("SELECT COUNT(*) FROM alert")
    fun getNumberOfAlerts(): LiveData<Int>

    @Query("DELETE FROM alert WHERE id = :id")
    suspend fun deleteAlertById(id: ParcelUuid)
}