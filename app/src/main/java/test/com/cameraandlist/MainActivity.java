package test.com.cameraandlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import test.com.aws.support.Constants;
import test.com.aws.support.TransferController;
import test.com.aws.support.TransferModel;
import test.com.aws.support.Util;
import test.com.models.DataModel;
import test.com.models.closeUpdate;
import test.com.models.closeUpload;
import test.com.models.openUpdate;
import test.com.models.openUpload;

public class MainActivity extends ActionBarActivity {




    static final int IMAGE_STATUS = 1;
    static final int PICK_IMAGE = 2;

    Button camera, gallery, list;
    ImageView mImageView;
    File photoFile = null;
    List<DataModel> data=Collections.emptyList();
    private AmazonS3 mClient;
    private ProgressDialog progressDialog,backgroundDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mClient = Util.getS3Client(this);
        EventBus.getDefault().register(this);

        camera = (Button) findViewById(R.id.camera);
        gallery = (Button) findViewById(R.id.gallery);

        mImageView = (ImageView) findViewById(R.id.imageView);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraCall = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraCall, IMAGE_STATUS);


            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryCall = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryCall, PICK_IMAGE);
            }
        });

        list = (Button) findViewById(R.id.list);
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowList.DataInilialization(data);
                Intent i = new Intent(getBaseContext(), ShowList.class);
                startActivity(i);

            }
        });

        new GetDataInformation().execute();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_STATUS && resultCode == RESULT_OK) {


            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

            mImageView.setImageBitmap(imageBitmap);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            try {
                file.createNewFile();
                FileOutputStream fo = new FileOutputStream(file);

                fo.write(bytes.toByteArray());
                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            TransferController.upload(this, Uri.fromFile(file));
            new GetDataInformation().execute();


        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            Uri pickedImage = data.getData();
            if (pickedImage != null) {

                TransferController.upload(this, pickedImage);
                new GetDataInformation().execute();

            }
        }
    }

    public void onEventMainThread(openUpdate s) {
        progressDialog=null;
        progressDialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Updating!");
    }

    public void onEventMainThread(closeUpdate c) {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog=null;
    }

    public void onEventMainThread(openUpload s) {
        backgroundDialog=null;
        backgroundDialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Uploading your Awesome Pic!");
    }

    public void onEventMainThread(closeUpload c) {
        if (backgroundDialog!= null)
            backgroundDialog.dismiss();
        backgroundDialog=null;
    }


    class GetDataInformation extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            EventBus.getDefault().post(new openUpdate());
        }

        @Override
        protected Void doInBackground(Void... params) {
            data = new ArrayList<DataModel>();
            int i = 0;
            List<S3ObjectSummary> Clientdata = mClient.listObjects(Constants.BUCKET_NAME.toLowerCase(Locale.US),
                    Util.getPrefix(
                            MainActivity.this)).getObjectSummaries();

            DataModel current;
            for (S3ObjectSummary obj : Clientdata) {
                current = new DataModel(obj.getKey(), null, "I am Image " + (i + 1));
                data.add(current);
                i++;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            EventBus.getDefault().post(new closeUpdate());
        }
    }


}

