/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.dialog;

/**
 *
 * @author alfonso
 */
public enum DialogType {
  CONFIRMATION("ConfirmationDialog.fxml"),
    INPUT("InputDialog.fxml"),
    MESSAGE("MessageDialog.fxml");
    
    private String fxml;
    
    private DialogType(String fxml) {
        this.fxml = fxml;
    }
    
    protected String getFXML() {
        return fxml;
    }
}
