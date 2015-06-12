/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xochiltapp.utils;

/**
 *
 * @author afelipelc
 */
public final class StringUtilities {
    /**
   * Creates a string left padded to the specified width with the supplied padding character.
   * @param fieldWidth the length of the resultant padded string.
   * @param padChar a character to use for padding the string.
   * @param s the string to be padded.
   * @return the padded string.
   */
  public static String pad(int fieldWidth, char padChar, String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = s.length(); i < fieldWidth; i++) {
      sb.append(padChar);
    }
    sb.append(s);
 
    return sb.toString();
  }
}
