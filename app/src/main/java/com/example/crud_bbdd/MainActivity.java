package com.example.crud_bbdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.app.ProgressDialog.show;

public class MainActivity extends AppCompatActivity {

    Button botonInsertar, botonActualizar, botonBorrar, botonBuscar;

    EditText textoId, textoNombre, textoApellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonInsertar=(Button)findViewById(R.id.insertar);
        botonActualizar=(Button)findViewById(R.id.actualizar);
        botonBorrar=(Button)findViewById(R.id.borrar);
        botonBuscar=(Button)findViewById(R.id.buscar);

        textoId = (EditText) findViewById(R.id.id);
        textoNombre=(EditText)findViewById(R.id.nombre);
        textoApellido=(EditText)findViewById(R.id.apellido);

        final BBDD_Helper helper  = new BBDD_Helper(this);

        botonInsertar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Gets the data repository in write mode
                SQLiteDatabase db = helper.getWritableDatabase();

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(Estructura_BBDD.NOMBRE_COLUMNA1, textoId.getText().toString());
                values.put(Estructura_BBDD.NOMBRE_COLUMNA2, textoNombre.getText().toString());
                values.put(Estructura_BBDD.NOMBRE_COLUMNA3, textoApellido.getText().toString());

                // Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(Estructura_BBDD.TABLE_NAME, null, values);

                Toast.makeText(getApplicationContext(), "Se guardo el registro con clave : " +
                        newRowId, Toast.LENGTH_LONG).show();
            }
        });
        botonActualizar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getReadableDatabase();

                // New value for one column
               // String title = "MyNewTitle";
                ContentValues values = new ContentValues();
                values.put(Estructura_BBDD.NOMBRE_COLUMNA2, textoNombre.getText().toString());
                values.put(Estructura_BBDD.NOMBRE_COLUMNA3, textoApellido.getText().toString());

                // Which row to update, based on the title
                String selection = Estructura_BBDD.NOMBRE_COLUMNA1+ " LIKE ?";
                String[] selectionArgs = { textoId.getText().toString()};

                int count = db.update(
                       Estructura_BBDD.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                Toast.makeText(getApplicationContext(), "Se actualizó el registro ",Toast.LENGTH_LONG).show();
            }
        });
        botonBorrar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                // Define 'where' part of query.
                String selection = Estructura_BBDD.NOMBRE_COLUMNA1 + " LIKE ?";
                // Specify arguments in placeholder order.
                String[] selectionArgs = { textoId.getText().toString() };
                // Issue SQL statement.
                 db.delete(Estructura_BBDD.TABLE_NAME, selection, selectionArgs);

                Toast.makeText(getApplicationContext(), "Se borró el registro con clave : " +
                        textoId.getText().toString(), Toast.LENGTH_LONG).show();
                //PARA QUE NO SALGA NADA EN LAS FILAS BORRADAS
                textoId.setText("");
                textoNombre.setText("");
                textoApellido.setText("");

            }
        });
        botonBuscar.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getReadableDatabase();

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = {
                        // BaseColumns._ID,  el id no se pone ya que es el que buscará la información de la fila
                        Estructura_BBDD.NOMBRE_COLUMNA2,
                        Estructura_BBDD.NOMBRE_COLUMNA3,
                };

                // Filter results WHERE "title" = 'My Title'
                String selection = Estructura_BBDD.NOMBRE_COLUMNA1 + " = ?";
                String[] selectionArgs = {textoId.getText().toString()};

                // How you want the results sorted in the resulting Cursor
               /* String sortOrder =
                        FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";*/
               //HAGO UN TRY/CATCH YA QUE SI NO DARIA ERROR AL PULSAR UN ID QUE NO EXISTE
                try {
                    Cursor c = db.query(
                            Estructura_BBDD.TABLE_NAME,   // LA TABLA DE LA CONSULTA
                            projection,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            null              //ORDENAMIENTO DE LA CONSULTA con sortOrder
                    );
                    c.moveToFirst();    //MUEVE EL ID AL PRIMERO
                    textoNombre.setText(c.getString(0));
                    textoApellido.setText(c.getString(1));

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No se encontró ningun registro ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}