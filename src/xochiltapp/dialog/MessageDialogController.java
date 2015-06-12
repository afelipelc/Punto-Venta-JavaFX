/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.dialog;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author alfonso
 */
public class MessageDialogController extends FXDialog {
  @FXML static HBox headerPane;
  @FXML static ImageView icon;
  @FXML static Label lblHeader;
  @FXML static Label lblMsg;
    
  @FXML private void ok(ActionEvent evt) {
      getInstance().primaryStage.close();
  }
}
