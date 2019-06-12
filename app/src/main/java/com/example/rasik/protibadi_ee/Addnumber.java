package com.example.rasik.protibadi_ee;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by RASIK on 27-Mar-17.
 */

public class Addnumber extends AppCompatActivity implements View.OnClickListener {

    Button btnAddnum, btnEditDelete;
    EditText etnum;
    SQLiteDatabase db;


    Cursor c;
    static final String SELECT_SQL = "SELECT * FROM persons";
    //SQLiteDatabase db;
    ListView lv1;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addnumber);

        createDatabase();

        c = db.rawQuery(SELECT_SQL, null);

        lv1 = (ListView) findViewById(R.id.lv2);
        openDatabase();
        if(c.getCount()!=0){
        showR();}

        btnAddnum = (Button) findViewById(R.id.btnaddnum);
        btnEditDelete = (Button) findViewById(R.id.btnEditDelete);
        etnum = (EditText) findViewById(R.id.etnum);


        btnAddnum.setOnClickListener(this);
        btnEditDelete.setOnClickListener(this);







    }

    void showR() {

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();


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

        lv1.setAdapter(adapter);



    }

    protected void createDatabase(){
        db=openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS persons(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, number VARCHAR NOT NULL);");
    }

    protected void insertIntoDB(){
        String NUMBER = etnum.getText().toString().trim();

        if(NUMBER.equals("")){
            Toast.makeText(getApplicationContext(),"Please insert a number", Toast.LENGTH_LONG).show();
            return;
        }

        String query = "INSERT INTO persons (number) VALUES('"+NUMBER+"');";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();
    }



    protected void openDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()== R.id.btnaddnum){
            insertIntoDB();
            showR();


}

        if(v.getId()== R.id.btnEditDelete){
            Intent intent = new Intent(this, NumberEditDelete.class);
            startActivity(intent);
        }


    }
}
