package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import javax.print.DocFlavor

class Argumento () {

    var expresionAritmetica : ExpresionAritmetica? = null
    var exprecionCadena : ExpresionCadena? = null

    constructor(expresionAritmetica: ExpresionAritmetica) : this() {
        this.expresionAritmetica = expresionAritmetica
    }

    constructor(expresionCadena: ExpresionCadena) : this() {
        this.exprecionCadena = expresionCadena
    }

    fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem<String>("Argumento:")

        if (expresionAritmetica != null) {
            raiz.children.add(expresionAritmetica!!.getArbolVisual())
        } else {
            if (exprecionCadena != null) {
                raiz.children.add(exprecionCadena!!.getArbolVisual())
            }
        }

        return raiz
    }

    fun obtenerTipoDato (tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>) : String {
        if (exprecionCadena != null) {
            return "Cadena"
        }
        if (expresionAritmetica != null) {
            return expresionAritmetica!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
        }
        return ""
    }

    fun getJavaCode(): String {
        var codigo = ""
        if (expresionAritmetica != null) {
            codigo += "${expresionAritmetica!!.getJavaCode()}"
        }
        if (exprecionCadena != null) {
            codigo += "${exprecionCadena!!.getJavaCode()}"
        }
        return codigo
    }

}
