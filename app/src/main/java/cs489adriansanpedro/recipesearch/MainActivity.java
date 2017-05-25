package cs489adriansanpedro.recipesearch;

import android.content.Context;
import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //MARK: Variables
    private ListView listView;
    private EditText searchText;
    private ArrayList<Recipe> recipes;
    //private ProgressBar progressBar;

    //Mark:Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.results);
        searchText = (EditText)findViewById(R.id.searchField);
        //progressBar = (ProgressBar)findViewById(R.id.progressBar);

        final Context context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe selectedRecipe = recipes.get(position);

                Intent detailIntent = new Intent(context, RecipeDetailActivity.class);
                detailIntent.putExtra("title", selectedRecipe.getTitle());
                detailIntent.putExtra("url", selectedRecipe.getUrl());

                startActivity(detailIntent);
            }

        });
    }

    public void searchPressed(View view){
        String recipe = searchText.getText().toString();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        new RetrieveFeedTask(this).execute(recipe);
    }

    private class RetrieveFeedTask extends AsyncTask<String, String, String> {

        private Exception exception;
        private String API_URL = "http://www.recipepuppy.com/api/?";
        private JSONObject object;
        private Context mContext;

        RetrieveFeedTask(Context context){
            this.mContext = context;
        }

        @Override
        protected void onPreExecute() {
          //  progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                URL url = new URL(API_URL + "q=" + args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String response) {
           // progressBar.setVisibility(View.GONE);
            try {
                object = (JSONObject) new JSONTokener(response).nextValue();
                Log.i("INFO", object.toString()+"\n");

                JSONArray results;
                results = object.getJSONArray("results");
                Log.i("INFO", results.toString()+"\n");

                recipes = getRecipeArray(results);
                Log.i("INFO", recipes.toString()+"\n");

                listView.setAdapter(new RecipeAdapter(mContext, recipes));
            } catch(JSONException e){
                Log.e("object error", e.getMessage(), e);
            }
        }

        ArrayList<Recipe> getRecipeArray(JSONArray results){
            ArrayList<Recipe> newRecipes = new ArrayList<>();

            Log.i("INFO", results.length()+"\n");
            for(int i = 0; i < results.length(); i++){
                try {
                    JSONObject currResult = results.getJSONObject(i);
                    Recipe newRecipe = new Recipe();

                   // Log.i("INFO: ", currResult.getString("title"));
                    newRecipe.setTitle(currResult.getString("title"));
                    Log.i("INFO: ", newRecipe.getTitle());
                    newRecipe.setImage(currResult.getString("thumbnail"));
                    newRecipe.setIngredients(currResult.getString("ingredients"));
                    newRecipe.setURL(currResult.getString("href"));

                    newRecipes.add(newRecipe);
                    for(Recipe recipe: newRecipes) {
                        Log.i("INFO: ", recipe.getTitle());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return newRecipes;
        }
    }
}
