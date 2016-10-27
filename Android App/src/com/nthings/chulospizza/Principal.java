package com.nthings.chulospizza;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Principal extends Activity implements LocationListener {
	GoogleMap mMap;
	double latitud, longitud;
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
            	Intent intencion = new Intent(Principal.this, PedirPizzas.class);
            	intencion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intencion);
                Toast.makeText(getBaseContext(), "Latitud:"+latitud+" Longitud:"+longitud, Toast.LENGTH_LONG).show(); 
            }
        });
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
 
}
