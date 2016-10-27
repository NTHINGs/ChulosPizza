package com.nthings.chulospizza.util;

import android.content.Context;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.sql.*;

/**
 * Created by nthings on 5/05/15.
 * Esta clase se encarga de manejar la conexión con la base de datos
 * y realiza todas las consultas que son necesarias por la app.
 */
public class BDManager {
    public BDManager(){

    }
    //Metodo regresa conexion
    public Connection getConexion(Context contexto,String basededatos)
    {
        Connection conexion=null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String servidor = "jdbc:mysql://nthings.me:3306/"+basededatos;
            conexion= DriverManager.getConnection(servidor, "chulo", "chulospizza");
        }
        catch(ClassNotFoundException ex)
        {
            Toast.makeText(contexto, "Error clase no encontrada"+ex.getMessage(), Toast.LENGTH_LONG).show();
            conexion=null;
        }
        catch(SQLException ex)
        {
            Toast.makeText(contexto, "Error SQL"+ex.getMessage(), Toast.LENGTH_LONG).show();
            conexion=null;
        }
        catch(Exception ex)
        {
            Toast.makeText(contexto, "Error Excepcion"+ex.getMessage(), Toast.LENGTH_LONG).show();
            conexion=null;
        }
        finally
        {
            return conexion;
        }
    }

    public boolean ejecutar(Context contexto,String sql) {
        try {
            Statement sentencia = getConexion(contexto,"chulospizza").createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            sentencia.executeUpdate(sql);
            sentencia.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }        return true;
    }

    public ResultSet consultar(Context contexto,String sql) {
        ResultSet resultado;
        try {
            Statement sentencia = getConexion(contexto,"chulospizza").createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return resultado;
    }

    public Object[][] obtenerCatalogo(ResultSet rs){
        Object obj[][]=null;
        try{
            rs.last();
            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();
            int numFils =rs.getRow();
            obj=new Object[numFils][numCols];
            int j = 0;
            rs.beforeFirst();
            while (rs.next()){
                obj[j][0]=rs.getString(1);
                obj[j][1]=IOUtils.toByteArray(rs.getBinaryStream(2));
                obj[j][2]=rs.getString(3);
                j++;
            }
        }
        catch(Exception e){

        }
        return obj;
    }
}
