package com.nthings.chulospizza;

import java.util.ArrayList;

import com.nthings.chulospizza.util.CatalogoPizzas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CatalogoAdapter extends BaseAdapter {
	 customButtonListener customListner;  
	  
	    public interface customButtonListener {  
	        public void onButtonClickListner(int position,String value);  
	    }  
	  
	    public void setCustomButtonListner(customButtonListener listener) {  
	        this.customListner = listener;  
	    }  
	private Context context;
	private ArrayList<CatalogoPizzas> datos;
	
	public CatalogoAdapter(Context contexto, ArrayList<CatalogoPizzas> datos){
		this.context=contexto;
		this.datos=datos;
	}
	
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		//Inflamos una nueva vista, que será la que se mostrará en la celda del listview.
		//Para ello creamos el inflater e inflamos
		LayoutInflater inflater=LayoutInflater.from(context);
		View item=inflater.inflate(R.layout.catalogopizza, null);

		final CatalogoPizzas dato=datos.get(position);
		//A partir de la vista, recogemos los controles que contiene
		//Recogemos el ImageView y le asignamos una foto
		ImageView imagen=(ImageView)item.findViewById(R.id.imageView1);
		imagen.setImageResource(dato.getimagenPizza());
		
		// Recogemos el TextView para mostrar el nombre y establecemos el nombre.
	    TextView nombre = (TextView) item.findViewById(R.id.textView1);
	    nombre.setText(dato.getPizza());
	    
	    TextView precio=(TextView) item.findViewById(R.id.textView2);
	    precio.setText(dato.getPrecio());
	    
	    Button ingredientes=(Button) item.findViewById(R.id.pagar);
	    final String temp = getItem(position).toString();  
	    ingredientes.setOnClickListener(new OnClickListener() {  
  
            @Override  
            public void onClick(View v) {  
                if (customListner != null) {  
                    customListner.onButtonClickListner(position,temp);  
                }  
  
            }  
        });  

	    return item;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
