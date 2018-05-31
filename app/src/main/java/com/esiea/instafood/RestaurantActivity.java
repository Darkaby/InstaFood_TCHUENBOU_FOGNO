package com.esiea.instafood;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.esiea.instafood.LoadingActivity.getLocation;


public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        //////////////////
        // RecyclerView //
        //////////////////
        RecyclerView list_restaurants = (RecyclerView)findViewById(R.id.rv_restaurant);
        list_restaurants.setLayoutManager(new LinearLayoutManager(RestaurantActivity.this));
        list_restaurants.setAdapter(new RestaurantAdapter());

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
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+getLocation().getLatitude()+","+getLocation().getLongitude() + "?z=20")));

                    return true;

                case R.id.sign_out:
                    if (SignInActivity.firebaseUser != null)
                        startActivity(new Intent(RestaurantActivity.this, SignInActivity.class));
                    else
                        new NotiClass(this, getString(R.string.sign_out_text_ko));
                    return true;

                case R.id.sign_in:
                    if (SignInActivity.firebaseUser == null)
                        startActivity(new Intent(RestaurantActivity.this, SignInActivity.class));
                    else
                        new NotiClass(this, getString(R.string.sign_in_text_ko));
                    return true;

                case R.id.aide:
                    new NotiClass(this, getString(R.string.help), getString(R.string.help_parameter));

            }
        return super.onOptionsItemSelected(item);
    }

    //////////////////
    // Gestion Menu //
    //////////////////


    //////////////////
    // RecyclerView //
    //////////////////

    public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>{

        @Override
        public int getItemCount() {
            return LocalisationService.getList_restaurant().size();
        }

        @NonNull
        @Override
        public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.list_item_resto, parent, false );
            return new RestaurantViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RestaurantViewHolder holder, int position) {
            Restaurant restaurant = LocalisationService.getList_restaurant().get(position);
            holder.display(restaurant);
        }

        public class RestaurantViewHolder extends RecyclerView.ViewHolder {

            private final TextView name;
            private final TextView address;
            private final ImageView imageView;
            private Restaurant currentRestaurant;

            public RestaurantViewHolder(final View itemView) {
                super(itemView);

                name = ((TextView) itemView.findViewById(R.id.name_restaurant));
                address = ((TextView) itemView.findViewById(R.id.address_resto));
                imageView = ((ImageView) itemView.findViewById(R.id.image_resto));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new AlertDialog.Builder(itemView.getContext())
                                .setTitle(currentRestaurant.getName())
                                .setMessage(currentRestaurant.getAddress())
                                .show();
                    }
                });
            }

            public void display(Restaurant restaurant) {
                currentRestaurant = restaurant;
                name.setText(restaurant.getName());
                address.setText(restaurant.getAddress());
                if(ParameterActivity.getType().equals(getString(R.string.African)))
                    imageView.setImageDrawable(getDrawable(R.drawable.africain));
                else if (ParameterActivity.getType().equals(getString(R.string.Chinese)))
                    imageView.setImageDrawable(getDrawable(R.drawable.chinois));
                else if (ParameterActivity.getType().equals(getString(R.string.Japanese)))
                    imageView.setImageDrawable(getDrawable(R.drawable.japonais));
                else if (ParameterActivity.getType().equals(getString(R.string.Greek)))
                    imageView.setImageDrawable(getDrawable(R.drawable.grec));
                else if (ParameterActivity.getType().equals(getString(R.string.Fast_Food)))
                    imageView.setImageDrawable(getDrawable(R.drawable.fastfood));
                else if (ParameterActivity.getType().equals(getString(R.string.Italian)))
                    imageView.setImageDrawable(getDrawable(R.drawable.italien));
                else if (ParameterActivity.getType().equals(getString(R.string.Indian)))
                    imageView.setImageDrawable(getDrawable(R.drawable.indien));
                else if (ParameterActivity.getType().equals(getString(R.string.No_particular_choice)))
                    imageView.setImageDrawable(getDrawable(R.drawable.no_choice));
                else if (ParameterActivity.getType().equals(getString(R.string.French)))
                    imageView.setImageDrawable(getDrawable(R.drawable.francais));
            }
        }

    }

}
