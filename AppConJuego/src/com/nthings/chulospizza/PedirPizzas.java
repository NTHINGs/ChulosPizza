package com.nthings.chulospizza;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nthings.chulospizza.util.CatalogoAdapter;
import com.nthings.chulospizza.util.CatalogoPizzas;
import com.nthings.chulospizza.util.PedidoPizzas;
import com.nthings.chulospizza.util.CatalogoAdapter.customButtonListener;
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
	ListView pedido;
	private ArrayList<CatalogoPizzas> catalogoarray;
	private ArrayList<String> pedidopizzas;
	private CatalogoAdapter adapter;
	private ArrayAdapter<String> adapterpedido;
	int contador=0;
	int contadorpizzas[];
	String [] precios;
	String[] ingredientes;
	boolean desborde=false;
	int idcliente=0;
	double totala=0;
	double ivaa=0;
	double totalnetoa=0;
	ListView catalogo;
	ProgressDialog dialogo1;
	ProgressDialog dialogo2;
	int menu=0;
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
		Bundle bundle = getIntent().getExtras();
		this.idcliente=bundle.getInt("idcliente");
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
		pedidopizzas=new ArrayList<String>();
		catalogo=(ListView)findViewById(R.id.pizzas);
		catalogoarray=new ArrayList<CatalogoPizzas>();
		Connect();
        
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
	    PayPalPayment payment = new PayPalPayment(new BigDecimal(Double.parseDouble(total.getText().toString())), "MXN", "Pizzas",
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
	                	Connect2();
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
	
	private void reset(){
		//Este metodo inicializa arreglos y regresa a cero los contadores
		pedido.setAdapter(null);
		for(int i=0; i<menu;i++){
			contadorpizzas[i]=0;
		}
		pedidopizzas.clear();
		desborde=false;
    	contador=0;
    	totala=0;
    	ivaa=0;
    	totalnetoa=0;
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

	private class Connect extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://nthings.me:3306/chulospizza", "chulo", "chulospizza");
				Statement statement = connection.createStatement();
				Statement statement2 = connection.createStatement();
				Statement statement3 = connection.createStatement();
				int j=0;
				ResultSet rs=statement.executeQuery("SELECT precio,foto,nombre FROM pizzas;");
				ResultSet rs1=statement3.executeQuery("SELECT COUNT(*) FROM pizzas");
				while(rs1.next()){
					menu=rs1.getInt(1);
				}
				contadorpizzas=new int[menu];
		        precios=new String[menu];
		        ingredientes=new String[menu];
				while (rs.next()) {
					precios[j] = rs.getString("precio");
					Drawable drw = Drawable.createFromStream(rs.getBinaryStream("foto"), "articleImage");
					String nombrepiz = rs.getString("nombre");
					catalogoarray.add(new CatalogoPizzas(nombrepiz, drw, precios[j]));
					ResultSet rs2 = statement2.executeQuery("SELECT I.nombre FROM ingredientes I,combinaciones C,pizzas P WHERE I.idingredientes=C.ingredientes_idingredientes AND C.pizzas_idpizzas=P.idpizzas AND P.nombre='" + nombrepiz + "' ");
					while (rs2.next()) {
						ingredientes[j] = ingredientes[j] + "\n-" + rs2.getString("nombre");
					}
					j++;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return response;
		}
		
		@Override
        protected void onPreExecute(){
        	super.onPreExecute();
        	dialogo1.show();
        }

		@Override
		protected void onPostExecute(String result) {
			adapter=new CatalogoAdapter(PedirPizzas.this,catalogoarray);
			adapter.setCustomButtonListner(PedirPizzas.this);
			catalogo.setAdapter(adapter);
			catalogo.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					// Comparamos si no se quieren elegir mas pizzas de las establecidas
					if(!desborde){
						//Aumentan los contadores
						contador=contador+1;
						contadorpizzas[position]=contadorpizzas[position]+1;
						//Se compara si aún podemos elegir pizzas y en caso de que ya sea mayor se asigna desborde y se mostra el mensaje
						if(contador>Integer.parseInt(numeropizzas.getText().toString())){
							Toast.makeText(getBaseContext(), "¡Ya elegiste tus "+numeropizzas.getText().toString()+" pizzas!", Toast.LENGTH_LONG).show();
							desborde=true;
						}else{
							//Se agrega la pizza elegida a la lista
							TextView nombre = (TextView) view.findViewById(R.id.textView1);
							pedidopizzas.add(nombre.getText().toString());
							String[]pedidoarray=pedidopizzas.toArray(new String[pedidopizzas.size()]);
							ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(PedirPizzas.this, R.layout.pedido, pedidoarray);
							pedido.setAdapter(spinnerArrayAdapter);
							//Se suma el precio de la pizza elegida al total
							double precio=Double.parseDouble(precios[position]);
							double totalneto=(precio*0.16)+precio;
							if(total.getText().toString().equals("--------")){
								total.setText(Double.toString(totalneto));
								totala=precio;
								totalnetoa=totalneto;
								ivaa=(precio*0.16);
							}else{
								total.setText(Double.toString(Double.parseDouble(total.getText().toString())+totalneto));
								totala=totala+precio;
								totalnetoa=totalnetoa+totalneto;
								ivaa=ivaa+(precio*0.16);
							}
						}
					}else{
						Toast.makeText(getBaseContext(), "¡Ya elegiste tus "+numeropizzas.getText().toString()+" pizzas!", Toast.LENGTH_LONG).show();
					}
				}
			});
			reset();
			ingredientes[0]="";
			dialogo1.dismiss();
		}
	}

	public void Connect() {
		dialogo1=new ProgressDialog(PedirPizzas.this);
    	dialogo1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	dialogo1.setMessage("Cargando pizzas...");
    	dialogo1.setCancelable(false);
		Connect task = new Connect();
		task.execute();

	}

	private class Connect2 extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				Connection connection = DriverManager.getConnection("jdbc:mysql://nthings.me:3306/chulospizza", "chulo", "chulospizza");
				Statement statement = connection.createStatement();
				Statement statement2 = connection.createStatement();
				statement.execute("INSERT INTO pedidos(clientes_idclientes,cantidad,total,iva,totalneto) VALUES('"+idcliente+"','"+numeropizzas.getText().toString()+"','"+totala+"','"+ivaa+"','"+totalnetoa+"');");
				ResultSet consultapedido = statement.executeQuery("SELECT AUTO_INCREMENT FROM information_schema.TABLES where TABLE_SCHEMA='chulospizza' and TABLE_NAME='pedidos';");
				int idpedido=0;
				while (consultapedido.next()) {
					idpedido = (consultapedido.getInt(1)) - 1;
				}
				for (int i = 0; i < pedidopizzas.size(); i++) {
					if(pedidopizzas.isEmpty()){
						Log.e("Insert", "PEDIDOARRAY VACIO");
					}else {
						ResultSet consultapizza = statement.executeQuery("SELECT idpizzas FROM pizzas WHERE nombre='" + pedidopizzas.get(i).toString() + "';");
						Log.e("Insert", "ENTRO AL SELECT"+pedidopizzas.get(i).toString());
						while (consultapizza.next()) {
							statement2.execute("INSERT INTO detalleventas (`pizzas_idpizzas`,`pedidos_idpedidos`) VALUES ('" + consultapizza.getString("idpizzas") + "','" + idpedido + "');");
							Log.e("Insert", "INSERT EN DETALLEVENTAS)");
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return response;
		}
		
		 @Override
	        protected void onPreExecute(){
	        	super.onPreExecute();
	        	dialogo2.show();
	        }

		@Override
		protected void onPostExecute(String result) {
			dialogo2.dismiss();
			Intent intencion = new Intent(PedirPizzas.this, Final.class);
			startActivity(intencion);
			PedirPizzas.this.finish();
		}
	}

	public void Connect2() {
		dialogo2=new ProgressDialog(PedirPizzas.this);
    	dialogo2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	dialogo2.setMessage("Registrando tu pedido...");
    	dialogo2.setCancelable(false);
		Connect2 task = new Connect2();
		task.execute();

	}
}
