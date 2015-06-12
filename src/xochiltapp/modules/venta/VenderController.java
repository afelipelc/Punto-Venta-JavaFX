/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.venta;

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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.datasource.VentasDataSource;
import xochiltapp.model.DetalleVenta;
import xochiltapp.model.Producto;
import xochiltapp.model.Venta;
import xochiltapp.modules.inventario.SeleccionarProductoController;
import xochiltapp.utils.ticket.GeneraTicket;
import xochiltapp.dialog.*;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class VenderController extends BorderPane {

    //InventarioDataSource inventarioDS = new InventarioDataSource();
    @FXML
    BorderPane contenedorPBP;
    @FXML
    TextField claveProductoAgregarText;
    @FXML
    TextField cantidadProductoAgregarText;
    @FXML
    TextField suPagoText;
    @FXML
    Label importeTotalLabel;
    @FXML
    Label suCambioLabel;
    @FXML
    Label totalArticulosLabel;
    @FXML
    Label notificacionLabel;
    @FXML
    Button cobrarButton;
    @FXML
    Button cancelarButton;
    @FXML
    TableView<DetalleVenta> tablaDetallesVenta;
    @FXML
    TableColumn<DetalleVenta, Integer> ClaveCol;
    @FXML
    TableColumn<DetalleVenta, String> DescripcionCol;
    @FXML
    TableColumn<DetalleVenta, Float> precioUnitarioCol;
    @FXML
    TableColumn<DetalleVenta, Integer> cantidadCol;
    @FXML
    TableColumn<DetalleVenta, Float> importeCol;
    //necesary vars
    private Venta nuevaVenta = new Venta();
    private String codigoIngreso = "";
    private float importeTotal = 0;
    private float suPago;
    private float suCambio;
    private String nombreVendedor;

    
    public void setFocus(Event ev)
    {
        EventHandler eventFocus = new EventHandler() {
            @Override
            public void handle(Event t) {
                claveProductoAgregarText.requestFocus();
            }
        };
        eventFocus.handle(ev);
    }
    
    
    public VenderController(String vendedor, double ancho, double alto) {
        this.nombreVendedor = vendedor;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Vender.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);

        }
        this.contenedorPBP.setPrefSize(ancho, alto);
        this.cobrarButton.setDisable(true);
        this.claveProductoAgregarText.requestFocus();
        //check for caja
        //set columns
        ClaveCol.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Integer>("idProducto"));
        DescripcionCol.setCellValueFactory(new PropertyValueFactory<DetalleVenta, String>("concepto"));
        precioUnitarioCol.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Float>("precioUnit"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Integer>("cantidad"));
        importeCol.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Float>("importe"));
        //set table content
        cargarDatosTabla();
         this.claveProductoAgregarText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    if (!claveProductoAgregarText.getText().equals("")) {
                        checkForAgregar();
                    } else {
                        //open for select producto
                        seleccionarProductoAction(((Node) t.getSource()).getScene().getWindow());
                    }
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    resetVenta();
                }
            }
        });

        this.cantidadProductoAgregarText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    checkForAgregar();
                }
            }
        });

        this.suPagoText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    calcularCambio(true);
                } else {
                    calcularCambio(false);
                }
            }
        });

        this.cobrarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //llamar a Cobrar
                realizarCobro();
            }
        });

        this.cancelarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                //cancelar venta
                resetVenta();
            }
        });

        this.tablaDetallesVenta.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
            //System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
            this.notificacionLabel.setText("Error al abrir ventana p/seleccionar producto.");
        }
    }

    private void checkForAgregar() {
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
        AgregarProducto(claveProductoAgregarText.getText(), cant);
    }

    private void AgregarProducto(String codigo, int cantidad) {
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
        DetalleVenta nuevoDetalle = new DetalleVenta();
        Producto productoAgregar = InventarioDataSource.findProductoById(claveProducto);
        if (productoAgregar != null && productoAgregar.getIdProducto() > 0) {
            //System.out.println("Producto a agregar: " + productoAgregar.getDescripcion());
            nuevoDetalle.setProducto(productoAgregar);
            nuevoDetalle.setCantidad(cantidad);
        } else {
            this.notificacionLabel.setText("No se encontró el artículo.");
            this.claveProductoAgregarText.setText("");
            return;
        }
        //agregar a detalles
        nuevaVenta.addDetalle(nuevoDetalle);
        this.claveProductoAgregarText.setText("");
        this.cantidadProductoAgregarText.setText("1");
        this.claveProductoAgregarText.requestFocus();
        this.cobrarButton.setDisable(true);
        this.suPagoText.setText("");
        this.notificacionLabel.setText("");
        calculaTotales();
        calcularCambio(false);
        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        //this.tablaDetallesVenta.getItems().clear();
        //if(this.tablaDetallesVenta.getItems() != null)
        //    this.tablaDetallesVenta.setItems(null);
        //detallesVenta = FXCollections.observableList(this.nuevaVenta.getDetallesVenta());

        //this.tablaDetallesVenta.setItems(detallesVenta);
        this.tablaDetallesVenta.setItems(FXCollections.observableList(this.nuevaVenta.getDetallesVenta()));
    }

    private void calculaTotales() {
        int totalArticulos = 0;
        this.importeTotal = 0;
        for (DetalleVenta item : nuevaVenta.getDetallesVenta()) {
            this.importeTotal += item.getImporte();
            this.nuevaVenta.setImporte(this.importeTotal);
            totalArticulos += item.getCantidad();
        }

        this.importeTotalLabel.setText(DecimalFormat.getCurrencyInstance().format(importeTotal));
        this.totalArticulosLabel.setText(totalArticulos + "");
    }

    private void calcularCambio(boolean EnterPresed) {
        this.cobrarButton.setDisable(true);
        this.suCambioLabel.setText("");
        this.suCambio = 0;
        this.suPago = 0;
        if (this.suPagoText.getText().equals("")) {
            //this.notificacionLabel.setText("Ingrese el pago del Cliente.");
            return;
        }

        try {
            this.suPago = Float.valueOf(this.suPagoText.getText());
            if (suPago >= this.importeTotal) {
                this.suCambio = this.suPago - this.importeTotal;
                this.suCambioLabel.setText(DecimalFormat.getCurrencyInstance().format(this.suCambio));
                this.cobrarButton.setDisable(false);
                this.notificacionLabel.setText("");
                if (EnterPresed) {
                    this.cobrarButton.requestFocus();
                }
            } else {
                //informar al usuario
                this.notificacionLabel.setText("El pago del Cliente no cubre el Importe.");
            }
        } catch (Exception ex) {
            FXDialog.showMessageDialog("Error al convertir a float: " + this.suPagoText.getText(), "Conversión", Message.INFORMATION);
            this.suPagoText.setText("");
            this.notificacionLabel.setText("Ingrese el pago en NÚMEROS");
        }
    }

    private void realizarCobro() {
        try {
            this.nuevaVenta.setEmpleado(nombreVendedor);
            this.nuevaVenta.setFecha(new Date());
            if (this.nuevaVenta.getDetallesVenta().isEmpty()) {
                this.notificacionLabel.setText("La Venta no incluye productos.");
                this.claveProductoAgregarText.requestFocus();
                return;
            }
            //nothing
            if (this.suPago < this.importeTotal) {
                this.notificacionLabel.setText("El pago del cliente no cubre el importe.");
                this.suPagoText.requestFocus();
                return;
            }
            //Go to Save Venta
            this.nuevaVenta = VentasDataSource.saveVenta(nuevaVenta);
            long idVenta = nuevaVenta.getIdVenta();
            //print ticket
            if (GeneraTicket.TicketVenta(nuevaVenta, suPago, suCambio)) {
                resetVenta();
                this.notificacionLabel.setText("La venta " + idVenta + " Ha sido registrada.");
            } else {
                resetVenta();
                this.notificacionLabel.setText("Ocurrió un error al imprimir el Ticket de la venta: " + idVenta);
            }
        } catch (Exception ex) {
          FXDialog.showMessageDialog("Error: " + ex.getMessage() + " :: " + ex.getCause() + " || " + ex.getStackTrace(), "Venta", Message.ERROR);
        }
    }

    private void resetVenta() {
        //nothing
        this.nuevaVenta = new Venta();
        this.suPagoText.setText("");
        this.suPago = 0;
        this.importeTotal = 0;
        cargarDatosTabla();
        calculaTotales();
        calcularCambio(false);
        this.claveProductoAgregarText.setText("");
        this.claveProductoAgregarText.requestFocus();
    }

    private void quitarDetalle() {
        if (this.tablaDetallesVenta.getSelectionModel().getSelectedItem() != null) {
            this.nuevaVenta.getDetallesVenta().remove(this.tablaDetallesVenta.getSelectionModel().getSelectedItem());
            this.cargarDatosTabla();
            this.calculaTotales();
            this.calcularCambio(false);
        }
    }
}
