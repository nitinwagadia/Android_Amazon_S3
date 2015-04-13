package test.com.aws.support;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.mobileconnectors.s3.transfermanager.Transfer;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import de.greenrobot.event.EventBus;
import test.com.cameraandlist.MyApplication;
import test.com.models.closeUpdate;
import test.com.models.closeUpload;

/**
 * Created by nitin on 4/11/15.
 */
public class UploadModel extends TransferModel {

    private Upload mUpload;
    private ProgressListener mListener;


    private Status mStatus;
    private File mFile;
    private String mExtension;

    public UploadModel(Context context, Uri data, TransferManager manager) {
        super(context, data, manager);
        mStatus = Status.IN_PROGRESS;
        mExtension = MimeTypeMap.getSingleton().getExtensionFromMimeType(context.getContentResolver().getType(data));
        mListener = new ProgressListener() {
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                if (progressEvent.getEventCode() == ProgressEvent.COMPLETED_EVENT_CODE) {
                    mStatus = Status.COMPLETED;
                    if (mFile != null) {
                        mFile.delete();
                        EventBus.getDefault().post(new closeUpload());
                    }
                }
            }


        };
    }

    public Runnable getUploadRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                upload();
            }
        };

    }



    @Override
    public Status getStatus() {
        return mStatus;
    }

    @Override
    public Transfer getTransfer() {
        return mUpload;
    }


    private void upload() {
        if (mFile == null) {
            saveTempFile();
        }

        if (mFile != null) {
            try {
                mUpload = getTransferManager().upload(Constants.BUCKET_NAME.toLowerCase(Locale.US), Util.getPrefix(getContext()) + super.getFileName() + "." + mExtension, mFile);
                mUpload.addProgressListener(mListener);
            } catch (Exception e) {
                Log.i("Exception while Uploading is ", e + "");
            }
        }
    }


    private void saveTempFile() {

        ContentResolver resolver = getContext().getContentResolver();
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = resolver.openInputStream(getUri());
            mFile = File.createTempFile("s3_demo_file_" + getId(), mExtension, getContext().getCacheDir());
            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();

        } catch (IOException e) {
            Log.i("Exception is Found :", e + "");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
