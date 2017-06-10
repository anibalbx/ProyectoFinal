package com.example.anibal.neumaticosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anibal on 17/05/2017.
 */

public class PcSQLiteHelperTire extends SQLiteOpenHelper {

    String sqlCreate = "CREATE TABLE Tires (referencia INT NOT NULL, marca TEXT, modelo TEXT, medida INT, stock INT, idc INT, idv TEXT, tipo TEXT)";




    public PcSQLiteHelperTire(Context contexto, String nombre,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS Tires");

        db.execSQL(sqlCreate);

    }


}
