package br.com.ramonilho.bolados.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.ramonilho.bolados.R;

public class EditStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_store);

        setTitle(getString(R.string.edit_store_info));
    }
}
