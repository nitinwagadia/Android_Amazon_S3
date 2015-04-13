package test.com.aws.support;

import android.content.Context;
import android.net.Uri;

import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;

import java.util.LinkedHashMap;

/**
 * Created by nitin on 4/11/15.
 */
public abstract class TransferModel {
    private static final String TAG = "TransferModel";

    public static enum Status {
        IN_PROGRESS, PAUSED, CANCELED, COMPLETED
    };


    public static class ShowDialog{}

    public static class CloseDialog{}

    // all TransferModels have associated id which is their key to sModels
    private static LinkedHashMap<Integer, TransferModel> sModels =
            new LinkedHashMap<Integer, TransferModel>();
    private static int sNextId = 1;

    private String mFileName;
    private Context mContext;
    private Uri mUri;
    private int mId;
    private TransferManager mManager;

    public static TransferModel getTransferModel(int id) {
        return sModels.get(id);
    }

    public static TransferModel[] getAllTransfers() {
        TransferModel[] models = new TransferModel[sModels.size()];
        return sModels.values().toArray(models);
    }

    public TransferModel(Context context, Uri uri, TransferManager manager) {
        mContext = context;
        mUri = uri;
        mManager = manager;
        String uriString = uri.toString();
        mFileName = Util.getFileName(uriString);
        mId = sNextId++;
        sModels.put(mId, this);
    }

    public String getFileName() {
        return mFileName;
    }

    public int getId() {
        return mId;
    }



    public Uri getUri() {
        return mUri;
    }


    public abstract Status getStatus();

    public abstract Transfer getTransfer();


    protected Context getContext() {
        return mContext;
    }

    protected TransferManager getTransferManager() {
        return mManager;
    }
}