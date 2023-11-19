package co.edu.uniquindio.compiladores.semantica

class Simbolo () {

    var nombre: String = ""
    var tipo: String = ""
    var modificable: Boolean = false
    var fila = 0
    var columna = 0
    var ambito: String = ""
    var tiposParametros: ArrayList<String>? = null

    /**
     * Constructor para crear un símbolo de tipo valor.
     */
    constructor(nombre: String, tipoDato: String, modificable: Boolean, ambito: String, fila: Int, columna: Int):this() {
        this.nombre = nombre
        this.tipo = tipoDato
        this.modificable = modificable
        this.ambito = ambito
        this.fila = fila
        this.columna = columna
    }

    /**
     * Constructor para crear un símbolo de tipo función.
     */
    constructor(nombre: String, tipoRetorno: String, tipoParametros: ArrayList<String>, ambito: String):this() {
        this.nombre = nombre
        this.tipo = tipoRetorno
        this.tiposParametros = tipoParametros
        this.ambito = ambito
    }

    override fun toString(): String {

        return if (tiposParametros == null) {
            "Símbolo (Nombre = '$nombre', Tipo = '$tipo', Modificable = $modificable, Ámbito = '$ambito', Fila = $fila, Columna = $columna)"
        } else {
            "Símbolo (Nombre = '$nombre', Tipo = '$tipo', Ámbito = '$ambito', Tipo de parámetros = $tiposParametros)"
        }
    }


}