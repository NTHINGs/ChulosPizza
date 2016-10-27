package com.nthings.chulospizza;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.nthings.chulospizza.util.BDManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;

public class Principal extends Activity implements LocationListener {
	GoogleMap mMap;
	double latitud, longitud;
    BDManager bd=new BDManager();
    String nombre=" ";
    int idcliente = 0;
    String direccion;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setUpMapIfNeeded(); 
		Button boton=(Button)findViewById(R.id.pagar);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Codigo para obtener la ubicacion
            	//Abrir la actividad para elegir pizzas
                LayoutInflater li = LayoutInflater.from(Principal.this);
                final View prompt = li.inflate(R.layout.dialogo1, null);
                AlertDialog.Builder Dialogo = new AlertDialog.Builder(Principal.this);
                Dialogo.setView(prompt);
                Dialogo
                        .setTitle("¿A Que Nombre Hacemos El Pedido?")
                        .setCancelable(false)
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        nombre = ((EditText) prompt.findViewById(R.id.editText)).getText().toString();
                                        direccion = obtenerDireccionString(latitud, longitud);
                                        if (nombre.equals("")) {
                                            Toast.makeText(Principal.this, "Necesitamos tu nombre", Toast.LENGTH_LONG).show();
                                        } else {
                                            Connect();
                                        }
                                    }
                                }
                        );
                            AlertDialog alerta = Dialogo.create();
                            alerta.show();
                        }
            });
	}

    private String obtenerDireccionString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Mi direccion", "" + strReturnedAddress.toString());
            } else {
                Log.w("Mi direccion", "No se recupero");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Mi direccion", "no se puede obtener!");
        }
        return strAdd;
    }
	
	private void setUpMapIfNeeded() {
		// Configuramos el objeto GoogleMaps con valores iniciales.
		   if (mMap == null) {
		      //Instanciamos el objeto mMap a partir del MapFragment definido bajo el Id "map"
		      mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		      // Chequeamos si se ha obtenido correctamente una referencia al objeto GoogleMap
		      if (mMap != null) {
		        // El objeto GoogleMap ha sido referenciado correctamente 
		        //ahora podemos manipular sus propiedades
		        
		        //Seteamos el tipo de mapa 
		        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		        	
		        //Activamos la capa o layer MyLocation
		        mMap.setMyLocationEnabled(true);
		        
		        // Getting LocationManager object from System Service LOCATION_SERVICE
	            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	 
	            // Creating a criteria object to retrieve provider
	            Criteria criteria = new Criteria();
	 
	            // Getting the name of the best provider
	            String provider = locationManager.getBestProvider(criteria, true);
	 
	            // Getting Current Location
	            Location location = locationManager.getLastKnownLocation(provider);
	 
	            if(location!=null){
	                onLocationChanged(location);
	            }
	            locationManager.requestLocationUpdates(provider, 20000, 0, this);
		      }
		   }
		}
	
	@Override
    public void onLocationChanged(Location location) {
 
        // Getting latitude of the current location
        latitud = location.getLatitude();
 
        // Getting longitude of the current location
        longitud= location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitud, longitud);
 
        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    private class Connect extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://nthings.me:3306/chulospizza", "chulo", "chulospizza");
                Statement statement = connection.createStatement();
                Log.e("Insert", "VALUES('" + nombre + "','" + direccion + "','" + latitud + "','" + longitud + "')");
                statement.execute("INSERT INTO clientes (nombre,direccion,latitud,longitud) VALUES('" + nombre + "','" + direccion + "','" + latitud + "','" + longitud + "')");
                ResultSet consultapedido = statement.executeQuery("SELECT AUTO_INCREMENT FROM information_schema.TABLES where TABLE_SCHEMA='chulospizza' and TABLE_NAME='clientes';");
                while (consultapedido.next()) {
                    idcliente = (consultapedido.getInt(1)) - 1;
                    Log.e("Insert", "IDCLIENTE"+idcliente);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intencion = new Intent(Principal.this, PedirPizzas.class);
            intencion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intencion.putExtra("idcliente", idcliente);
            startActivity(intencion);
        }
    }

    public void Connect() {
        Connect task = new Connect();
        task.execute();

    }
}
