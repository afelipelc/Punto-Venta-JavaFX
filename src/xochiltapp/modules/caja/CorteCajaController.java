/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.caja;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.CajaDataSource;
import xochiltapp.model.CorteCaja;
import xochiltapp.model.DetalleCorte;
import xochiltapp.model.Empleado;
import xochiltapp.model.ResumenIngreso;
import xochiltapp.utils.ticket.GeneraTicket;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class CorteCajaController extends BorderPane {

    @FXML
    Label FHAperturaLabel;
    @FXML
    Label FHCierreLabel;
    @FXML
    Label montoInicioLabel;
    @FXML
    Label ingresoDiaLabel;
    @FXML
    Label montoFinalLabel;
    @FXML
    Label empleadoAbreLabel;
    @FXML
    Button crearCorteButton;
    @FXML
    TableView<DetalleCorte> detallesCorteTable;
    @FXML
    TableColumn<DetalleCorte, String> conceptoCol;
    @FXML
    TableColumn<DetalleCorte, Integer> cantidadCol;
    @FXML
    TableColumn<DetalleCorte, Float> ingresoCol;
    CorteCaja corteActual;
    List<DetalleCorte> detallesCorte;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Empleado empleado;

    /**
     * Initializes the controller class.
     */
    public CorteCajaController(Empleado empleado) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CorteCaja.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.empleado = empleado;
        this.crearCorteButton.setDisable(true);

        conceptoCol.setCellValueFactory(new PropertyValueFactory<DetalleCorte, String>("concepto"));
        cantidadCol.setCellValueFactory(new PropertyValueFactory<DetalleCorte, Integer>("cantidad"));
        ingresoCol.setCellValueFactory(new PropertyValueFactory<DetalleCorte, Float>("ingreso"));


        //load corteActual
        //obtener caja actual si el corte aun está abierto

        if (!CajaDataSource.isCajaAbierta()) {
            FXDialog.showMessageDialog("El corte está cerrado.\nReinicie el programa.", "Corte de Caja", Message.INFORMATION);
            return;
        }

        //continuar con el corte actual
        corteActual = CajaDataSource.cajaActual();

        if (corteActual == null) {
            FXDialog.showMessageDialog("El corte está cerrado.\nReinicie el programa.", "Corte de Caja", Message.INFORMATION);
            return;
        }

        //mostrar los datos de la caja
        CargarDatosCorte();

        this.crearCorteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cerrarCorte();
            }
        });
    }

    private void CargarDatosCorte() {
        this.FHAperturaLabel.setText(dateFormat.format(corteActual.getFechaHoraApertura()));
        this.FHCierreLabel.setText(dateFormat.format(new Date()));
        this.montoInicioLabel.setText(DecimalFormat.getCurrencyInstance().format(corteActual.getMontoInicial()));

        //cargar los ingresos
        //obtener el ingreso total del corte del resumen de ingresos
        ResumenIngreso resumenIngreso = CajaDataSource.getResumenIngreso(corteActual.getFechaHoraApertura(), new Date());

        if (resumenIngreso == null) {
            FXDialog.showMessageDialog("No se encontraron ingresos en el corte actual.\nReinicie el programa.", "Corte de Caja", Message.INFORMATION);
            return;
        }

        this.corteActual.setMontoIngreso(resumenIngreso.getIngresoTotal());
        this.corteActual.setMontoCierre(resumenIngreso.getIngresoTotal() + corteActual.getMontoInicial());

        this.ingresoDiaLabel.setText(DecimalFormat.getCurrencyInstance().format(resumenIngreso.getIngresoTotal()));
        this.empleadoAbreLabel.setText(corteActual.getEmpleadoAbre());
        this.montoFinalLabel.setText(DecimalFormat.getCurrencyInstance().format(corteActual.getMontoCierre()));

        detallesCorte = CajaDataSource.getProductosVendidos(corteActual.getFechaHoraApertura(), new Date());

        //ingreso por pedidos o rentas
        DetalleCorte ingPedidosRentas = CajaDataSource.getResumenIngresoPedidosRentas(corteActual.getFechaHoraApertura(), new Date());
        detallesCorte.add(0, ingPedidosRentas);

        this.detallesCorteTable.setItems(FXCollections.observableList(detallesCorte));

        this.crearCorteButton.setDisable(false);
    }

    private void cerrarCorte() {
        if (corteActual == null && corteActual.getIdCorte() == 0) {
            FXDialog.showMessageDialog("El corte está cerrado.\nReinicie el programa.", "Corte de Caja", Message.INFORMATION);
            return;
        }

        if (!FXDialog.showConfirmDialog("¿Confirma realizar el corte de Caja?", "Corte de Caja", ConfirmationType.YES_NO_OPTION)) {
            return;
        }

        this.corteActual.setFechaHoraCierre(new Date());
        this.corteActual.setEmpleadoCierra(empleado.getNombre());

        Stage stage = (Stage) this.crearCorteButton.getScene().getWindow();
        this.corteActual = CajaDataSource.cerrarCorte(corteActual);
        if (corteActual == null) {
            FXDialog.showMessageDialog("Ocurrió un error al intentar\ncerrar el corte.\nInicie el programa y verifique que puede\niniciar un nuevo corte.\n\nEl programa requiere soporte técnico.", "Corte de Caja", Message.INFORMATION);
            stage.close();
            return;
        }
        //cerrar el corte

        if (GeneraTicket.TicketCorte(corteActual, detallesCorte)) {
            FXDialog.showMessageDialog("El Corte se ha Cerrado.\nDebe reiniciar el programa.", "Corte de Caja", Message.INFORMATION);
        } else {
            FXDialog.showMessageDialog("Ocurrió un error al imprimir el corte.\nEl corte se ha realizado.\n\nDebe iniciar el programa.", "Corte de Caja", Message.INFORMATION);
        }

        stage.close();
    }
}
