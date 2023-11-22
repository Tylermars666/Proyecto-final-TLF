package co.edu.uniquindio.compiladores.lexico

import java.util.*


class AnalizadorLexico(var codigoFuente: String) {

    var posicionActual = 0
    var caracterActual = codigoFuente[0]
    var listaTokens = ArrayList<Token>()
    var finCodigo = 0.toChar()
    var filaActual = 0
    var columnaActual = 0
    var listaErrores = ArrayList<Error>()

    /**
     * Método que obtiene el siguiente caracter de una palabra.
     */
    fun obtenerSiguienteCaracter() {
        if (posicionActual == codigoFuente.length - 1) {
            caracterActual = finCodigo
        } else {
            if (caracterActual == '\n') {
                filaActual++
                columnaActual = 0
            } else {
                columnaActual++
            }
            posicionActual++
            caracterActual = codigoFuente[posicionActual]
        }
    }

    /**
     * Método que permite almacenar el token actual a la lista de tokens.
     */
    fun almacenarToken(parametro: String, categoria: Categoria, fila: Int, columna: Int) =
        listaTokens.add(Token(parametro, categoria, fila, columna))

    /**
     * Método que guarda los tokens que se catalogan como error en una lista.
     */
   fun reportarError(error: String) =
        listaErrores.add(Error(error, filaActual, columnaActual))

    /**
     * Método que hace BackTraking.
     */
    fun hacerBT(posicionInicial: Int, filaInicial: Int, columnaInicial: Int) {
        posicionActual = posicionInicial
        filaActual = filaInicial
        columnaActual = columnaInicial
        caracterActual = codigoFuente[posicionActual]
    }

    /**
     * Método que analiza el código fuente.
     */
    fun analizar() {
        while (caracterActual != finCodigo) {
            if (caracterActual == ' ' || caracterActual == '\t' ||
                caracterActual == '\n'
            ) {

                obtenerSiguienteCaracter()
                continue
            }
            if (esNumero()) continue
            if (esIdentificadorPalabraReservada()) continue
            if (esOperadorLogicoRelacional()) continue
            if (esOperadorAritAsig()) continue
            if (esCadenaCaracteres()) continue
            if (esFinSentencia()) continue
            if (esLlave()) continue
            if (esParentesis()) continue
            if (esSeparador()) continue
            if (esCorchete()) continue

            almacenarToken(
                "" + caracterActual,
                Categoria.DESCONOCIDO, filaActual, columnaActual
            )
            obtenerSiguienteCaracter()
        }
    }

    /**
     * Método que valida si una palabra es un número decimal, la palabra debe empezar con un dígito y
     * debe estar concatenado con cualquier cantidad de dígitos.
     * Si no hay un punto (.) de por medio en la palabra lo categoriza como ENTERO (D)(D)*,
     * de lo contrario sería REAL (D)(D)*(.)(D)(D)*.
     * @return true or false
     */
    fun esNumero(): Boolean {
        if (caracterActual.isDigit()) {

            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual

            parametro += caracterActual
            obtenerSiguienteCaracter()

            while (caracterActual.isDigit()) {

                parametro += caracterActual
                obtenerSiguienteCaracter()
            }

            if (caracterActual == '.') {
                var parametroInt = parametro
                val filaInt = filaActual
                val columnaInt = columnaActual
                val posicionInt = posicionActual
                parametro += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual.isDigit()) {
                    parametro += caracterActual
                    obtenerSiguienteCaracter()
                    while (caracterActual.isDigit()) {
                        parametro += caracterActual
                        obtenerSiguienteCaracter()
                    }
                    almacenarToken(
                        parametro,
                        Categoria.REAL, filaInicial, columnaInicial
                    )
                    return true
                } else {
                    almacenarToken(
                        parametroInt,
                        Categoria.ENTERO, filaInicial, columnaInicial
                    )
                    hacerBT(posicionInt, filaInt, columnaInt)
                    return true
                }

            }
                almacenarToken(
                    parametro,
                    Categoria.ENTERO, filaInicial, columnaInicial
                )
                return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es un identificador  [(A-Z)(a-z)_,$][(A-Z)(a-z)(0-9)_,$]* o palabra reservada del lenguaje
     * (for)u(while)u(do)u(break)u(continue)u(if)u(else)u(elseIf)u(switch)u(case)u(default)u(class)u(constructor)u(public)u(private)u
     * (protected)u(static)u(extends)u(super)u(abstract)u(implements).
     * .
     * @return true or false
     */
    fun esIdentificadorPalabraReservada(): Boolean {
        if (caracterActual.isLetter() || caracterActual == '$' || caracterActual == '_') {
            var parametro = ""
            var filaInicial = filaActual
            var columnaInicial = columnaActual
            var contador = 1

            parametro += caracterActual
            obtenerSiguienteCaracter()

            if (codigoFuente[posicionActual] == ' ' || caracterActual == finCodigo) {
                almacenarToken(
                    parametro,
                    Categoria.CARACTER, filaInicial, columnaInicial
                )
                return true
            }

            while (caracterActual.isLetter() || caracterActual == '$' || caracterActual == '_' ||
                caracterActual.isDigit()
            ) {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                contador++
                if (contador == 10) break
            }
            return if (palabraReservada(parametro)) {
                almacenarToken(
                    parametro,
                    Categoria.PALABRA_RESERVADA, filaInicial, columnaInicial
                )
                true
            } else {
                almacenarToken(
                    parametro,
                    Categoria.IDENTIFICADOR, filaInicial, columnaInicial
                )
                true
            }
        }
        return false
    }

    /**
     * Método que valida si una palabra es un operador lógico (&&)u(||)u(!)
     * Relacional (<)u(>)u(<=)u(>=)u(==)u(!=).
     * Asignacion (=)u(+=)u(-=)u(*=)u(/=)u(%=)u(&=)u(^=)u(<<=)u(>>=)u(>>>=)
     * @return true or false
     */
    fun esOperadorLogicoRelacional(): Boolean {
        if (caracterActual == '!') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial
                )
                return true
            }
            almacenarToken(
                parametro,
                Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '=') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial
                )
                return true
            }
            almacenarToken(
                parametro,
                Categoria.OPERADOR_DE_ASIGNACION, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '<' || caracterActual == '>') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial
                )
                return true
            }
            almacenarToken(
                parametro,
                Categoria.OPERADOR_RELACIONAL, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '&') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '&' || caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial
                )
                return true
            }
        }
        if (caracterActual == '|') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            val posicionInicial = posicionActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '|') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_LOGICO, filaInicial, columnaInicial
                )
                return true
            } else {
                reportarError("Falta el segundo '|' para completar el operador binario (or).")
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
            }
        }

        return false
    }

    /**
     * Método que valida si una palabra es un operador aritmético (+)u(-)u(*)u(/)u(%),
     * o de asignación (=)u(+=)u(-=)u(*=)u(/=)u(%=).
     * @return true or false
     */
    fun esOperadorAritAsig(): Boolean {
        if ( caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_DE_ASIGNACION, filaInicial, columnaInicial
                )
                return true
            }
            almacenarToken(
                parametro,
                Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '+') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_DE_ASIGNACION, filaInicial, columnaInicial
                )
                return true
            }

            almacenarToken(
                parametro,
                Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '-') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            if (caracterActual == '=') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_DE_ASIGNACION, filaInicial, columnaInicial
                )
                return true
            }

            almacenarToken(
                parametro,
                Categoria.OPERADOR_ARITMETICO, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '=') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.OPERADOR_DE_ASIGNACION, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es una cadena de caracteres S={Ascii}, (")(S)*(").
     * @return true or false
     */
    fun esCadenaCaracteres(): Boolean {
        if (caracterActual == '\"') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            obtenerSiguienteCaracter()
            while (caracterActual != '\"') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == finCodigo) {
                    if (caracterActual != '\"') {

                        almacenarToken(
                            parametro,
                            Categoria.CADENA_CARACTERES_SIN_CERRAR, filaInicial, columnaInicial
                        )
                        return true
                    }
                }
            }
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.CADENA_CARACTERES, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es un paréntesis de apertura o cierre (()u()).
     * @return true or false
     */
    fun esParentesis(): Boolean {
        if (caracterActual == '(') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_APERTURA, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == ')') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_CIERRE, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es una llave de apertura o cierre ({)u(}).
     * @return true or false
     */
    fun esLlave(): Boolean {
        if (caracterActual == '{') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_APERTURA, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == '}') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_CIERRE, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es una llave de apertura o cierre ([)u(]).
     * @return true or false
     */
    fun esCorchete(): Boolean {
        if (caracterActual == '[') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_APERTURA, filaInicial, columnaInicial
            )
            return true
        }
        if (caracterActual == ']') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SIMBOLO_CIERRE, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es un terminal fin de sentencia (;).
     * @return true or false
     */
    fun esFinSentencia(): Boolean {
        if (caracterActual == ';') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.TERMINAL_FIN_SENTENCIA, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es dos puntos (:).
     * @return true or false
     */
    fun esDosPuntos(): Boolean {
        if (caracterActual == ':') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.DOS_PUNTOS, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es un separador de sentencias (,).
     * @return true or false
     */
    fun esSeparador(): Boolean {
        if (caracterActual == ',' || caracterActual == '.') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.SEPARADOR, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }


    /**
     * Método que compara si una palabra que entra como parámetro es una de las palabras reservadas del lenguaje,
     * la palabra debe coincidir exactamente en cuanto a mayúsculas y minúsculas ( for  while  do  break  continue
     * if else else+if switch case default class constructor public private protected static extends super abstract implements ).
     * @param String cadena
     * @return boolean true or false
     */
    fun palabraReservada(cadena: String): Boolean {
        return if (cadena == "for") true
        else if (cadena == "while") true
        else if (cadena == "do") true
        else if (cadena == "break") true
        else if (cadena == "continue") true
        else if (cadena == "if") true
        else if (cadena == "else") true
        else if (cadena == "elseIf") true
        else if (cadena == "switch") true
        else if (cadena == "case") true
        else if (cadena == "default") true
        else if (cadena == "class") true
        else if (cadena == "constructor") true
        else if (cadena == "public") true
        else if (cadena == "private") true
        else if (cadena == "protected") true
        else if (cadena == "static") true
        else if (cadena == "extends") true
        else if (cadena == "super") true
        else if (cadena == "abstract") true
        else if (cadena == "implements") true
        else false
    }

    init {
        listaTokens = ArrayList()
        caracterActual = codigoFuente[posicionActual]
        finCodigo = 0.toChar()
    }
}