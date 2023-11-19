package co.edu.uniquindio.compiladores.sintaxis

import co.edu.uniquindio.compiladores.lexico.Categoria
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error

class AnalizadorSintactico(var listaTokens: ArrayList<Token>) {

    var posicionActual = 0
    var tokenActual = listaTokens[posicionActual]
    var listaErrores = ArrayList<Error>()

    fun obtenerSiguienteToken() {
        posicionActual++;
        if (posicionActual < listaTokens.size) {
            tokenActual = listaTokens[posicionActual]
        }
    }

    fun reportarError(mensaje: String) {
        listaErrores.add(Error(mensaje, tokenActual.fila, tokenActual.columna))
    }

    fun hacerBT(posicion: Int) {
        posicionActual = posicion
        tokenActual = listaTokens[posicionActual]
    }

    /**
     * <UnidadCompilacion>::= <ListaFunciones>
     */
    fun esUnidadDeCompilacion(): UnidadDeCompilacion? {
        val listaFunciones: ArrayList<Funcion> = esListaFunciones()
        return if (listaFunciones.size > 0) {
            UnidadDeCompilacion(listaFunciones)
        } else null
    }

    /**
     * <ListaFunciones>::= <Funcion> [<ListaFunciones>]
     */
    fun esListaFunciones(): ArrayList<Funcion> {
        var listaFunciones = ArrayList<Funcion>()
        var funcion = esFuncion()
        while (funcion != null) {
            listaFunciones.add(funcion)
            funcion = esFuncion()
        }
        return listaFunciones
    }

    /**
     * <Funcion>::= Fun <TipoDeRetorno> nombre "(" [<ListaParametros>] ")" <BloqueSentencias>
     */
    fun esFuncion(): Funcion? {

        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Fun") {
            obtenerSiguienteToken()

            var tipoRetorno = esTipoRetorno()
            if (tipoRetorno != null) {
                obtenerSiguienteToken()

                if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                    val nombreFuncion = tokenActual
                    obtenerSiguienteToken()

                    if (tokenActual.categoria == Categoria.PARENTESIS_APERTURA) {
                        obtenerSiguienteToken()
                        val listaParametros: ArrayList<Parametro> = esListaParametros()

                        if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                            obtenerSiguienteToken()
                            val bloqueSentencias = esBloqueSentencias()
                            if (bloqueSentencias != null) {
                                return Funcion(nombreFuncion, listaParametros, tipoRetorno, bloqueSentencias)

                            } else {
                                reportarError("El bloque de sentencias está vacío.")
                            }
                        } else {
                            print(tokenActual.parametro)
                            reportarError("Falta parentesis derecho.")
                        }
                    } else {
                        reportarError("Falta parentesis izquierdo.")
                    }
                } else {
                    reportarError("Falta el identificador de la función.")
                }
            } else {
                reportarError("Falta el tipo de retorno.")
            }
        }
        return null
    }

    /**
     * <TipoDeRetorno> ::= Entero | Real | Cadena | Caracter | Binario | Vacio
     */
    fun esTipoRetorno(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual.parametro == "Entero" || tokenActual.parametro == "Real" || tokenActual.parametro == "Cadena" || tokenActual.parametro == "Caracter" || tokenActual.parametro == "Binario" || tokenActual.parametro == "Vacio") {
                return tokenActual
            }
        }
        return null
    }

    /**
     * <ListaParametros>::= <Parametro> ["," <ListaParametros>]
     */
    fun esListaParametros(): ArrayList<Parametro> {
        var listaParametros = ArrayList<Parametro>()
        var p = esParametro()

        while (p != null) {
            listaParametros.add(p)

            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                p = esParametro()
            } else {
                if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                    break
                } else {
                    break
                }
            }
        }
        return listaParametros
    }

    /**
     * <Parametro>::= Identificador ":" <TipoDeDato>
     */
    fun esParametro(): Parametro? {
        if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
            val nombre = tokenActual
            obtenerSiguienteToken()

            if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                obtenerSiguienteToken()
                val tipoDeDato = esTipoDato()

                if (tipoDeDato != null) {
                    obtenerSiguienteToken()
                    return Parametro(tipoDeDato, nombre)
                } else {
                    reportarError("Falta el tipo de dato.")
                }
            } else {
                reportarError("Falta dos puntos.")
            }
        }
        return null
    }

    /**
     * <TipoDato>::= Entero | Real | Cadena | Caracter
     */

    fun esTipoDato(): Token? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA) {
            if (tokenActual.parametro == "Entero" || tokenActual.parametro == "Real" || tokenActual.parametro == "Cadena" || tokenActual.parametro == "Caracter" || tokenActual.parametro == "Binario") {
                return tokenActual;
            }
        }
        return null
    }

    /**
     * <BloqueSentencias>::= "{" [<ListaSentencias>] "}"
     */
    fun esBloqueSentencias(): ArrayList<Sentencia>? {
        if (tokenActual.categoria == Categoria.LLAVE_APERTURA) {
            obtenerSiguienteToken()
            var listaSentencias = esListaSentencias()
            if (tokenActual.categoria == Categoria.LLAVE_CIERRE) {
                obtenerSiguienteToken()
                return listaSentencias
            }
        }
        return null
    }

    /**
     * <ListaSentencias>::= [<Sentencia>]
     */
    fun esListaSentencias(): ArrayList<Sentencia>? {
        val listaSentencias = ArrayList<Sentencia>()
        var s = esSentencia()

        while (s != null) {
            listaSentencias.add(s)
            s = esSentencia()
        }
        return listaSentencias
    }

    /**
     * <Sentencia>::= <DecisiónSimple> | <DeclaracionVariable> | <Asignacion> | <Impresion> | <Ciclo> | <Retorno> | <LecturaDatos> | <InvocarFuncion> |
     *     <Incremento> | <Decremento> | <DeclaracionVariable> | <Arreglo>
     */
    fun esSentencia(): Sentencia? {
        var s: Sentencia? = esDecisionSimnple()
        if (s != null) {
            return s
        }
        s = esAsignacion()
        if (s != null) {
            return s
        }
        s = esImpresion()
        if (s != null) {
            return s
        }
        s = esCicloMientras()
        if (s != null) {
            return s
        }
        s = esRetorno()
        if (s != null) {
            return s
        }
        /*s = esLecturaDatos()
        if (s != null){
        }*/
        s = esInvocacionFuncion()
        if (s != null) {
            return s
            s = esInvocacionFuncion()
        }
        /*s = esIncremento()
        if (s != null){
            return s
        }*/
        /*s = esDecremento()
        if (s != null){
            return s
        }*/
        s = esDeclaracionVariable()
        if (s != null) {
            return s
        }
        s = esArreglo()
        if (s != null){
            return s
        }
        return null
    }

    /**
     * <DecisionSimple>::= Si <ExpresionLogica> [<BloqueSentencias>] [Sino [<BloqueSentencias>]]
     */
    fun esDecisionSimnple(): Sentencia? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Si") {
            obtenerSiguienteToken()
            val expresion = esExpresionLogica()
            if (expresion != null) {
                hacerBT(posicionActual - 1)
                val bloqueSentenciasV = esBloqueSentencias()
                if (bloqueSentenciasV != null) {
                    if (tokenActual.parametro == "Sino") {
                        obtenerSiguienteToken()
                        val bloqueSentenciasF = esBloqueSentencias()
                        if (bloqueSentenciasF != null) {
                            return DesicionSimple(expresion, bloqueSentenciasV, bloqueSentenciasF)
                        }
                    } else {
                        return DesicionSimple(expresion, bloqueSentenciasV, null)
                    }
                }
            } else {
                reportarError("Error en condición.")
            }
        }
        return null
    }

    /**
     * <DeclaracionVariable>::= <VariableMutable> | <VariableInmutable>
     */
    fun esDeclaracionVariable(): DeclaracionDeVariable? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Var") {
            return esVariableMutable()
        }
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Val") {
            return esVariableInmutable()
        }

        return null
    }

    /**
     * <VariableMutable>::= Var <tipoDato> Identificador
     */
    fun esVariableMutable(): DeclaracionDeVariable? {
        val mutable = tokenActual
        obtenerSiguienteToken()
        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
                val identificador = tokenActual
                obtenerSiguienteToken()
                return DeclaracionDeVariable(tipoDato, mutable, identificador)
            } else {
                reportarError("Falta identificador.")
                return null
            }

        } else {
            reportarError("Falta definir tipo de dato.")
            return null
        }

    }

    /**
     * <VariableInmutable>::= Val <tipoDato> Identificador
     */
    fun esVariableInmutable(): DeclaracionDeVariable? {
        val inMutable = tokenActual
        obtenerSiguienteToken()
        val tipoDato = esTipoDato()
        if (tipoDato != null) {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
                val identificador = tokenActual
                obtenerSiguienteToken()
                return DeclaracionDeVariable(tipoDato, inMutable, identificador)
            } else {
                reportarError("Falta identificador.")
                return null
            }

        } else {
            reportarError("Falta definir tipo de dato.")
            return null
        }

    }

    /**
     * <Asignacion>::=<IdentificadorDeVariable> OpAsignacion <Expresion> | <IdentificadorDeVariable> OpAsignacion <InvocacionFuncion>
     */
    fun esAsignacion(): Asignacion? {

        if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
            var identificador = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.OPERADOR_DE_ASIGNACION) {
                var operadorAsignacion = tokenActual
                obtenerSiguienteToken()
                var invocacion = esInvocacionFuncion()
                if (invocacion != null) {
                    return Asignacion(identificador, operadorAsignacion, invocacion)
                } else {
                    val expresion = esExpresion()
                    print(expresion)
                    if (expresion != null) {
                        return Asignacion(identificador, operadorAsignacion, expresion)
                    } else {
                        reportarError("Falta expresión asignación.")
                    }
                }
            } else {
                reportarError("Falta un operador de asignación.")
            }
        }
        return null
    }

    /**
     * <CicloMientras>::= Mientras  <Expresionlogica> Haga <BloqueSentencias>
     */
    fun esCicloMientras(): CicloMientras? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Mientras") {
            obtenerSiguienteToken()
            val expresion = esExpresionLogica()
            if (expresion != null) {
                hacerBT(posicionActual - 1)
                if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Haga") {
                    obtenerSiguienteToken()
                    val bloqueSentencias = esBloqueSentencias()
                    if (bloqueSentencias != null) {
                        return CicloMientras(expresion, bloqueSentencias)
                    }
                } else {
                    reportarError("Falta la palabra reservada 'Haga'.")
                }
            } else {
                reportarError("Falta expresión lógica.")
            }
        }
        return null
    }

    /**
     *<Expresion> ::= <ExpresionAritmetica> | <ExpresionRelacional> | <ExpresionLogica> | <ExpresionCadena>
     */
    fun esExpresion(): Expresion? {

        val expresionLogica = esExpresionLogica()
        if (expresionLogica != null) {
            return expresionLogica
        }
        val expresionAritmetica = esExpresionAritmetica()
        if (expresionAritmetica != null) {
            return expresionAritmetica
        }
        val expresionRelacional = esExpresionRelacional()
        if (expresionRelacional != null) {
            return expresionRelacional
        }
        val expresionCadena = esExpresionCadena()
        if (expresionCadena != null) {
            return expresionCadena
        }
        return null
    }

    /**
     * <ExpAritmetica> ::= "("<ExpAritmetica>")" [operadorAritmetico <ExpAritmetica>] |<ValorNumerico> [operadorAritmetico <ExpAritmetica>]
     */
    fun esExpresionAritmetica(): ExpresionAritmetica? {
        if (tokenActual.categoria == Categoria.PARENTESIS_APERTURA) {
            obtenerSiguienteToken()
            val expA1 = esExpresionAritmetica()
            if (expA1 != null) {
                if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                    obtenerSiguienteToken()
                    if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                        var oPa = tokenActual

                        obtenerSiguienteToken()

                        val exPA2 = esExpresionAritmetica()
                        if (exPA2 != null) {
                            return ExpresionAritmetica(expA1, oPa, exPA2)
                        }
                    } else {

                        return ExpresionAritmetica(expA1)
                    }
                } else {
                    reportarError("Falta paréntesis de cierre.")
                }
            } else {
                reportarError("Falta expresión aritmética.")
            }
        } else {
            val valorNum = esValorNumerico()
            if (valorNum != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO) {
                    var oPa = tokenActual
                    obtenerSiguienteToken()
                    val exPA = esExpresionAritmetica()
                    if (exPA != null) {
                        obtenerSiguienteToken()
                        return ExpresionAritmetica(valorNum, oPa, exPA)
                    }
                } else {
                    print("El método esExpresionAritmetica() retornará un valor numérico: $valorNum\n")
                    return ExpresionAritmetica(valorNum)
                }
            }
        }
        return null
    }

    /**
     * <valorNumerico>::= [<signo>]Entero | [<signo>]Doble | [<signo>]IdentificadorDeVariable
     */

    fun esValorNumerico(): ValorNumerico? {

        if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && (tokenActual.parametro == "-")) {
            var signo = tokenActual
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.REAL || tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
                val termino = tokenActual
                return ValorNumerico(signo.parametro, termino)
            }
        } else {
            if (tokenActual.categoria == Categoria.ENTERO || tokenActual.categoria == Categoria.REAL || tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
                val termino = tokenActual
                return ValorNumerico("+", termino)
            }
        }
        return null
    }

    /**
     *<ExpresionLogica>::=<ExpresionLogica> operadorLogicoBin <ExpresionLogica>|operadorNegacion<ExpresionLogica> | <valorLogico>
     * <ExpresionLogica>::= operadorNegacion <ExpresionLogica> [oLogicoBin<ExpresionLogica>] | <ExpresionRelacional>[operadorLogicoBin<ExpresionLogica>]
     */
    fun esExpresionLogica(): ExpresionLogica? {

        if (tokenActual.categoria == Categoria.OPERADOR_LOGICO && (tokenActual.parametro == "!")) {
            val oLogico = tokenActual
            obtenerSiguienteToken()
            var expLogica1 = esExpresionLogica()
            if (expLogica1 != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_BINARIO && (tokenActual.parametro == "&&" || tokenActual.parametro == "||")) {
                    val oBinario = tokenActual
                    obtenerSiguienteToken()
                    val expLogica2 = esExpresionLogica()
                    if (expLogica2 != null) {
                        return ExpresionLogica(oLogico, expLogica1, oBinario, expLogica2)
                    } else {
                        reportarError("Falta la otra expresión lógica")
                    }

                } else {
                    return ExpresionLogica(oLogico, expLogica1)
                }
            } else {
                reportarError("Falta la expresión lógica ")
            }
        } else {

            val expRela = esExpresionRelacional()
            if (expRela != null) {
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.OPERADOR_BINARIO && (tokenActual.parametro == "&&" || tokenActual.parametro == "||")) {
                    val oBinario2 = tokenActual
                    obtenerSiguienteToken()
                    val expLogica3 = esExpresionLogica()
                    if (expLogica3 != null) {
                        obtenerSiguienteToken()
                        return ExpresionLogica(expRela, oBinario2, expLogica3)
                    } else {
                        reportarError("Falta la otra expresión lógica")
                    }
                } else {
                    return ExpresionLogica(expRela)
                }
            } else {

                return null
            }

        }

        return null
    }


    /**
     * <ListaExpresionesCadena> ::= ExpresionCadena ["+"<ExpresionCadena>]
     */
    fun esListaExpresionCadena(): ArrayList<ExpresionCadena> {
        val listaExpresionCadena = ArrayList<ExpresionCadena>()
        var expresionCadena = esExpresionCadena()
        while (expresionCadena != null) {
            listaExpresionCadena.add(expresionCadena)
            if (tokenActual.categoria == Categoria.OPERADOR_ARITMETICO && tokenActual.parametro == "+"){
                obtenerSiguienteToken()
                expresionCadena = esExpresionCadena()
            } else {
                if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                    break
                } else {
                    reportarError("Falta concatenar una expresión válida.")
                    break
                }
            }
        }
        return listaExpresionCadena
    }

    /**
     * <ExpresionCadena> ::= CadenaCaracteres
     */
    fun esExpresionCadena(): ExpresionCadena? {
        if (tokenActual.categoria == Categoria.CADENA_CARACTERES){
            var cadena = tokenActual.parametro
            obtenerSiguienteToken()
            return ExpresionCadena(cadena)
        }
        return null
    }

    /**
     * <Impresion>::= Imprimir "(" [<ListaExpresionesCadena>] ")"
     */
    fun esImpresion(): Impresion? {

        if (tokenActual.parametro == "Imprimir") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.PARENTESIS_APERTURA) {
                obtenerSiguienteToken()
                val listaExpresionCadena: ArrayList<ExpresionCadena> = esListaExpresionCadena()
                if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                    obtenerSiguienteToken()

                    return Impresion(listaExpresionCadena)

                } else {
                    reportarError("Falta paréntesis de cierre.")
                }

            } else {
                reportarError("Falta paréntesis de apertura.")
            }
        }
        return null
    }


    /**
     * <InvocacionFuncion>::= Invocar identificador"("<ListaArgumentos>")
     */
    fun esInvocacionFuncion(): InvocacionFuncion? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Invocar") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR) {
                var nombreFuncion = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.PARENTESIS_APERTURA) {
                    obtenerSiguienteToken()
                    val listaArgumentos: ArrayList<Argumento> = esListaArgumentos()
                    if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                        obtenerSiguienteToken()
                        return InvocacionFuncion(nombreFuncion, listaArgumentos)
                    } else {
                        reportarError("Falta paréntesis de cierre.")
                        return null
                    }
                } else {
                    reportarError("Falta paréntesis de apertura.")
                    return null
                }
            } else {
                reportarError("Falta identificador de función a invocar.")
            }
        }
        return null
    }

    /**
     * <ListaArgumentos>::= <Argumento> ["," <ListaArgumentos>]
     */
    fun esListaArgumentos(): ArrayList<Argumento> {
        var listaArgumentos = ArrayList<Argumento>()
        var a = esArgumento()
        while (a != null) {
            listaArgumentos.add(a)
            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                a = esArgumento()
            } else {
                if (tokenActual.categoria == Categoria.PARENTESIS_CIERRE) {
                    break
                } else {
                    break
                }
            }
        }
        return listaArgumentos
    }

    /**
     * <Argumento>::= [<ExpresionAritmetica>] | [<ExpresionCadena>]
     */
    fun esArgumento(): Argumento? {
        var expresionAritmetica: ExpresionAritmetica? = esExpresionAritmetica()
        if (expresionAritmetica != null) {
            return Argumento(expresionAritmetica)
        } else {
            var expresionCadena: ExpresionCadena? = esExpresionCadena()
            if (expresionCadena != null) {
                return Argumento(expresionCadena)
            }
        }
        return null
    }

    /**
     *<ExpresionRelacional> ::= <ExpresionAritmetica> OperadorRelacional <ExpresionAritmetica>
     */
    fun esExpresionRelacional(): ExpresionRelacional? {
        val expreAritmetica = esExpresionAritmetica()
        if (expreAritmetica != null) {
            print("$expreAritmetica\n")
            if (tokenActual.categoria == Categoria.OPERADOR_RELACIONAL) {
                val operador = tokenActual
                obtenerSiguienteToken()
                val expreAritmetica2 = esExpresionAritmetica()
                if (expreAritmetica2 != null) {
                    return ExpresionRelacional(expreAritmetica, operador, expreAritmetica2)
                } else {
                    reportarError("Falta expresión aritmetica.")
                }
            } else {

                return null

            }
        }
        return null
    }

    /**
     *<Retrono> ::= Retorno <Expresion> | Retorno <InvocacionFuncion> | Retorno Nulo
     */
    fun esRetorno(): Retorno? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Retorna") {
            obtenerSiguienteToken()
            var expresion: Expresion? = esExpresion()
            if (expresion != null) {
                return Retorno(expresion)
            } else {
                var invocacionFuncion = esInvocacionFuncion()
                if (invocacionFuncion != null) {
                    obtenerSiguienteToken()
                    return Retorno(invocacionFuncion)
                } else {
                    if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Nulo") {
                        var nulo = tokenActual
                        obtenerSiguienteToken()
                        return Retorno(nulo)
                    } else {
                        reportarError("Falta agregar retorno.")
                    }
                }
            }
        }
        return null
    }

    /**
     * <Arreglo> ::= Arreglo identificador ":" <TipoDato> [ "=" "["<ListaArgumentos>"]"]
     */
    fun esArreglo(): Arreglo? {
        if (tokenActual.categoria == Categoria.PALABRA_RESERVADA && tokenActual.parametro == "Arreglo") {
            obtenerSiguienteToken()
            if (tokenActual.categoria == Categoria.IDENTIFICADOR || tokenActual.categoria == Categoria.CARACTER) {
                val nombreArreglo = tokenActual
                obtenerSiguienteToken()
                if (tokenActual.categoria == Categoria.DOS_PUNTOS) {
                    obtenerSiguienteToken()
                    val tipoDato = esTipoDato()
                    if (tipoDato != null) {
                        obtenerSiguienteToken()
                        var listaExpresiones: ArrayList<Expresion>
                        if (tokenActual.categoria == Categoria.OPERADOR_DE_ASIGNACION && tokenActual.parametro == "=") {
                            obtenerSiguienteToken()
                            if (tokenActual.categoria == Categoria.CORCHETE_APERTURA) {
                                obtenerSiguienteToken()
                                listaExpresiones = esListaExpresion()
                                obtenerSiguienteToken()
                                return Arreglo(nombreArreglo, tipoDato, listaExpresiones)
                            } else {
                                reportarError("Arreglo: Falta corchete apertura.")
                            }
                        } else {
                            reportarError("Arreglo: Falta operador de asignación.")
                        }
                    } else {
                        reportarError("Arreglo: Falta definir tipo de dato.")
                    }
                } else {
                    reportarError("Arreglo: Falta dos puntos.")
                }
            } else {
                reportarError("Arreglo: Falta identificador.")
            }
        }
        return null
    }

    /**
     * <ListaExpresiones>::= <Expresion> ["," <ListaExpresiones>]
     */
    fun esListaExpresion(): ArrayList<Expresion> {
        var listaExpresiones = ArrayList<Expresion>()
        var e = esExpresion()
        print("$e\n")
        while (e != null) {
            listaExpresiones.add(e)
            if (tokenActual.categoria == Categoria.SEPARADOR) {
                obtenerSiguienteToken()
                e = esExpresion()
            } else {
                if (tokenActual.categoria == Categoria.CORCHETE_CIERRE) {
                    break
                } else {
                    break
                }
            }
        }
        return listaExpresiones
    }
}