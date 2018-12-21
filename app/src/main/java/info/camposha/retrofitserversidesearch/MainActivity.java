package info.camposha.retrofitserversidesearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.FormUrlEncoded;

public class MainActivity extends AppCompatActivity {
    //private static final String BASE_URL = "http://10.0.2.2"; or "http://10.0.3.2" or your computers ip address
    private static final String BASE_URL = "http://192.168.12.2";//replace this wih the ip address for your computer
    private static final String FULL_URL = BASE_URL+"/PHP/spaceship/";
    class Spacecraft {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("propellant")
        private String propellant;
        @SerializedName("destination")
        private String destination;
        @SerializedName("image_url")
        private String imageURL;
        @SerializedName("technology_exists")
        private int technologyExists;

        public Spacecraft(String name){
            this.name=name;
        }
        public Spacecraft(int id, String name, String propellant,String destination, String imageURL, int technologyExists) {
            this.id = id;
            this.name = name;
            this.propellant = propellant;
            this.destination=destination;
            this.imageURL = imageURL;
            this.technologyExists = technologyExists;
        }

        /*
         *GETTERS AND SETTERS
         */
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public String getPropellant() {
            return propellant;
        }
        public String getDestination() {
            return destination;
        }
        public String getImageURL() {
            return imageURL;
        }
        public int getTechnologyExists() {
            return technologyExists;
        }
        @Override
        public String toString() {
            return name;
        }
    }

    interface MyAPIService {

        @GET("/PHP/spaceship")
        Call<List<Spacecraft>> getSpacecrafts();
		@FormUrlEncoded
        @POST("/PHP/spaceship/index.php")
        Call<List<Spacecraft>> searchSpacecraft(@Field("query") String query);
    }

    static class RetrofitClientInstance {
        private static Retrofit retrofit;

        public static Retrofit getRetrofitInstance() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
    class ListViewAdapter extends BaseAdapter {

        private List<Spacecraft> spacecrafts;
        private Context context;

        public ListViewAdapter(Context context, List<Spacecraft> spacecrafts) {
            this.context = context;
            this.spacecrafts = spacecrafts;
        }

        @Override
        public int getCount() {
            return spacecrafts.size();
        }

        @Override
        public Object getItem(int pos) {
            return spacecrafts.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.model, viewGroup, false);
            }

            TextView nameTxt = view.findViewById(R.id.nameTextView);
            TextView txtPropellant = view.findViewById(R.id.propellantTextView);
            CheckBox chkTechExists = view.findViewById(R.id.myCheckBox);
            ImageView spacecraftImageView = view.findViewById(R.id.spacecraftImageView);

            final Spacecraft thisSpacecraft = spacecrafts.get(position);

            nameTxt.setText(thisSpacecraft.getName());
            txtPropellant.setText(thisSpacecraft.getPropellant());
            chkTechExists.setChecked(thisSpacecraft.getTechnologyExists() == 1);
            chkTechExists.setEnabled(false);

            if (thisSpacecraft.getImageURL() != null && thisSpacecraft.getImageURL().length() > 0) {
                Picasso.get().load(FULL_URL + "/images/" + thisSpacecraft.getImageURL()).placeholder(R.drawable.placeholder).into(spacecraftImageView);
            } else {
                Toast.makeText(context, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.placeholder).into(spacecraftImageView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, thisSpacecraft.getName(), Toast.LENGTH_SHORT).show();
                    String techExists = "";
                    if (thisSpacecraft.getTechnologyExists() == 1) {
                        techExists = "YES";
                    } else {
                        techExists = "NO";
                    }
                    String[] spacecrafts = {
                            thisSpacecraft.getName(),
                            thisSpacecraft.getPropellant(),
                            thisSpacecraft.getDestination(),
                            techExists,
                            FULL_URL + "/images/" + thisSpacecraft.getImageURL()
                    };
                    openDetailActivity(spacecrafts);
                }
            });

            return view;
        }

        private void openDetailActivity(String[] data) {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putExtra("NAME_KEY", data[0]);
            intent.putExtra("PROPELLANT_KEY", data[1]);
            intent.putExtra("DESTINATION_KEY", data[2]);
            intent.putExtra("TECHNOLOGY_EXISTS_KEY", data[3]);
            intent.putExtra("IMAGE_KEY", data[4]);
            startActivity(intent);
        }
    }
    private ListViewAdapter adapter;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private SearchView mSearchView;

    private void initializeWidgets(){
        mListView = findViewById(R.id.mListView);
        mProgressBar= findViewById(R.id.mProgressBar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);
        mSearchView=findViewById(R.id.mSearchView);
        mSearchView.setIconified(true);
    }

    private void populateListView(List<Spacecraft> spacecraftList) {
        adapter = new ListViewAdapter(this,spacecraftList);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initializeWidgets();

        /*Create handle for the RetrofitInstance interface*/
        final MyAPIService myAPIService = RetrofitClientInstance.getRetrofitInstance().create(MyAPIService.class);

        //Call<List<Spacecraft>> call = myAPIService.getSpacecrafts();
        final Call<List<Spacecraft>> call = myAPIService.searchSpacecraft("");
        call.enqueue(new Callback<List<Spacecraft>>() {

            @Override
            public void onResponse(Call<List<Spacecraft>> call, Response<List<Spacecraft>> response) {
                mProgressBar.setVisibility(View.GONE);
                populateListView(response.body());
            }
            @Override
            public void onFailure(Call<List<Spacecraft>> call, Throwable throwable) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                final Call<List<Spacecraft>> call = myAPIService.searchSpacecraft(query);
                call.enqueue(new Callback<List<Spacecraft>>() {

                    @Override
                    public void onResponse(Call<List<Spacecraft>> call, Response<List<Spacecraft>> response) {
                        mProgressBar.setVisibility(View.GONE);
                        populateListView(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<Spacecraft>> call, Throwable throwable) {
						populateListView(new ArrayList<Spacecraft>());
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "ERROR: "+throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                return false;
            }
        });
    }
}
//end
