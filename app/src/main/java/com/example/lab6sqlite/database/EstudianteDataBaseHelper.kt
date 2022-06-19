package com.example.lab6sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EstudianteDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 TEXT PRIMARY KEY, $COL_2 TEXT, $COL_3 TEXT, $COL_4 INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertUser(id: String, nombre: String, apellidos: String, edad: Int) {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()
            contentValues.put(COL_1, id)
            contentValues.put(COL_2, nombre)
            contentValues.put(COL_3, apellidos)
            contentValues.put(COL_4, edad)

            db.insert(TABLE_NAME, null, contentValues)
            db.close()
        }
    }

    companion object {
        val DATABASE_NAME = "matricula.db"
        val TABLE_NAME = "estudiantes"
        val COL_1 = "ID"
        val COL_2 = "NOMBRE"
        val COL_3 = "APELLIDOS"
        val COL_4 = "EDAD"
    }
}