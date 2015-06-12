/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import xochiltapp.datasource.CajaDataSource;
import xochiltapp.model.Empleado;
import xochiltapp.modules.caja.AbrirCajaController;
import xochiltapp.modules.login.LoginController;

/**
 *
 * @author afelipelc
 */
public class MainApp extends Application {

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 327.0;
    private final double MINIMUM_WINDOW_HEIGHT = 240.0;
    private double anchoPantalla;
    private double altoPantalla;
    private Empleado empleado;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(MainApp.class, (java.lang.String[]) null);
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            stage.setTitle("Florerías 25 Rosas");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            this.anchoPantalla = bounds.getWidth();
            this.altoPantalla = bounds.getHeight();
            gotoLogin();
            primaryStage.show();
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void gotoLogin() {
        try {
            LoginController loginwindow = (LoginController) replaceSceneContent("modules/login/Login.fxml");
            loginwindow.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void gotoComprobarCaja() {
        if (!CajaDataSource.isCajaAbierta()) {
            gotoIniciarCaja();
        } else {
            gotoMainWindow();
        }
    }
    public void gotoIniciarCaja() {
        try {
            AbrirCajaController abrirCaja = (AbrirCajaController) replaceSceneContent("modules/caja/AbrirCaja.fxml");
            abrirCaja.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void gotoMainWindow() {
        try {
            MainWindowController mainWindow = (MainWindowController) replaceSceneContent("MainWindow.fxml");
            mainWindow.setApp(this);
            stage.setX(0);
            stage.setY(0);
            stage.setWidth(this.anchoPantalla);
            stage.setHeight(this.altoPantalla);
            //enable / disable buttons for Empleado Role
        } catch (Exception ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = MainApp.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(MainApp.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page);// MINIMUM_WINDOW_WIDTH, MINIMUM_WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }
    /**
     * @return the anchoPantalla
     */
    public double getAnchoPantalla() {
        return anchoPantalla;
    }
    /**
     * @return the altoPantalla
     */
    public double getAltoPantalla() {
        return altoPantalla;
    }
    /**
     * @return the empleado
     */
    public Empleado getEmpleado() {
        return empleado;
    }
    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public Stage getStage() {
        return this.stage;
    }
}
