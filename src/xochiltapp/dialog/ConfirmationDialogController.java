/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.dialog;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author alfonso
 */
public class ConfirmationDialogController extends MessageDialogController {

    @FXML static Button btnAccept;
    @FXML static Button btnDecline;
    
    @FXML protected void accept(ActionEvent evt) {
        getInstance().setReponse(Response.APPROVE);
        getInstance().primaryStage.close();
    }
    
    @FXML protected void decline(ActionEvent evt) {
        getInstance().setReponse(Response.DECLINE);
        getInstance().primaryStage.close();
    }
}
