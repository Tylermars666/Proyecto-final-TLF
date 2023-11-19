package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Error
import javafx.scene.control.TreeItem
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos

class Retorno () : Sentencia() {

    var expresion : Expresion? = null
    var invocacionFuncion : InvocacionFuncion? = null
    var nulo : Token? = null

    constructor(expresion: Expresion) : this() {
        this.expresion = expresion
    }

    constructor(invocacionFuncion: InvocacionFuncion) : this() {
        this.invocacionFuncion = invocacionFuncion
    }

    constructor(nulo: Token) : this() {
        this.nulo = nulo
    }

    override fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem<String>("Retorno:")

        if (expresion != null) {
            raiz.children.add(expresion!!.getArbolVisual())
        } else {
            if (invocacionFuncion != null) {
                raiz.children.add(invocacionFuncion!!.getArbolVisual())
            } else {
                if (nulo!!.categoria == Categoria.PALABRA_RESERVADA && nulo!!.parametro == "Nulo") {
                    raiz.children.add(TreeItem(nulo!!.parametro))
                }
            }
        }

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (expresion != null) {
            expresion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        if (invocacionFuncion != null) {
            invocacionFuncion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun getJavaCode(): String {
        if (expresion != null) {
            return "return ${expresion!!.getJavaCode()};\n"
        } else if (invocacionFuncion != null) {
            return "return ${invocacionFuncion!!.getJavaCode()}"
        } else if (nulo != null) {
            return "return ${nulo!!.getJavaCode()};\n"
        }
        return "//Error al generar retorno"
    }

}
