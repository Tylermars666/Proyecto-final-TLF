package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class Asignacion():Sentencia() {
    var identificadoVariable: Token?=null
    var operadorAsignacion:Token?=null
    var expresion:Expresion?=null
    var invocacion:InvocacionFuncion?=null

    constructor(identificadoVariable: Token?,operadorAsignacion:Token?,expresion:Expresion?):this(){
        this.identificadoVariable=identificadoVariable
        this.operadorAsignacion=operadorAsignacion
        this.expresion=expresion
    }
    constructor(identificadoVariable: Token?,operadorAsignacion:Token?, invovacion:InvocacionFuncion?):this(){
        this.identificadoVariable=identificadoVariable
        this.operadorAsignacion=operadorAsignacion
        this.invocacion =invovacion

    }

    override fun toString(): String {
        return "Asignacion(identificadoVariable=$identificadoVariable, operadorAsignacion=$operadorAsignacion, expresion=$expresion, invovacion=$invocacion)"
    }

    override fun getArbolVisual(): TreeItem<String> {

            var raiz = TreeItem("Asignacion")

        if (expresion != null) {
            raiz.children.add(TreeItem("${identificadoVariable!!.parametro} ${operadorAsignacion!!.parametro} ${expresion}"))
        } else {
            raiz.children.add(TreeItem("${identificadoVariable!!.parametro} ${operadorAsignacion!!.parametro} ${invocacion}"))
        }
            return raiz
        }

    override fun analizarSemantica(tablaSimbolos: TablaSimbolos, listaErrores: ArrayList<Error>, ambito: String) {
        var s = tablaSimbolos.buscarSimboloValor(identificadoVariable!!.parametro, ambito)
        if (s == null){
            listaErrores.add(Error("El campo (${identificadoVariable!!.parametro}) no existe dentro del ámbito ($ambito).", identificadoVariable!!.fila, identificadoVariable!!.columna))
        } else {
            var tipo = s.tipo
            if (expresion != null) {
                expresion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
                var tipoExpresion = expresion!!.obtenerTipo(tablaSimbolos, ambito, listaErrores)
                if (tipoExpresion != tipo){
                    listaErrores.add(Error("El tipo de dato de la expresión ($tipoExpresion) no coincide con el tipo de dato del campo ($tipo).", identificadoVariable!!.fila, identificadoVariable!!.columna))
                }
            } else if (invocacion != null) {
                invocacion!!.analizarSemantica(tablaSimbolos, listaErrores, ambito)
            }
        }
    }

    override fun getJavaCode(): String {

        var codigo = "${identificadoVariable!!.getJavaCode()} ${operadorAsignacion!!.getJavaCode()} "

        if (expresion != null) {
            codigo += "${expresion!!.getJavaCode()};\n"
        }
        if (invocacion != null ){
            codigo += "${invocacion!!.getJavaCode()}"
        }
        return codigo
    }
}
