package com.cropfit.cropfit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class Main4Activity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        String temp = getIntent().getStringExtra("temp");
        String humid = getIntent().getStringExtra("humid");
        String moist = getIntent().getStringExtra("moist");
        String press = getIntent().getStringExtra("press");
        String ph = getIntent().getStringExtra("ph");

        DatabaseAccess access = new DatabaseAccess(Main4Activity.this);

        access.getInfo(temp,humid,moist,press,ph);

        this.listView = (ListView)findViewById(R.id.listView);
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        List<String> quotes = databaseAccess.getQuotes();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, quotes);
        this.listView.setAdapter(adapter);
    }
}
