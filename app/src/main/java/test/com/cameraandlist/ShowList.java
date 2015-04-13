package test.com.cameraandlist;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
    int i = 0;

    public static void DataInilialization(List<DataModel> data) {
        ShowList.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        AlertDialog.Builder alert=new AlertDialog.Builder(this);


        progressDialog = ProgressDialog.show(ShowList.this, "Loading List", "Please Wait!");
        list = (ListView) findViewById(android.R.id.list);
        adapter = new MyCustomAdapter(this, data);
        setListAdapter(adapter);

        if(data.isEmpty())
        {

            alert.setMessage("No File in the Server");
            alert.setTitle("Error");
            alert.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            alert.show();
        }



        for (DataModel obj : data) {
            obj.loadData(adapter, data.get(i).getUrl());
            i++;
        }
        if (progressDialog != null)
            progressDialog.dismiss();

    }


}