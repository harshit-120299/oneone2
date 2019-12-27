package com.swavlambibharat.myenglishvocab;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String Database_Name = "database.db";
    private static final String Table_Name="MylistTable";
    private static final String COL1="ITEM1";
    private static final String COL2="ITEM2";
    private static final String COL3="ITEM3";
    public DatabaseHelper(Context context) {
        super(context,Database_Name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable="CREATE TABLE "+Table_Name+"(ITEM1 TEXT NOT NULL,ITEM2 INTEGER PRIMARY KEY AUTOINCREMENT,ITEM3 TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(" DROP TABLE IF EXISTS "+Table_Name);
        onCreate(db);

    }
    public boolean insertData(String Item1){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL1,Item1);
        long result=db.insert(Table_Name,null,contentValues);
        db.close();
        if(result==-1)
            return false;
        else return true;

    }
    public boolean insertMeaning(String Item3){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL3,Item3);
        long result=db.insert(Table_Name,null,contentValues);
        db.close();
        if (result==-1)
            return false;
        else
            return true;

    }
    public Cursor getAlldata(){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor res= db.rawQuery("Select * from "+Table_Name,null);
        return res;
    }
    public Cursor RWAlldata(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res= db.rawQuery("Select * from "+Table_Name,null);
        return res;
    }

    public void deleteAlldata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop table "+ Table_Name);
        db.execSQL(" Create table "+ Table_Name +"(ITEM1 TEXT NOT NULL,ITEM2 INTEGER PRIMARY KEY AUTOINCREMENT)");
    }
    public boolean deleteSelectedData(int key)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Table_Name, COL2 + "=" + key, null) > 0;

    }
}
