/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.compra;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.dialog.*;
import xochiltapp.datasource.ComprasDataSource;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.model.Compra;
import xochiltapp.model.DetalleCompra;
import xochiltapp.model.Producto;
import xochiltapp.modules.inventario.SeleccionarProductoController;
import xochiltapp.utils.ticket.GeneraTicket;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class ComprarController extends BorderPane {

    @FXML
    BorderPane contenedorPBP;
    @FXML
    TextField claveProductoAgregarText;
    @FXML
    TextField cantidadProductoAgregarText;
    @FXML
    Label importeTotalLabel;
    @FXML
    Label totalArticulosLabel;
    @FXML
    Label notificacionLabel;
    @FXML
    Button registrarButton;
    @FXML
    Button cancelarButton;
    @FXML
    TableView<DetalleCompra> tablaDetallesCompra;
    @FXML
    TableColumn<DetalleCompra, Integer> ClaveCol;
    @FXML
    TableColumn<DetalleCompra, String> DescripcionCol;
    @FXML
    TableColumn<DetalleCompra, Float> precioUnitarioCol;
    @FXML
    TableColumn<DetalleCompra, Integer> cantidadCol;
    @FXML
    TableColumn<DetalleCompra, Float> importeCol;
    private Compra nuevaCompra = new Compra();
    private float importeTotal = 0;
    private String nombreVendedor;

    public void setFocus(Event ev) {
        EventHandler eventFocus = new EventHandler() {
            @Override
            public void handle(Event t) {
                claveProductoAgregarText.requestFocus();
            }
        };
        eventFocus.handle(ev);
    }

    /**
     * Initializes the controller class.
     */
    public ComprarController(String vendedor, double ancho, double alto) {
        this.nombreVendedor = vendedor;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Comprar.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);

        }

        this.contenedorPBP.setPrefSize(ancho, alto);
        this.registrarButton.setDisable(true);
        this.claveProductoAgregarText.requestFocus();

        //set columns
        ClaveCol.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("idProducto"));
        DescripcionCol.setCellValueFactory(new PropertyValueFactory<DetalleCompra, String>("concepto"));
        precioUnitarioCol.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Float>("precioUnit"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Integer>("cantidad"));
        importeCol.setCellValueFactory(new PropertyValueFactory<DetalleCompra, Float>("importe"));


        //set table content
        cargarDatosTabla();

        this.claveProductoAgregarText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    if (!claveProductoAgregarText.getText().equals("")) {
                        checkForAgregar(((Node) t.getSource()).getScene().getWindow());
                    } else {
                        //open for select producto
                        seleccionarProductoAction(((Node) t.getSource()).getScene().getWindow());
                    }
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    resetCompra();
                }
            }
        });
        this.cantidadProductoAgregarText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    checkForAgregar(((Node) t.getSource()).getScene().getWindow());
                }
            }
        });

        this.registrarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //llamar a Cobrar
                registrarCompra();
            }
        });

        this.cancelarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //cancelar venta
                resetCompra();
            }
        });

        this.tablaDetallesCompra.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.DELETE) {
                    quitarDetalle();
                }
            }
        });

    }

    @FXML
    public void seleccionarProductoAction(Window parent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SeleccionarProductoController.class.getResource("SeleccionarProducto.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Seleccionar Producto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parent);

            SeleccionarProductoController controller = fxmlLoader.getController();
            stage.showAndWait();
            if (controller.getProductoSeleccionado() != null) {
                this.claveProductoAgregarText.setText(controller.getProductoSeleccionado().getIdProducto() + "");
            } else {
                this.notificacionLabel.setText("No seleccionó ningún producto.");
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    private void checkForAgregar(Window parent) {
        if (claveProductoAgregarText.getText().equals("")) {
            this.notificacionLabel.setText("Ingrese la clave de producto.");
            this.claveProductoAgregarText.requestFocus();
            return;
        }

        //cuando se ingrese el código del producto
        int cant;

        try {
            cant = Integer.valueOf(cantidadProductoAgregarText.getText());
        } catch (Exception ex) {
            //nothing
            this.notificacionLabel.setText("La cantidad debe ser número.");
            this.cantidadProductoAgregarText.setText("1");
            this.cantidadProductoAgregarText.requestFocus();
            return;
        }

        if (!(cant > 0)) {
            this.notificacionLabel.setText("Mínimo 1 artículo.");
            this.cantidadProductoAgregarText.setText("1");
            this.cantidadProductoAgregarText.requestFocus();
            return;
        }

        AgregarProducto(claveProductoAgregarText.getText(), cant, parent);
    }

    private void AgregarProducto(String codigo, int cantidad, Window parent) {
        //agregar a detalles
        int claveProducto = 0;
        try {
            claveProducto = Integer.valueOf(codigo);
        } catch (Exception ex) {
            //System.out.println("Error al identificar clave del producto como int: " + codigo);
            this.notificacionLabel.setText("Ingrese un código de producto.");
            this.claveProductoAgregarText.setText("");
            claveProductoAgregarText.requestFocus();
        }

        if (claveProducto == 0) {
            //notificar al usuario
            return;
        }
        DetalleCompra nuevoDetalle = new DetalleCompra();

        Producto productoAgregar = InventarioDataSource.findProductoById(claveProducto);

        if (productoAgregar != null && productoAgregar.getIdProducto() > 0) {

            //preguntar por precio
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(PrecioCompraProductoController.class.getResource("PrecioCompraProducto.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setTitle("Establecer precio del Proveedor");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(parent);
                PrecioCompraProductoController controllerPrecio = fxmlLoader.getController();

                controllerPrecio.setProducto(productoAgregar);
                stage.showAndWait();
                if (controllerPrecio.getPrecioProducto() != 0) {
                    nuevoDetalle.setPrecioUnit(controllerPrecio.getPrecioProducto());
                } else {
                    this.notificacionLabel.setText("No se ingresó precio de compra.");
                    this.claveProductoAgregarText.setText("");
                    this.cantidadProductoAgregarText.setText("1");
                    this.claveProductoAgregarText.requestFocus();
                    return;
                }

            } catch (Exception ex) {
                this.notificacionLabel.setText("Error al solicitar precio de compra.");
                return;
            }

            //System.out.println("Producto a agregar: " + productoAgregar.getDescripcion());
            nuevoDetalle.setProducto(productoAgregar);
            nuevoDetalle.setCantidad(cantidad);
        } else {
            this.notificacionLabel.setText("No se encontró el artículo.");
            this.claveProductoAgregarText.setText("");
            return;
        }

        //agregar a detalles
        nuevaCompra.addDetalle(nuevoDetalle);


        this.claveProductoAgregarText.setText("");
        this.cantidadProductoAgregarText.setText("1");
        this.claveProductoAgregarText.requestFocus();

        this.registrarButton.setDisable(false);
        this.notificacionLabel.setText("");
        calculaTotales();
        cargarDatosTabla();
    }

    private void registrarCompra() {
        try {
            this.nuevaCompra.setEmpleado(nombreVendedor);
            this.nuevaCompra.setFecha(new Date());
            if (this.nuevaCompra.getDetallesCompra().isEmpty()) {
                this.notificacionLabel.setText("La Compra no incluye productos.");
                this.claveProductoAgregarText.requestFocus();
                return;
            }

            //preguntar
            if (!FXDialog.showConfirmDialog("¿Confirma registrar\nla compra?", "Confirmar registro", ConfirmationType.YES_NO_OPTION)) {
                this.notificacionLabel.setText("Puede continuar editando.");
                this.claveProductoAgregarText.requestFocus();
                return;
            }

            //Go to Save Venta
            this.nuevaCompra = ComprasDataSource.saveCompra(nuevaCompra);

            long idCompra = nuevaCompra.getIdCompra();
            //print ticket
            if (GeneraTicket.TicketCompra(nuevaCompra)) {
                resetCompra();
                this.notificacionLabel.setText("La Compra " + idCompra + " Ha sido registrada.");
            } else {
                resetCompra();
                this.notificacionLabel.setText("Ocurrió un error al imprimir el Ticket de la compra: " + idCompra);

            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " :: " + ex.getCause() + " || " + ex.getStackTrace());
        }
    }

    private void cargarDatosTabla() {
        this.tablaDetallesCompra.setItems(FXCollections.observableList(this.nuevaCompra.getDetallesCompra()));
    }

    private void calculaTotales() {
        int totalArticulos = 0;
        this.importeTotal = 0;
        for (DetalleCompra item : nuevaCompra.getDetallesCompra()) {
            this.importeTotal += item.getImporte();
            this.nuevaCompra.setImporte(this.importeTotal);
            totalArticulos += item.getCantidad();
        }

        this.importeTotalLabel.setText(DecimalFormat.getCurrencyInstance().format(importeTotal));
        this.totalArticulosLabel.setText(totalArticulos + "");
    }

    private void resetCompra() {
        //nothing
        this.nuevaCompra = new Compra();
        this.importeTotal = 0;
        cargarDatosTabla();
        calculaTotales();
        this.claveProductoAgregarText.setText("");
        this.claveProductoAgregarText.requestFocus();
        this.registrarButton.setDisable(true);
    }

    private void quitarDetalle() {
        if (this.tablaDetallesCompra.getSelectionModel().getSelectedItem() != null) {
            this.nuevaCompra.getDetallesCompra().remove(this.tablaDetallesCompra.getSelectionModel().getSelectedItem());
            this.cargarDatosTabla();
            this.calculaTotales();
        }
    }
}