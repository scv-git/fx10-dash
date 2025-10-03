package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import co.edu.uniquindio.fx10.modelo.Producto;
import co.edu.uniquindio.fx10.repositorio.ProductoRepository;
import javafx.event.ActionEvent;          // ✅ Import correcto
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FormularioProductoController {

    private ProductoRepository productoRepository;

    @FXML private TextField txtCodigo, txtNombre, txtDescripcion, txtPrecio, txtStock;

    public FormularioProductoController() {
        // defensa por si initialize() no corre
        this.productoRepository = ProductoRepository.getInstancia();
    }

    @FXML
    public void initialize() {
        if (productoRepository == null) productoRepository = ProductoRepository.getInstancia();
        // Si usas contenedorPrincipal, valida su inyección:
        // assert contenedorPrincipal != null : "fx:id contenedorPrincipal no inyectado";
    }

    @FXML
    private void onCancelar(ActionEvent event) {      // ← Debe existir tal cual
        irALista(event);
    }

    @FXML
    private void onGuardarProducto(ActionEvent event) {  // ← Debe existir tal cual
        if (!validarCampos()) return;

        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());

            Producto existente = productoRepository.buscarPorCodigo(codigo);
            if (existente != null) {
                mostrar("Código repetido",
                        "Ya existe un producto con el código " + codigo, Alert.AlertType.WARNING);
                return;
            }

            productoRepository.agregarProducto(new Producto(codigo, nombre, descripcion, precio, stock));
            mostrar("Éxito", "Producto guardado correctamente", Alert.AlertType.INFORMATION);
            irALista(event);
        } catch (NumberFormatException nfe) {
            mostrar("Datos inválidos", "Precio y stock deben ser numéricos", Alert.AlertType.ERROR);
        } catch (Exception e) {
            mostrar("Error", "No fue posible guardar: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void irALista(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(
                    App.class.getResource("/co/edu/uniquindio/fx10/vista/Dashboard.fxml"));
            ((Node) event.getSource()).getScene().setRoot(root);
        } catch (IOException e) {
            mostrar("Error", "No fue posible volver al Dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        return !txtCodigo.getText().trim().isEmpty()
                && !txtNombre.getText().trim().isEmpty()
                && !txtDescripcion.getText().trim().isEmpty()
                && !txtPrecio.getText().trim().isEmpty()
                && !txtStock.getText().trim().isEmpty();
    }

    private void mostrar(String t, String m, Alert.AlertType tipo) {
        new Alert(tipo, m, ButtonType.OK).showAndWait();
    }
}
