package test.com.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;

import test.com.adapters.MyCustomAdapter;
import test.com.aws.support.DownloadModel;
import test.com.aws.support.Util;
import test.com.cameraandlist.MyApplication;

/**
 * Created by nitin on 4/12/15.
 */
public class DataModel
{
    private String url;
    public Bitmap image;
    private String data;
    private MyCustomAdapter adapter;
    Context context;
    TransferManager manager;
    public DataModel(String url, Bitmap image, String data) {
        this.url = url;
        this.image = image;
        this.data = data;
        manager=new TransferManager(Util.getCredProvider(MyApplication.getInstance()));

    }


    public String getUrl() {
        return url;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getData() {
        return data;
    }

    public void loadData(MyCustomAdapter adapter, String url) {

        this.adapter=adapter;
        if(url !=null )
        new ImageDownloader().execute(url);
    }

    class ImageDownloader extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... params) {

            //TransferController.download(MyApplication.getInstance(),params[0]);
            DownloadModel model=new DownloadModel(MyApplication.getInstance(),url,manager);
            Bitmap images=model.download();
            return images;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            image=bitmap;
            if(adapter!=null)
            adapter.notifyDataSetChanged();
        }
    }
}
