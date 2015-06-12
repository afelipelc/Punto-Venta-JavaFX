/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.EmpleadosDataSource;
import xochiltapp.model.Empleado;

/**
 * FXML Controller class
 *
 * @author afelipelc
 */
public class EmpleadosFormController implements Initializable {

    @FXML
    TextField nombreText;
    @FXML
    TextField usuarioText;
    @FXML
    PasswordField passwordText;
    @FXML
    PasswordField confirmPasswordText;
    @FXML
    CheckBox administradorCheck;
    @FXML
    CheckBox vendedorCheck;
    @FXML
    CheckBox consultarCheck;
    @FXML
    CheckBox activoCheck;
    @FXML
    Button actualizarEmpleadoButton;
    @FXML
    Button agregarButton;
    @FXML
    Button cerrarButton;
    @FXML
    TableView<Empleado> empleadosTable;
    @FXML
    TableColumn<Empleado, String> nombreCol;
    @FXML
    TableColumn<Empleado, Boolean> activoCol;
    Empleado empleado;
    Empleado selectedItem;
    List<Empleado> empleados = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        activoCol.setCellValueFactory(new PropertyValueFactory<Empleado, Boolean>("estaActivo"));

        this.actualizarEmpleadoButton.setDisable(true);

        this.agregarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                LimpiarTodo();
                selectedItem = new Empleado();
                nombreText.requestFocus();
                actualizarEmpleadoButton.setDisable(false);
            }
        });

        this.actualizarEmpleadoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                GuardarEmpleado();
            }
        });

        this.cerrarButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                Stage primaryStage = (Stage) cerrarButton.getScene().getWindow();
                primaryStage.close();
            }
        });

        this.empleadosTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {

                selectedItem = empleadosTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    LimpiarTodo();
                    EditarEmpleado();
                } else {
                    actualizarEmpleadoButton.setDisable(true);
                }
            }
        });
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
        if (empleado != null) {
            CargarEmpleados();
        }
    }

    private void CargarEmpleados() {
        this.empleados = EmpleadosDataSource.getListaEmpleados(this.empleado);
        if (empleados == null) {
            empleados = new ArrayList<>();
        }

        this.empleadosTable.setItems(FXCollections.observableList(empleados));
    }

    private void EditarEmpleado() {
        this.nombreText.setText(selectedItem.getNombre());
        this.usuarioText.setText(selectedItem.getUsuario());
        this.usuarioText.setEditable(false);
        this.passwordText.setText("");
        this.confirmPasswordText.setText("");
        this.administradorCheck.setSelected(selectedItem.isAdministrador());
        this.vendedorCheck.setSelected(selectedItem.isVendedor());
        this.consultarCheck.setSelected(selectedItem.isConsulta());
        this.activoCheck.setSelected(selectedItem.isActivo());
        actualizarEmpleadoButton.setDisable(false);
        this.usuarioText.setEditable(false);
    }

    private boolean setDatosEmpleado() {
        if (this.nombreText.getText().equals("")) {
            this.nombreText.requestFocus();
            return false;
        }

        if (this.selectedItem != null && this.selectedItem.getIdEmpleado() == 0 && this.usuarioText.getText().equals("")) {
            this.usuarioText.requestFocus();
            return false;
        }

        if (!this.passwordText.getText().equals("") && !this.passwordText.getText().equals(this.confirmPasswordText.getText())) {
            this.passwordText.setText("");
            this.confirmPasswordText.setText("");
            this.passwordText.requestFocus();
            return false;
        }

        //set datos
        this.selectedItem.setNombre(this.nombreText.getText());
        if (this.selectedItem.getIdEmpleado() > 0) {
            this.selectedItem.setUsuario(null);
        }else {
          this.selectedItem.setUsuario(this.usuarioText.getText());
        }
        if (!this.passwordText.getText().equals("")) {
            this.selectedItem.setPassword(this.passwordText.getText());
        }

        this.selectedItem.setAdministrador(this.administradorCheck.isSelected());
        this.selectedItem.setVendedor(this.vendedorCheck.isSelected());
        this.selectedItem.setConsulta(this.consultarCheck.isSelected());
        this.selectedItem.setActivo(this.activoCheck.isSelected());

        return true;

    }

    private void GuardarEmpleado() {
        if (!setDatosEmpleado()) {
            return;
        }

        boolean esNuevo = selectedItem.getIdEmpleado() == 0;

        this.selectedItem = EmpleadosDataSource.guardarEmpleado(this.selectedItem);

        if (selectedItem == null) {
            FXDialog.showMessageDialog("Ocurrió un error al actualizar los datos\n del empleado.", "Configuración", Message.INFORMATION);
            return;
        }

//        if(selectedItem!=null && esNuevo)
//            this.empleados.add(selectedItem);

        CargarEmpleados();

        LimpiarTodo();
        selectedItem = new Empleado();
        nombreText.requestFocus();
    }

    private void LimpiarTodo() {
        this.nombreText.setText("");
        this.passwordText.setText("");
        this.confirmPasswordText.setText("");
        this.usuarioText.setText("");
        this.activoCheck.setSelected(false);
        this.administradorCheck.setSelected(false);
        this.vendedorCheck.setSelected(false);
        this.consultarCheck.setSelected(false);
        this.actualizarEmpleadoButton.setDisable(true);
        this.usuarioText.setEditable(true);
    }
}
