/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xochiltapp.modules.catalogo;
import java.util.*;
import java.io.*;
/**
 *
 * @author Alfonso
 */
public class FindImage {
  private File actualDir;
  private File[] temp;
  private List<String> images;
  private String[] pintar;

  public FindImage(){
  }
  public String[] getImage( String ss) throws Exception{
    images = new ArrayList<>();
    actualDir = new File(ss);
    temp = actualDir.listFiles();
    for (File pf : temp) {
       if (pf.isFile() && ((getFileExtend(pf).indexOf("jpg") !=-1)||(getFileExtend(pf).indexOf("JPG") !=-1))) {
        images.add(pf.getName());
      }
    }
    pintar = (String[])images.toArray(new String[0]);
    return pintar;
  }
  public String getFileExtend(File f){
    if (f.getName().indexOf(".") == -1) {
      return "";
    } else {
      return f.getName().substring(f.getName().length() - 3, f.getName().length());
    }
  }
}
