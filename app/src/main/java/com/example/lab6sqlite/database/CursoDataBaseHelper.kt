package com.example.lab6sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CursoDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${AdminDataBaseHelper.TABLE_NAME} (${AdminDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${AdminDataBaseHelper.COL_2} TEXT)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 TEXT PRIMARY KEY, $COL_2 TEXT, $COL_3 INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE ${EstudianteDataBaseHelper.TABLE_NAME} (${EstudianteDataBaseHelper.COL_1} TEXT PRIMARY KEY, ${EstudianteDataBaseHelper.COL_2} TEXT, ${EstudianteDataBaseHelper.COL_3} TEXT, ${EstudianteDataBaseHelper.COL_4} INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE ${CursosDeEstudianteDataBaseHelper.TABLE_NAME} (${CursosDeEstudianteDataBaseHelper.COL_1} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "${CursosDeEstudianteDataBaseHelper.COL_2} TEXT, ${CursosDeEstudianteDataBaseHelper.COL_3} TEXT, " +
                    " FOREIGN KEY (${CursosDeEstudianteDataBaseHelper.COL_2}) REFERENCES ${EstudianteDataBaseHelper.TABLE_NAME}(${EstudianteDataBaseHelper.COL_1})," +
                    " FOREIGN KEY (${CursosDeEstudianteDataBaseHelper.COL_3}) REFERENCES ${TABLE_NAME}(${COL_1})," +
                    " UNIQUE(${CursosDeEstudianteDataBaseHelper.COL_2}, ${CursosDeEstudianteDataBaseHelper.COL_3}))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertCurso(id: String, descripcion: String, creditos: Int): Long {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()
            contentValues.put(COL_1, id)
            contentValues.put(COL_2, descripcion)
            contentValues.put(COL_3, creditos)

            return db.insert(TABLE_NAME, null, contentValues)
        }
        return -1
    }

    fun deleteCurso(id: String): Int {
        val db = this.writableDatabase

        if (db.isOpen) {
            return db.delete(TABLE_NAME, "ID = ?", arrayOf(id))
        }
        return 0
    }

    fun updateCurso(id: String, descripcion: String, creditos: Int): Boolean {

        val db = this.writableDatabase
        if (db.isOpen) {
            val contentValues = ContentValues()
            contentValues.put(COL_1, id)
            contentValues.put(COL_2, descripcion)
            contentValues.put(COL_3, creditos)
            return db.update(TABLE_NAME, contentValues, "ID = ?", arrayOf(id)) != 0

        }
        return false
    }

    val allCursos: Cursor?
        get() {
            val db = this.writableDatabase
            if (db.isOpen) {
                return db.rawQuery("SELECT * FROM " + TABLE_NAME, null)
            }
            return null
        }

    companion object {
        val DATABASE_NAME = "matricula.db"
        val TABLE_NAME = "cursos"
        val COL_1 = "ID"
        val COL_2 = "DESCRIPCION"
        val COL_3 = "CREDITOS"
    }
}