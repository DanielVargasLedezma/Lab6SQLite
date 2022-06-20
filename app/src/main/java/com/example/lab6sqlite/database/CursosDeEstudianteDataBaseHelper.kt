package com.example.lab6sqlite.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CursosDeEstudianteDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${AdminDataBaseHelper.TABLE_NAME} (${AdminDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${AdminDataBaseHelper.COL_2} TEXT)"
        )
        db.execSQL(
            "CREATE TABLE ${EstudianteDataBaseHelper.TABLE_NAME} (${EstudianteDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${EstudianteDataBaseHelper.COL_2} TEXT, ${EstudianteDataBaseHelper.COL_3} TEXT, " +
                    "${EstudianteDataBaseHelper.COL_4} INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE ${CursoDataBaseHelper.TABLE_NAME} (${CursoDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${CursoDataBaseHelper.COL_2} TEXT, ${CursoDataBaseHelper.COL_3} INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL_2 TEXT, $COL_3 TEXT, " +
                    " FOREIGN KEY ($COL_2) REFERENCES ${EstudianteDataBaseHelper.TABLE_NAME}(${EstudianteDataBaseHelper.COL_1})," +
                    " FOREIGN KEY ($COL_3) REFERENCES ${CursoDataBaseHelper.TABLE_NAME}(${CursoDataBaseHelper.COL_1})," +
                    " UNIQUE($COL_2, $COL_3))"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertarMatricula(id_curso: String, id_estudiante: String): Boolean {
        val db = this.writableDatabase

        if (db.isOpen) {
            val contentValues = ContentValues()

            contentValues.put(COL_2, id_estudiante)
            contentValues.put(COL_3, id_curso)

            return db.insert(TABLE_NAME, null, contentValues) != -1L
        }
        return false
    }

    fun eliminarMatricula(id_curso: String, id_estudiante: String): Boolean {
        val db = this.writableDatabase

        if (db.isOpen) {
            return db.delete(TABLE_NAME, "$COL_2 = ? AND $COL_3 = ?", arrayOf(id_estudiante, id_curso)) != -1
        }
        return false
    }

    fun cursosDeEstudiante(id: String): Cursor? {
        val db = this.writableDatabase

        if (db.isOpen) {
            return db.rawQuery(
                "SELECT * FROM ${CursoDataBaseHelper.TABLE_NAME} WHERE ${CursoDataBaseHelper.COL_1} " +
                        "IN (SELECT $COL_3 FROM $TABLE_NAME WHERE $COL_2 = ?)", arrayOf(id)
            )
        }
        return null
    }

    companion object {
        val DATABASE_NAME = "matricula.db"
        val TABLE_NAME = "cursos_matriculados"
        val COL_1 = "ID"
        val COL_2 = "id_estudiante"
        val COL_3 = "id_curso"
    }
}