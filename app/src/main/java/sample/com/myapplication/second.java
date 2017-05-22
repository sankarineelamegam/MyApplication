package sample.com.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by web on 5/18/2017.
 */

public class second extends Activity {
    private DBHelper dbHelper;
    ArrayList<HashMap<String, String>> studentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyler);
        dbHelper = new DBHelper(this);
        studentList = new ArrayList<HashMap<String, String>>();
        studentList= getStudentList();
        RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        RVAdapter adapter = new RVAdapter(this,studentList);
        rv.setAdapter(adapter);
    }

    private ArrayList<HashMap<String, String>> getStudentList() {

        //Open connection to read only
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        String selectQuery =  "SELECT  " +
                sample.KEY_ID + "," +
                sample.KEY_name + "," +
                sample.KEY_email +
                " FROM " + sample.TABLE;

        //Student student = new Student();
        ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();

        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list


        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> student = new HashMap<String, String>();
                student.put("name", cursor.getString(1));
                student.put("email", cursor.getString(2));
                temp.add(student);

            } while (cursor.moveToNext());
        }

        cursor.close();
        System.out.println(cursor.getCount());
        db.close();
        return temp;

    }
}
