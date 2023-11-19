package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem


class ValorNumerico( var signo: String?,  var numero: Token?):Expresion() {
    override fun toString(): String {
        return "ValorNumerico(signo= $signo, numero=(${numero!!.parametro}))"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Valor numérico:")
        if (signo == "-") {
            raiz.children.add(TreeItem("${signo}${numero!!.parametro}"))
        } else {
            raiz.children.add(TreeItem("${numero!!.parametro}"))
        }
        return raiz
    }

    fun obtenerTipo(numero: Token) : String {
        if (numero.categoria == Categoria.ENTERO) {
            return "Entero"
        } else {
            return "Real"
        }
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (signo == "-") {
            codigo += signo+numero!!.getJavaCode()
        } else {
            codigo += numero!!.getJavaCode()
        }
        return codigo
    }

}