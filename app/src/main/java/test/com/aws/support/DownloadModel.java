package test.com.aws.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;

import java.io.File;
import java.util.Locale;

import test.com.cameraandlist.MyApplication;

/**
 * Created by nitin on 4/12/15.
 */
public class DownloadModel extends TransferModel {


    private final String mKeys;
    private Status mStatus;
    private Download mDownload;
    private Uri mUri;
    private TransferManager mTransferManager = new TransferManager(Util.getCredProvider(MyApplication.getInstance()));


    public DownloadModel(Context context, String key, TransferManager manager) {
        super(context, Uri.parse(key), manager);
        mKeys = key;
        mStatus = Status.IN_PROGRESS;

    }

    public Bitmap download() {

        Bitmap image = null;

        Log.i("I am here in download ", mKeys);

        String root=Environment.getExternalStorageDirectory().toString();
        File myDir=new File(root,"/temps");
        if(!myDir.exists())
        myDir.mkdirs();


        File f = new File(myDir, getFileName());
        Log.i("File is in", f.getAbsolutePath());
        mUri = Uri.fromFile(f);
        mDownload = getTransferManager().download(
                Constants.BUCKET_NAME.toLowerCase(Locale.US), mKeys, f);
        f.deleteOnExit();

        String path= myDir+"/"+getFileName();
        Log.i("Path of the file is",path);
        image= BitmapFactory.decodeFile(path);

        return image;
    }

    @Override
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public Transfer getTransfer() {
        return mDownload;
    }
}
