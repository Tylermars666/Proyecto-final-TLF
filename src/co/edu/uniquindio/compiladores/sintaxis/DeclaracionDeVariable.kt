package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class DeclaracionDeVariable(var tipoDato:Token, var tipoVariable:Token,var  identificadorVariable: Token) :Sentencia(){

    override fun toString(): String {
        return "DeclaracionDeVariable(tipoDato='$tipoDato', tipoVariable='$tipoVariable', identificadorVariable='$identificadorVariable')"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Declaración de Variable")
        if (tipoVariable.parametro == "Var") {
            raiz.children.add(TreeItem("Mutable:\n${tipoVariable.parametro} ${tipoDato.parametro} ${identificadorVariable.parametro}"))
        } else {
            raiz.children.add(TreeItem("Inmutable:\n${tipoVariable.parametro} ${tipoDato.parametro} ${identificadorVariable.parametro}"))
        }
        return raiz
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloValor(identificadorVariable.parametro, tipoDato.parametro, true, ambito, identificadorVariable.fila, identificadorVariable.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var simbolo = tablaSimbolos.buscarSimboloValor(identificadorVariable.parametro, ambito)
        if (simbolo!!.nombre == identificadorVariable.parametro && simbolo!!.tipo == tipoDato.parametro) {
            listaErrores.add(Error("El campo (${identificadorVariable.parametro}) ya existe dentro del ámbito ($ambito).", identificadorVariable.fila, identificadorVariable.columna))
        }
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (tipoVariable.parametro == "Var") {
            codigo += "${tipoDato.getJavaCode()} ${identificadorVariable.getJavaCode()};\n"
        } else if (tipoVariable.parametro == "Val") {
            codigo += "${tipoVariable.getJavaCode()} ${tipoDato.getJavaCode()} ${identificadorVariable.getJavaCode()};\n"
        }
        return codigo
    }

}