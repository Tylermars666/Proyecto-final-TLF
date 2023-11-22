package co.edu.uniquindio.compiladores.lexico

enum class Categoria {

        //valor de asignación
        ENTERO,
        REAL,
        CARACTER,
        IDENTIFICADOR,
        CADENA_CARACTERES,
        CADENA_CARACTERES_SIN_CERRAR,
        OPERADOR_ARITMETICO,
        PALABRA_RESERVADA,
        OPERADOR_RELACIONAL,
        OPERADOR_LOGICO,
        OPERADOR_DE_ASIGNACION,
        SIMBOLO_APERTURA,
        SIMBOLO_CIERRE,
        TERMINAL_FIN_SENTENCIA,
        DOS_PUNTOS,
        SEPARADOR,
        COMENTARIO_DE_LINEA,
        COMENTARIO_DE_BLOQUE,
        DESCONOCIDO
}