package com.aayush.fleetmanager.util.android.base

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(vararg obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE) fun update(obj: T)

    @Delete fun delete(obj: T)
}