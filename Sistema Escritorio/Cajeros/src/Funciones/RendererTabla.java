package Funciones;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

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
public class RendererTabla extends DefaultTableCellRenderer{
    Color fondo;
    Color letra;
    
    public RendererTabla(Color fondo, Color letra){
        super();
        this.fondo=fondo;
        this.letra=letra;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean isSelected, boolean hasFocus, int fila, int columna){
        Component comp=super.getTableCellRendererComponent(tabla, valor, isSelected, hasFocus, fila, columna);
        comp.setBackground(fondo);
        comp.setForeground(letra);
        return comp;
    }
}