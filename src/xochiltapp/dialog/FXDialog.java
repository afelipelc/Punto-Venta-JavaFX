/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.dialog;
import java.io.*;
import java.util.logging.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.stage.*;
/**
 *
 * @author alfonso
 */
public class FXDialog {
  private double initX;       // X-Coordinate location of the dialog
  private double initY;       // Y-Coordinate location of the dialog
    
    private Parent root;
    private Response response;
    private static FXDialog main;

    protected Stage primaryStage;

    public FXDialog() {
        primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
    }

    protected static FXDialog getInstance() {
        if(main == null) {
            main = new FXDialog();
        }
        return main;
    }
    
    /*
     * The purpose of this method is to change the scene depending on the speciied 
     * type of the dialog will be shown.
     * 
     * @param dialogType
     */
    private void replaceScene(DialogType dialogType) {
        try {
            root = FXMLLoader.load(getClass().getResource(dialogType.getFXML()));

            Scene scene = new Scene(root, Color.TRANSPARENT);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();

            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    initX = me.getScreenX() - primaryStage.getX();
                    initY = me.getScreenY() - primaryStage.getY();
                }
            });

            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me) {
                    primaryStage.setX(me.getScreenX() - initX);
                    primaryStage.setY(me.getScreenY() - initY);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FXDialog.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            System.gc();
        }
    }
    
    /*
     * The purpose of this method is to retain the selected action in the confirmation
     * dialog.
     * 
     * @param response;
     */
    protected void setReponse(Response response) { this.response = response; }

    public static void showMessageDialog(String message, String title, Message messageType) {
        getInstance().replaceScene(DialogType.MESSAGE);

        if (messageType == Message.ERROR) {
            MessageDialogController.icon.setImage(new Image("/xochiltapp/dialog/icons/" + messageType.getIcon()));
            MessageDialogController.headerPane.setStyle("-fx-background-color: red;");
        } else if (messageType == Message.INFORMATION) {
            MessageDialogController.icon.setImage(new Image("/xochiltapp/dialog/icons/" + messageType.getIcon()));
            MessageDialogController.headerPane.setStyle("-fx-background-color: blue;");

        } else if (messageType == Message.WARNING) {
            MessageDialogController.icon.setImage(new Image("/xochiltapp/dialog/icons/" + messageType.getIcon()));
            MessageDialogController.headerPane.setStyle("-fx-background-color: orange;");
        }
 
        MessageDialogController.lblHeader.setText(title);
        MessageDialogController.lblMsg.setText(message);
        
        getInstance().primaryStage.setTitle(messageType.toString());
        getInstance().primaryStage.showAndWait();
    }

    public static boolean showConfirmDialog(String caption, String title, ConfirmationType confirmType) {
        getInstance().replaceScene(DialogType.CONFIRMATION);
        
        if(confirmType == ConfirmationType.DELETE_OPTION) {
            ConfirmationDialogController.btnAccept.setText("Delete");
            ConfirmationDialogController.btnDecline.setDefaultButton(true);
            ConfirmationDialogController.btnDecline.requestFocus();
            ConfirmationDialogController.btnDecline.setText("Don't Delete");  
        }
        else if(confirmType == ConfirmationType.YES_NO_OPTION) {
            ConfirmationDialogController.btnAccept.setText("Yes");
            ConfirmationDialogController.btnAccept.setDefaultButton(true);
            ConfirmationDialogController.btnDecline.setText("No");  
        }
        else if(confirmType == ConfirmationType.ACCEPT_DECLINE_OPTION) {
            ConfirmationDialogController.btnAccept.setText("Accept");
            ConfirmationDialogController.btnAccept.setDefaultButton(true);
            ConfirmationDialogController.btnDecline.setText("Decline");  
        }
        
        ConfirmationDialogController.lblHeader.setText(title);
        ConfirmationDialogController.lblMsg.setText(caption);
        
        getInstance().primaryStage.setTitle("CONFIRMATION");
        getInstance().primaryStage.showAndWait();
        
        return (getInstance().response.getValue() ? true : false);
    }
    
    public static String showInputDialog(String caption, String title) {
        getInstance().replaceScene(DialogType.INPUT);
        
        InputDialogController.lblHeader.setText(title);
        InputDialogController.lblMsg.setText(caption);
        
        getInstance().primaryStage.showAndWait();
     
        return (getInstance().response.getValue() ? InputDialogController.inputField.getText().trim() : null);
    }
    
    protected enum Response {

        APPROVE(true),
        DECLINE(false);
        private boolean val;

        private Response(boolean val) {
            this.val = val;
        }

        public boolean getValue() {
            return val;
        }
    }
}
