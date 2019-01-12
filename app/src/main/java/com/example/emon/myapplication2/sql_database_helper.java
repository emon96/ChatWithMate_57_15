package com.example.emon.myapplication2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class sql_database_helper extends SQLiteOpenHelper{
    private static final String database_name="Student.db";
    private static final String table_name="student_details";
    private static final String id="ID";
    private static final String name="NAME";
    private static final String age="AGE";
    private static final String gender="GENDER";
    private static final String drop_table="DROP TABLE IF EXISTS "+table_name;
    private static final int db_version=5;
    private Context context;
    private static final String selectallformtable="SELECT * FROM "+table_name;
    private static final String create_table="CREATE TABLE "+table_name+" ("+id+" INTEGER PRIMARY KEY AUTOINCREMENT,"+name+" VARCHAR(200),"+age+" INTEGER,"+gender+"  VARCHAR(200));";
    public sql_database_helper(Context context) {
        super(context,database_name, null, db_version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            Toast.makeText(context,"Table created",Toast.LENGTH_LONG).show();
            sqLiteDatabase.execSQL(create_table);

        }
        catch (Exception e)
        {
            Toast.makeText(context,"Exception in creating table "+e,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        try {

            Toast.makeText(context,"Table Upgraded",Toast.LENGTH_SHORT).show();
            sqLiteDatabase.execSQL(drop_table);

            onCreate(sqLiteDatabase);
        }catch (Exception e)
        {
            Toast.makeText(context,"Exception in upgrading "+e,Toast.LENGTH_LONG).show();
        }
    }
    public long insertData(String namee,String agee,String gendere)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(name,namee);
        contentValues.put(age,agee);
        contentValues.put(gender,gendere);
        long r=sqLiteDatabase.insert(table_name,null,contentValues);
                return  r;
    }
    public Cursor showData()
    {

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(selectallformtable,null);
        return cursor;

    }
}
