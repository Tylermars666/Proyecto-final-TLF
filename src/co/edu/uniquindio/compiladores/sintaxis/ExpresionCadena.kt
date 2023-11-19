package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem


class ExpresionCadena(var cadena: String) : Expresion() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Expresión Cadena:")
        raiz.children.add(TreeItem(cadena))
        return raiz
    }

    override fun toString(): String {
        return "Expresion Cadena ($cadena)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): String {
        return "Cadena"
    }

    override fun getJavaCode(): String {
        return "\"$cadena\""
    }

}
