package test.com.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import test.com.cameraandlist.R;
import test.com.models.DataModel;

/**
 * Created by nitin on 4/12/15.
 */
public class MyCustomAdapter extends BaseAdapter{

    Context context;
    List<DataModel> data= Collections.emptyList();
    LayoutInflater inflater;
    Animation animation;
    public MyCustomAdapter(Context context, List<DataModel> data)
    {
        this.context=context;
        this.data=data;
        animation= AnimationUtils.loadAnimation(context,R.anim.myanimation);
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        DataModel current=data.get(position);;

        if(convertView==null)
        {

            convertView=inflater.inflate(R.layout.recyclerviewcontentcard,parent,false);
            holder=new ViewHolder();
            holder.text=(TextView)convertView.findViewById(R.id.textView);
            holder.image= (ImageView) convertView.findViewById(R.id.imageView);
            Log.i("ViewHolder of item " + position, holder.toString());
            convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

            holder.text.setText(current.getData());
            if(current.getImage()!=null)
            {
                holder.image.setImageDrawable(new BitmapDrawable(Resources.getSystem(),current.getImage()));

            }



        return convertView;
    }

    private class ViewHolder
    {
        TextView text;
        ImageView image;
    }
}
