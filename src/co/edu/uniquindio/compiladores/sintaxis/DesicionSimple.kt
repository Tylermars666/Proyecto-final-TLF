package co.edu.uniquindio.compiladores.sintaxis
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class DesicionSimple(var expresionLogica: ExpresionLogica?, var listaSentenciasV:ArrayList<Sentencia>, var listaSentenciasF:ArrayList<Sentencia>?):Sentencia() {


    override fun toString(): String {
        return "DesicionSimple(expresionLogica=$expresionLogica, listaSencia=$listaSentenciasV, listaSenciaSino=$listaSentenciasF)"
    }

    override fun getArbolVisual(): TreeItem<String> {

        var raiz = TreeItem("Decicion Simple")
        raiz.children.add(expresionLogica!!.getArbolVisual())
        var raizSentenciasV = TreeItem("Lista sentencias (V)")
        for (p in listaSentenciasV){
            raizSentenciasV.children.add(p.getArbolVisual())
        }
        raiz.children.add(raizSentenciasV)

        var listaF = listaSentenciasF
        if (listaF != null) {
            var raizSentenciasF = TreeItem("Lista sentencias (F)")
            for (p in listaF) {
                raizSentenciasF.children.add(p.getArbolVisual())
            }
            raiz.children.add(raizSentenciasF)
        }
        return raiz

    }

    override fun llenarTablaSimbolos(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        for (s in listaSentenciasV) {
            s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)
        }
        if (listaSentenciasF != null) {
            for (s in listaSentenciasF!!) {
                s.llenarTablaSimbolos(tablaSimbolos, listaErrores, ambito)
            }
        }
    }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        if (expresionLogica != null) {
            expresionLogica!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        for (s in listaSentenciasV) {
            s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
        }
        if (listaSentenciasF != null) {
            for (s in listaSentenciasF!!) {
                s.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            }
        }
    }

    override fun getJavaCode(): String {
        var codigo = "if ("+expresionLogica!!.getJavaCode()+"){\n"
        for (s in listaSentenciasV) {
            codigo += s.getJavaCode()
        }
        codigo += "}\n"
        if (listaSentenciasF != null) {
            codigo += "else {\n"
            for (s in listaSentenciasF!!) {
                codigo += s.getJavaCode()
            }
            codigo += "}\n"
        }
        return codigo
    }

}