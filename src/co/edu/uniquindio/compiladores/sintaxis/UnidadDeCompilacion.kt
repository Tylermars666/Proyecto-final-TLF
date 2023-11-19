package co.edu.uniquindio.compiladores.sintaxis
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class UnidadDeCompilacion (var listaFunciones:ArrayList<Funcion>){
    override fun toString(): String {
        return "UnidadDeCompilacion(listaFunciones=$listaFunciones)"
    }

    fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Unidad de Compilacion")

        for (p in listaFunciones){
            raiz.children.add(p.getArbolVisual())
        }
        return raiz
    }

    fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>){
        for (f in listaFunciones) {
            f.llenarTablaSimbolos (tablaSimbolos, listaErrores, "Unidad de Compilación")
        }

    }

    fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>){
        for (f in listaFunciones) {
            f.analizarSemantica(tablaSimbolos, listaErrores)
        }
    }

    fun getJavaCode(): String {
        var codigo = "\nimport javax.swing.JOptionPane;\npublic class Principal {\n"
        for (f in listaFunciones){
            codigo += f.getJavaCode()
        }
        codigo += "}"
        return codigo
    }
}