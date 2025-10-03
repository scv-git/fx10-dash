package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import co.edu.uniquindio.fx10.modelo.Producto;
import co.edu.uniquindio.fx10.repositorio.ProductoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
// ⚠️ Quitar import java.awt.event.ActionEvent;
// (Si quieres usar evento, usa javafx.event.ActionEvent)

import java.io.IOException;

public class ListaProductosController {

    @FXML private Button btnDevolver;
    @FXML private Button btnEliminar;

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colCodigo;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colDescripcion;
    @FXML private TableColumn<Producto, Double> colPrecio;
    @FXML private TableColumn<Producto, Integer> colStock;

    @FXML private VBox contenedorPrincipal;
    @FXML private Label lblTitulo;

    private ProductoRepository productoRepository;
    private ObservableList<Producto> productosObservable;

    @FXML
    private void onDevolver() {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("/co/edu/uniquindio/fx10/vista/Dashboard.fxml"));
            contenedorPrincipal.getScene().setRoot(root);
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR,
                    "No fue posible volver al Dashboard: " + e.getMessage(),
                    ButtonType.OK).showAndWait();
        }
    }

    @FXML
    private void onEliminarProducto() {
        Producto seleccionado = tablaProductos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Advertencia", "Por favor seleccione un producto para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Está seguro de eliminar el producto?");
        confirmacion.setContentText("Producto: " + seleccionado.getNombre());

        confirmacion.showAndWait().ifPresent(respuesta -> {
            if (respuesta == ButtonType.OK) {
                try {
                    productoRepository.eliminarProducto(seleccionado);
                    // refrescar vista: elimina solo el seleccionado
                    productosObservable.remove(seleccionado);
                    mostrarAlerta("Éxito", "Producto eliminado correctamente.", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error", "No fue posible eliminar el producto: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    public void initialize() {
        productoRepository = ProductoRepository.getInstancia();

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colPrecio.setCellFactory(column -> new TableCell<Producto, Double>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : String.format("$%,.2f", value));
            }
        });

        productosObservable = FXCollections.observableArrayList(productoRepository.getProductos());
        tablaProductos.setItems(productosObservable);

        // UX: deshabilita “Eliminar” si no hay selección
        if (btnEliminar != null) {
            btnEliminar.disableProperty().bind(
                    tablaProductos.getSelectionModel().selectedItemProperty().isNull()
            );
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarProductos() {
        productosObservable.setAll(productoRepository.getProductos());
    }
}





