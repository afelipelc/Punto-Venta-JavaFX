/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import xochiltapp.dialog.FXDialog;
import xochiltapp.dialog.Message;
import xochiltapp.modules.caja.CorteCajaController;
import xochiltapp.modules.catalogo.CatalogoController;
import xochiltapp.modules.compra.ComprarController;
import xochiltapp.modules.config.PanelConfiguracionController;
import xochiltapp.modules.inventario.InventarioController;
import xochiltapp.modules.pedidorenta.PedidoRentaController;
import xochiltapp.modules.proveedores.ProveedoresController;
import xochiltapp.modules.reportes.ReportesController;
import xochiltapp.modules.venta.VenderController;
import xochiltapp.utils.StringUtilities;
import xochiltapp.modules.catalogo.CategoriasController;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class MainWindowController implements Initializable {
  @FXML
  Label nombreUsuario;
    @FXML
    Label diaLabel;
    @FXML
    Label horaLabel;
    @FXML
    VBox contenedorMenu;
    @FXML
    GridPane contenedorTop;
    @FXML
    HBox contenedorBottom;
    @FXML
    Button ventasButton;
    @FXML
    Button comprasButton;
    @FXML
    Button pedidosButton;
    @FXML
    Button corteCajaButton;
    @FXML
    Button inventarioButton;
    @FXML
    Button proveedoresButton;
    @FXML
    Button catalogoButton;
    @FXML
    Button configButton;
    @FXML
    Button reportesButton;
    @FXML
    Button SalirButton;
    @FXML
    Pane contenedorPrincipal;
    @FXML
    Label tituloModulo;
    VenderController ventas;
    InventarioController inventario;
    ProveedoresController proveedores;
    PedidoRentaController pedidosRentas;
    ComprarController compras;
    CatalogoController catalogo;
    CategoriasController categoria;
    PanelConfiguracionController panelConfig;
    ReportesController reportes;
    private double anchoContenedorP;
    private double altoContenedorP;
    private MainApp application;
    static TimerTask relojTask;
    static Timer contadorTiempo;

    public void setApp(MainApp application) {
        this.application = application;
        this.anchoContenedorP = this.application.getAnchoPantalla() - contenedorMenu.getWidth() - 15;
        this.altoContenedorP = this.application.getAltoPantalla() - contenedorTop.getHeight() - contenedorBottom.getHeight() - 30;
        this.contenedorPrincipal.setPrefWidth(anchoContenedorP);
        this.contenedorPrincipal.setPrefHeight(altoContenedorP);

        //set start of buttons
//        this.ventasButton.setDisable(!application.getEmpleado().isAdministrador() || !application.getEmpleado().isVendedor());
//        this.corteCajaButton.setDisable(!application.getEmpleado().isAdministrador() || !application.getEmpleado().isVendedor());
//
//        this.inventarioButton.setDisable(!application.getEmpleado().isAdministrador());
//        this.pedidosButton.setDisable(!application.getEmpleado().isAdministrador() || !application.getEmpleado().isVendedor());
//
//        this.proveedoresButton.setDisable(!application.getEmpleado().isAdministrador());
//        this.comprasButton.setDisable(!application.getEmpleado().isAdministrador());
//
//        this.configButton.setDisable(!application.getEmpleado().isAdministrador());
//        this.reportesButton.setDisable(!application.getEmpleado().isAdministrador());

        if(application.getEmpleado().isVendedor() && !application.getEmpleado().isAdministrador()){
            contenedorMenu.getChildren().remove(proveedoresButton.getParent());
            contenedorMenu.getChildren().remove(comprasButton.getParent());
            contenedorMenu.getChildren().remove(reportesButton.getParent());
            contenedorMenu.getChildren().remove(configButton.getParent());
        }else if(application.getEmpleado().isConsulta() && !application.getEmpleado().isVendedor() && !application.getEmpleado().isAdministrador()){
            contenedorMenu.getChildren().remove(ventasButton.getParent());
            contenedorMenu.getChildren().remove(pedidosButton.getParent());
            contenedorMenu.getChildren().remove(corteCajaButton.getParent());
            contenedorMenu.getChildren().remove(inventarioButton.getParent());
            contenedorMenu.getChildren().remove(proveedoresButton.getParent());
            contenedorMenu.getChildren().remove(comprasButton.getParent());
            contenedorMenu.getChildren().remove(reportesButton.getParent());
            contenedorMenu.getChildren().remove(configButton.getParent());
        }
        
        if (application.getEmpleado().isAdministrador() || application.getEmpleado().isVendedor()) {
            handleVentas_Button();
            final Event ev=null;
            ventas.setFocus(ev);
        }
        this.nombreUsuario.setText("Usuario: " + application.getEmpleado().getNombre());
        Timeline timeline;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Calendar time = Calendar.getInstance();
                    String monthString = StringUtilities.pad(2, '0', time.get(Calendar.DAY_OF_MONTH) == 0 ? "12" : time.get(Calendar.DAY_OF_MONTH) + "");
                    String nameMonth = new SimpleDateFormat("MMMMM").format(time.getTime());
                    nameMonth = nameMonth.replaceFirst(nameMonth.substring(0, 1), nameMonth.substring(0, 1).toUpperCase());
                    diaLabel.setText(monthString + " " + nameMonth);
                    String hourString = StringUtilities.pad(2, ' ', time.get(Calendar.HOUR) == 0 ? "12" : time.get(Calendar.HOUR) + "");
                    String minuteString = StringUtilities.pad(2, '0', time.get(Calendar.MINUTE) + "");
                    //String secondString = StringUtilities.pad(2, '0', time.get(Calendar.SECOND) + "");
                    String ampmString = time.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM";
                    horaLabel.setText(hourString + ":" + minuteString + " " + ampmString);
                } catch (Exception ex) {
                  FXDialog.showMessageDialog("Cheque el siguiente error \n" + ex.getMessage().toString(),"Ventana principal" , Message.INFORMATION);
                }
            }
        }),
                new KeyFrame(Duration.seconds(30)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.ventasButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleVentas_Button();
                ventas.setFocus(t);
            }
        });

        this.corteCajaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleCorte_Button();
            }
        });

        this.inventarioButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleInventario_Button();
                inventario.setFocus(t);
            }
        });

        this.proveedoresButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleProveedores_Button();
            }
        });

        this.pedidosButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handlePedidos_Button();
                pedidosRentas.setFocus(t);
            }
        });
        this.catalogoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleCatalogo_Button();
            }
        });

        this.comprasButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleCompras_Button();
                compras.setFocus(t);
            }
        });
        
        this.reportesButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                handleReportes_Button();
            }
        });

        this.configButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                handleConfig_Button();
            }
        });

        this.SalirButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                application.getStage().close();
            }
        });
    }

    public void handleVentas_Button() {
        if (ventas == null) {
            ventas = new VenderController(application.getEmpleado().getNombre(), anchoContenedorP, altoContenedorP);
        }
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(ventas);
        this.tituloModulo.setText("Registrar Ventas");
    }

    private void handleCorte_Button() {
        CorteCajaController corteCaja = new CorteCajaController(application.getEmpleado());
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(corteCaja);
        this.tituloModulo.setText("Corte de Caja");

    }

    public void handlePedidos_Button() {
        if (pedidosRentas == null) {
            pedidosRentas = new PedidoRentaController(application.getEmpleado().getNombre(), anchoContenedorP, altoContenedorP);
        }

        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(pedidosRentas);
        this.tituloModulo.setText("Pedidos & Rentas");
    }

    public void handleInventario_Button() {

        if (inventario == null) {
            inventario = new InventarioController(anchoContenedorP, altoContenedorP);
        } else {
          inventario = null;
          inventario = new InventarioController(anchoContenedorP, altoContenedorP);
        }
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(inventario);

        this.tituloModulo.setText("Inventario");
    }

    public void handleProveedores_Button() {
        if (proveedores == null) {
            proveedores = new ProveedoresController(anchoContenedorP, altoContenedorP);
        }
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(proveedores);
        this.tituloModulo.setText("Proveedores");
    }

    public void handleCatalogo_Button() {
        //crear instancia del controlador de catalogo
        //limpiar el contenedor principal
        //agregar el objeto controlador al contenedor
        if (this.categoria == null) {
            categoria = new CategoriasController(application.getEmpleado(), anchoContenedorP, altoContenedorP);
        }
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(categoria);
        this.tituloModulo.setText("Catálogo de arreglos");
        
        /*if (this.catalogo == null) {
            catalogo = new CatalogoController(application.getEmpleado(), anchoContenedorP, altoContenedorP);
        }
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(catalogo);
        this.tituloModulo.setText("Catálogo de arreglos");*/
    }

    public void handleCompras_Button() {
        if (compras == null) {
            compras = new ComprarController(application.getEmpleado().getNombre(), anchoContenedorP, altoContenedorP);
        }

        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(compras);
        this.tituloModulo.setText("Registrar Compra");
    }

    private void handleConfig_Button() {
        if (panelConfig == null) {
            panelConfig = new PanelConfiguracionController(application.getEmpleado(), anchoContenedorP, altoContenedorP);
        }

        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(panelConfig);
        this.tituloModulo.setText("Panel de Configuración");
    }
    
    private void handleReportes_Button()
    {
        if(reportes == null)
        {
            reportes = new ReportesController(anchoContenedorP, altoContenedorP);
        }
        
        this.contenedorPrincipal.getChildren().clear();
        this.contenedorPrincipal.getChildren().add(reportes);
        this.tituloModulo.setText("Panel de reportes");
    }
}
