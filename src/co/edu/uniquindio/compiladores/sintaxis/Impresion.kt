package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Impresion(var listaExpresionCadena: ArrayList<ExpresionCadena>):Sentencia() {

    override fun getArbolVisual(): TreeItem<String> {
        val raiz = TreeItem<String>("Imprimir:")
        var raizExpresionCadena = TreeItem("Expresiones Cadena:")
        for (expresionCadena in listaExpresionCadena) {
            raizExpresionCadena.children.add(expresionCadena.getArbolVisual())
        }
        raiz.children.add(raizExpresionCadena)
        return raiz
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (e in listaExpresionCadena) {
            e!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
    }

    override fun getJavaCode(): String {
        var codigo = ""
        for (e in listaExpresionCadena) {
            codigo += "${e.getJavaCode()}+"
        }
        codigo = codigo.substring(0, codigo.length - 1)
        return "JOptionPane.showMessageDialog(null, $codigo);\n"
    }

}