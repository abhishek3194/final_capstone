package Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.poornima.clickforchange.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by poornima on 13/10/16.
 */
public class FeedAdapter extends BaseAdapter{

    private String [] result;
    Activity activity;


    public ImageLoader imageLoader;

    public Bitmap bmp = null;

    final String PROB_ID = "prob_id";
    final String PROB_IMG = "prob_image";
    final String PROB_TYPE = "prob_type";
    final String USER_ID = "user_id";
    final String LOC_ID = "loc_id";
    final String LATITUDE = "latitude";
    final String LONGITUDE = "longitude";
    final String DATE_TIME = "date_time";
    final String NUM_REACTIONS = "num_reactions";

    private static LayoutInflater inflater=null;


    public FeedAdapter(Activity mainActivity, String[] prblmList) {
        // TODO Auto-generated constructor stub
        result=prblmList;
        activity=mainActivity;
        //imageId=prgmImages;
        inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        String path = null;

        String user_id = null;

        double latitude = 0;

        double longitude = 0;

        JSONObject problemObject = null;
        try {
            problemObject = new JSONObject(result[i]);
            path = problemObject.getString(PROB_IMG);

            user_id = problemObject.getString(USER_ID);

            latitude = problemObject.getDouble(LATITUDE);

            longitude = problemObject.getDouble(LONGITUDE);

        } catch (JSONException e) {
            e.printStackTrace();
        }




        View rowView;
        rowView = inflater.inflate(R.layout.list_item_newsfeed, null);

        TextView description=(TextView) rowView.findViewById(R.id.list_item_textview);
       ImageView problemPic= (ImageView) rowView.findViewById(R.id.list_item_icon);



        description.setText(user_id+latitude+longitude);


        imageLoader.DisplayImage(path, problemPic);

        return rowView;
    }


    public class ImageDownloader extends AsyncTask<String,Void,Void>
    {

        @Override
        protected Void doInBackground(String... params) {


            URL url = null;

            try {
                url = new URL(params[0]);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

           return null;
        }
    }
}
