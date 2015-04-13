package test.com.aws.support;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import de.greenrobot.event.EventBus;

/**
 * Created by nitin on 4/11/15.
 */
public class TransferController
{


    public static void upload(Context context, Uri pickedImage)
    {
        Intent intent=new Intent(context,NetworkService.class);
        intent.setAction(Intent.ACTION_SEND);
        intent.setData(pickedImage);
        EventBus.getDefault().post(new TransferModel.ShowDialog());
        context.startService(intent);

    }

  /*  public static void download(Context context, String keys) {
        Intent intent=new Intent(context,NetworkService.class);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(NetworkService.S3_KEYS_EXTRA,keys);
        context.startService(intent);
    }*/
}
