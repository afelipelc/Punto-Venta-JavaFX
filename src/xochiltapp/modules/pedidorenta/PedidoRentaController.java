/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.pedidorenta;

import xochiltapp.modules.inventario.SeleccionarProductoController;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.dialog.*;
import xochiltapp.datasource.ClientesDataSource;
import xochiltapp.datasource.InventarioDataSource;
import xochiltapp.datasource.PedidosRentasDataSource;
import xochiltapp.model.Cliente;
import xochiltapp.model.DetallePedidoRenta;
import xochiltapp.model.PedidoRenta;
import xochiltapp.model.Producto;
import xochiltapp.utils.ticket.GeneraTicket;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class PedidoRentaController extends BorderPane {

    //InventarioDataSource inventarioDS = new InventarioDataSource();
    @FXML
    BorderPane contenedorPBP;
    @FXML
    TitledPane DatosPedidoTP;
    @FXML
    TitledPane totalesTP;
    @FXML
    TitledPane datosClienteTP;
    @FXML
    HBox fechaHoraEntregaHB;
    @FXML
    VBox datosClienteVB;
    @FXML
    TextField claveProductoAgregarText;
    @FXML
    TextField cantidadProductoAgregarText;
    @FXML
    TextField suAbonoText;
    @FXML
    Label suAdeudoLabel;
    @FXML
    Label importeTotalLabel;
    @FXML
    Label suCambioLabel;
    @FXML
    Label totalArticulosLabel;
    @FXML
    Label notificacionLabel;
    @FXML
    Button guardarButton;
    @FXML
    Button cancelarButton;
    @FXML
    Button verPendientesButton;
    @FXML
    Label fechaEntregaLabel;
    @FXML
    Label fechaSolicitudLabel;
    @FXML
    TableView<DetallePedidoRenta> tablaDetallePedidoRenta;
    @FXML
    TableColumn<DetallePedidoRenta, Integer> ClaveCol;
    @FXML
    TableColumn<DetallePedidoRenta, String> DescripcionCol;
    @FXML
    TableColumn<DetallePedidoRenta, Float> precioUnitarioCol;
    @FXML
    TableColumn<DetallePedidoRenta, Integer> cantidadCol;
    @FXML
    TableColumn<DetallePedidoRenta, Float> importeCol;
    //items for Datos
    @FXML
    TextField nombreCliente;
    @FXML
    TextField domicilioCliente;
    @FXML
    TextField refDomClienteText;
    @FXML
    TextField telefonoText;
    @FXML
    TextField telefono2Text;
    @FXML
    TextField direccionEntregaText;
    @FXML
    TextField nombreRecibe;
    private DatePicker fechaEntrega;
    @FXML
    TextField horaEntrega;
    @FXML
    TextArea notaAdicional;
    private PedidoRenta nuevoPedidoRenta = new PedidoRenta();
    private String nombreVendedor;
    private float importeTotal = 0;
    private float suAbono;
    private float suCambio;

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
        
    public PedidoRentaController(String vendedor, double ancho, double alto) {
        this.nombreVendedor = vendedor;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PedidoRenta.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.contenedorPBP.setPrefSize(ancho, alto);

        this.fechaEntrega = new DatePicker(Locale.getDefault());
        this.fechaEntrega.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
        this.fechaEntrega.getCalendarView().todayButtonTextProperty().set("Hoy");
        this.fechaEntrega.getCalendarView().setShowWeeks(false);
        this.fechaEntrega.getStylesheets().add("xochiltapp/css/DatePicker.css");
        this.fechaEntrega.setPrefWidth(130);
        fechaHoraEntregaHB.getChildren().add(0, this.fechaEntrega);

        //set columns
        ClaveCol.setCellValueFactory(new PropertyValueFactory<DetallePedidoRenta, Integer>("idProducto"));
        DescripcionCol.setCellValueFactory(new PropertyValueFactory<DetallePedidoRenta, String>("concepto"));
        precioUnitarioCol.setCellValueFactory(new PropertyValueFactory<DetallePedidoRenta, Float>("precioUnit"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<DetallePedidoRenta, Integer>("cantidad"));
        importeCol.setCellValueFactory(new PropertyValueFactory<DetallePedidoRenta, Float>("importe"));

        this.totalesTP.setExpanded(true);

        //set table content
        cargarDatosTabla();

        this.claveProductoAgregarText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    //System.out.println("Clave buscar:" + claveProductoAgregarText.getText().equals("") + ".");
                    if (!claveProductoAgregarText.getText().equals("")) {
                        if (!checkForExistData()) {
                            if (!claveProductoAgregarText.getText().equals("")) {
                                checkForAgregar();
                            }
                        }
                    } else {
                        //open for select producto
                        seleccionarProductoAction(((Node) t.getSource()).getScene().getWindow());
                    }
                } else if (t.getCode().equals(KeyCode.ESCAPE)) {
                    resetPedidoRenta();
                    notificacionLabel.setText("Acción anulada. Puede levantar nuevo Pedido o Renta.");
                }
            }
        });
        
        this.verPendientesButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                mostrarPedidosRentasPendientes(((Node) t.getSource()).getScene().getWindow());
            }
        });

        this.cantidadProductoAgregarText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                    checkForAgregar();
                }
            }
        });

        this.nombreCliente.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                notificacionLabel.setText("Presione Enter para buscar cliente.");
                if (t.getCode() == KeyCode.ENTER) {
                    if (!nombreCliente.getText().equals("")) {
                        buscarCliente(nombreCliente.getText());
                        
                    } else {
                        notificacionLabel.setText("Ingrese el nombre del cliente");
                    }
                }
            }
        });

        this.suAbonoText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    if (suAbonoText.getText().equals("")) {
                        if (nuevoPedidoRenta.getIdPedidoRenta() == 0) {
                            notificacionLabel.setText("El abono quedará en 0");
                        }

                        datosClienteTP.setExpanded(true);
                        nombreCliente.requestFocus();
                    }

                    calcularCambio(true);
                } else {
                    calcularCambio(false);
                }
            }
        });

        this.tablaDetallePedidoRenta.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.DELETE) {
                    if (nuevoPedidoRenta.isEnviado()) {
                        notificacionLabel.setText("El Pedido/Renta enviado o entregado no se puede modificar.");
                        return;
                    }
                    quitarDetalle();
                    claveProductoAgregarText.requestFocus();
                }
//                if (t.getCode() == KeyCode.TAB) {
//                    suAbonoText.requestFocus();
//                }
            }
        });

        this.domicilioCliente.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    refDomClienteText.requestFocus();
                }
            }
        });

        this.refDomClienteText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    telefonoText.requestFocus();
                }
            }
        });

        this.telefonoText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    telefono2Text.requestFocus();
                }
            }
        });

        this.telefono2Text.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    DatosPedidoTP.setExpanded(true);
                    direccionEntregaText.requestFocus();
                }
            }
        });

        this.direccionEntregaText.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    if (direccionEntregaText.getText().equals("")) {
                        notificacionLabel.setText("Ingrese el domicilio donde se entrega.");
                        return;
                    }
                    nombreRecibe.requestFocus();
                }
            }
        });

        this.nombreRecibe.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    fechaEntrega.requestFocus();
                }
            }
        });

        this.fechaEntrega.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    if (fechaEntrega.getSelectedDate() == null) {
                        notificacionLabel.setText("Seleccione la fecha de entrega.");
                        fechaEntrega.requestFocus();
                        return;
                    }

                    horaEntrega.requestFocus();
                }
            }
        });

        this.horaEntrega.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    notaAdicional.requestFocus();
                }
            }
        });

        this.notaAdicional.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode() == KeyCode.ENTER) {
                    guardarButton.requestFocus();
                }
            }
        });

        this.guardarButton.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                guardarPedidoRenta(((Node) t.getSource()).getScene().getWindow());
            }
        });


        this.cancelarButton.setOnAction(
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cancelarPedidoRenta();
            }
        });

    }

    /**
     * Determina si el usuario ingreso codigo de pedido o renta, entonces cargar
     * los datos
     *
     * @return
     */
    private boolean checkForExistData() {

        String claveIngresada = this.claveProductoAgregarText.getText();
        if (claveIngresada.startsWith("p")) {
            //buscar y cargar el pedidorenta

            claveIngresada = claveIngresada.replace("p", "");

            long claveLong = 0;
            try {
                claveLong = Long.valueOf(claveIngresada);
            } catch (Exception ex) {
                this.notificacionLabel.setText("Ingresó una clave de pedido o renta incorrecta.");
            }
            //System.out.println(claveLong + "");
            PedidoRenta pedidoRentaCargado;
            pedidoRentaCargado = PedidosRentasDataSource.findPedidoRentaById(claveLong);

            if (pedidoRentaCargado != null && pedidoRentaCargado.getIdPedidoRenta() > 0) {
                cargarDatosPedidoRenta(pedidoRentaCargado);

                return true;
            } else {
                this.notificacionLabel.setText("No se encontró el pedido o renta No. " + claveLong);
                this.claveProductoAgregarText.setText("");
                return false;
            }
        } else {
            return false;
        }
    }

    private void buscarCliente(String q) {
        checkNuevoPedidoRenta();

        List<Cliente> listaresultado;
        listaresultado = ClientesDataSource.buscarCliente(this.nombreCliente.getText());

        if (listaresultado != null && listaresultado.size() == 1) {
            //si solo es un resultado
            this.nuevoPedidoRenta.setCliente(listaresultado.get(0));
            //if (this.nuevoPedidoRenta.getCliente() != null) {
            this.nombreCliente.setText(this.nuevoPedidoRenta.getCliente().getNombre());
            this.domicilioCliente.setText(this.nuevoPedidoRenta.getCliente().getDomicilio());
            this.refDomClienteText.setText(this.nuevoPedidoRenta.getCliente().getReferenciaDomicilio());
            this.telefonoText.setText(this.nuevoPedidoRenta.getCliente().getTelefono());
            this.telefono2Text.setText(this.nuevoPedidoRenta.getCliente().getTelefono2());

            this.domicilioCliente.requestFocus();
            this.notificacionLabel.setText("");
            //return;
            //}
        } else {
            if (listaresultado == null || listaresultado.isEmpty()) {
                this.nuevoPedidoRenta.setCliente(null);
                this.notificacionLabel.setText("Este cliente aún no está registrado.");
                this.domicilioCliente.requestFocus();
            } else {
                //mostrarle al usuario una ventana para seleccionar cliente
                System.out.println("Existen mas de 2 clientes");
            }
        }

    }

    /**
     * Verifica si es viable agregar producto al presionar enter sobre texto de
     * clave o cantidad de producto
     */
    private void checkForAgregar() {
        if (nuevoPedidoRenta.isEnviado()) {
            this.notificacionLabel.setText("El Pedido/Renta enviado o entregado no se puede modificar.");
            this.claveProductoAgregarText.setText("");
            return;
        }

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

    @FXML
    public void seleccionarProductoAction(Window parent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(SeleccionarProductoController.class
                    .getResource("SeleccionarProducto.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle(
                    "Seleccionar Producto");
            stage.setScene(
                    new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);

            stage.initOwner(parent);
            SeleccionarProductoController controller = fxmlLoader.getController();

            stage.showAndWait();

            if (controller.getProductoSeleccionado()
                    != null) {
                this.claveProductoAgregarText.setText(controller.getProductoSeleccionado().getIdProducto() + "");
            } else {
                this.notificacionLabel.setText("No seleccionó ningún producto.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }
    
    @FXML
    public void mostrarPedidosRentasPendientes(Window parent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PedidosRentasPendientesController.class
                    .getResource("PedidosRentasPendientes.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();

            stage.setTitle(
                    "Seleccionar Pedido o Renta para visualizar");
            stage.setScene(
                    new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);

            stage.initOwner(parent);
            PedidosRentasPendientesController controller = fxmlLoader.getController();

            stage.showAndWait();

            if (controller.getIdPedidoRenta() > 0) {
                this.claveProductoAgregarText.setText("p"+controller.getIdPedidoRenta());
                this.claveProductoAgregarText.requestFocus();
            } else {
                this.notificacionLabel.setText("No seleccionó ningún pedio o renta.");
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    private void AgregarProducto(String codigo, int cantidad) {
        this.notificacionLabel.setText("");
        //agregar a detalles
        int claveProducto = 0;
        try {
            claveProducto = Integer.valueOf(codigo);
        } catch (Exception ex) {
            //System.out.println("Error al identificar clave del producto como int: " + codigo);
            claveProductoAgregarText.requestFocus();
        }

        if (claveProducto == 0) {
            //notificar al usuario
            return;
        }

        // -----------
        checkNuevoPedidoRenta();

        DetallePedidoRenta nuevoDetalle = new DetallePedidoRenta();

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

        this.totalesTP.setExpanded(true);
        //agregar a detalles
        nuevoPedidoRenta.addDetalle(nuevoDetalle);
        //System.out.println("Total de artículos en venta: " + nuevoPedidoRenta.getDetallesPedidoRenta().size());

        this.claveProductoAgregarText.setText("");
        this.cantidadProductoAgregarText.setText("1");
        this.claveProductoAgregarText.requestFocus();

        //this.guardarButton.setDisable(true);
        //this.guardar2Button.setDisable(true);

        this.suAbonoText.setText("");
        this.notificacionLabel.setText("");
        this.suAbonoText.setDisable(false);

        calculaTotales();
        calcularCambio(false);
        cargarDatosTabla();
        alternarPedidoRenta();
    }

    private void cargarDatosTabla() {
        this.tablaDetallePedidoRenta.setItems(FXCollections.observableList(this.nuevoPedidoRenta.getDetallesPedidoRenta()));
    }

    private void calculaTotales() {
        int totalArticulos = 0;
        this.importeTotal = 0;
        for (DetallePedidoRenta item : nuevoPedidoRenta.getDetallesPedidoRenta()) {
            this.importeTotal += item.getImporte();
            this.nuevoPedidoRenta.setImporte(this.importeTotal);
            totalArticulos += item.getCantidad();
        }

        this.importeTotalLabel.setText(DecimalFormat.getCurrencyInstance().format(importeTotal));
        this.totalArticulosLabel.setText(totalArticulos + "");
    }

    private void calcularCambio(boolean EnterPresed) {
        //this.guardarButton.setDisable(true);
        this.suCambioLabel.setText("$0.00");
        this.suAdeudoLabel.setText("$0.00");
        this.suAdeudoLabel.setText("$0.00");
        this.suCambio = 0;
        this.suAbono = 0;
        
        if (this.suAbonoText.getText().equals("")) {
            //this.notificacionLabel.setText("Ingrese el pago del Cliente.");
            float adeudo = this.importeTotal - (this.suAbono + this.nuevoPedidoRenta.getAbono());

            this.suAdeudoLabel.setText(DecimalFormat.getCurrencyInstance().format(adeudo));
            return;
        }

        try {
            if (this.suAbonoText.getText().equals("")) {
                return;
            }

            this.suAbono = Float.valueOf(this.suAbonoText.getText());
            if ((suAbono + this.nuevoPedidoRenta.getAbono()) >= this.importeTotal) {
                this.suCambio = (this.suAbono + this.nuevoPedidoRenta.getAbono()) - this.importeTotal;
                //poner el abono con el adeudo total
                this.suAbono = this.importeTotal - this.nuevoPedidoRenta.getAbono();
                
                this.suCambioLabel.setText(DecimalFormat.getCurrencyInstance().format(this.suCambio));
                this.suAdeudoLabel.setText("$0.00");
                //this.cobrarButton.setDisable(false);
                this.notificacionLabel.setText("El importe del pedido quedaría cubierto.");
                if (EnterPresed) {
                    if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0) {
                        this.datosClienteTP.setExpanded(true);
                        this.nombreCliente.requestFocus();
                    } else {
                        this.guardarButton.requestFocus();
                    }
                }
            } else {
                float adeudo = this.importeTotal - (this.suAbono + this.nuevoPedidoRenta.getAbono());

                this.suAdeudoLabel.setText(DecimalFormat.getCurrencyInstance().format(adeudo));

                if (EnterPresed) {
                    if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0) {
                        this.datosClienteTP.setExpanded(true);
                        this.nombreCliente.requestFocus();
                    } else {
                        this.guardarButton.requestFocus();
                    }
                }
            }
        } catch (Exception ex) {
            this.suAbonoText.setText("");
            this.notificacionLabel.setText("Ingrese el abono en NÚMEROS");
        }
    }

    private void cancelarPedidoRenta() {
        if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0) {
            resetPedidoRenta();
            this.notificacionLabel.setText("El pedido aún no registrado fue desechado. Levantar nuevamente.");
            //return;
        }

        //procede a cancelar por completo el pedido YA registrado
        //preguntar
        if (FXDialog.showConfirmDialog("¿Confirma cancelar por completo este Pedido/Renta? \nEsta acción no se puede revertir.", "Confirmar acción", ConfirmationType.YES_NO_OPTION)) {
            nuevoPedidoRenta.setCancelado(true);
            nuevoPedidoRenta.setEmpleado(nombreVendedor);



            String pedidoORenta;
            if (this.nuevoPedidoRenta.isPedido()) {
                pedidoORenta = "El Pedido";
            } else {
                pedidoORenta = "La Renta";
            }
            long idPedidoRenta = nuevoPedidoRenta.getIdPedidoRenta();
            //enviar a guardar
            PedidosRentasDataSource.guardarApartadoPedido(nuevoPedidoRenta);
            //Imprimir ticket
            resetPedidoRenta();
            this.notificacionLabel.setText(pedidoORenta + " " + idPedidoRenta + " ha siso marcado como CANCELADO.");

            if (!GeneraTicket.TicketPedidoRenta(nuevoPedidoRenta, suAbono, suCambio)) {
                //resetPedidoRenta();
                this.notificacionLabel.setText("Ocurrió un error al imprimir el Ticket del apartado: " + idPedidoRenta + " que fue cancelado ");
            }
        }
    }

    private void checkNuevoPedidoRenta() {
        if (nuevoPedidoRenta == null || nuevoPedidoRenta != null && (nuevoPedidoRenta.getIdPedidoRenta() == 0 && nuevoPedidoRenta.getDetallesPedidoRenta().isEmpty() && nuevoPedidoRenta.getDireccionCliente() == null && nuevoPedidoRenta.getCliente() == null)) {
            this.nuevoPedidoRenta = new PedidoRenta();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            fechaSolicitudLabel.setText(formatoFecha.format(new Date()));
        }
    }

    private void resetPedidoRenta() {
        //nothing
        this.nuevoPedidoRenta = new PedidoRenta();
        this.suAbonoText.setText("");
        this.suAbonoText.setPromptText("");
        this.suAbono = 0;
        this.importeTotal = 0;
        this.nombreRecibe.setPromptText("");
        fechaSolicitudLabel.setText("");
        //this.nombreRecibe.setDisable(false);
        fechaEntregaLabel.setText("Fecha y hora de entrega: *");
        this.guardarButton.setText("Registrar Pedido");
        cargarDatosTabla();
        calculaTotales();
        calcularCambio(false);
        this.limpiarDatosCliente();
        this.totalesTP.setExpanded(true);
        this.claveProductoAgregarText.setText("");
        this.claveProductoAgregarText.requestFocus();
    }

    private void alternarPedidoRenta() {
        if (nuevoPedidoRenta == null) {
            return;
        }

        boolean esPedido = true;
        for (DetallePedidoRenta item : nuevoPedidoRenta.getDetallesPedidoRenta()) {
            //si son bases, es para renta
            if (item.getProducto().getCategoria().equals("Bases")) {
                esPedido = false;
                break;
            }
        }


        if ((esPedido && nuevoPedidoRenta.isRenta()) || (esPedido && !nuevoPedidoRenta.isRenta() && !nuevoPedidoRenta.isPedido())) {
            this.nombreRecibe.setPromptText("");
            //this.nombreRecibe.setDisable(false);
            fechaEntregaLabel.setText("Fecha y hora de entrega: *");
            this.guardarButton.setText("Registrar Pedido");
            DatosPedidoTP.setText("DATOS DEL PEDIDO");
            this.nuevoPedidoRenta.setPedido(true);
            this.notificacionLabel.setText("ESTÁ REGISTRANDO UN PEDIDO");
        } else if ((!esPedido && nuevoPedidoRenta.isPedido()) || (!esPedido && !nuevoPedidoRenta.isRenta() && !nuevoPedidoRenta.isPedido())) {
            this.nombreRecibe.setPromptText("No se requiere");
            //this.nombreRecibe.setDisable(true);
            fechaEntregaLabel.setText("Fecha y hora de devolución: *");
            this.guardarButton.setText("Registrar Renta");
            DatosPedidoTP.setText("DATOS DEL ARRENDADOR");
            this.nuevoPedidoRenta.setRenta(true);
            this.notificacionLabel.setText("ESTÁ REGISTRANDO UNA RENTA");
        }
    }

    private void limpiarDatosCliente() {
        nombreCliente.setText("");
        domicilioCliente.setText("");
        refDomClienteText.setText("");
        telefonoText.setText("");
        telefono2Text.setText("");

        direccionEntregaText.setText("");
        nombreRecibe.setText("");
        fechaEntrega.setSelectedDate(null);
        horaEntrega.setText("");
        notaAdicional.setText("");
    }

    private void quitarDetalle() {
        if (this.tablaDetallePedidoRenta.getSelectionModel().getSelectedItem() != null) {
            
            this.nuevoPedidoRenta.getDetallesPedidoRenta().remove(this.tablaDetallePedidoRenta.getSelectionModel().getSelectedItem());
            this.nuevoPedidoRenta.addDetalleEliminar(this.tablaDetallePedidoRenta.getSelectionModel().getSelectedItem().getIdDetallePedidoRenta());
            this.cargarDatosTabla();
            this.calculaTotales();
            this.calcularCambio(false);
        }
    }

    private void guardarPedidoRenta(Window parent) {
        try {
            if (this.nuevoPedidoRenta.getDetallesPedidoRenta().isEmpty()) {
                this.notificacionLabel.setText("No ha incluido artículos en este apartado.");
                this.claveProductoAgregarText.requestFocus();
                return;
            }
            if (!this.comprobarDatosCliente()) {
                //this.notificacionLabel.setText("Ingrese todos los datos marcados con *");
                DatosPedidoTP.setExpanded(true);
                this.nombreCliente.requestFocus();
                return;
            }
            if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0) {
                this.nuevoPedidoRenta.setEmpleado(nombreVendedor);
                this.nuevoPedidoRenta.setFechaSolicitud(new Date());
                if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0 && !setDatosPedidoRenta()) {
                    return;
                }
                //enviar a guardar
                this.setAbono();
                this.nuevoPedidoRenta = PedidosRentasDataSource.guardarApartadoPedido(nuevoPedidoRenta);
                long idPedidoRenta = nuevoPedidoRenta.getIdPedidoRenta();
                //print ticket
//            if(GeneraTicket.TicketVenta(nuevaVenta, suPago, suCambio)){
//            resetVenta();
                String pedidoORenta;
                String registrad = "";
                if (this.nuevoPedidoRenta.isPedido()) {
                    pedidoORenta = "El Pedido";
                    registrad = "o";
                } else {
                    pedidoORenta = "La Renta";
                    registrad = "a";
                }


                //print ticket
                if (GeneraTicket.TicketPedidoRenta(nuevoPedidoRenta, suAbono, suCambio)) {
                  //preguntar por impresion
                  if ( FXDialog.showConfirmDialog("Imprimir el Formato de Pedido", "Imprimir", ConfirmationType.YES_NO_OPTION)){
                     GeneraTicket.printTicket();
                  }
                    resetPedidoRenta();
                    this.notificacionLabel.setText(pedidoORenta + " " + idPedidoRenta + " se ha registrado.");
                } else {
                    resetPedidoRenta();
                    this.notificacionLabel.setText("Ocurrió un error al imprimir el Ticket del apartado: " + idPedidoRenta);
                }


            } else {
                //actualizarlo en Entrega, Cancelar, ETC

                if (!setDatosPedidoRenta()) {
                    return;
                }

                this.nuevoPedidoRenta.setEmpleado(nombreVendedor);

                //Antes de guardar, preguntar por la accion a realizar
                //Solo Actualizar, Enviar, Cancelar Pedido, Marcar como entregado, o devuelto si es renta

                AccionPedido accionSeleccionada = AccionPedido.Ninguno;
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(AccionPedidoRentaController.class.getResource("AccionPedidoRenta.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Seleccionar acción");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.WINDOW_MODAL);
                    //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                    stage.initOwner(parent);

                    AccionPedidoRentaController controller = fxmlLoader.getController();
                    controller.setPedidoRenta(nuevoPedidoRenta);

                    stage.showAndWait();
                    accionSeleccionada = controller.getAccion();

                } catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
                }

                if (accionSeleccionada == AccionPedido.Ninguno) {
                    this.notificacionLabel.setText("No se realizó ninguna acción sobre el Pedido/Renta.");
                    return;
                }
                String complementMsg = "actualizado";
                if (accionSeleccionada == AccionPedido.Actualizar) {
                    this.setAbono();
                    //complementMsg = "actualizado";
                }else if (accionSeleccionada == AccionPedido.Enviado) {
                    this.nuevoPedidoRenta.setEnviado(true);
                    this.nuevoPedidoRenta.setFechaEnviado(new Date());
                    this.setAbono();
                    complementMsg = "marcado como Enviado";
                } else if (accionSeleccionada == AccionPedido.Entregado) {
                    
                    if (!(nuevoPedidoRenta.getImporte() == (nuevoPedidoRenta.getAbono() + this.suAbono)))
                    {
                        this.notificacionLabel.setText("El Pedido/Renta no se puede marcar como Entregado, aún tiene adeudo.");
                        this.suAbonoText.requestFocus();
                        return;
                    }
                    
                    this.setAbono();
                    this.nuevoPedidoRenta.setEntregado(true);
                    this.nuevoPedidoRenta.setFechaEntregado(new Date());
                    complementMsg = "marcado como Entregado";
                } else if (accionSeleccionada == AccionPedido.Devuelto) {
                    this.nuevoPedidoRenta.setDevuelto(true);
                    this.setAbono();
                    complementMsg = "marcado como Devuelto";
                }


                this.nuevoPedidoRenta = PedidosRentasDataSource.guardarApartadoPedido(nuevoPedidoRenta);

                String pedidoORenta;
                if (this.nuevoPedidoRenta.isPedido()) {
                    pedidoORenta = "El Pedido";
                } else {
                    pedidoORenta = "La Renta";
                }
                long idPedidoRenta = nuevoPedidoRenta.getIdPedidoRenta();
                //enviar a guardar
                //Imprimir ticket
                //resetPedidoRenta();
                //SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                //System.out.print(dateFormat.format(nuevoPedidoRenta.getFechaEntrega()));

                if (GeneraTicket.TicketPedidoRenta(nuevoPedidoRenta, suAbono, suCambio)) {
                    this.resetPedidoRenta();
                this.notificacionLabel.setText(pedidoORenta + " " + idPedidoRenta + " se ha " + complementMsg + ".");
                }else
                {
                    resetPedidoRenta();
                    this.notificacionLabel.setText("Ocurrió un error al imprimir el Ticket del apartado: " + idPedidoRenta);
                }

                
            }
        } catch (Exception ex) {
            //System.out.println("Error: " + ex.getMessage() + " :: " + ex.getCause() + " || " + ex.getStackTrace());
        }
    }

    private boolean setDatosPedidoRenta() {
        try {
            try {
                Calendar fechaEntrega2 = Calendar.getInstance();
                fechaEntrega2.setTime(this.fechaEntrega.getSelectedDate());
                String horaEntregaS = this.horaEntrega.getText();
                if (!horaEntregaS.equals("")) {
                    horaEntregaS = horaEntregaS.trim();
                    horaEntregaS = horaEntregaS.replace('.', ':');
                }
                String[] horaMin = horaEntregaS.split(":");
                fechaEntrega2.set(Calendar.HOUR, Integer.valueOf(horaMin[0]));
                fechaEntrega2.set(Calendar.MINUTE, Integer.valueOf(horaMin[1]));

                this.nuevoPedidoRenta.setFechaEntrega(fechaEntrega2.getTime());
                if (this.nuevoPedidoRenta.getIdPedidoRenta() == 0) {
                    this.nuevoPedidoRenta.setFechaSolicitud(new Date());
                }

            } catch (Exception ex) {
                this.notificacionLabel.setText("Seleccione la Fecha para entrega e ingrese la hora.");
                this.horaEntrega.requestFocus();
                return false;
            }
            this.nuevoPedidoRenta.setImporte(importeTotal);
//            this.nuevoPedidoRenta.setAbono(this.nuevoPedidoRenta.getAbono() + suAbono);
//            if (nuevoPedidoRenta.getImporte() == nuevoPedidoRenta.getAbono()) {
//                nuevoPedidoRenta.setPagado(true);
//            } else {
//                nuevoPedidoRenta.setPagado(false);
//            }

            this.nuevoPedidoRenta.setDireccionCliente(this.domicilioCliente.getText());
            this.nuevoPedidoRenta.setDireccionEntrega(this.direccionEntregaText.getText());
            //if (nuevoPedidoRenta.isPedido()) {
            this.nuevoPedidoRenta.setNombreRecibe(this.nombreRecibe.getText());
//            } else {
//                this.nuevoPedidoRenta.setNombreRecibe("");
//            }

            this.nuevoPedidoRenta.setNota(this.notaAdicional.getText());

            if (this.nuevoPedidoRenta.getCliente() == null) {
                this.nuevoPedidoRenta.setCliente(new Cliente());
            }

            this.nuevoPedidoRenta.getCliente().setNombre(this.nombreCliente.getText());
            this.nuevoPedidoRenta.getCliente().setDomicilio(this.domicilioCliente.getText());
            this.nuevoPedidoRenta.getCliente().setReferenciaDomicilio(this.refDomClienteText.getText());
            this.nuevoPedidoRenta.getCliente().setTelefono(this.telefonoText.getText());
            this.nuevoPedidoRenta.getCliente().setTelefono2(this.telefono2Text.getText());

            return true;
        } catch (Exception ex) {
            this.notificacionLabel.setText("Ingrese los datos marcados con *");
            return false;
        }
    }

    private void setAbono()
    {
           this.nuevoPedidoRenta.setAbono(this.nuevoPedidoRenta.getAbono() + suAbono);
            if (nuevoPedidoRenta.getImporte() == nuevoPedidoRenta.getAbono()) {
                nuevoPedidoRenta.setPagado(true);
            } else {
                nuevoPedidoRenta.setPagado(false);
            }
    }
    
    private boolean comprobarDatosCliente() {
        if (nuevoPedidoRenta.getCliente() == null && this.nombreCliente.getText().equals("")) {
            this.nombreCliente.requestFocus();
            this.notificacionLabel.setText("Ingrese el nombre del cliente.");
            return false;
        } else {
            return !(this.nombreCliente.getText().equals("") || this.direccionEntregaText.getText().equals("") || this.fechaEntrega.getSelectedDate() == null || (this.horaEntrega.getText().equals("") && this.nuevoPedidoRenta.isPedido()));
        }
    }

    private void cargarDatosPedidoRenta(PedidoRenta pedidoRenta) {
        this.resetPedidoRenta();
        this.limpiarDatosCliente();

        //poner los nuevos datos
        this.nuevoPedidoRenta = pedidoRenta;
        this.cargarDatosTabla();
        this.calculaTotales();
        this.suAbonoText.setPromptText(DecimalFormat.getCurrencyInstance().format(nuevoPedidoRenta.getAbono()));
        this.calcularCambio(false);

        //datos cliente
        this.nombreCliente.setText(nuevoPedidoRenta.getCliente().getNombre());
        this.domicilioCliente.setText(nuevoPedidoRenta.getDireccionCliente());
        this.refDomClienteText.setText(nuevoPedidoRenta.getCliente().getReferenciaDomicilio());
        this.telefonoText.setText(nuevoPedidoRenta.getCliente().getTelefono());
        this.telefono2Text.setText(nuevoPedidoRenta.getCliente().getTelefono2());

        this.direccionEntregaText.setText(nuevoPedidoRenta.getDireccionEntrega());
        this.nombreRecibe.setText(nuevoPedidoRenta.getNombreRecibe());
        this.fechaEntrega.setSelectedDate(nuevoPedidoRenta.getFechaEntrega());
        SimpleDateFormat formatHora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat formatFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.fechaSolicitudLabel.setText(formatFecha.format(nuevoPedidoRenta.getFechaSolicitud()));
        this.horaEntrega.setText(formatHora.format(nuevoPedidoRenta.getFechaEntrega()));
        this.notaAdicional.setText(nuevoPedidoRenta.getNota());

        String tipo = "";
        if (nuevoPedidoRenta.isPedido()) {
            tipo = "Pedido";
        }
        if (nuevoPedidoRenta.isRenta()) {
            tipo = "Renta";
        }
        this.guardarButton.setText("Actualizar " + tipo);
        cancelarButton.setText("Cancelar " + tipo);

        if (nuevoPedidoRenta.isPagado()) {
            this.suAbonoText.setDisable(true);
        }else
        {
            this.suAbonoText.setDisable(false);
        }


        this.guardarButton.setDisable((nuevoPedidoRenta.isCancelado() || nuevoPedidoRenta.isEntregado()));
        this.cancelarButton.setDisable((nuevoPedidoRenta.isCancelado() || this.nuevoPedidoRenta.isEnviado() || this.nuevoPedidoRenta.isEntregado() || this.nuevoPedidoRenta.isDevuelto()));

        if (this.nuevoPedidoRenta.isCancelado()) {
            this.notificacionLabel.setText("Este Pedido/Renta se encuentra cancelado.");
        }else if(nuevoPedidoRenta.isEntregado())
            this.notificacionLabel.setText("Este Pedido/Renta #"+nuevoPedidoRenta.getIdPedidoRenta()+" ya fue entregado y liquidado.");
        else
            this.notificacionLabel.setText("Datos del pedido No. " + nuevoPedidoRenta.getIdPedidoRenta() + ", Estado: " + nuevoPedidoRenta.getEstado());

        this.claveProductoAgregarText.setText("");
    }
}
