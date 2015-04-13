package test.com.aws.support;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;

import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;

public class NetworkService extends IntentService {

    public static String S3_KEYS_EXTRA = "keys";
    private TransferManager mTransferManager;

    public NetworkService() {
        super("NetworkService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTransferManager = new TransferManager(Util.getCredProvider(this));
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            if (intent.getAction().equals(Intent.ACTION_SEND) && intent.getData() != null) {
                upload(intent.getData());
            } else if (intent.getAction().equals(Intent.ACTION_GET_CONTENT) && intent.getStringExtra(S3_KEYS_EXTRA) != null) {
                //download(intent.getStringExtra(S3_KEYS_EXTRA));

            }

        }
    }

   /* private void download(String keys) {

            DownloadModel model=new DownloadModel(this,keys,mTransferManager);
            model.download();


    }*/


    private void upload(Uri data) {
        UploadModel model = new UploadModel(this, data, mTransferManager);
        new Thread(model.getUploadRunnable()).run();
    }

}
