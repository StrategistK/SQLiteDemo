package com.study.android.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MySQLiteOpenHelper mMySQLiteOpenHelper;
    private Button mCreateDatabase;
    private Button mAddData;
    private Button mUpdateData;
    private Button mDeleteData;
    private Button mQueryData;
    private Button mReplaceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMySQLiteOpenHelper = new MySQLiteOpenHelper(this,"BookStore.db",null,2);

        mCreateDatabase = (Button)findViewById(R.id.create_database);
        mCreateDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMySQLiteOpenHelper.getWritableDatabase();
            }
        });

        mAddData = (Button)findViewById(R.id.add_data);
        mAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMySQLiteOpenHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
//                start first values
                values.put("name", "The Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages", 454);
                values.put("price",16.95);
//                insert first values
                db.insert("Book",null,values);
//                clear first values
                values.clear();
//                start second values
                values.put("name","The Lost Symbol");
                values.put("author","Dan Brown");
                values.put("pages",510);
                values.put("price",19.95);
//                insert second values
                db.insert("Book",null,values);
            }
        });

        mUpdateData = (Button)findViewById(R.id.update_data);
        mUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMySQLiteOpenHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",10.99);
                db.update("Book",values,"name = ?",new String[]{"The Da Vinci Code"});
            }
        });

        mDeleteData = (Button)findViewById(R.id.delete_data);
        mDeleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMySQLiteOpenHelper.getWritableDatabase();
                db.delete("Book","pages > ?",new String[]{"500"});
            }
        });

        mQueryData = (Button)findViewById(R.id.query_data);
        mQueryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMySQLiteOpenHelper.getWritableDatabase();
//                查询Book中所有的数据
                Cursor cursor = db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()) {
                    do {
//                        遍历Cursor对象，取出数据
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
//                        打印数据
                        Log.d("MainActivity","book name is " + name);
                        Log.d("MainActivity","book author is " + author);
                        Log.d("MainActivity","book pages is " + pages);
                        Log.d("MainActivity","book price is " + price);

                    }while (cursor.moveToNext());
                }
                cursor.close();
            }
        });

        mReplaceData = (Button)findViewById(R.id.replace_data);
        mReplaceData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mMySQLiteOpenHelper.getWritableDatabase();
//                开启事务
                db.beginTransaction();

                try {
                    db.delete("Book", null,null);
                    if (true) {
                        throw new NullPointerException();
                    }
                    ContentValues values = new ContentValues();
                    values.put("name","Game of Thrones");
                    values.put("author","George Martin");
                    values.put("pages",720);
                    values.put("price",22.85);
                    db.insert("Book",null,null);
                    db.setTransactionSuccessful();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } finally {
                    db.endTransaction();
                }
            }
        });
    }
}
