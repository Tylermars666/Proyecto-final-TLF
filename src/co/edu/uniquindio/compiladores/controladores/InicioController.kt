package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Token
import co.edu.uniquindio.compiladores.lexico.Error
/*import co.edu.uniquindio.compiladores.semantica.AnalizadorSemantico
import co.edu.uniquindio.compiladores.sintaxis.AnalizadorSintactico
import co.edu.uniquindio.compiladores.sintaxis.UnidadDeCompilacion*/
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.io.File
import java.lang.Exception

class InicioController : Initializable {
    @FXML lateinit var tablaTokens: TableView<Token>
    @FXML lateinit var tablaErroresL: TableView<Error>
    @FXML lateinit var tablaErroresSin: TableView<Error>
    @FXML lateinit var tablaErroresSem: TableView<Error>
    @FXML lateinit var mensajeError: TableColumn<Error, String>
    @FXML lateinit var filaError: TableColumn<Error, Int>
    @FXML lateinit var columnaError: TableColumn<Error, Int>
    @FXML lateinit var mensajeErrorSin: TableColumn<Error, String>
    @FXML lateinit var filaErrorSin: TableColumn<Error, Int>
    @FXML lateinit var columnaErrorSin: TableColumn<Error, Int>
    @FXML lateinit var mensajeErrorSem: TableColumn<Error, String>
    @FXML lateinit var filaErrorSem: TableColumn<Error, Int>
    @FXML lateinit var columnaErrorSem: TableColumn<Error, Int>
    @FXML lateinit var codigoFuente: TextArea
    @FXML lateinit var colLexema: TableColumn<Token, String>
    @FXML lateinit var colCategoria: TableColumn<Token, String>
    @FXML lateinit var colFila: TableColumn<Token, Int>
    @FXML lateinit var colColumna: TableColumn<Token, Int>
    @FXML lateinit var arbolVisual: TreeView<String>
    /*private var uc:UnidadDeCompilacion? = null*/
    private var lexico:AnalizadorLexico? = null
    /*private var sintaxis:AnalizadorSintactico? = null
    private var semantica:AnalizadorSemantico? = null*/

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        colLexema.cellValueFactory = PropertyValueFactory("parametro")
        colCategoria.cellValueFactory = PropertyValueFactory("categoria")
        colFila.cellValueFactory = PropertyValueFactory("fila")
        colColumna.cellValueFactory = PropertyValueFactory("columna")
        /* mensajeError.cellValueFactory = PropertyValueFactory("error")
        filaError.cellValueFactory = PropertyValueFactory("fila")
        columnaError.cellValueFactory = PropertyValueFactory("columna")
        mensajeErrorSin.cellValueFactory = PropertyValueFactory("error")
        filaErrorSin.cellValueFactory = PropertyValueFactory("fila")
        columnaErrorSin.cellValueFactory = PropertyValueFactory("columna")
        mensajeErrorSem.cellValueFactory = PropertyValueFactory("error")
        filaErrorSem.cellValueFactory = PropertyValueFactory("fila")
        columnaErrorSem.cellValueFactory = PropertyValueFactory("columna") */
    }

    @FXML
    fun analizarCodigo(e: ActionEvent) {

        if (codigoFuente.text.length > 0) {

            lexico = AnalizadorLexico(codigoFuente.text)
            lexico!!.analizar()
            tablaTokens.items = FXCollections.observableArrayList(lexico!!.listaTokens)
            // tablaErroresL.items = FXCollections.observableArrayList(lexico!!.listaErrores)
            print(lexico!!.listaTokens)
            print("\n")

            /* if (lexico!!.listaErrores.isEmpty()) {

                sintaxis = AnalizadorSintactico(lexico!!.listaTokens)
                uc = sintaxis!!.esUnidadDeCompilacion()
                tablaErroresSin.items = FXCollections.observableArrayList(sintaxis!!.listaErrores)
                if (uc != null) {
                    arbolVisual.root = uc!!.getArbolVisual()
                    semantica = AnalizadorSemantico(uc!!)
                    semantica!!.llenarTablaSimbolos()
                    print(semantica!!.tablaSimbolos)
                    semantica!!.analizarSemantica()
                    print(semantica!!.listaErrores)
                    tablaErroresSem.items = FXCollections.observableArrayList(semantica!!.listaErrores)
                }
            } else {
                var alert = Alert(Alert.AlertType.WARNING)
                alert.headerText = "Error"
                alert.contentText = "Hay errores léxicos en el código."
            } */
        }
    }

    /*@FXML
    fun traducirCodigo(e:ActionEvent){

        //Porción de código para ejecutar traducciones de prueba sin tener en cuenta errores léxicos, sintácticos o semánticos.
*//*        val codigo = uc!!.getJavaCode()
        print(codigo)*//*

        var alerta = Alert(Alert.AlertType.NONE)
        if (uc != null && lexico!!.listaErrores.isEmpty() && sintaxis!!.listaErrores.isEmpty() && semantica!!.listaErrores.isEmpty()) {
            val codigo = uc!!.getJavaCode()
            val ruta = "src/co/edu/uniquindio/compiladores/Principal.java"
            File(ruta).writeText(codigo)
            alerta = Alert(Alert.AlertType.INFORMATION)
            alerta.headerText = null
            alerta.contentText = "El código fue traducido con éxito y el archivo está en la ruta:\n$ruta"
            alerta.show()
            try {
                val runtime = Runtime.getRuntime().exec("javac $ruta")
                runtime.waitFor()
                Runtime.getRuntime().exec("java Principal", null, File("src/co/edu/uniquindio/compiladores"))
            } catch (ea : Exception) {
                ea.printStackTrace()
            }
        } else {
            alerta = Alert(Alert.AlertType.ERROR)
            alerta.headerText = null
            alerta.contentText = "El código no se puede traducir porque tiene errores."
            alerta.show()
        }
    }*/
}

