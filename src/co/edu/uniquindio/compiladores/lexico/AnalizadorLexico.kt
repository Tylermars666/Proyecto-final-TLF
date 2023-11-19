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
            if (esOperadorAritIncreDecreAsig()) continue
            if (esCadenaCaracteres()) continue
            if (esComentarioDeBloque()) continue
            if (esComentarioDeLinea()) continue
            if (esFinSentencia()) continue
            if (esDosPuntos()) continue
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
     * Método que valida si una palabra es un identificador ($u_uL)($u_uLuD)* o palabra reservada del lenguaje
     * (Entero)u(Real)u(Para)u(Mientras)u(Private)u(Public)u(Paquete)u(Importar)u(Clase)u(Return)u(Break).
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
     * Método que valida si una palabra es un operador lógico (!)u(&&)u(||)
     * o relacional (!=)u(==)u(<)u(>)u(<=)u(>=).
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
            if (caracterActual == '&') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_BINARIO, filaInicial, columnaInicial
                )
                return true
            } else {
                reportarError("Falta el segundo '&' para completar el operador binario (and).")
                hacerBT(posicionInicial, filaInicial, columnaInicial)
                return false
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
                    Categoria.OPERADOR_BINARIO, filaInicial, columnaInicial
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
     * de incremento (++), decremento (--) o de asignación (=)u(+=)u(-=)u(*=)u(/=)u(%=).
     * @return true or false
     */
    fun esOperadorAritIncreDecreAsig(): Boolean {
        if (caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {
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
            if (caracterActual == '+') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_DE_INCREMENTO, filaInicial, columnaInicial
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
            if (caracterActual == '-') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                almacenarToken(
                    parametro,
                    Categoria.OPERADOR_DE_DECREMENTO, filaInicial, columnaInicial
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
                        reportarError("Falta cerrar cadena de caracteres.")
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
                Categoria.PARENTESIS_APERTURA, filaInicial, columnaInicial
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
                Categoria.PARENTESIS_CIERRE, filaInicial, columnaInicial
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
                Categoria.LLAVE_APERTURA, filaInicial, columnaInicial
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
                Categoria.LLAVE_CIERRE, filaInicial, columnaInicial
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
                Categoria.CORCHETE_APERTURA, filaInicial, columnaInicial
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
                Categoria.CORCHETE_CIERRE, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que valida si una palabra es un fin de sentencia (;).
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
     * Método que valida si una palabra es un separador (,).
     * @return true or false
     */
    fun esSeparador(): Boolean {
        if (caracterActual == ',') {
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
     * Método que valida si una palabra (línea de código) es un comentario S={Ascii}, (#)(S)*(\n).
     * @return true or false
     */
    fun esComentarioDeLinea(): Boolean {
        if (caracterActual == '#') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            while (filaInicial == filaActual) {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == finCodigo) break
            }
            almacenarToken(
                parametro,
                Categoria.COMENTARIO_DE_LINEA, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    fun esComentarioDeBloque(): Boolean {
        if (caracterActual == '@') {
            var parametro = ""
            val filaInicial = filaActual
            val columnaInicial = columnaActual
            parametro += caracterActual
            obtenerSiguienteCaracter()
            while (caracterActual != '@') {
                parametro += caracterActual
                obtenerSiguienteCaracter()
                if (caracterActual == finCodigo) {
                    if (caracterActual != '@') {
                        reportarError("Falta cerrar comentario de bloque.")
                        almacenarToken(
                            parametro,
                            Categoria.COMENTARIO_DE_BLOQUE_SIN_CERRAR, filaInicial, columnaInicial
                        )
                        return true
                    }
                }
            }
            parametro += caracterActual
            obtenerSiguienteCaracter()
            almacenarToken(
                parametro,
                Categoria.COMENTARIO_DE_BLOQUE, filaInicial, columnaInicial
            )
            return true
        }
        return false
    }

    /**
     * Método que compara si una palabra que entra como parámetro es una de las palabras reservadas del lenguaje,
     * la palabra debe coincidir exactamente en cuanto a mayúsculas y minúsculas ("Entero", "Real",
     * "Para", "Mientras", "Privado", Returna", "Break").
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
        else if (cadena == "elseif") true
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