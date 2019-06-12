package com.example.rasik.protibadi_ee;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by RASIK on 30-Mar-17.
 */

public class NumberEditDelete extends AppCompatActivity implements View.OnClickListener  {

    private static final String SELECT_SQL = "SELECT * FROM persons";

    private SQLiteDatabase db;

    private Cursor c;

    EditText editTextId,editTextNumber;
    Button btnPrev, btnNext, btnSave, btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.numbereditdelete);

        openDatabase();


        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);


        btnPrev = (Button) findViewById(R.id.btnPrev);
        btnNext = (Button) findViewById(R.id.btnNext);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        showRecords();

    }


    protected void showRecords() {
        String id = c.getString(0);
        String number = c.getString(1);
        editTextId.setText(id);
        editTextNumber.setText(number);

    }

    protected void moveNext() {
        if (!c.isLast())
            c.moveToNext();

        showRecords();
    }

    protected void movePrev() {
        if (!c.isFirst())
            c.moveToPrevious();

        showRecords();

    }

    protected void openDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
    }


    protected void saveRecord() {
        String number = editTextNumber.getText().toString().trim();

        String id = editTextId.getText().toString().trim();

        String sql = "UPDATE persons SET number='" + number + "' WHERE id=" + id + ";";

        if (number.equals("")) {
            Toast.makeText(getApplicationContext(), "You cannot save blank values", Toast.LENGTH_LONG).show();
            return;
        }

        db.execSQL(sql);
        Toast.makeText(getApplicationContext(), "Records Saved Successfully", Toast.LENGTH_LONG).show();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToPosition(Integer.parseInt(id));
    }

    private void deleteRecord() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want delete this person?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String id = editTextId.getText().toString().trim();

                        String sql = "DELETE FROM persons WHERE id=" + id + ";";
                        db.execSQL(sql);
                        Toast.makeText(getApplicationContext(), "Record Deleted", Toast.LENGTH_LONG).show();
                        c = db.rawQuery(SELECT_SQL,null);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



    @Override
    public void onClick(View v) {

        if (v == btnNext) {
            moveNext();
        }

        if (v == btnPrev) {
            movePrev();
        }

        if (v == btnSave) {
            saveRecord();
        }

        if (v == btnDelete) {
            deleteRecord();
        }

    }
}
