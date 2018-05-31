package com.esiea.instafood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static com.esiea.instafood.LoadingActivity.getLocation;

public class ParameterActivity extends AppCompatActivity {

    private static String place;
    private static String type;
    private static String filename;

    TextView bienvenue;
    EditText choixSelection;
    EditText placeSelection;
    RecyclerView rv_choix;
    RecyclerView rv_place;
    Button restoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        bienvenue = (TextView) findViewById(R.id.bienvenue_text);
        if (SignInActivity.connection)
            bienvenue.setText(getString(R.string.bienvenue_text, SignInActivity.firebaseUser.getDisplayName()));
        else
            bienvenue.setText(getString(R.string.bienvenue_text, getString(R.string.inconnu)));

        rv_choix = (RecyclerView) findViewById(R.id.list_choix_resto);
        rv_choix.setLayoutManager(new LinearLayoutManager(this));
        rv_choix.setAdapter(new ChoixAdapter());

        choixSelection = (EditText) findViewById(R.id.choix_resto_edit_text);
        choixSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_choix.setVisibility(View.VISIBLE);
                choixSelection.getText().clear();
            }
        });

        rv_place = (RecyclerView) findViewById(R.id.list_lieu_resto);
        rv_place.setLayoutManager(new LinearLayoutManager(this));
        rv_place.setAdapter(new PlaceAdapter());

        placeSelection = (EditText) findViewById(R.id.choix_resto_lieu_edit_text);
        placeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_place.setVisibility(View.VISIBLE);
                placeSelection.getText().clear();
            }
        });

        restoButton = (Button) findViewById(R.id.resto);
        restoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getPlace().equals(getString(R.string.geolocalisation)))
                    setFilename(getString(R.string._json_here, type));
                else if (getType().equals(getString(R.string.No_particular_choice)))
                    setFilename(getString(R.string._json_here, place));
                else if (getType().equals(getString(R.string.Fast_Food))){
                    setType("Fast_Food");
                    setFilename(getString(R.string._json, type, place));
                }
                else if (getPlace().equals(getString(R.string.geolocalisation)) && getString(R.string.No_particular_choice).equals(getType()))
                    setFilename("restos.json");
                else
                    setFilename(getString(R.string._json, type, place));
                startActivity(new Intent(ParameterActivity.this, LoadingActivity.class));
            }
        });

    }

    //////////////////
    // Gestion Menu //
    //////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.geo:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + getLocation().getLatitude() + "," + getLocation().getLongitude() + "?z=20")));
                return true;

            case R.id.sign_out:
                if (SignInActivity.firebaseUser != null)
                    startActivity(new Intent(ParameterActivity.this, SignInActivity.class));
                else
                    new NotiClass(this, getString(R.string.sign_out_text_ko));
                return true;

            case R.id.sign_in:
                if (SignInActivity.firebaseUser == null)
                    startActivity(new Intent(ParameterActivity.this, SignInActivity.class));
                else
                    new NotiClass(this, getString(R.string.sign_in_text_ko));
                return true;

            case R.id.aide:
                new NotiClass(this, getString(R.string.help), getString(R.string.help_parameter));

        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////
    // Adaptater RV Choix //
    ////////////////////////

    public class ChoixAdapter extends RecyclerView.Adapter<ChoixAdapter.ChoixViewHolder> {

        private final String choixResto[] = {getString(R.string.Chinese), getString(R.string.Indian),
                getString(R.string.Greek), getString(R.string.African), getString(R.string.Italian),
                getString(R.string.Japanese), getString(R.string.French), getString(R.string.No_particular_choice)};

        @Override
        public ChoixViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);

            return new ChoixViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChoixViewHolder holder, int position) {
            String choix = choixResto[position];
            holder.name.setText(choix);
        }

        @Override
        public int getItemCount() {
            return choixResto.length;
        }

        public class ChoixViewHolder extends RecyclerView.ViewHolder {

            final TextView name;

            public ChoixViewHolder(View itemView) {
                super(itemView);

                name = ((TextView) itemView.findViewById(R.id.choix_name));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choixSelection.setText(name.getText());
                        rv_choix.setVisibility(View.GONE);
                        type = String.valueOf(choixSelection.getText());
                    }
                });

            }
        }
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder> {

        private final String placeResto[] = {getString(R.string.geolocalisation), getString(R.string.Paris),
                getString(R.string.Lille), getString(R.string.Marseilles), getString(R.string.Lyon), getString(R.string.Cannes)};

        @Override
        public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item, parent, false);

            return new PlaceViewHolder(view);
        }

        @Override
        public void onBindViewHolder(PlaceViewHolder holder, int position) {
            String place = placeResto[position];
            holder.name.setText(place);
        }

        @Override
        public int getItemCount() {
            return placeResto.length;
        }

        public class PlaceViewHolder extends RecyclerView.ViewHolder {

            final TextView name;

            public PlaceViewHolder(View itemView) {
                super(itemView);

                name = ((TextView) itemView.findViewById(R.id.choix_name));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeSelection.setText(name.getText());
                        rv_place.setVisibility(View.GONE);
                        place = String.valueOf(placeSelection.getText());

                    }
                });

            }
        }
    }


    ///////////////////////////////
    // Localisation du téléphone //
    ///////////////////////////////


    public static String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        ParameterActivity.place = place;
    }

    public static String getType() {
        return type;
    }

    public void setType(String type) {
        ParameterActivity.type = type;
    }

    public static String getFilename() {
        return filename;
    }

    public static void setFilename(String filename) {
        ParameterActivity.filename = filename;
    }
}
