package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DashboardController {

    @FXML private VBox contenedorPrincipal;
    @FXML private Label lblTitulo;

    @FXML
    public void initialize() {
        // Nada específico por ahora
    }

    @FXML
    private void onIrListadoProducto() {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("/co/edu/uniquindio/fx10/vista/ListaProductos.fxml"));
            contenedorPrincipal.getScene().setRoot(root);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "No fue posible abrir ListaProductos: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void onIrFormularioProducto() {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("/co/edu/uniquindio/fx10/vista/FormularioProducto.fxml"));
            contenedorPrincipal.getScene().setRoot(root);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "No fue posible abrir FormularioProducto: " + e.getMessage(), ButtonType.OK).showAndWait();
        }
    }
}
