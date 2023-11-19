package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class CicloMientras (var expLogicoRelacional:ExpresionLogica?, var listaSentencias:ArrayList<Sentencia>):Sentencia(){

    override fun toString(): String {
        return "CicloMientras(exp=$expLogicoRelacional, lista=$listaSentencias)"
    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Ciclo Mientras")
        raiz.children.add(expLogicoRelacional!!.getArbolVisual())
        var raizParametros = TreeItem("Lista Sentencia")
        for (p in listaSentencias){
            raizParametros.children.add(p.getArbolVisual())
        }
        raiz.children.add(raizParametros)
        return raiz

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentencias) {
            s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        expLogicoRelacional!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        for (s in listaSentencias) {
            s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = "while ("+expLogicoRelacional!!.getJavaCode()+"){\n"
        for (s in listaSentencias) {
            codigo += s.getJavaCode()
        }
        codigo += "}\n"
        return codigo
    }

}