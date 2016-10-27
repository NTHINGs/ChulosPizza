package Funciones;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Mauricio Martinez 12041156
 * ____  __  ___  __  __  __   ___     __  _  _  __   _  _  ___   __   ___    __   ___    ___  ___ 
 *(_  _)/  \(  ,\(  )/ _)/  \ / __)   (  )( )( )(  ) ( \( )(_  ) (  ) (   \  /  \ / __)  (   \(  _)
 *  )( ( () )) _/ )(( (_( () )\__ \   /__\ \\// /__\  )  (  / /  /__\  ) ) )( () )\__ \   ) ) )) _)
 * (__) \__/(_)  (__)\__)\__/ (___/  (_)(_)(__)(_)(_)(_)\_)(___)(_)(_)(___/  \__/ (___/  (___/(___)
 * ___  ___   __    __  ___    __   __  __   __   __  __  __  _  _ 
 *(  ,\(  ,) /  \  / _)(  ,)  (  ) (  \/  ) (  ) / _)(  )/  \( \( )
 * ) _/ )  \( () )( (/\ )  \  /__\  )    (  /__\( (_  )(( () ))  ( 
 *(_)  (_)\_)\__/  \__/(_)\_)(_)(_)(_/\/\_)(_)(_)\__)(__)\__/(_)\_)
 */
public class FileChuser {
    JFileChooser chooser = new JFileChooser();
    JFrame frame;

    public FileChuser() {
       
    }
    
    public String obtenerDirectorio(String extension){
        String directorio=""; 
        chooser.setDialogTitle("Guardar");
        chooser.setMultiSelectionEnabled(false);
        int sel = chooser.showSaveDialog(null);
        if (sel == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            directorio=selectedFile.getAbsolutePath()+extension;
        } 
        return directorio;
    }
}
