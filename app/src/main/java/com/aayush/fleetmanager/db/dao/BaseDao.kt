package com.aayush.fleetmanager.db.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE) fun insert(vararg obj: T)

    @Update(onConflict = OnConflictStrategy.REPLACE) fun update(obj: T)

    @Delete fun delete(obj: T)
}