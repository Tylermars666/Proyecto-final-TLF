package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Token
import javafx.scene.control.TreeItem

class Parametro(var tipoDato:Token, var identificador:Token) {

    override fun toString(): String {
        return "Parametro Tipo ='$tipoDato', identificador = $identificador"
    }
    fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Parámetro")
        raiz.children.add(TreeItem("${identificador.parametro} : ${tipoDato.parametro}"))
        return raiz
    }

    fun getJavaCode():String {
        return "${tipoDato.getJavaCode()} ${identificador.getJavaCode()}"
    }

}