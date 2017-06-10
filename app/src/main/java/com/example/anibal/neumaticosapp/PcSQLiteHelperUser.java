package com.example.anibal.neumaticosapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anibal on 08/05/2017.
 */

public class PcSQLiteHelperUser extends SQLiteOpenHelper {


    String sqlCreate = "CREATE TABLE Users (user TEXT, pass TEXT)";

    String sqlInsertUno = "INSERT INTO Users VALUES ('administrador', '91f5167c34c400758115c2a6826ec2e3')";
    String sqlInsertDos = "INSERT INTO Users VALUES ('anibalbx', '3dc612b769e82d3e4e70ecf4b3343128')";




    public PcSQLiteHelperUser(Context contexto, String nombre,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCreate);
        db.execSQL(sqlInsertUno);
        db.execSQL(sqlInsertDos);

    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior,
                          int versionNueva) {

        db.execSQL("DROP TABLE IF EXISTS Users");

        db.execSQL(sqlCreate);
        db.execSQL(sqlInsertUno);
        db.execSQL(sqlInsertDos);

    }
}