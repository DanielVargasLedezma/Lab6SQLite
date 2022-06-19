package com.example.lab6sqlite.modelo

data class Estudiante(var id: String = "", var nombre: String = "", var apellidos: String = "", var edad: Int = 0) :
    java.io.Serializable