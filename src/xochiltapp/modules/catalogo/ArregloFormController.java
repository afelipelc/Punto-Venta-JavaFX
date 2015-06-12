/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.modules.catalogo;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import xochiltapp.dialog.*;
import xochiltapp.datasource.ArreglosDataSource;
import xochiltapp.model.Arreglo;
import xochiltapp.model.ComponenteArreglo;

/**
 * FXML Controller class
 *
 * @author ELIBETH
 */
public class ArregloFormController implements Initializable {

    @FXML
    ImageView imagenArreglo;
    @FXML
    TextField claveProductoTxt;
    @FXML
    TextField conceptoTxt;
    @FXML
    TextField descripcionTxt;
    @FXML
    TextArea procedimientoTxt;
    @FXML
    TextField precioUnitarioTxt;
    @FXML
    CheckBox activoChk;
    @FXML
    Label ultimaActualizacionLabel;
    @FXML
    TableView<ComponenteArreglo> componentesArregloTbl;
    @FXML
    TableColumn<ComponenteArreglo, String> nombreCompCol;
    @FXML
    TableColumn<ComponenteArreglo, Integer> cantidadCompCol;
    @FXML
    Button cancelarBtn;
    @FXML
    Button aceptarBtn;
    @FXML
    Button agregarComponenteBtn;
    @FXML
    Button quitarComponenteBtn;
    private Arreglo arreglo;
    String dir = "", file = "", caterogira="", ruta="images/";
    File imgfile;
    Stage primaryStage;
    boolean actionResult = false;
    SimpleDateFormat dateFNomArchivo = new SimpleDateFormat("yyMMddHHmmss");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imagenArreglo.setCursor(Cursor.HAND);

        this.arreglo = new Arreglo();

        this.nombreCompCol.setCellValueFactory(new PropertyValueFactory<ComponenteArreglo, String>("nombreComponente"));
        this.cantidadCompCol.setCellValueFactory(new PropertyValueFactory<ComponenteArreglo, Integer>("cantidad"));
        //al soltar la imagen
        imagenArreglo.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent t) {
                Dragboard db = t.getDragboard();
                boolean success = false;
                if (db.hasFiles()) {
                    success = true;
                    String filePath = null;
                    for (File file : db.getFiles()) {
                        filePath = file.getAbsolutePath();
                        System.out.println(filePath);
                    }
                }
                t.setDropCompleted(success);
                t.consume();
            }
        });

        //al presionar la imagen
        imagenArreglo.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                obtenerArchivo();
            }
        });
        this.aceptarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                doGuardar();
            }
        });

        this.cancelarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                doCancelar();
            }
        });
    }
    FilenameFilter filter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            if (name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".jpeg")) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void obtenerArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccione la imagen");       
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todas las imágenes", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg")
            );

        imgfile =  fileChooser.showOpenDialog(primaryStage);
        
        if (imgfile == null) {
            return;
        }
        
        file = imgfile.getName();
        dir = imgfile.getPath();

        if (!file.toLowerCase().endsWith(".jpg") && !file.toLowerCase().endsWith(".jpeg")) {
            FXDialog.showMessageDialog("Tipo de imagen no válida.", "Agregar foto", Message.INFORMATION);
            return;
        }

        if (file.equals("") || dir.equals("")) {
            return;
        }

        arreglo.setFoto(file);
        Image imagen = new Image(imgfile.toURI().toString());
        this.imagenArreglo.setImage(imagen);
    }

    public void setCategoria(String Categoria, String Ruta){
        this.caterogira = Categoria;
        this.ruta = Ruta;
    }
    
    public static void copyFile(File inFile, File outFile) throws IOException {
        if (inFile.getCanonicalPath().equals(outFile.getCanonicalPath())) {
            // inFile and outFile are the same;
            // hence no copying is required.
            return;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(inFile));
            out = new BufferedOutputStream(new FileOutputStream(outFile));
            for (int c = in.read(); c != -1; c = in.read()) {
                out.write(c);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    private boolean datosArreglo() {
        if (this.conceptoTxt.getText().equals("")
                || this.precioUnitarioTxt.getText().equals("") || this.procedimientoTxt.getText().equals("")) {
            FXDialog.showMessageDialog("Ingrese los datos faltantes", "Ingrese datos", Message.INFORMATION);
            return false;
        }
        float precio;

        try {
            precio = Float.valueOf(this.precioUnitarioTxt.getText());
        } catch (Exception ex) {
            this.precioUnitarioTxt.setPromptText("Número");
            return false;
        }

        arreglo.setConcepto(conceptoTxt.getText());
        arreglo.setDescripcion(descripcionTxt.getText());
        arreglo.setPrecio(precio);
        arreglo.setActivo(activoChk.isSelected());
        arreglo.setProcedimiento(procedimientoTxt.getText());
        arreglo.setCategoria(caterogira);
        
        return true;
    }

    private void doGuardar() {
        primaryStage = (Stage) aceptarBtn.getScene().getWindow();

        System.out.println("Ruta final: " + ruta);
        
        
        if (!datosArreglo()) {
            return;
        }

        if (arreglo.getFoto() == null || arreglo.getFoto().equals("")) {
            FXDialog.showMessageDialog("Agregue la foto del arreglo.", "Datos del arreglo", Message.INFORMATION);
            return;
        }

        //copiar archivo
        if (imgfile != null) {
            try {
                String nombreImg = dateFNomArchivo.format(new Date()) + arreglo.getIdProducto() + ".jpg";
                arreglo.setFoto(nombreImg);
                copyFile(imgfile, new File(ruta + "/" + nombreImg));
                
                System.out.println("Ruta final: " + ruta + "/" + nombreImg);
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + " >>>>\n " + ex.getStackTrace());
            }
        }

        this.arreglo = ArreglosDataSource.saveArreglo(arreglo);
        if (arreglo != null) {
            FXDialog.showMessageDialog("El arreglo ha sido guardado.", "Arreglo guardado", Message.INFORMATION);
            actionResult = true;
            primaryStage.close();
        } else {
            FXDialog.showMessageDialog("Ocurrió un error al intentar guardar el arreglo.", "Error:", Message.ERROR);
            actionResult = false;
            primaryStage.close();
        }

    }

    private void doCancelar() {
        primaryStage = (Stage) cancelarBtn.getScene().getWindow();
        actionResult = false;
        primaryStage.close();

    }

    private void cargarDatosArreglo() {
        if (arreglo == null) {
            FXDialog.showMessageDialog("No se proporcionó el arreglo.\nSe guardará como nuevo arreglo.", "ERROR:", Message.INFORMATION);
            arreglo = new Arreglo();
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.claveProductoTxt.setText(this.arreglo.getIdProducto() + "");
        this.conceptoTxt.setText(this.arreglo.getConcepto());
        this.precioUnitarioTxt.setText(this.arreglo.getPrecio() + "");
        this.activoChk.setSelected(this.arreglo.isActivo());
        this.ultimaActualizacionLabel.setText(format.format(arreglo.getUltimaActualizacion()));
        this.procedimientoTxt.setText(arreglo.getProcedimiento());

        imgfile = new File(ruta + "/" + arreglo.getFoto());
        Image imagen = new Image(imgfile.toURI().toString());
        this.imagenArreglo.setImage(imagen);
    }

    private void cargarComponentesTableData() {
        try {
            if (arreglo != null) {
                componentesArregloTbl.setItems(FXCollections.observableList(arreglo.getComponentes()));
            }
        } catch (Exception ex) {
        }
    }

    /**
     * @return the arreglo
     */
    public Arreglo getArreglo() {
        return arreglo;
    }

    /**
     * @param arreglo the arreglo to set
     */
    public void setArreglo(Arreglo arreglo) {
        this.arreglo = arreglo;
        cargarDatosArreglo();
        cargarComponentesTableData();
    }

    public boolean ActionResult() {
        return actionResult;
    }
}
