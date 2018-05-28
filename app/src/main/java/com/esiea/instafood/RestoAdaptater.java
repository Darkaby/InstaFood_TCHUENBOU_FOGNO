package com.esiea.instafood;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class RestoAdaptater extends RecyclerView.Adapter<RestoAdaptater.RestoHolder>{

    private JSONArray restos;

    public RestoAdaptater(JSONArray array){
        super();
        this.restos = array;
    }


    @Override
    public RestoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rv_restaurants, parent, false);
        return new RestoHolder(view);
    }

    @Override
    public void onBindViewHolder(RestoHolder holder, int position) {


        try {
            JSONObject restoJSON = restos.getJSONObject(position);
            holder.name.setText(restoJSON.getString(String.valueOf(holder.name)));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return restos.length();
    }

    public class RestoHolder extends RecyclerView.ViewHolder{

        public TextView name;

        public RestoHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.rv_resto_name);
        }
    }

    public void setNewResto (JSONArray array){
        notifyDataSetChanged();
    }
}

