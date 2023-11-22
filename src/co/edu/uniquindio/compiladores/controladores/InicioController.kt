package co.edu.uniquindio.compiladores.controladores

import co.edu.uniquindio.compiladores.lexico.AnalizadorLexico
import co.edu.uniquindio.compiladores.lexico.Token
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.cell.PropertyValueFactory
import java.net.URL
import java.util.*
import javafx.fxml.Initializable
import javafx.scene.control.*

class InicioController : Initializable {
    @FXML lateinit var tablaTokens: TableView<Token>
    @FXML lateinit var codigoFuente: TextArea
    @FXML lateinit var colLexema: TableColumn<Token, String>
    @FXML lateinit var colCategoria: TableColumn<Token, String>
    @FXML lateinit var colFila: TableColumn<Token, Int>
    @FXML lateinit var colColumna: TableColumn<Token, Int>
    @FXML lateinit var arbolVisual: TreeView<String>
    private var lexico:AnalizadorLexico? = null


    override fun initialize(location: URL?, resources: ResourceBundle?) {
        colLexema.cellValueFactory = PropertyValueFactory("parametro")
        colCategoria.cellValueFactory = PropertyValueFactory("categoria")
        colFila.cellValueFactory = PropertyValueFactory("fila")
        colColumna.cellValueFactory = PropertyValueFactory("columna")

    }

    @FXML
    fun analizarCodigo(e: ActionEvent) {

        if (codigoFuente.text.length > 0) {

            lexico = AnalizadorLexico(codigoFuente.text)
            lexico!!.analizar()
            tablaTokens.items = FXCollections.observableArrayList(lexico!!.listaTokens)

            print(lexico!!.listaTokens)
            print("\n")


        }
    }

}

