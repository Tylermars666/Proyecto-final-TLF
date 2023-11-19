package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem


class Funcion(
    var nombreFuncion: Token,
    var listaParametros: ArrayList<Parametro>,
    var tipoRetorno: Token,
    var listaSentencias: ArrayList<Sentencia>
) {

    override fun toString(): String {
        return "Funcion(nombre='$nombreFuncion', parametros=$listaParametros, tipo='$tipoRetorno', sentencias=$listaSentencias)"
    }

    fun getArbolVisual(): TreeItem<String> {

        val raiz = TreeItem("Función")

        raiz.children.add(TreeItem("Nombre: ${nombreFuncion.parametro}"))
        raiz.children.add(TreeItem("Tipo de retorno: ${tipoRetorno.parametro}"))

        var raizParametros = TreeItem("Parámetros")
        for (parametro in listaParametros) {
            raizParametros.children.add(parametro.getArbolVisual())
        }
        raiz.children.add(raizParametros)

        var raizSentencias = TreeItem("Sentencias")
        for (sentencia in listaSentencias) {
            raizSentencias.children.add(sentencia.getArbolVisual())
        }
        raiz.children.add(raizSentencias)

        return raiz
    }

    fun obtenerTiposParametros(): ArrayList<String> {
        var lista = ArrayList<String>()
        for (p in listaParametros) {
            lista.add(p.tipoDato.parametro)
        }
        return lista
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito : String){
        var listaTiposParametros = obtenerTiposParametros()
        var cadenaTipos = ""
        if (listaTiposParametros.isNotEmpty()) {
            cadenaTipos += "["
            for (t in listaTiposParametros) {
                cadenaTipos += t+", "
            }
            cadenaTipos = cadenaTipos.substring(0, cadenaTipos.length-2)
            cadenaTipos += "]"
        } else {
            cadenaTipos += "[]"
        }
        tablaSimbolos.guardarSimboloFuncion("${nombreFuncion.parametro}$cadenaTipos", tipoRetorno.parametro, listaTiposParametros, ambito, nombreFuncion.fila, nombreFuncion.columna)
        for (p in listaParametros) {
            tablaSimbolos.guardarSimboloValor(p.identificador.parametro, p.tipoDato.parametro, true, "${nombreFuncion.parametro}$cadenaTipos", p.identificador.fila, p.identificador.columna)
        }
        for (s in listaSentencias) {
            s.llenarTablaSimbolos(tablaSimbolos, listaErrores, nombreFuncion.parametro)
        }
    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>) {
        for (s in listaSentencias) {
            s.analizarSemantica(tablaSimbolos, listaErrores, nombreFuncion.parametro)
        }
    }

    fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): String {
        return tipoRetorno.parametro
    }

    fun getJavaCode():String {
        var codigo = ""
        if (nombreFuncion.parametro == "principal" && tipoRetorno.parametro == "Vacio") {
            codigo = "public static void main (String[] args) {\n"
        } else {
            codigo = "static ${tipoRetorno.getJavaCode()} ${nombreFuncion.getJavaCode()} ("
            if (listaParametros.isNotEmpty()) {
                for (p in listaParametros) {
                    codigo += p.getJavaCode() + ", "
                }
                codigo = codigo.substring(0, codigo.length - 2)
            }
            codigo += ") {\n"
        }
        for (s in listaSentencias){
            codigo += s.getJavaCode()
        }
        codigo += "}\n"
        return codigo
    }

}





