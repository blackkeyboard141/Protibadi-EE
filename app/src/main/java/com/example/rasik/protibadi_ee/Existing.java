package com.example.rasik.protibadi_ee;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by RASIK on 31-Mar-17.
 */

public class Existing extends AppCompatActivity {

    Cursor c;
    static final String SELECT_SQL = "SELECT * FROM persons";
    SQLiteDatabase db;
    ListView lv1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.existing);


        lv1 = (ListView) findViewById(R.id.lv1);
        openDatabase();
        showR();


        }


    void showR() {

        c = db.rawQuery(SELECT_SQL, null);
         if(c.getCount()!=0){c.moveToFirst();


        Toast.makeText(getApplicationContext()," "+c.getCount() , Toast.LENGTH_LONG).show();

       final String[] values= new String[c.getCount()];
        int i=0;

        values[0]= c.getString(1);
        i++;

        while(c.moveToNext()){
            values[i]= c.getString(1);
            i++;
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        lv1.setAdapter(adapter);}



    }


    protected void openDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
    }



}
