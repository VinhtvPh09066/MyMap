package com.example.mymap;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface locationDAO {
    //Lấy danh sách location từ bảng location
    @Query("Select * From locationClass")
    List<locationClass> getAll();

    //Thêm 1 hoặc nhiều
    @Insert
    long[] insertAll(locationClass... local);

    //Xóa đối tượng
    @Delete
    int deleteLocal(locationClass local);

    //Xóa theo id
    @Query("DELETE FROM locationClass WHERE id =:input")
    int deleteLocalByID(double input);

    //get local theo lat và long
    @Query("Select * From locationClass where lat =:lat and lng =:lng")
    locationClass getLocation(double lat, double lng);

    @Update
    int updateLocal(locationClass... local);
}
