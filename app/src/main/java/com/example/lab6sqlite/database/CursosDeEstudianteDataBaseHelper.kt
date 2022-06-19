package com.example.lab6sqlite.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CursosDeEstudianteDataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, EstudianteDataBaseHelper.DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE ${EstudianteDataBaseHelper.TABLE_NAME} (${EstudianteDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${EstudianteDataBaseHelper.COL_2} TEXT, ${EstudianteDataBaseHelper.COL_3} TEXT, " +
                    "${EstudianteDataBaseHelper.COL_4} INTEGER)"
        )
        db.execSQL(
            "CREATE TABLE ${AdminDataBaseHelper.TABLE_NAME} (${AdminDataBaseHelper.COL_1} TEXT PRIMARY KEY, " +
                    "${AdminDataBaseHelper.COL_2} TEXT)"
        )
        db.execSQL(
            "CREATE TABLE $TABLE_NAME ($COL_1 INTEGER PRIMARY KEY AUTOINCREMENT, $COL_2 TEXT )"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        val DATABASE_NAME = "matricula.db"
        val TABLE_NAME = "cursos_matriculados"
        val COL_1 = "ID"
        val COL_2 = "id_estudiante"
        val COL_3 = "id_curso"
    }
}