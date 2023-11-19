package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem
import kotlin.collections.ArrayList


open class Arreglo(var nombre: Token, var tipoDato:Token, var listaExpresiones:ArrayList<Expresion>):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem<String>("Arreglo")
        raiz.children.add(TreeItem("${nombre.parametro} :${tipoDato.parametro}"))
        if (listaExpresiones.isNotEmpty()) {
            val raizExp = TreeItem("Argumentos")
            for (expresion in listaExpresiones) {
                raizExp.children.add(expresion.getArbolVisual())
            }
            raiz.children.add(raizExp)
        }
        return raiz


    }

    override fun toString(): String {
        return "Arreglo(nombre=$nombre, tipoDato=$tipoDato, listaExpresiones=$listaExpresiones)"
    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        tablaSimbolos.guardarSimboloValor(nombre.parametro, tipoDato.parametro, true, ambito, nombre.fila, nombre.columna)
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (e in listaExpresiones) {
            var tipo = e.obtenerTipo(tablaSimbolos, ambito, listaErrores)
            if (tipo != tipoDato.parametro) {
                listaErrores.add(Error("El tipo de dato de la expresión ($tipo) no coincide con el tipo de dato del arreglo (${tipoDato.parametro}).", nombre.fila, nombre.columna))
            }
        }
    }

    override fun getJavaCode(): String {
        var codigo = "${tipoDato.getJavaCode()}[] ${nombre.getJavaCode()}"
        if (listaExpresiones != null) {
            codigo += " = {"
            if (listaExpresiones.isNotEmpty()) {
                for (e in listaExpresiones) {
                    codigo += "${e.getJavaCode()}, "
                }
                codigo = codigo.substring(0, codigo.length - 2)
            }
            codigo += "}"
        }
        codigo += ";\n"
        return codigo
    }
}
