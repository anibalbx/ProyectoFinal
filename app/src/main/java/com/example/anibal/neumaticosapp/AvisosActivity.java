package com.example.anibal.neumaticosapp;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AvisosActivity extends AppCompatActivity {
    private ListView lstSinStock;
    private ListView lstPocoStock;

    private ArrayList<Rueda> ruedassin= new ArrayList<Rueda>();
    private ArrayList<Rueda> ruedaspoco= new ArrayList<Rueda>();

    private SQLiteDatabase db;
    private Cursor c=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);
        lstSinStock=(ListView)findViewById(R.id.lstSinStock);
        lstPocoStock=(ListView)findViewById(R.id.lstPocoStock);

        PcSQLiteHelperTire usdbh =new PcSQLiteHelperTire(this, "DBTire", null, 1 );
        db = usdbh.getWritableDatabase();

        registerForContextMenu(lstSinStock);
        registerForContextMenu(lstPocoStock);

        buscarSin();
        buscarPoco();



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();


        if(v.getId() == R.id.lstSinStock)
        {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;


            menu.setHeaderTitle(
                    ruedassin.get(info.position).getMarca()+" "+ruedassin.get(info.position).getModelo()+" Ref:"+ruedassin.get(info.position).getReferencia());


            inflater.inflate(R.menu.menu_avisos_sin, menu);
        }

        if(v.getId() == R.id.lstPocoStock)
        {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo)menuInfo;


            menu.setHeaderTitle(
                    ruedaspoco.get(info.position).getMarca()+" "+ruedaspoco.get(info.position).getModelo()+" Ref:"+ruedaspoco.get(info.position).getReferencia());


            inflater.inflate(R.menu.menu_avisos_pocos, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
//sin
            case R.id.opAnadirSin:

                createAddDialog(ruedassin.get(info.position).getReferencia(),Integer.parseInt(ruedassin.get(info.position).getStock())).show();


                return true;
            case R.id.opEditarSin:

                Intent i = new Intent(getApplicationContext(), AgregarActivity.class);
                i.putExtra("referencia",ruedassin.get(info.position).getReferencia());
                finish();
                startActivity(i);

                return true;
            case R.id.opRetirarSin:
                createRetirarDialog(ruedassin.get(info.position).getReferencia(),Integer.parseInt(ruedassin.get(info.position).getStock())).show();

                return true;
            case R.id.opEliminarSin:
                createDialogEliminar(Integer.parseInt(ruedassin.get(info.position).getReferencia()));


                return true;

//pocos
            case R.id.opAnadirPoco:

                createAddDialog(ruedaspoco.get(info.position).getReferencia(),Integer.parseInt(ruedaspoco.get(info.position).getStock())).show();


                return true;
            case R.id.opEditarPoco:

                Intent a = new Intent(getApplicationContext(), AgregarActivity.class);
                a.putExtra("referencia",ruedaspoco.get(info.position).getReferencia());
                finish();
                startActivity(a);

                return true;
            case R.id.opRetirarPoco:
                createRetirarDialog(ruedaspoco.get(info.position).getReferencia(),Integer.parseInt(ruedaspoco.get(info.position).getStock())).show();

                return true;
            case R.id.opEliminarPoco:
                createDialogEliminar(Integer.parseInt(ruedaspoco.get(info.position).getReferencia()));


                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    private void buscarSin(){
        ruedassin.clear();

        c = db.rawQuery("SELECT * FROM Tires WHERE stock=0 ORDER BY referencia", null);

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
                ruedassin.add(nuevarueda);

            } while(c.moveToNext());


        }else{
            Toast.makeText(getApplicationContext(),"Todavía no hay registros sin stock",Toast.LENGTH_SHORT).show();

        }
        AdaptadorRuedas adaptadorlst =
                new AdaptadorRuedas(AvisosActivity.this, ruedassin);



        lstSinStock.setAdapter(adaptadorlst);
    }
    private void buscarPoco(){
        ruedaspoco.clear();

        c = db.rawQuery("SELECT * FROM Tires WHERE stock>0 AND stock<=10 ORDER BY referencia", null);

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
                ruedaspoco.add(nuevarueda);

            } while(c.moveToNext());


        }else{
            Toast.makeText(getApplicationContext(),"Todavía no hay registros de poco stock",Toast.LENGTH_SHORT).show();

        }
        AdaptadorRuedasSin adaptadorlst =
                new AdaptadorRuedasSin(AvisosActivity.this, ruedaspoco);



        lstPocoStock.setAdapter(adaptadorlst);
    }

    class AdaptadorRuedas extends ArrayAdapter<Rueda> {

        public AdaptadorRuedas(Context context, ArrayList<Rueda> datosa) {
            super(context, R.layout.lista_forma, datosa);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_forma, null);

            TextView txtMarca = (TextView)item.findViewById(R.id.txtMarca);
            txtMarca.setText(ruedassin.get(position).getMarca().toUpperCase());

            TextView txtModelo = (TextView)item.findViewById(R.id.txtModelo);
            txtModelo.setText(ruedassin.get(position).getModelo());

            TextView txtMedida = (TextView)item.findViewById(R.id.txtMedida);
            String medi=ruedassin.get(position).getMedida();
            if(medi.length()>5){
                txtMedida.setText(medi.substring(0,3)+"/"+medi.substring(3,5)+"R"+medi.substring(5, medi.length()));

            }else{

                txtMedida.setText(medi);
            }

            TextView txtStock = (TextView)item.findViewById(R.id.txtStock);
            txtStock.setText(ruedassin.get(position).getStock());
            if(Integer.parseInt(ruedassin.get(position).getStock())>10){
                item.setBackgroundColor(Color.parseColor("#88E365"));


            }else if(Integer.parseInt(ruedassin.get(position).getStock()) <=10 && Integer.parseInt(ruedassin.get(position).getStock()) >0 ){
                item.setBackgroundColor(Color.parseColor("#E6ED53"));

            }else{
                item.setBackgroundColor(Color.parseColor("#FF5F5F"));

            }

            TextView txtReferencia = (TextView)item.findViewById(R.id.txtReferencia);
            txtReferencia.setText(ruedassin.get(position).getReferencia());

            return(item);
        }
    }

    class AdaptadorRuedasSin extends ArrayAdapter<Rueda> {

        public AdaptadorRuedasSin(Context context, ArrayList<Rueda> datosa) {
            super(context, R.layout.lista_forma, datosa);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.lista_forma, null);

            TextView txtMarca = (TextView)item.findViewById(R.id.txtMarca);
            txtMarca.setText(ruedaspoco.get(position).getMarca().toUpperCase());

            TextView txtModelo = (TextView)item.findViewById(R.id.txtModelo);
            txtModelo.setText(ruedaspoco.get(position).getModelo());

            TextView txtMedida = (TextView)item.findViewById(R.id.txtMedida);
            String medi=ruedaspoco.get(position).getMedida();
            if(medi.length()>5){
                txtMedida.setText(medi.substring(0,3)+"/"+medi.substring(3,5)+"R"+medi.substring(5, medi.length()));

            }else{

                txtMedida.setText(medi);
            }

            TextView txtStock = (TextView)item.findViewById(R.id.txtStock);
            txtStock.setText(ruedaspoco.get(position).getStock());
            if(Integer.parseInt(ruedaspoco.get(position).getStock())>10){
                item.setBackgroundColor(Color.parseColor("#88E365"));


            }else if(Integer.parseInt(ruedaspoco.get(position).getStock()) <=10 && Integer.parseInt(ruedaspoco.get(position).getStock()) >0 ){
                item.setBackgroundColor(Color.parseColor("#E6ED53"));

            }else{
                item.setBackgroundColor(Color.parseColor("#FF5F5F"));

            }

            TextView txtReferencia = (TextView)item.findViewById(R.id.txtReferencia);
            txtReferencia.setText(ruedaspoco.get(position).getReferencia());

            return(item);
        }
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
                    buscarSin();
                    buscarPoco();



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


    private Dialog createRetirarDialog(String ref, final int oldStock) {

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
                    Toast.makeText(getApplicationContext(),"El numero introducido es mayor que el de existencias",Toast.LENGTH_LONG).show();
                }else{
                    int newstock = oldStock-minusStock;
                    ContentValues addStockRegistro = new ContentValues();
                    addStockRegistro.put("stock", newstock);
                    db.update("Tires", addStockRegistro, "referencia='"+refe+"'", null);
                    Toast.makeText(getApplicationContext(),"Se han retirado "+minusStock+" para la referencia "+refe,Toast.LENGTH_LONG).show();
                    buscarSin();
                    buscarPoco();

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

    public void createDialogEliminar(final int ref){

        AlertDialog.Builder seleccion = new AlertDialog.Builder(AvisosActivity.this);
        seleccion.setTitle("Eliminar");
        seleccion.setMessage("¿Está seguro?, el registro seleccionado se eliminara.");
        seleccion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                db.execSQL("DELETE FROM Tires WHERE referencia="+ref);
                Toast.makeText(AvisosActivity.this,"Articulo con referencia "+ref+ " ,eliminado", Toast.LENGTH_LONG).show();

                buscarSin();
                buscarPoco();
                dialog.dismiss();






            }
        });
        seleccion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(AvisosActivity.this,"Operación cancelada", Toast.LENGTH_LONG).show();
                dialog.dismiss();



            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }



}
