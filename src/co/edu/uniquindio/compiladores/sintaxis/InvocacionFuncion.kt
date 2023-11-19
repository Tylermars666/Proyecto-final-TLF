package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem


class InvocacionFuncion(var nombreFuncion: Token, var listaArgumentos:ArrayList<Argumento>) : Sentencia() {
    override fun toString(): String {
        return "Invocacion(nombre='$nombreFuncion', listaArguementos=$listaArgumentos)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem("Invocar Función:")

        raiz.children.add(TreeItem("Nombre: ${nombreFuncion.parametro}"))

        var raizArgumentos = TreeItem("Argumentos:")
        for (argumento in listaArgumentos) {
            raizArgumentos.children.add(argumento.getArbolVisual())
        }
        raiz.children.add(raizArgumentos)

        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var listaTiposArgumentos = obtenerTiposArgumentos(tablaSimbolos, ambito, listaErrores)
        var cadenaTipos = ""
        if (listaTiposArgumentos.isNotEmpty()) {
            cadenaTipos += "["
            for (t in listaTiposArgumentos) {
                cadenaTipos += t+", "
            }
            cadenaTipos = cadenaTipos.substring(0, cadenaTipos.length-2)
            cadenaTipos += "]"
        } else {
            cadenaTipos += "[]"
        }
        var s = tablaSimbolos.buscarSimboloFuncion("${nombreFuncion.parametro}$cadenaTipos", listaTiposArgumentos)
        if (s == null) {
            listaErrores.add(Error("La función (${nombreFuncion.parametro}$cadenaTipos) no existe.", nombreFuncion.fila, nombreFuncion.columna))
        }
    }

    fun obtenerTiposArgumentos (tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>) : ArrayList<String> {
        var listaTiposArgumentos = ArrayList<String>()
        for (a in listaArgumentos) {
            listaTiposArgumentos.add(a.obtenerTipoDato(tablaSimbolos, ambito, listaErrores))
        }
        return listaTiposArgumentos
    }

    override fun getJavaCode(): String {
        var codigo = "${nombreFuncion.getJavaCode()}("
        if (listaArgumentos.isNotEmpty()) {
            for (argumento in listaArgumentos) {
                codigo += "${argumento.getJavaCode()}, "
            }
            codigo = codigo.substring(0, codigo.length - 2)
        }
        codigo += ");\n"

        return codigo
    }
}