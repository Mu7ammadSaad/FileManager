package com.android.saadtechn.saadfilemanager;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2_main);
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity2_container,new FileManagerFragment())
                    .commit();
        }


        /**
        tempList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File currentFile = new File(fileDirectories.get(position));
                if (currentFile.canExecute()){
                    Intent intent = new Intent(getContext(),Main2Activity.class);
                    intent.putExtra("KeyName",fileDirectories.get(position));
                    startActivity(intent);
                }
                else
                    Toast.makeText(getContext(), fileDirectories.get(position),Toast.LENGTH_LONG).show();
            }
        });

        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
