package com.example.anibal.neumaticosapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txtUser;
    private EditText txtPass;
    private CheckBox cbxRemember;
    private Button btnEnter;
    private String User;
    private String Pass;
    private String PassMD;
    private SQLiteDatabase dbUser;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    public static final String USER_NAME = "USERNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<String> nombre = new ArrayList<String>();
        final List<String> contras = new ArrayList<String>();

        txtUser = (EditText)findViewById(R.id.txtUser);
        txtPass = (EditText)findViewById(R.id.txtPass);
        cbxRemember = (CheckBox)findViewById(R.id.cbxRemember);
        btnEnter = (Button)findViewById(R.id.btnEnter);
        final Context context = getApplicationContext();

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            txtUser.setText(loginPreferences.getString("username", ""));
            txtPass.setText(loginPreferences.getString("password", ""));
            cbxRemember.setChecked(true);
        }


        PcSQLiteHelperUser usdbh =new PcSQLiteHelperUser(this, "DBUsers", null, 1 );

        dbUser = usdbh.getWritableDatabase();

        Cursor c = dbUser.rawQuery("SELECT * FROM Users", null);

        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya m?s registros
            do {
                String nom = c.getString(0);
                String pass = c.getString(1);
                nombre.add(nom);
                contras.add(pass);

            } while(c.moveToNext());
        }


        btnEnter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                User= String.valueOf(txtUser.getText());
                Pass = String.valueOf(txtPass.getText());
                PassMD = passMD(Pass);


                for(int i=0;i<nombre.size();i++){

                    if(nombre.get(i).equals(User)){


                        if(contras.get(i).equals(PassMD)){
                            Intent intent = new Intent(MainActivity.this, EleccionActivity.class);
                            intent.putExtra(USER_NAME, User);
                            finish();
                            startActivity(intent);

                        }else{

                            Toast toast = Toast.makeText(context, "Error de usuario", Toast.LENGTH_LONG);
                            toast.show();
                        }

                    }else{
                    }

                }

                //shared preferences
                if (cbxRemember.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", User);
                    loginPrefsEditor.putString("password", Pass);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }








            }
        });




    }
    private String passMD(String pass){
        byte[] plainText;
        byte[] resumen;
        String resumenCadena;
        try{
            plainText = pass.getBytes("UTF-8");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update( plainText);
            resumen = messageDigest.digest();
            resumenCadena = bytesToHex(resumen);
            return resumenCadena;
        }
        catch(Exception ex){
            System.out.println(ex);
            return null;
        }



    }
    public static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
