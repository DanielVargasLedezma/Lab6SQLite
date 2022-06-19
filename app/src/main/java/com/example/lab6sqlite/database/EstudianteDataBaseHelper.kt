package com.example.lab6sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.Currency

class EstudianteDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 TEXT PRIMARY KEY, $COL_2 TEXT, $COL_3 TEXT, $COL_4 INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE ${AdminDataBaseHelper.TABLE_NAME} (${AdminDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${AdminDataBaseHelper.COL_2} TEXT)"
        )
        db.execSQL(
            "CREATE TABLE ${CursoDataBaseHelper.TABLE_NAME} (${CursoDataBaseHelper.COL_1} TEXT PRIMARY KEY, ${CursoDataBaseHelper.COL_2} TEXT, ${CursoDataBaseHelper.COL_3} INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertarEstudiante(id: String, nombre: String, apellidos: String, edad: Int): Long {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()
            contentValues.put(COL_1, id)
            contentValues.put(COL_2, nombre)
            contentValues.put(COL_3, apellidos)
            contentValues.put(COL_4, edad)

            return db.insert(TABLE_NAME, null, contentValues)
        }

        return -1
    }

    fun editarEstudiante(id: String, nombre: String, apellidos: String, edad: Int): Boolean {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()

            contentValues.put(COL_2, nombre)
            contentValues.put(COL_3, apellidos)
            contentValues.put(COL_4, edad)

            return db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id)) != 0
        }
        return false
    }

    fun deleteEstudiante(id: String): Int {
        val db = this.writableDatabase
        if (db.isOpen) {
            return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
        }
        return 0
    }

//    fun cursosDeEstudiante(id: String): Cursor? {
//        val db = this.writableDatabase
//
//        if(db.isOpen) {
//            return db.rawQuery("SELECT * FROM ${CursoDataBaseHelper.TABLE_NAME}  WHERE ${}", arrayOf(id))
//        }
//        return null
//    }

    val allData: Cursor?
        get() {
            val db = this.writableDatabase
            if (db.isOpen) {
                return db.rawQuery(
                    "SELECT * FROM " +
                            TABLE_NAME, null
                )
            }
            return null
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