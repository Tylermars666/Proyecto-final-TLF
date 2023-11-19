package co.edu.uniquindio.compiladores.lexico

class Token(var parametro: String, var categoria: Categoria, var fila: Int, var columna: Int) {

    override fun toString(): String {
        return ("\nToken [Parametro: " + parametro + ", categoria: " + categoria + ", fila: " + fila + ", columna: " + columna
                + "]")
    }

    init {
        this.categoria = categoria
        this.fila = fila
        this.columna = columna
    }

    fun getJavaCode(): String {
        if (categoria == Categoria.PALABRA_RESERVADA) {
            when (parametro) {
                "for" -> {
                    return "for"
                }
                "while" -> {
                    return "while"
                }
                "do" -> {
                    return "do"
                }
                "break" -> {
                    return "break"
                }
                "continue" -> {
                    return "continue"
                }
                "if" -> {
                    return "if"
                }
                "else" -> {
                    return "else"
                }
                "elseIf" -> {
                    return "elseIf"
                }
                "switch" -> {
                    return "switch"
                }
                "case" -> {
                    return "case"
                }
                "default" -> {
                    return "default"
                }
                "class" -> {
                    return "class"
                }
                "constructor" -> {
                    return "constructor"
                }
                "public" -> {
                    return "public"
                }
                "private" -> {
                    return "private"
                }
                "protected" -> {
                    return "protected "
                }
                "static" -> {
                    return "static"
                }
                "extends" -> {
                    return "extends"
                }
                "super" -> {
                    return "super"
                }
                "abstract" -> {
                    return "abstract"
                }
                "implements" -> {
                    return "implements"
                }
            }
        } else if (categoria == Categoria.COMENTARIO_DE_LINEA) {
            return parametro.replace("#","//")
        } else if (categoria == Categoria.COMENTARIO_DE_BLOQUE) {
            parametro.replaceFirst("@","/*")
            parametro.replace("@","*/")
            return parametro
        }
        return parametro
    }

}