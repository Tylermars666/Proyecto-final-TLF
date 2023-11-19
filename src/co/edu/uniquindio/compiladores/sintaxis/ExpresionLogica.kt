package co.edu.uniquindio.compiladores.sintaxis
import co.edu.uniquindio.compiladores.lexico.Error
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.semantica.TablaSimbolos
import javafx.scene.control.TreeItem

class ExpresionLogica ():Expresion() {

    var expresionAritmetica1:ExpresionLogica?=null
    var expresionAritmetica2:ExpresionLogica?=null
    var expresionRelacional:ExpresionRelacional?=null
    var oLogico: Token?=null
    var oBinario:Token?=null

    constructor(oLogico: Token?, expresionLogica1:ExpresionLogica?, oBinario:Token?, expresionLogica2:ExpresionLogica?):this(){
        this.expresionAritmetica1=expresionLogica1
        this.expresionAritmetica2=expresionLogica2
        this.oLogico=oLogico
        this.oBinario=oBinario

    }
    constructor(oLogico: Token?, expresionLogica:ExpresionLogica?):this(){
        this.expresionAritmetica1=expresionLogica

        this.oLogico=oLogico


    }
    constructor(expresionRelacional:ExpresionRelacional?, oBinario:Token?, expresionAritmetica2:ExpresionLogica?):this(){
        this.expresionRelacional=expresionRelacional
        this.expresionAritmetica2=expresionAritmetica2
        this.oBinario=oBinario

    }
    constructor( espresionRelacional:ExpresionRelacional?):this(){
        this.expresionRelacional=espresionRelacional


    }

    override fun getArbolVisual(): TreeItem<String> {
        var raiz = TreeItem("Expresión Lógica")
        if (oLogico != null && oBinario != null && expresionAritmetica1 != null && expresionAritmetica2 != null ) {
            raiz.children.add(TreeItem("${oLogico!!.parametro}"))
            raiz.children.add(expresionAritmetica1!!.getArbolVisual())
            raiz.children.add(TreeItem("${oBinario!!.parametro}"))
            raiz.children.add(expresionAritmetica2!!.getArbolVisual())
        } else {
            if (oLogico != null && expresionAritmetica1 != null) {
                raiz.children.add(TreeItem("${oLogico!!.parametro}"))
                raiz.children.add(expresionAritmetica1!!.getArbolVisual())
            } else {
                if (expresionRelacional != null && oBinario != null && expresionAritmetica2 != null) {
                    raiz.children.add(expresionRelacional!!.getArbolVisual())
                    raiz.children.add(TreeItem("${oBinario!!.parametro}"))
                    raiz.children.add(expresionAritmetica2!!.getArbolVisual())
                } else {
                    if (expresionRelacional != null) {
                        raiz.children.add(expresionRelacional!!.getArbolVisual())
                    }
                }
            }
        }
        return raiz
    }

    override fun toString(): String {
        return "ExpresionLogica(espresionAritmetica1=$expresionAritmetica1, espresionAritmetica2=$expresionAritmetica2, espresionRelacional=$expresionRelacional, oLogico=$oLogico, oBinario=$oBinario)"
    }

    override fun obtenerTipo(tablaSimbolos: TablaSimbolos, ambito: String, listaErrores: ArrayList<Error>): String {
        return "Binario"
    }

    override fun getJavaCode(): String {
        var codigo = ""
        if (oLogico != null && oBinario != null && expresionAritmetica1 != null && expresionAritmetica2 != null) {
            codigo += "${oLogico!!.getJavaCode()}(${expresionAritmetica1!!.getJavaCode()} ${oBinario!!.getJavaCode()} ${expresionAritmetica2!!.getJavaCode()})"
        } else {
            if (oLogico != null && expresionAritmetica1 != null) {
                codigo += "${oLogico!!.getJavaCode()}(${expresionAritmetica1!!.getJavaCode()})"
            } else {
                if (expresionRelacional != null && oBinario != null && expresionAritmetica2 != null) {
                    codigo += "${expresionRelacional!!.getJavaCode()} ${oBinario!!.getJavaCode()} ${expresionAritmetica2!!.getJavaCode()}"
                } else {
                    if (expresionRelacional != null) {
                        codigo += "${expresionRelacional!!.getJavaCode()}"
                    }
                }
            }
        }
        return codigo
    }

}
