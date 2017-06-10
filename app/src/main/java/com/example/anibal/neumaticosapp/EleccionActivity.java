package com.example.anibal.neumaticosapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class EleccionActivity extends AppCompatActivity {
    private Button btnAgregar;
    private Button btnEliminar;
    private Button btnAvisos;
    private Button btnSincronizar;
    private ArrayList<RuedaCompleto> datosa =new ArrayList<RuedaCompleto>();
    private boolean nohayconexion =false;


    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion);
        btnAgregar = (Button)findViewById(R.id.btnAgregar);
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnAvisos = (Button)findViewById(R.id.btnConsultar);
        btnSincronizar = (Button)findViewById(R.id.btnSincronizar);


        PcSQLiteHelperTire usdbh =new PcSQLiteHelperTire(this, "DBTire", null, 1 );
        db = usdbh.getWritableDatabase();

        buscarTodos();



        btnAgregar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EleccionActivity.this, AgregarActivity.class);
                startActivity(intent);

            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EleccionActivity.this, EliminarActivity.class);
                startActivity(intent);

            }
        });
        btnAvisos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EleccionActivity.this, AvisosActivity.class);
                startActivity(intent);

            }
        });
        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buscarTodos();
                exportChartCsv(datosa);
                upload(Environment.getExternalStorageDirectory().getAbsolutePath() + "/charts.csv","neumaticos.csv");



            }
        });

    }



        // Activity code here

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_eleccion, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.opCerrar:
                    finish();
                    Intent intent = new Intent(EleccionActivity.this, MainActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.opSalir:
                    finish();
                    return true;

                case R.id.opSincro:
                    createDialogRecargar();

                default:
                    return super.onOptionsItemSelected(item);
            }
        }

    private void exportChartCsv(ArrayList<RuedaCompleto> datosa) {

        try {
            CSVWriter mWriter = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/charts.csv"));
            String[] mExportChartHeaders = {
                    "Referencia",
                    "Marca",
                    "Modelo",
                    "Medida",
                    "Stock",
                    "Idc",
                    "Idv",
                    "Tipo"
            };

            mWriter.writeNext(mExportChartHeaders);

            for (RuedaCompleto c : datosa) {
                String referencia = c.getReferencia();
                String marca = c.getMarca();
                String modelo = c.getModelo();
                String medida = c.getMedida();
                String stock = c.getStock();
                String idc = c.getIdc();
                String idv = c.getIdv();
                String tipo = c.getTipo();


                String[] mExportChart = {referencia, marca, modelo, medida, stock, idc, idv, tipo};
                mWriter.writeNext(mExportChart);
                System.out.println("Finish!!");
            }
            mWriter.close();
        } catch (IOException e) {
            System.out.println(e);

        }

    }


    public void buscarTodos (){
        if (!datosa.isEmpty()){
            datosa.clear();

        }



        Cursor c = db.rawQuery("SELECT * FROM Tires ORDER BY referencia", null);

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

                RuedaCompleto nuevarueda = new RuedaCompleto(mar,mod,med,stock,ref,idc,idv,tipoaux);
                datosa.add(nuevarueda);

            } while(c.moveToNext());


        }else{
            Toast.makeText(getApplicationContext(),"Todavía no hay registros",Toast.LENGTH_LONG).show();

        }




    }

    public void ReadCsv(){
        CSVReader csvReader = null;

        try
        {
            /**
             * Reading the CSV File
             * Delimiter is comma
             * Start reading from line 1
             */
            csvReader = new CSVReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/neumaticos.csv"),',','"',1);
            //employeeDetails stores the values current line
            String[] TireDetails = null;
            while((TireDetails = csvReader.readNext())!=null)
            {
                //Printing to the console
                System.out.println(TireDetails[0]);
                System.out.println(Arrays.toString(TireDetails));


                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put("referencia",TireDetails[0]);
                nuevoRegistro.put("marca",TireDetails[1] );
                nuevoRegistro.put("modelo", TireDetails[2]);
                nuevoRegistro.put("medida", Integer.parseInt(TireDetails[3]));
                nuevoRegistro.put("stock", Integer.parseInt(TireDetails[4]));
                nuevoRegistro.put("idc", Integer.parseInt(TireDetails[5]));
                nuevoRegistro.put("idv", TireDetails[6]);
                nuevoRegistro.put("tipo", TireDetails[7]);


                db.insert("Tires", null, nuevoRegistro);

                System.out.println("Insertado");
            }
        }
        catch(Exception ee)
        {
            ee.printStackTrace();
        }
        finally
        {
            try
            {
                //closing the reader
                csvReader.close();
            }
            catch(Exception ee)
            {
                ee.printStackTrace();
            }
        }
    }

    private void upload(final String name, final String finalname){
        class UploadAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(EleccionActivity.this, "Por favor espere", "Subiendo...");
            }

            @Override
            protected String doInBackground(String... params) {
                HttpFileUploader uploader = new HttpFileUploader("http://192.168.1.201/insertar_imagen.php", finalname);
                try {
                    uploader.doStart(new FileInputStream(name));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                }
                System.out.println("ERRRROR: "+uploader.getError());

                try{
                    if(uploader.getError().equals("error")){
                        System.out.println("Ha ocurrido un error de red");

                    }else{

                    }


                }catch (NullPointerException ex){


                }
                return null;
            }
            @Override
            protected void onPostExecute(String result){
                loadingDialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Sincronización exterior completa", Toast.LENGTH_LONG).show();







            }
        }
        UploadAsync la = new UploadAsync();
        la.execute(name);
    }
/*
    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        Dialog pdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = ProgressDialog.show(EleccionActivity.this, "Por favor espere", "descargarndo...");
        }


        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/neumaticos.csv");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }  catch (FileNotFoundException ex){
                Toast.makeText(getApplicationContext(),"Error de sincronización: " +ex,Toast.LENGTH_SHORT).show();



            }catch (IOException ix){
                Toast.makeText(getApplicationContext(),"Error de sincronización: " +ix,Toast.LENGTH_SHORT).show();


            }

            return null;
        }



        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pdialog.dismiss();
            ReadCsv();
            Toast.makeText(getApplicationContext(),"Base de datos recargada satisfactoriamente",Toast.LENGTH_LONG).show();

        }

    } */

    public void accionRecargar(){
        db.execSQL("DELETE FROM Tires");
        //new DownloadFileFromURL().execute("http://192.168.1.201/out/neumaticos.csv");
        final DownloadTask downloadTask = new DownloadTask(EleccionActivity.this);
         downloadTask.execute("http://192.168.1.201/out/neumaticos.csv");


    }

    public void createDialogRecargar(){

        AlertDialog.Builder seleccion = new AlertDialog.Builder(EleccionActivity.this);
        seleccion.setTitle("Recargar base de datos");
        seleccion.setMessage("¿Está seguro?, si acepta y no ha sincronizado antes todos los datos se perderan.");
        seleccion.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                accionRecargar();








            }
        });
        seleccion.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                dialog.dismiss();



            }
        });

        AlertDialog editDialog = seleccion.create();
        editDialog.show();
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private Dialog pdialog;
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = ProgressDialog.show(EleccionActivity.this, "Por favor espere", "descargarndo...");
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    System.out.println("NO HAY NADA");
                    nohayconexion =true;

                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();


                }else{
                    System.out.println("SI LO HAY");
                    nohayconexion =false;

                }





                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + "/neumaticos.csv");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                System.out.println(e);

            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            pdialog.dismiss();
            ReadCsv();

            if(nohayconexion){
                System.out.println("No hay conexion true");
                createDialogNoconexion();
                nohayconexion=false;
            }
            Toast.makeText(getApplicationContext(),"Base de datos recargada satisfactoriamente",Toast.LENGTH_LONG).show();

        }




    }

    public void createDialogNoconexion(){

        AlertDialog.Builder seleccion = new AlertDialog.Builder(EleccionActivity.this);
        seleccion.setTitle("Error de servidor");
        seleccion.setMessage("El fichero de comunicación no existe o no tiene aceso a internet," +
                " se cargara la información anterior.");
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

