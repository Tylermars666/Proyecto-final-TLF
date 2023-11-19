package co.edu.uniquindio.compiladores.semantica

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.sintaxis.UnidadDeCompilacion

class AnalizadorSemantico (var uc : UnidadDeCompilacion) {

    var listaErrores: ArrayList<Error> = ArrayList()
    var tablaSimbolos: TablaSimbolos = TablaSimbolos(listaErrores)

    fun llenarTablaSimbolos () {
        uc.llenarTablaSimbolos (tablaSimbolos, listaErrores)
    }

    fun analizarSemantica () {
        uc.analizarSemantica (tablaSimbolos, listaErrores)
    }
}