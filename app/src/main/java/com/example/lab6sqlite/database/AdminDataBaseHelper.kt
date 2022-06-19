package com.example.lab6sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 TEXT PRIMARY KEY, $COL_2 TEXT)"
        )
        db.execSQL(
            "CREATE TABLE ${EstudianteDataBaseHelper.TABLE_NAME} (${EstudianteDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${EstudianteDataBaseHelper.COL_2} TEXT, ${EstudianteDataBaseHelper.COL_3} TEXT, " +
                    "${EstudianteDataBaseHelper.COL_4} INTEGER)"
        )
        db.execSQL(
                "CREATE TABLE ${CursoDataBaseHelper.TABLE_NAME} (${CursoDataBaseHelper.COL_1} TEXT PRIMARY KEY, ${CursoDataBaseHelper.COL_2} TEXT, ${CursoDataBaseHelper.COL_3} INTEGER)"
                )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertAdmin(id: String, clave: String): Long {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()
            contentValues.put(COL_1, id)
            contentValues.put(COL_2, clave)

            return db.insert(TABLE_NAME, null, contentValues)
        }

        return -1
    }

    fun doLogin(id: String, clave: String): Boolean {
        val db = this.writableDatabase

        if (db.isOpen) {
            val querySearch = "SELECT * FROM $TABLE_NAME  WHERE $COL_1 = ? AND $COL_2 = ?"
            val res = db.rawQuery(querySearch, arrayOf(id, clave))

            return res.count != 0
        }

        return false
    }

    companion object {
        val DATABASE_NAME = "matricula.db"
        val TABLE_NAME = "administradores"
        val COL_1 = "ID"
        val COL_2 = "CLAVE"
    }
}