package sample.com.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by web on 5/18/2017.
 */

public class sample extends Activity implements View.OnClickListener {


    Button load, display;
    // json array response url
    private String urlJsonArry = "http://api.androidhive.info/volley/person_array.json";

    private static String TAG = sample.class.getSimpleName();
    // Progress dialog
    private ProgressDialog pDialog;

    // Labels table name
    public static final String TABLE = "Student";

    // Labels Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_name = "name";
    public static final String KEY_email = "email";

    // property help us to keep data
    public int student_ID;
    public String name;
    public String email;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load=(Button)findViewById(R.id.load);
        display=(Button)findViewById(R.id.display);

        dbHelper = new DBHelper(this);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        load.setOnClickListener(this);
        display.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(R.id.load==view.getId()){

            makeJsonArrayRequest();
        }else if(R.id.display==view.getId()){

            Intent i=new Intent(sample.this,second.class);
            startActivity(i);

        }
    }

    private void makeJsonArrayRequest() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray res) {


                        try {
                            // Parsing json array response
                            // loop through each json object
                            Gson gson = new Gson();
                            reponse[]   response = gson.fromJson(res.toString(), reponse[].class);
                            insert(response);

                            Log.d(TAG, res.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        hidepDialog();
                        display.setVisibility(View.VISIBLE);


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void insert(reponse[] response) {



        reponse obj=new reponse();
        //Open connection to write data
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(sample.TABLE, null, null);

      for (int i=0;i<response.length;i++){
          ContentValues values = new ContentValues();
          values.put(sample.KEY_email,response[i].email);
          Log.d(TAG, response[i].email);
          values.put(sample.KEY_name, response[i].name);
          Log.d(TAG, response[i].name);
          // Inserting Row
          db.insert(sample.TABLE, null, values);
      }


        db.close(); // Closing database connection

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
