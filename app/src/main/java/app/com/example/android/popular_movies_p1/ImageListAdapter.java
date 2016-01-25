package app.com.example.android.popular_movies_p1;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.view.View;

// Import Picasso Image Loading Lib
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by shavant on 1/4/16.
 */
public class ImageListAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    private String[] imageUrls;
    private List<String> listImgUrls;
   // private JSONArray jsonDataArray;

    /* Need to Override Constructor for using List to continue
    using methods such as "clear()"*/
    public ImageListAdapter(Context context, List<String> listImgUrls) {
        super(context, R.layout.grid_item_image, listImgUrls);

        this.context = context;
        this.listImgUrls= listImgUrls;

        inflater = LayoutInflater.from(context);


    }

   // public ImageListAdapter(Context context, List<JSONObject> listJsonData) {

   // }

    /*public void setJsonArray(JSONArray array) {
        this.jsonDataArray = array;
    }

    public JSONArray getJsonArray() {
        return this.jsonDataArray;
    } */

    /* Credit to Future Studio Blog for ImageListAdapter implementation
    https://futurestud.io/blog/picasso-adapter-use-for-listview-gridview-etc
     */
    public ImageListAdapter(Context context, String[] imageUrls) {
        super(context, R.layout.grid_item_image, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }

    /* Credit to Future Studio Blog for ImageListAdapter implementation
    https://futurestud.io/blog/picasso-adapter-use-for-listview-gridview-etc
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_item_image, parent, false);
        }

        Picasso
                .with(context)
                .load(listImgUrls.get(position))
                //.load(imageUrls[position])
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }
}
