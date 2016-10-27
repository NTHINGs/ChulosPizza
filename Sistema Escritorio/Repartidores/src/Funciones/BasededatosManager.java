package Funciones;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Esta clase establece la conexion con la base de datos
 * 
 * autor: Mauricio Martinez
 */
public class BasededatosManager {

    public BasededatosManager(){

    }
    //Metodo regresa conexion
    public Connection getConexion(String basededatos)
    {
        Connection conexion=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://128.199.58.57:3306/"+basededatos;
            conexion= DriverManager.getConnection(servidor,"chulo","chulospizza");
        }
        catch(ClassNotFoundException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error clase no encontrada"+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error SQL"+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex, "Error Excepcion"+ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            conexion=null;
        }
        finally
        {
            return conexion;
        }
    }
    
    public boolean ejecutar(String sql) {
        try {
            Statement sentencia = getConexion("chulospizza").createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            sentencia.executeUpdate(sql);
            sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }        return true;
    }
    
    public ResultSet consultar(String sql) {
        ResultSet resultado;
        try {
            Statement sentencia = getConexion("chulospizza").createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }        
        return resultado;
    }
    
    //Método para llenar jTables mediante consultas SQL
    public DefaultTableModel llenarTabla(int numcolumnas, String [] nomcolumnas, ResultSet consulta){
        DefaultTableModel modelo=new DefaultTableModel();
        //Se crean las columnas del modelo dependiendo de los parametros que se le manden al método
        for(int i=0; i<numcolumnas;i++){
            modelo.addColumn(nomcolumnas[i]);
            System.out.println("columna agregada "+nomcolumnas[i]);
        }
        try {
            //El ciclo while recorre el objeto resultset y va metiendo las filas de la consulta en el modelo
            while(consulta.next()){
                System.out.println("entro al while");
                Object [] fila=new Object[numcolumnas];
                for(int i=0;i<numcolumnas;i++){
                    //El primer indice en un ResultSet es el 1 por lo que hay que sumar uno
                    System.out.println(consulta.getObject(i+1));
                    fila[i]=consulta.getObject(i+1);
                }
                modelo.addRow(fila);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BasededatosManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return modelo;
    }
    
}