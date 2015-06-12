/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.dialog;

/**
 *
 * @author alfonso
 */
public enum Message {
   ERROR("error.png"),
    INFORMATION("information.png"),
    WARNING("warning.png");
    private String ico;

    private Message(String ico) {
        this.ico = ico;
    }

    protected String getIcon() {
        return ico;
    }
}
