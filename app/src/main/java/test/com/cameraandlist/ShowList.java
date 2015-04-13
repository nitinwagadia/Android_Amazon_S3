package test.com.cameraandlist;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Collections;
import java.util.List;

import test.com.adapters.MyCustomAdapter;
import test.com.models.DataModel;


public class ShowList extends ListActivity {

    static List<DataModel> data = Collections.emptyList();
    ListView list;
    ProgressDialog progressDialog;
    MyCustomAdapter adapter;
    int i=0;
    public static void DataInilialization(List<DataModel> data) {
        ShowList.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        progressDialog = ProgressDialog.show(ShowList.this, "Loading List", "Please Wait!");
        list = (ListView) findViewById(android.R.id.list);
        adapter=new MyCustomAdapter(this,data);
        setListAdapter(adapter);

        for(DataModel obj : data) {
            obj.loadData(adapter, data.get(i).getUrl());
            i++;
        }
        if (progressDialog != null)
            progressDialog.dismiss();

    }



}