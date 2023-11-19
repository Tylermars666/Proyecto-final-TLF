package co.edu.uniquindio.compiladores.lexico

fun main (){
    val lexico =
        AnalizadorLexico(" + - * / % abcdefghijKlmnopqrstuvxyz < > <= >= == != &&  ||   !  =   +=   -=  *=  /=  %=  &=   ^=   <<=   >>=   >>>= { [ “  } ]” for  while  do  break  continue if else else+if switch case default class constructor public private protected static extends super abstract implements \"prueba\"")
    lexico.analizar()
    print(lexico.listaTokens)
}