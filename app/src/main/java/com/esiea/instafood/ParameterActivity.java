package com.esiea.instafood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class ParameterActivity extends AppCompatActivity {

    private ListView listView;
    ArrayAdapter<String> adaptater;
    EditText choixSearch;
    ArrayList<HashMap<String, String>> choixList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);

        String choixResto[] = {"Chinois", "Indien", "Grec", "Africain",
                "Italien", "Japonais", "Français", "Aucun choix particulier"};

        listView = (ListView)findViewById(R.id.list_choix_resto);
        choixSearch = (EditText)findViewById(R.id.choix_resto_edit_text);

        adaptater = new ArrayAdapter<>(this, R.layout.list_item, R.id.choix_name, choixResto);
        listView.setAdapter(adaptater);

        choixSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ParameterActivity.this.adaptater.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
