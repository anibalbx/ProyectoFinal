package com.example.anibal.neumaticosapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class AgregarActivity extends AppCompatActivity {
    private EditText txtReferencia;
    private EditText txtStock;
    private EditText txtMarca;
    private EditText txtModelo;
    private EditText txtMedida;
    private EditText txtIDC;
    private EditText txtIDV;


    private TextView lblStock;
    private TextView lblMarca;
    private TextView lblModelo;
    private TextView lblMedida;
    private TextView lblIDC;
    private TextView lblIDV;
    private TextView lblTipo;


    private Spinner spiTipo;
    private ImageButton btnBuscar;
    private ImageButton btnAgregar;
    private ImageButton btnCamara;

    private String Referencia;
    private boolean add = false;
    private boolean edit = false;
    private boolean addStock= false;

    private String stock;
    private String tipo;
    private int pos;

    private SQLiteDatabase db;
    private final String[] tipoVehiculo =
            new String[]{"Turismo","Furgoneta","4x4","RoF","Otro"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        txtReferencia = (EditText) findViewById(R.id.txtReferencia);
        txtStock = (EditText) findViewById(R.id.txtStock);
        txtMarca = (EditText) findViewById(R.id.txtMarca);
        txtModelo = (EditText) findViewById(R.id.txtModelo);
        txtMedida = (EditText) findViewById(R.id.txtMedida);
        txtIDC = (EditText) findViewById(R.id.txtIdc);
        txtIDV = (EditText) findViewById(R.id.txtIdv);
        spiTipo = (Spinner) findViewById(R.id.spiTipo);
        btnBuscar = (ImageButton) findViewById(R.id.btnBuscar);
        btnAgregar = (ImageButton) findViewById(R.id.btnAgregar);
        btnCamara = (ImageButton) findViewById(R.id.btnCamara);

        lblStock    = (TextView)findViewById(R.id.lblStock) ;
        lblMarca= (TextView)findViewById(R.id.lblMarca) ;
        lblModelo= (TextView)findViewById(R.id.lblModelo) ;
        lblMedida= (TextView)findViewById(R.id.lblMedida) ;
        lblIDC= (TextView)findViewById(R.id.lblIdc) ;
        lblIDV= (TextView)findViewById(R.id.lblIdv) ;
        lblTipo= (TextView)findViewById(R.id.lblTipo) ;

        PcSQLiteHelperTire usdbh =new PcSQLiteHelperTire(this, "DBTire", null, 1 );
        db = usdbh.getWritableDatabase();

        ArrayAdapter<String> adaptador =
                new ArrayAdapter <String>(this,
                        android.R.layout.simple_spinner_item, tipoVehiculo);
        adaptador.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spiTipo.setAdapter(adaptador);


        spiTipo.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        tipo= tipoVehiculo[position];







                    }

                    public void onNothingSelected(AdapterView<?> parent) {


                    }
                });


        setOff();


        //intents


        Bundle bundle = getIntent().getExtras();





        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Referencia = txtReferencia.getText().toString();
                if (Referencia.equals("")) {
                    Toast.makeText(getApplicationContext(), "Por favor rellene REFERENCIA", Toast.LENGTH_LONG).show();


                }else{
                    Cursor c = db.rawQuery("SELECT * FROM Tires WHERE referencia='"+Referencia+"'", null);

                    if (c.moveToFirst()) {
                        //Recorremos el cursor hasta que no haya m?s registros

                        System.out.println("Existe el registro");
                        do {
                            String ref = c.getString(0);
                            String mar = c.getString(1);
                            String mod = c.getString(2);
                            String med = c.getString(3);
                            stock = c.getString(4);
                            String idc = c.getString(5);
                            String idv = c.getString(6);
                            String tipoaux = c.getString(7);
                            // tipo

                            for(int i=0;i<tipoVehiculo.length;i++){
                                if(tipoaux.equalsIgnoreCase(tipoVehiculo[i])){
                                    pos=i;

                                }
                            }
                            System.out.println(pos);

                            txtReferencia.setText(ref);
                            txtStock.setText(stock);
                            txtMarca.setText(mar);
                            txtModelo.setText(mod);
                            txtMedida.setText(med);
                            txtIDC.setText(idc);
                            txtIDV.setText(idv);
                            spiTipo.setSelection(pos);



                        } while(c.moveToNext());

                        createDialogAdd();


                    }else{
                        createDialogNuevo();
                        limpiar();
                        spiTipo.setSelection(0);
                        System.out.println("No hay registros");
                        setOn();
                        add=true;
                    }

                }







            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add){
                    try{
                        ContentValues nuevoRegistro = new ContentValues();
                        nuevoRegistro.put("referencia",txtReferencia.getText().toString().toLowerCase());
                        nuevoRegistro.put("marca", txtMarca.getText().toString().toLowerCase());
                        nuevoRegistro.put("modelo", txtModelo.getText().toString().toLowerCase());
                        nuevoRegistro.put("medida", Integer.parseInt(txtMedida.getText().toString()));
                        nuevoRegistro.put("stock", Integer.parseInt(txtStock.getText().toString()));
                        nuevoRegistro.put("idc", Integer.parseInt(txtIDC.getText().toString()));
                        nuevoRegistro.put("idv", txtIDV.getText().toString().toLowerCase());
                        nuevoRegistro.put("tipo", tipo.toLowerCase());


                        db.insert("Tires", null, nuevoRegistro);

                        System.out.println("Insertado");
                        createDialogAñadirRueda(txtReferencia.getText().toString(),Integer.parseInt(txtStock.getText().toString()));
                        limpiar();
                        setOff();
                        txtReferencia.setText("");

                        add=false;


                    }catch (NumberFormatException nx){
                        Toast.makeText(getApplicationContext(),"Por favor introduzca los datos correctamente",Toast.LENGTH_SHORT).show();
                    }






                }else if(edit){
                    try{
                        ContentValues editRegistro = new ContentValues();
                        String ref =txtReferencia.getText().toString();
                        editRegistro.put("marca", txtMarca.getText().toString().toLowerCase());
                        editRegistro.put("modelo", txtModelo.getText().toString().toLowerCase());
                        editRegistro.put("medida", Integer.parseInt(txtMedida.getText().toString()));
                        editRegistro.put("stock", Integer.parseInt(txtStock.getText().toString()));
                        editRegistro.put("idc", Integer.parseInt(txtIDC.getText().toString()));
                        editRegistro.put("idv", txtIDV.getText().toString().toLowerCase());
                        editRegistro.put("tipo", tipo.toLowerCase());
                        db.update("Tires", editRegistro, "referencia='"+ref+"'", null);
                        Toast.makeText(getApplicationContext(), "Rueda editada", Toast.LENGTH_LONG).show();
                        limpiar();
                        setOff();
                        edit=false;
                        txtReferencia.setText("");


                    }catch(NumberFormatException nx){
                        Toast.makeText(getApplicationContext(),"Por favor introduzca los datos correctamente",Toast.LENGTH_SHORT).show();

                    }



                }else if(addStock){
                    try{
                        int oldStock = Integer.valueOf(stock);
                        int newStock = Integer.valueOf(txtStock.getText().toString());
                        int finalStock=oldStock+newStock;
                        ContentValues addStockRegistro = new ContentValues();
                        String ref =txtReferencia.getText().toString();
                        addStockRegistro.put("stock", finalStock);
                        db.update("Tires", addStockRegistro, "referencia='"+ref+"'", null);
                        createDialogAñadirStock(newStock,finalStock,ref);
                        setOff();
                        limpiar();
                        addStock=false;
                        txtReferencia.setText("");



                    }catch(NumberFormatException nx){
                        Toast.makeText(getApplicationContext(),"Por favor introduzca los datos correctamente",Toast.LENGTH_SHORT).show();

                    }







                } else{
                    Toast.makeText(getApplicationContext(), "Por favor seleccione una opción", Toast.LENGTH_SHORT).show();

                }



            }
        });

        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(AgregarActivity.this);
                scanIntegrator.initiateScan();


            }
        });

        if(bundle!=null){


            txtReferencia.setText(bundle.getString("referencia"));
            btnBuscar.performClick();



        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            txtReferencia.setText(scanContent);

        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }



    }
    public void setOff(){
        txtReferencia.setEnabled(true);
        txtStock.setEnabled(false);
        txtMarca.setEnabled(false);
        txtModelo.setEnabled(false);
        txtMedida.setEnabled(false);
        txtIDC.setEnabled(false);
        txtIDV.setEnabled(false);
        btnAgregar.setEnabled(false);
        spiTipo.setEnabled(false);

        lblStock.setTextColor(Color.parseColor("#FF5F5F"));
        lblMarca.setTextColor(Color.parseColor("#FF5F5F"));
        lblModelo.setTextColor(Color.parseColor("#FF5F5F"));
        lblMedida.setTextColor(Color.parseColor("#FF5F5F"));
        lblIDC.setTextColor(Color.parseColor("#FF5F5F"));
        lblIDV.setTextColor(Color.parseColor("#FF5F5F"));
        lblTipo.setTextColor(Color.parseColor("#FF5F5F"));



    }
    public void setOn(){
        txtReferencia.setEnabled(false);
        txtStock.setEnabled(true);
        txtMarca.setEnabled(true);
        txtModelo.setEnabled(true);
        txtMedida.setEnabled(true);
        txtIDC.setEnabled(true);
        txtIDV.setEnabled(true);
        btnAgregar.setEnabled(true);
        spiTipo.setEnabled(true);

        lblStock.setTextColor(Color.parseColor("#88E365"));
        lblMarca.setTextColor(Color.parseColor("#88E365"));
        lblModelo.setTextColor(Color.parseColor("#88E365"));
        lblMedida.setTextColor(Color.parseColor("#88E365"));
        lblIDC.setTextColor(Color.parseColor("#88E365"));
        lblIDV.setTextColor(Color.parseColor("#88E365"));
        lblTipo.setTextColor(Color.parseColor("#88E365"));



    }


    public void setStockOn(){
        txtReferencia.setEnabled(false);
        txtStock.setEnabled(true);
        lblStock.setTextColor(Color.parseColor("#88E365"));
        btnAgregar.setEnabled(true);


    }
    public void setStockOff(){
        txtStock.setEnabled(false);
        txtReferencia.setEnabled(true);
        lblStock.setTextColor(Color.parseColor("#FF5F5F"));
        btnAgregar.setEnabled(false);

    }


    public void limpiar(){

        //txtReferencia.setText("");
        txtStock.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtMedida.setText("");
        txtIDC.setText("");
        txtIDV.setText("");

    }

    public void createDialogAdd(){
        AlertDialog.Builder seleccion = new AlertDialog.Builder(AgregarActivity.this);
        seleccion.setTitle("El registro ya existe");
        seleccion.setMessage("El registro ya existe, seleccione una opción");
        seleccion.setPositiveButton("Añadir stock", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setOff();
                setStockOn();
                dialog.dismiss();
                txtStock.setText("");
                addStock=true;



            }
        });
        seleccion.setNegativeButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edit=true;
                dialog.dismiss();
                setOn();


            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }

    public void createDialogNuevo(){
        AlertDialog.Builder seleccion = new AlertDialog.Builder(AgregarActivity.this);
        seleccion.setTitle("El registro no existe");
        seleccion.setMessage("El registro no existe se creara uno nuevo");
        seleccion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();


            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }
    public void createDialogAñadirStock(int nuevo, int fin, String ref){
        AlertDialog.Builder seleccion = new AlertDialog.Builder(AgregarActivity.this);
        seleccion.setTitle("Stock añadido");
        seleccion.setMessage("Se ha añadido a la referencia "+ref+", "+nuevo+" unidades de stock, TOTAL: "+fin);
        seleccion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }

    public void createDialogAñadirRueda( String ref, int stock){
        AlertDialog.Builder seleccion = new AlertDialog.Builder(AgregarActivity.this);
        seleccion.setTitle("Nueva rueda añadida");
        seleccion.setMessage("Se ha añadido un articulo nuevo con REFERENCIA "+ref+" y con un STOCK: "+stock);
        seleccion.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }



}
