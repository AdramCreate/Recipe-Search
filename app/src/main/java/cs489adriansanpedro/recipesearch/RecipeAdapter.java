package cs489adriansanpedro.recipesearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Adrian on 5/13/2017.
 */

class RecipeAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Recipe> mDataSource;

    RecipeAdapter(Context context, ArrayList<Recipe> items){
        mContext = context;
        mDataSource = items;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.list_row_layout, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.title);
            holder.subtitle = (TextView)convertView.findViewById(R.id.subtitle);
            holder.thumbnail = (ImageView)convertView.findViewById(R.id.thumbnail);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        Recipe recipe = (Recipe) getItem(position);

        Log.i("INFO: ", recipe.getTitle());
        holder.title.setText(recipe.getTitle());
        holder.subtitle.setText(recipe.getIngredients());
        //new DownloadImageTask(holder.thumbnail).execute(recipe.getUrl());

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView subtitle;
        ImageView thumbnail;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        ImageView imageView;

        public DownloadImageTask(ImageView newIView){
            this.imageView = newIView;
        }

        @Override
        protected Bitmap doInBackground(String... args){
            String urlString = args[0];
            Bitmap newThumbnail = null;
            try{
                InputStream in = new java.net.URL(urlString).openStream();
                newThumbnail = BitmapFactory.decodeStream(in);
            } catch(Exception e){
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return newThumbnail;
        }

        @Override
        protected void onPostExecute(Bitmap result){
            this.imageView.setImageBitmap(result);
        }
    }
}
