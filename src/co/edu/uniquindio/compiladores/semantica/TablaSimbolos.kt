package co.edu.uniquindio.compiladores.semantica

import co.edu.uniquindio.compiladores.lexico.Error

class TablaSimbolos (var listaErrores: ArrayList<Error>) {

    var listaSimbolos: ArrayList<Simbolo> = ArrayList()

    fun reportarError(mensaje: String, fila: Int, columna: Int) {
        listaErrores.add(Error(mensaje, fila, columna))
    }

    /**
     * Función que permite guardar un símbolo de tipo variable en la tabla de símbolos.
     */
    fun guardarSimboloValor (nombre: String, tipoDato: String, modificable: Boolean, ambito: String, fila: Int, columna: Int){
        val s = buscarSimboloValor(nombre, ambito)
        if (s == null){
            listaSimbolos.add(Simbolo(nombre, tipoDato, modificable, ambito, fila, columna))
        } else {
            reportarError("El campo con el nombre $nombre ya existe dentro del ámbito $ambito", fila, columna)
        }
    }

    /**
     * Función que permite guardar un símbolo de tipo función en la tabla de símbolos.
     */
    fun guardarSimboloFuncion (nombre: String, tipoRetorno: String, tiposParametros: ArrayList<String>, ambito: String, fila: Int, columna: Int){
        val s = buscarSimboloFuncion(nombre, tiposParametros)
        if (s == null) {
            listaSimbolos.add(Simbolo(nombre, tipoRetorno, tiposParametros, ambito))
        } else {
            reportarError("La función $nombre ya existe dentro del ámbito $ambito", fila, columna)
        }
    }

    /**
     * Permite buscar un valor dentro de la tabla de símbolos.
     */
    fun buscarSimboloValor (nombre: String, ambito: String):Simbolo? {

        for (s in listaSimbolos) {
            if (s.tiposParametros == null) {
                if (s.nombre == nombre && s.ambito == ambito) {
                    return s
                }
            }
        }
        return null
    }

    /**
     * Permite buscar una función dentro de la tabla de símbolos.
     */
    fun buscarSimboloFuncion (nombre: String, tiposParametros: ArrayList<String> ):Simbolo? {

        for (s in listaSimbolos) {
            if (s.tiposParametros != null) {
                if (s.nombre == nombre && s.tiposParametros == tiposParametros) {
                    return s
                }
            }
        }
        return null
    }

    override fun toString(): String {
        return "TablaSimbolos(listaSimbolos=$listaSimbolos)"
    }


}