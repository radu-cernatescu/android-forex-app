package ca.senecacollege.dps924.assignment4_forexapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface CurrencyConversionDao {
    @Insert
    Completable addConversion(CurrencyConversionResult conversion);

    @Delete
    Completable removeConversion(CurrencyConversionResult conversion);

    @Query("SELECT * FROM conversions")
    Single<List<CurrencyConversionResult>> getAllConversions();
}
