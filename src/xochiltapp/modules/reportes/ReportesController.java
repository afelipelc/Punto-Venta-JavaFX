/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.reportes;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import xochiltapp.datasource.ReportesDataSource;
import xochiltapp.model.ReporteModel;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class ReportesController extends AnchorPane {

    @FXML
    AnchorPane contenedorPAP;
    //POR FECHA
    @FXML
    HBox contentFechaDia;
    @FXML
    Button verReporteDiaBtn;
    @FXML
    Label notifDia;
    private DatePicker diaVerDTP;
    @FXML
    TableView<ReporteModel> reporteDiarTB;
    @FXML
    TableColumn<ReporteModel, String> repDiaPeriodo;
    @FXML
    TableColumn<ReporteModel, String> repDiaIngreso;
    @FXML
    TableColumn<ReporteModel, String> repDiaEgreso;
    @FXML
    TableColumn<ReporteModel, String> repDiaSaldo;
//FIN POR FECHA
    //POR SEMANA
    @FXML
    HBox contentFechaSemana;
    @FXML
    Button verReporteSemanaBtn;
    @FXML
    Label notifSemana;
    @FXML
    Label ingresoSemanaLabel;
    @FXML
    Label egresoSemanaLabel;
    @FXML
    Label saldoSemanaLabel;
    private DatePicker semanaVerDTP;
    @FXML
    TableView<ReporteModel> reporteSemanaTB;
    @FXML
    TableColumn<ReporteModel, String> repSemanaPeriodo;
    @FXML
    TableColumn<ReporteModel, String> repSemanaIngreso;
    @FXML
    TableColumn<ReporteModel, String> repSemanaEgreso;
    @FXML
    TableColumn<ReporteModel, String> repSemanaSaldo;
//FIN POR SEMANA
    //POR MES
    @FXML
    HBox contentFechaMes;
    @FXML
    Button verReporteMesBtn;
    @FXML
    Label notifMes;
    @FXML
    Label ingresoMesLabel;
    @FXML
    Label egresoMesLabel;
    @FXML
    Label saldoMesLabel;
    private DatePicker mesVerDTP;
    @FXML
    TableView<ReporteModel> reporteMesTB;
    @FXML
    TableColumn<ReporteModel, String> repMesPeriodo;
    @FXML
    TableColumn<ReporteModel, String> repMesIngreso;
    @FXML
    TableColumn<ReporteModel, String> repMesEgreso;
    @FXML
    TableColumn<ReporteModel, String> repMesSaldo;
//FIN POR MES
    //POR ANIO
    @FXML
    ComboBox<Integer> anioInicioCmb;
    @FXML
    ComboBox<Integer> anioFinCmb;
    @FXML
    Button verReporteAniosBtn;
    @FXML
    Label notifAnio;
    @FXML
    Label ingresoAnioLabel;
    @FXML
    Label egresoAnioLabel;
    @FXML
    Label saldoAnioLabel;
    @FXML
    TableView<ReporteModel> reporteAnioTB;
    @FXML
    TableColumn<ReporteModel, String> repAnioPeriodo;
    @FXML
    TableColumn<ReporteModel, String> repAnioIngreso;
    @FXML
    TableColumn<ReporteModel, String> repAnioEgreso;
    @FXML
    TableColumn<ReporteModel, String> repAnioSaldo;
//FIN POR ANIO

    public ReportesController(double ancho, double alto) {
        // TODO
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reportes.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);

        }

        this.contenedorPAP.setPrefSize(ancho, alto);

        /*
         * Reporte por Fecha
         */
        this.diaVerDTP = new DatePicker(Locale.getDefault());
        this.diaVerDTP.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
        this.diaVerDTP.getCalendarView().todayButtonTextProperty().set("Hoy");
        this.diaVerDTP.getCalendarView().setShowWeeks(false);
        this.diaVerDTP.getStylesheets().add("xochiltapp/css/DatePicker.css");
        this.diaVerDTP.setPrefWidth(130);

        contentFechaDia.getChildren().add(1, this.diaVerDTP);
        this.diaVerDTP.setAlignment(Pos.CENTER);
        //contentFechaDia.setAlignment(Pos.CENTER_LEFT);
        repDiaPeriodo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("periodo"));
        repDiaIngreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("ingresoSt"));
        repDiaEgreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("egresoSt"));
        repDiaSaldo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("saldoSt"));
        this.verReporteDiaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cargarReporteFecha();
            }
        });
        /*
         * Fin Reporte Fecha
         */

        /*
         * Reporte por SEMANA
         */
        this.semanaVerDTP = new DatePicker(Locale.getDefault());
        this.semanaVerDTP.setDateFormat(new SimpleDateFormat("dd/MM/yyyy"));
        this.semanaVerDTP.getCalendarView().todayButtonTextProperty().set("Hoy");
        this.semanaVerDTP.getCalendarView().setShowWeeks(false);
        this.semanaVerDTP.getStylesheets().add("xochiltapp/css/DatePicker.css");
        this.semanaVerDTP.setPrefWidth(130);

        contentFechaSemana.getChildren().add(1, this.semanaVerDTP);
        this.semanaVerDTP.setAlignment(Pos.CENTER);
        //contentFechaDia.setAlignment(Pos.CENTER_LEFT);
        repSemanaPeriodo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("periodo"));
        repSemanaIngreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("ingresoSt"));
        repSemanaEgreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("egresoSt"));
        repSemanaSaldo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("saldoSt"));
        this.verReporteSemanaBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cargarReporteSemana();
            }
        });
        /*
         * Fin Reporte SEMANA
         */

        /*
         * Reporte por MES
         */
        this.mesVerDTP = new DatePicker(Locale.getDefault());
        this.mesVerDTP.setDateFormat(new SimpleDateFormat("MMMM/yy"));
        this.mesVerDTP.getCalendarView().todayButtonTextProperty().set("Mes");
        this.mesVerDTP.getCalendarView().setShowWeeks(false);
        this.mesVerDTP.getStylesheets().add("xochiltapp/css/DatePicker.css");
        this.mesVerDTP.setPrefWidth(130);
        this.mesVerDTP.getCalendarView().setShowTodayButton(false);
        this.mesVerDTP.getCalendarView().setShowWeeks(true);
        
        contentFechaMes.getChildren().add(1, this.mesVerDTP);
        this.mesVerDTP.setAlignment(Pos.CENTER);
        //contentFechaDia.setAlignment(Pos.CENTER_LEFT);
        repMesPeriodo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("periodo"));
        repMesIngreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("ingresoSt"));
        repMesEgreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("egresoSt"));
        repMesSaldo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("saldoSt"));
        this.verReporteMesBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cargarReporteMes();
            }
        });
        /*
         * Fin Reporte MES
         */

        /*
         * Reporte por ANIO
         */
        List<Integer> listaAnios = new ArrayList<>();
        for (int i = 2013; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            listaAnios.add(i);
        }

        this.anioInicioCmb.setItems(FXCollections.observableList(listaAnios));
        this.anioFinCmb.setItems(FXCollections.observableList(listaAnios));

        repAnioPeriodo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("periodo"));
        repAnioIngreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("ingresoSt"));
        repAnioEgreso.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("egresoSt"));
        repAnioSaldo.setCellValueFactory(new PropertyValueFactory<ReporteModel, String>("saldoSt"));
        this.verReporteAniosBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                cargarReporteAnual();
            }
        });
        /*
         * Fin Reporte ANIO
         */
    }

    private void cargarReporteFecha() {
        this.notifDia.setText("");
        if (this.diaVerDTP.getSelectedDate() == null) {
            this.notifDia.setText("Seleccione la fecha.");
            this.diaVerDTP.requestFocus();
            return;
        }

        if (this.diaVerDTP.getSelectedDate().equals(new Date())) {
            this.notifDia.setText("Vea el corte de Caja actual.");
        }

        this.reporteDiarTB.setItems(FXCollections.observableList(ReportesDataSource.saldoFecha(this.diaVerDTP.getSelectedDate())));
    }

    private void cargarReporteSemana() {
        this.notifSemana.setText("");
        this.ingresoSemanaLabel.setText("$0.00");
        this.egresoSemanaLabel.setText("$0.00");
        this.saldoSemanaLabel.setText("$0.00");

        if (this.semanaVerDTP.getSelectedDate() == null) {
            this.notifSemana.setText("Seleccione un día de la semana.");
            this.semanaVerDTP.requestFocus();
            return;
        }

        this.reporteSemanaTB.setItems(FXCollections.observableList(ReportesDataSource.saldoSemana(this.semanaVerDTP.getSelectedDate())));

        if (this.reporteSemanaTB.getItems().size() > 0) {
            float ingreso = 0;
            float egreso = 0;
            float saldo = 0;

            for (ReporteModel item : this.reporteSemanaTB.getItems()) {
                ingreso += item.getIngreso();
                egreso += item.getEgreso();
                saldo += item.getSaldo();
            }

            this.ingresoSemanaLabel.setText(DecimalFormat.getCurrencyInstance().format(ingreso));
            this.egresoSemanaLabel.setText(DecimalFormat.getCurrencyInstance().format(egreso));
            this.saldoSemanaLabel.setText(DecimalFormat.getCurrencyInstance().format(saldo));
        }
    }

    private void cargarReporteMes() {
        this.notifMes.setText("");
        this.ingresoMesLabel.setText("$0.00");
        this.egresoMesLabel.setText("$0.00");
        this.saldoMesLabel.setText("$0.00");

        if (this.mesVerDTP.getSelectedDate() == null) {
            this.notifMes.setText("Seleccione el mes.");
            this.mesVerDTP.requestFocus();
            return;
        }

        this.reporteMesTB.setItems(FXCollections.observableList(ReportesDataSource.saldoMes(this.mesVerDTP.getSelectedDate())));

        if (this.reporteMesTB.getItems().size() > 0) {
            float ingreso = 0;
            float egreso = 0;
            float saldo = 0;

            for (ReporteModel item : this.reporteMesTB.getItems()) {
                ingreso += item.getIngreso();
                egreso += item.getEgreso();
                saldo += item.getSaldo();
            }

            this.ingresoMesLabel.setText(DecimalFormat.getCurrencyInstance().format(ingreso));
            this.egresoMesLabel.setText(DecimalFormat.getCurrencyInstance().format(egreso));
            this.saldoMesLabel.setText(DecimalFormat.getCurrencyInstance().format(saldo));
        }
    }

    private void cargarReporteAnual() {
        this.notifAnio.setText("");
        this.ingresoAnioLabel.setText("$0.00");
        this.egresoAnioLabel.setText("$0.00");
        this.saldoAnioLabel.setText("$0.00");

        if (this.anioInicioCmb.getSelectionModel().getSelectedItem() == 0) {
            this.notifAnio.setText("Seleccione el año de inicio.");
            this.anioInicioCmb.requestFocus();
            return;
        }

        if (this.anioFinCmb.getSelectionModel().getSelectedItem() == 0) {
            this.notifAnio.setText("Seleccione el año de inicio.");
            this.anioInicioCmb.requestFocus();
            return;
        }

        if (this.anioFinCmb.getSelectionModel().getSelectedItem() > this.anioInicioCmb.getSelectionModel().getSelectedItem()) {
            this.notifAnio.setText("El año de Fin debe ser menor al de inicio.");
            this.anioFinCmb.requestFocus();
            return;
        }

        this.reporteAnioTB.setItems(FXCollections.observableList(ReportesDataSource.saldoAnios(this.anioInicioCmb.getSelectionModel().getSelectedItem(), this.anioFinCmb.getSelectionModel().getSelectedItem())));

        if (this.reporteAnioTB.getItems().size() > 0) {
            float ingreso = 0;
            float egreso = 0;
            float saldo = 0;

            for (ReporteModel item : this.reporteAnioTB.getItems()) {
                ingreso += item.getIngreso();
                egreso += item.getEgreso();
                saldo += item.getSaldo();
            }

            this.ingresoAnioLabel.setText(DecimalFormat.getCurrencyInstance().format(ingreso));
            this.egresoAnioLabel.setText(DecimalFormat.getCurrencyInstance().format(egreso));
            this.saldoAnioLabel.setText(DecimalFormat.getCurrencyInstance().format(saldo));
        }
    }
}
