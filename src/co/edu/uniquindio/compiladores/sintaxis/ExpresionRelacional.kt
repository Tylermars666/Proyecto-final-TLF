package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionRelacional():Expresion() {

    var expresionAritmetica1:ExpresionAritmetica?=null
    var expresionAritmetica2:ExpresionAritmetica?=null
    var operador: Token?=null

    constructor(espresionAritmetica1:ExpresionAritmetica?, operador: Token?, espresionAritmetica2:ExpresionAritmetica?):this(){
        this.expresionAritmetica1=espresionAritmetica1
        this.expresionAritmetica2=espresionAritmetica2
        this.operador=operador
    }

    override fun toString(): String {
        return "ExpresionRelacional(espresionAritmetica1=$expresionAritmetica1 operador=$operador espresionAritmetica2=$expresionAritmetica2)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresión Relacional")
        raiz.children.add(expresionAritmetica1!!.getArbolVisual())
        raiz.children.add(TreeItem("Operador relacional: ${operador!!.parametro}"))
        raiz.children.add(expresionAritmetica2!!.getArbolVisual())
        return raiz
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): String {
        return "Binario"
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (expresionAritmetica1 != null && expresionAritmetica2 != null){
            expresionAritmetica1!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            expresionAritmetica2!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = ""
        codigo += "${expresionAritmetica1!!.getJavaCode()} ${operador!!.getJavaCode()} ${expresionAritmetica2!!.getJavaCode()}"
        return codigo
    }
}
