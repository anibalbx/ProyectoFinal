package com.example.anibal.neumaticosapp;


import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class EliminarActivity extends AppCompatActivity {
    private ImageButton btnBuscar;
    private ImageButton btnCamara;
    private ImageButton btnClear;
    private Spinner spiBusqueda;
    private ListView lstRuedas;
    private EditText txtBusqueda;


    private final String[] tipoBusqueda =
            new String[]{"Referencia","Marca","Modelo","Medida"};

    private String eleccion="";


    private ArrayList<Rueda> datosa= new ArrayList<Rueda>();

    private SQLiteDatabase db;
    private Cursor c=null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        btnBuscar= (ImageButton) findViewById(R.id.btnBuscar);
        btnCamara= (ImageButton) findViewById(R.id.btnCamara);
        btnClear= (ImageButton) findViewById(R.id.btnClear);
        spiBusqueda = (Spinner) findViewById(R.id.spiBusqueda);
        lstRuedas= (ListView) findViewById(R.id.lstRuedas);
        txtBusqueda = (EditText) findViewById(R.id.txtBusqueda);






        PcSQLiteHelperTire usdbh =new PcSQLiteHelperTire(this, "DBTire", null, 1 );
        db = usdbh.getWritableDatabase();



//ListView
        buscarTodos();
        registerForContextMenu(lstRuedas);







//Spinner
        ArrayAdapter<String> adaptador =
                new ArrayAdapter <String>(this,
                        android.R.layout.simple_spinner_item, tipoBusqueda);
        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spiBusqueda.setAdapter(adaptador);


        spiBusqueda.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        eleccion = spiBusqueda.getSelectedItem().toString();
                        Toast.makeText(getApplicationContext(), "Busqueda por: "+eleccion, Toast.LENGTH_LONG).show();


                    }

                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });

//Botones
        btnBuscar.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {
                datosa.clear();



                    if(eleccion.equalsIgnoreCase("Referencia")) {
                        c = db.rawQuery("SELECT * FROM Tires WHERE referencia='" + txtBusqueda.getText().toString() + "'", null);
                    }else if (eleccion.equalsIgnoreCase("Marca")){
                        c = db.rawQuery("SELECT * FROM Tires WHERE marca='" + txtBusqueda.getText().toString() + "'", null);
                    }else if(eleccion.equalsIgnoreCase("Modelo")){
                        c = db.rawQuery("SELECT * FROM Tires WHERE modelo='" + txtBusqueda.getText().toString() + "'", null);

                    }else if(eleccion.equalsIgnoreCase("Medida")){
                        c = db.rawQuery("SELECT * FROM Tires WHERE medida='" + txtBusqueda.getText().toString() + "'", null);

                    }





                    if (c.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya m?s registros

                        System.out.println("Existe el registro");
                        do {
                            String ref = c.getString(0);
                            String mar = c.getString(1);
                            String mod = c.getString(2);
                            String med = c.getString(3);
                            String stock = c.getString(4);
                            String idc = c.getString(5);
                            String idv = c.getString(6);
                            String tipoaux = c.getString(7);

                            Rueda nuevarueda = new Rueda(mar,mod,med,stock,ref);
                            datosa.add(nuevarueda);






                        } while(c.moveToNext());


                    }else{

                            Toast.makeText(getApplicationContext(), "No hay registros de la busqueda", Toast.LENGTH_LONG).show();


                        }

                    AdaptadorRuedas adaptadorlst =
                            new AdaptadorRuedas(EliminarActivity.this, datosa);

                    lstRuedas= (ListView) findViewById(R.id.lstRuedas);

                    lstRuedas.setAdapter(adaptadorlst);



            }


        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(EliminarActivity.this);
                scanIntegrator.initiateScan();


            }
        });

        btnClear.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                txtBusqueda.setText("");
                buscarTodos();

            }
        });


        //Accion ListView

/*

        lstRuedas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                System.out.println("Long press :"+pos+" id: "+id);
                System.out.println(datosa.get(pos).getMarca());




                        FragmentManager fragmentManager = getSupportFragmentManager();
                        DialogoSeleccion dialogo = new DialogoSeleccion();
                        dialogo.show(fragmentManager,"hopa");




                return true;
            }
        });   */
    }


    //result camara

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            txtBusqueda.setText(scanContent);
            spiBusqueda.setSelection(0);
            btnBuscar.performClick();

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }



    }

    //menus


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();


         if(v.getId() == R.id.lstRuedas)
        {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;


            menu.setHeaderTitle(
                    datosa.get(info.position).getMarca()+" "+datosa.get(info.position).getModelo()+" Ref:"+datosa.get(info.position).getReferencia());


            inflater.inflate(R.menu.menu_eliminar, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {

            case R.id.opEliminar:
                System.out.println(datosa.get(info.position).getMarca());


                createDialogEliminar(Integer.parseInt(datosa.get(info.position).getReferencia()));

                buscarTodos();


                return true;
            case R.id.opRetirar:

                createExampleDialog(datosa.get(info.position).getReferencia(),Integer.parseInt(datosa.get(info.position).getStock())).show();

                return true;
            case R.id.opEditar:
                Intent i = new Intent(getApplicationContext(), AgregarActivity.class);
                i.putExtra("referencia",datosa.get(info.position).getReferencia());
                finish();
                startActivity(i);



                return true;
            case R.id.opAnadir:


                createAddDialog(datosa.get(info.position).getReferencia(),Integer.parseInt(datosa.get(info.position).getStock())).show();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }



    class AdaptadorRuedas extends ArrayAdapter<Rueda> {

        public AdaptadorRuedas(Context context, ArrayList<Rueda> datosa) {
            super(context, R.layout.lista_forma, datosa);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_forma, null);

            TextView txtMarca = (TextView)item.findViewById(R.id.txtMarca);
            txtMarca.setText(datosa.get(position).getMarca().toUpperCase());

            TextView txtModelo = (TextView)item.findViewById(R.id.txtModelo);
            txtModelo.setText(datosa.get(position).getModelo());

            TextView txtMedida = (TextView)item.findViewById(R.id.txtMedida);
            String medi=datosa.get(position).getMedida();
            if(medi.length()>5){
                txtMedida.setText(medi.substring(0,3)+"/"+medi.substring(3,5)+"R"+medi.substring(5, medi.length()));

            }else{

                txtMedida.setText(medi);
            }





            TextView txtStock = (TextView)item.findViewById(R.id.txtStock);
            txtStock.setText(datosa.get(position).getStock());
            if(Integer.parseInt(datosa.get(position).getStock())>10){
                item.setBackgroundColor(Color.parseColor("#88E365"));


            }else if(Integer.parseInt(datosa.get(position).getStock()) <=10 && Integer.parseInt(datosa.get(position).getStock()) >0 ){
                item.setBackgroundColor(Color.parseColor("#E6ED53"));

            }else{
                item.setBackgroundColor(Color.parseColor("#FF5F5F"));

            }

            TextView txtReferencia = (TextView)item.findViewById(R.id.txtReferencia);
            txtReferencia.setText(datosa.get(position).getReferencia());

            return(item);
        }
    }

    public void buscarTodos(){

        datosa.clear();

        c = db.rawQuery("SELECT * FROM Tires ORDER BY referencia", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m?s registros

            System.out.println("Existe el registro");
            do {
                String ref = c.getString(0);
                String mar = c.getString(1);
                String mod = c.getString(2);
                String med = c.getString(3);
                String stock = c.getString(4);
                String idc = c.getString(5);
                String idv = c.getString(6);
                String tipoaux = c.getString(7);

                Rueda nuevarueda = new Rueda(mar,mod,med,stock,ref);
                datosa.add(nuevarueda);

            } while(c.moveToNext());


        }else{
            Toast.makeText(getApplicationContext(),"Todavía no hay registros",Toast.LENGTH_LONG).show();

        }
        AdaptadorRuedas adaptadorlst =
                new AdaptadorRuedas(EliminarActivity.this, datosa);

        lstRuedas= (ListView) findViewById(R.id.lstRuedas);

        lstRuedas.setAdapter(adaptadorlst);



    }
    public void buscarRef(String refe){
        datosa.clear();

        c = db.rawQuery("SELECT * FROM Tires WHERE referencia='"+refe+"'", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m?s registros

            System.out.println("Existe el registro");
            do {
                String ref = c.getString(0);
                String mar = c.getString(1);
                String mod = c.getString(2);
                String med = c.getString(3);
                String stock = c.getString(4);
                String idc = c.getString(5);
                String idv = c.getString(6);
                String tipoaux = c.getString(7);

                Rueda nuevarueda = new Rueda(mar,mod,med,stock,ref);
                datosa.add(nuevarueda);

            } while(c.moveToNext());


        }else{
            Toast.makeText(getApplicationContext(),"Todavía no hay registros",Toast.LENGTH_LONG).show();

        }
        AdaptadorRuedas adaptadorlst =
                new AdaptadorRuedas(EliminarActivity.this, datosa);

        lstRuedas= (ListView) findViewById(R.id.lstRuedas);

        lstRuedas.setAdapter(adaptadorlst);

    }

    public void createDialogEliminar(final int ref){

        AlertDialog.Builder seleccion = new AlertDialog.Builder(EliminarActivity.this);
        seleccion.setTitle("Eliminar");
        seleccion.setMessage("¿Está seguro?, el registro seleccionado se eliminara.");
        seleccion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db.execSQL("DELETE FROM Tires WHERE referencia="+ref);
                Toast.makeText(EliminarActivity.this,"Articulo con referencia "+ref+ " ,eliminado", Toast.LENGTH_LONG).show();

                buscarTodos();
                dialog.dismiss();






            }
        });
        seleccion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(EliminarActivity.this,"Operación cancelada", Toast.LENGTH_LONG).show();
                dialog.dismiss();



            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }




    private Dialog createExampleDialog(String ref, final int oldStock) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Retirar Stock");
        builder.setMessage("Por favor introduzca el stock a reirar para la referencia: "+ref);

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(0);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        final String refe=ref;

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                int minusStock=Integer.parseInt(value);
                if(minusStock>oldStock){
                    Toast.makeText(getApplicationContext(),"El numero introducido es mayor que el de existencias",Toast.LENGTH_SHORT).show();
                }else{
                    int newstock = oldStock-minusStock;
                    ContentValues addStockRegistro = new ContentValues();
                    addStockRegistro.put("stock", newstock);
                    db.update("Tires", addStockRegistro, "referencia='"+refe+"'", null);
                    Toast.makeText(getApplicationContext(),"Se han retirado "+minusStock+" para la referencia "+refe,Toast.LENGTH_SHORT).show();
                    buscarRef(refe);

                }


                return;
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.create();
    }


    private Dialog createAddDialog(String ref, final int oldStock) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Añadir Stock");
        builder.setMessage("Por favor introduzca el stock a añadir para la referencia: "+ref);

        // Use an EditText view to get user input.
        final EditText input = new EditText(this);
        input.setId(0);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        final String refe=ref;

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();

                int minusStock=Integer.parseInt(value);



                int newstock = oldStock+minusStock;
                ContentValues addStockRegistro = new ContentValues();
                addStockRegistro.put("stock", newstock);
                db.update("Tires", addStockRegistro, "referencia='"+refe+"'", null);
                Toast.makeText(getApplicationContext(),"Se han añadido "+minusStock+" para la referencia "+refe+ "TOTAL:"+ newstock,Toast.LENGTH_SHORT).show();

                buscarRef(refe);



                return;
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        return builder.create();
    }


}
