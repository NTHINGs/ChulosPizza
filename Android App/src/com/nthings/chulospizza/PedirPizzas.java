package com.nthings.chulospizza;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nthings.chulospizza.CatalogoAdapter.customButtonListener;
import com.nthings.chulospizza.util.CatalogoPizzas;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ProofOfPayment;

public class PedirPizzas extends Activity implements customButtonListener{
	TextView numeropizzas;
	TextView pizzas;
	TextView total;
	String[] pedidoarray;
	ListView pedido;
	private ArrayList<CatalogoPizzas> catalogoarray;
	private CatalogoAdapter adapter;
	int contador=0;
	int contadorpizzas[];
	String [] precios;
	String[] ingredientes;
	boolean desborde=false;
	private static PayPalConfiguration config = new PayPalConfiguration()

    // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
    // or live (ENVIRONMENT_PRODUCTION)
    .environment(PayPalConfiguration.ENVIRONMENT_NO_NETWORK)

    .clientId("Aav46vMZipy3okz5SX8f7pc_M_YyJBBRk83mf20-N74HEV89jYI7k9_r1Xzoz97B80UeISdHiXD5-PYy");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pedir_pizzas);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//Declaracion de variables
		contador=0;
		Button menospizza=(Button) findViewById(R.id.menospizzas);
		Button maspizza=(Button) findViewById(R.id.maspizzas);
		total=(TextView)findViewById(R.id.total);
		numeropizzas=(TextView)findViewById(R.id.numeropizzas);
		pizzas=(TextView)findViewById(R.id.textView1);
		

		//Acciones de los botones
		maspizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	//Aumenta en uno el numero de pizzas
                numeropizzas.setText(Integer.toString((Integer.parseInt(numeropizzas.getText().toString())+1)));
                //Agregar una "s" a "Pizza" para que se distinga que ya está en plural
                pizzas.setText("Pizzas");
                
                reset();
            }
        });
		
		menospizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numeropizzas.getText().toString().equals("1")){
                	//No se puede decrementar mas el numero de pizzas
                	Toast.makeText(getBaseContext(), "¡No puedes pedir cero pizzas!", Toast.LENGTH_LONG).show(); 
                }else{
                	if(numeropizzas.getText().toString().equals("2")){
                    	//Condicion para quitarle la "s" a "Pizzas" ya que regresa a singular
                		pizzas.setText("Pizza");
                    }
                	//Decrementa en uno el numero de pizzas
                	numeropizzas.setText(Integer.toString((Integer.parseInt(numeropizzas.getText().toString())-1)));
                	
                    reset();
                }
                
            }
        });
		
		//Llenar listas
		pedido=(ListView)findViewById(R.id.pedido);
		pedidoarray=new String[5];
        ListView catalogo=(ListView)findViewById(R.id.pizzas);
        catalogoarray=new ArrayList<CatalogoPizzas>();
        contadorpizzas=new int[5];
        precios=new String[5];
        ingredientes=new String[5];
        reset();
        rellenarArrayList();
        adapter=new CatalogoAdapter(this,catalogoarray);
        adapter.setCustomButtonListner(PedirPizzas.this);  
        catalogo.setAdapter(adapter);
        catalogo.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
            	// Comparamos si no se quieren elegir mas pizzas de las establecidas
        		if(desborde==false){
        			//Aumentan los contadores
        			contador=contador+1;
        			contadorpizzas[position]=contadorpizzas[position]+1;
        			//Se compara si aún podemos elegir pizzas y en caso de que ya sea mayor se asigna desborde y se mostra el mensaje 
        			if(contador>Integer.parseInt(numeropizzas.getText().toString())){
        				Toast.makeText(getBaseContext(), "¡Ya elegiste tus "+numeropizzas.getText().toString()+" pizzas!", Toast.LENGTH_LONG).show(); 
        				desborde=true;
        			}else{
        				//Se agrega la pizza elegida a la lista
        				pedidoarray[position]=contadorpizzas[position]+" "+catalogoarray.get(position).getPizza();
        				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PedirPizzas.this, R.layout.pedido, pedidoarray); 
        		        pedido.setAdapter(spinnerArrayAdapter);
        		        //Se suma el precio de la pizza elegida al total
        		        if(total.getText().toString().equals("--------")){
        		        	total.setText(precios[position]);
        		        }else{
        		        	total.setText(Double.toString(Double.parseDouble(total.getText().toString())+Double.parseDouble(precios[position])));
        		        }
        			}
        		}else{
        			Toast.makeText(getBaseContext(), "¡Ya elegiste tus "+numeropizzas.getText().toString()+" pizzas!", Toast.LENGTH_LONG).show(); 
        		}
            }
        });
        
        //Pagar
        Button pagar=(Button)findViewById(R.id.pagar);
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	/*Intent intencion = new Intent(PedirPizzas.this, Pagar.class);
            	intencion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	intencion.putExtra("total", total.getText().toString());
                startActivity(intencion);*/
            	Intent intent = new Intent(view.getContext(), PaymentActivity.class);

        	    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        	    startService(intent); 
        	    onBuyPressed(view);
            }
        });
        
	}
	
	@Override
	public void onDestroy() {
	    stopService(new Intent(this, PayPalService.class));
	    super.onDestroy();
	}
	
	public void onBuyPressed(View pressed) {

	    PayPalPayment payment = new PayPalPayment(new BigDecimal(total.getText().toString()), "MXN", "Pizzas",
	            PayPalPayment.PAYMENT_INTENT_SALE);

	    Intent intent = new Intent(this, PaymentActivity.class);

	    // send the same configuration for restart resiliency
	    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

	    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

	    startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
	    if (resultCode == Activity.RESULT_OK) {
	        PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
	        if (confirm != null) {
	            try {
	                Log.i("paymentExample", confirm.toJSONObject().toString(4));
	                ProofOfPayment proof=confirm.getProofOfPayment();
	                if(proof.getState().equals("approved")){
	                	Intent intencion = new Intent(PedirPizzas.this, Final.class);
	                    startActivity(intencion);
	                    PedirPizzas.this.finish();
	                }else{
	                	Toast.makeText(getBaseContext(), "¡ERROR EN PAGO!", Toast.LENGTH_LONG).show(); 
	                }
	                // TODO: send 'confirm' to your server for verification.
	                // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
	                // for more details.

	            } catch (JSONException e) {
	                Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
	            }
	        }
	    }
	    else if (resultCode == Activity.RESULT_CANCELED) {
	        Log.i("paymentExample", "The user canceled.");
	    }
	    else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
	        Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
	    }
	}
	private void rellenarArrayList() {
		catalogoarray.add(new CatalogoPizzas("Express", R.drawable.pizza1,"$"+precios[0]));
		catalogoarray.add(new CatalogoPizzas("Tres Carnes", R.drawable.pizza1,"$"+precios[1]));
		catalogoarray.add(new CatalogoPizzas("Vegetariana", R.drawable.pizza1,"$"+precios[2]));
		catalogoarray.add(new CatalogoPizzas("Peperonni", R.drawable.pizza1,"$"+precios[3]));
		catalogoarray.add(new CatalogoPizzas("Tradicional", R.drawable.pizza1,"$"+precios[4]));
	}
	
	private void reset(){
		//Este metodo inicializa arreglos y regresa a cero los contadores
		pedido.setAdapter(null);
		precios[0]="89.00";
		precios[1]="110.50";
		precios[2]="99.99";
		precios[3]="95.00";
		precios[4]="90.00";
		ingredientes[0]="-Jamón\n-Salami";
		ingredientes[1]="-Jamón\n-Carne de res\n-Carne de cerdo";
		ingredientes[2]="-Champiñones\n-Peperonni de soya";
		ingredientes[3]="-Jamón\n-Peperonni";
		ingredientes[4]="-Jamón\n-Champiñones";
		contadorpizzas[0]=0;
		contadorpizzas[1]=0;
		contadorpizzas[2]=0;
		contadorpizzas[3]=0;
		contadorpizzas[4]=0;
		pedidoarray[0]=" ";
		pedidoarray[1]=" ";
		pedidoarray[2]=" ";
		pedidoarray[3]=" ";
		pedidoarray[4]=" ";
		desborde=false;
    	contador=0;
    	total.setText("--------");
	}

	@Override
	public void onButtonClickListner(int position, String value) {
		// Muestra los ingredientes de la pizza elegida
		new AlertDialog.Builder(this)
	    .setTitle("Ingredientes")
	    .setMessage(ingredientes[position])
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            
	        }
	     }).show();
	}
}
