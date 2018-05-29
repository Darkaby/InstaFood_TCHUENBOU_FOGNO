package com.esiea.instafood;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class ParameterActivity extends AppCompatActivity {

    EditText choixSelection;
    EditText placeSelection;
    RecyclerView rv_choix;
    RecyclerView rv_place;

    //final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        rv_choix = (RecyclerView)findViewById(R.id.list_choix_resto);
        rv_choix.setLayoutManager(new LinearLayoutManager(this));
        rv_choix.setAdapter(new ChoixAdapter());

        choixSelection = (EditText)findViewById(R.id.choix_resto_edit_text);
        choixSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_choix.setVisibility(View.VISIBLE);
                choixSelection.getText().clear();
            }
        });

        rv_place = (RecyclerView)findViewById(R.id.list_lieu_resto);
        rv_place.setLayoutManager(new LinearLayoutManager(this));
        rv_place.setAdapter(new PlaceAdapter());

        placeSelection = (EditText)findViewById(R.id.choix_resto_lieu_edit_text);
        placeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rv_place.setVisibility(View.VISIBLE);
                placeSelection.getText().clear();
            }
        });

    }

    //////////////////
    // Gestion Menu //
    //////////////////

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_restaurant, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.geo:
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+getLocation().getLatitude()+","+getLocation().getLongitude()+"?q=Restaurants")));
                return true;
            case R.id.sign_out:
                //Toast.makeText(MainActivity.this, " Item du Menu sélectionné", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(MainActivity.this, SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////
    // Adaptater RV Choix //
    ////////////////////////

    public class ChoixAdapter extends RecyclerView.Adapter<ChoixAdapter.ChoixViewHolder>{

        private final String choixResto[] = {"Chinois", "Indien", "Grec", "Africain",
                "Italien", "Japonais", "Français", "Aucun choix particulier"};

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

        public class ChoixViewHolder extends RecyclerView.ViewHolder{

            final TextView name;

            public ChoixViewHolder(View itemView) {
                super(itemView);

                name = ((TextView)itemView.findViewById(R.id.choix_name));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        choixSelection.setText(name.getText());
                        rv_choix.setVisibility(View.GONE);
                    }
                });

            }
        }
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>{

        private final String placeResto[] = {"Ma position", "Paris", "Lille", "Marseilles", "Lyon", "Cannes"};

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

        public class PlaceViewHolder extends RecyclerView.ViewHolder{

            final TextView name;

            public PlaceViewHolder(View itemView) {
                super(itemView);

                name = ((TextView)itemView.findViewById(R.id.choix_name));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        placeSelection.setText(name.getText());
                        rv_place.setVisibility(View.GONE);
                    }
                });

            }
        }
    }


}
