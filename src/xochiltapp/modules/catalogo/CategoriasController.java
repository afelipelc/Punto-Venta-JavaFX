/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.catalogo;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import xochiltapp.datasource.ArreglosDataSource;
import xochiltapp.dialog.FXDialog;
import xochiltapp.dialog.Message;
import xochiltapp.model.Arreglo;
import xochiltapp.model.Empleado;

/**
 * FXML Controller class
 *
 * @author alfonso
 */
public class CategoriasController extends AnchorPane {

    @FXML
    ComboBox cmbCategorias;
    @FXML
    Button btnNewElement;
    @FXML
    Button btnAnter;
    @FXML
    Button btnSelect;
    @FXML
    Button btnNext;
    @FXML
    ImageView imgImagen;
    @FXML
    AnchorPane contenedorCategoria;
    private Empleado empleado;
    private String ruta;
    private Image image;
    private File imgFile;
    private FindImage findImage;
    private String[] icons;
    private int next, t;

    /**
     * Initializes the controller class.
     */
    public CategoriasController(Empleado empleado, double ancho, double alto) {
        this.empleado = empleado;
        findImage = new FindImage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Categorias.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        ruta = "images/Arreglos de Iglesias";
        try {
            fxmlLoader.load();
            icons = ArreglosDataSource.getListaImagenes("Arreglos de Iglesias"); //findImage.getImage(ruta);

            //System.out.println("Total imagenes "+ icons.length);

            if (icons.length == 0) {
                icons = new String[]{"logo.jpg"};
                ruta = "images";
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (Exception ex) {
            Logger.getLogger(CategoriasController.class.getName()).log(Level.SEVERE, null, ex.toString());
        }
        contenedorCategoria.setPrefSize(ancho, alto);
        cmbCategorias.getItems().addAll("15 años", "Arcos", "Arreglos de Iglesias", "Bautizos", "Casamientos", "Centros de mesa", "Flores Artificiales", "Funerales", "Ramos de Novias");
        cmbCategorias.setValue("Arreglos de Iglesias");
        loadImage();
    }

    public void loadImage() {
        imgFile = new File(ruta + "/" + icons[next]);
        image = new Image(imgFile.toURI().toString(), 600, 450, false, false);
        imgImagen.setImage(image);
    }

    @FXML
    private void cambiarRuta(ActionEvent event) {

        if (cmbCategorias == null || cmbCategorias.getValue() == null) {
            return;
        }

        if (cmbCategorias.getValue().toString().equals("15 años")) {
            ruta = "images/15 anios";
        } else {
            ruta = "images/" + cmbCategorias.getValue().toString();
        }
        next = 0;
        try {
            icons = ArreglosDataSource.getListaImagenes(cmbCategorias.getValue().toString()); //findImage.getImage(ruta);

            if (icons.length == 0) {
                icons = new String[]{"logo.jpg"};
                ruta = "images/";
            }
        } catch (Exception ex) {
            Logger.getLogger(CategoriasController.class.getName()).log(Level.SEVERE, null, ex.toString());
        }
        loadImage();
    }

    @FXML
    private void newElement(ActionEvent event) {

        AbrirForm(((Node) event.getSource()).getScene().getWindow(), null);
    }

    private void AbrirForm(Window parentWindow, Arreglo arregloMostrar) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ArregloFormController.class.getResource("ArregloForm.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Agregar arreglo");
            stage.setScene(new Scene(root));
            //stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(parentWindow);

            ArregloFormController controller = fxmlLoader.getController();

            controller.setCategoria(cmbCategorias.getValue().toString(), ruta);

            if (arregloMostrar != null) {
                controller.setArreglo(arregloMostrar);
            }

            stage.showAndWait();
            if (controller.actionResult) {
                //controller.getArreglo());
                //Agregarlo al contenedor
                //contenedorItems.getChildren().add(1, arregloItem);
                icons = findImage.getImage(ruta);
                loadImage();
            }

        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage() + " stack: " + ex.getCause());
        }
    }

    @FXML
    private void gotoBack(ActionEvent event) {
        if ((next > icons.length) || (next == icons.length)) {
            next = next - 2;
            loadImage();
            t = 1;
        } else if ((next > 0) && (t == 1)) {
            next--;
            loadImage();
            t = 1;
        } else if ((next > 0) && (t == 0)) {
            next = next - 2;
            loadImage();
            t = 1;
        } else {
            FXDialog.showMessageDialog("Primera imagen", "Aviso", Message.INFORMATION);
        }
    }

    @FXML
    private void selectElement(ActionEvent event) {
        //saber el nombre de la imagen
        //localizar el arreglo por nombre de imagen
        //si se encuentra, pasar el objeto arreglo al formulario
        Arreglo arregloMostrar = ArreglosDataSource.findArregloByImage(icons[next]);
        if (arregloMostrar != null) {
            //Mostrar el arreglo
            AbrirForm(((Node) event.getSource()).getScene().getWindow(), arregloMostrar);
        } else {
            FXDialog.showMessageDialog("No se localizó el registro en la Base de Datos.", "Xochilt", Message.INFORMATION);
        }
    }

    @FXML
    private void gotoFront(ActionEvent event) {
        if (next == icons.length - 1 && icons.length == 1) {
            FXDialog.showMessageDialog("última imagen", "Aviso", Message.INFORMATION);
            return;
        }

        if ((next < icons.length) && (next == 0)) {
            next++;
            loadImage();
            next++;
        } else if ((next < icons.length) && (t == 0)) {
            loadImage();
            next++;
            t = 0;
        } else if ((next < icons.length) && (t == 1)) {
            next++;
            loadImage();
            next++;
            t = 0;
        } else {
            FXDialog.showMessageDialog("última imagen", "Aviso", Message.INFORMATION);
        }
    }
}
