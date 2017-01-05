
package com.android.saadtechn.saadfilemanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<File[]> {

    private ArrayList<String> filesPathsList;
    private ArrayList<String> filesNamesList;
    private FilesAdapter directoryAdapter;
    private String list_item;
    private String list_item_name;
    private String  path;
    private Object mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filesNamesList = new ArrayList<>();
        filesPathsList = new ArrayList<>();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String defalutFolder = sharedPrefs.getString(
                getString(R.string.settings_default_folder_key),
                getString(R.string.settings_default_folder_default));
        path = Environment.getExternalStorageDirectory().toString() + defalutFolder;


        directoryAdapter = new FilesAdapter(this, new ArrayList<String>());
        ListView tempList = (ListView)findViewById(R.id.list_item_files);
        tempList.setAdapter(directoryAdapter);

        //launch the loader on the background to load the default folder
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

        //handle the item click
        tempList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File currentFile = new File(filesPathsList.get(position));
                // Check what kind of file you are trying to open, by comparing the url with extensions.
                // When the if condition is matched, plugin sets the correct intent (mime) type,
                if (currentFile.canExecute()) {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    intent.putExtra("KeyName", filesPathsList.get(position));
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (currentFile.toString().contains(".pdf"))
                    openFile(currentFile,"application/pdf");

                 else if (currentFile.toString().contains(".doc") || currentFile.toString().contains(".docx"))
                    openFile(currentFile,"application/msword");

                 else if (currentFile.toString().contains(".jpg") ||
                        currentFile.toString().contains(".jpeg") || currentFile.toString().contains(".png"))
                    openFile(currentFile,"image/jpeg");
                 else if(currentFile.toString().contains(".ppt") || currentFile.toString().contains(".pptx"))
                     // Powerpoint file
                    openFile(currentFile,"application/vnd.ms-powerpoint");
                 else if(currentFile.toString().contains(".xls") || currentFile.toString().contains(".xlsx"))
                    // Excel file
                    openFile(currentFile, "application/vnd.ms-excel");
                  else if(currentFile.toString().contains(".zip") || currentFile.toString().contains(".rar"))
                    // WAV audio file
                    openFile(currentFile,"application/x-wav");

                 else if(currentFile.toString().contains(".rtf"))
                    // RTF file
                    openFile(currentFile,"application/rtf");

                 else if(currentFile.toString().contains(".wav") || currentFile.toString().contains(".mp3"))
                    // WAV audio file
                    openFile(currentFile,"audio/x-wav");

                 else if(currentFile.toString().contains(".gif"))
                    // GIF file
                    openFile(currentFile,"image/gif");
                 else if(currentFile.toString().contains(".txt"))
                    // Text file
                    openFile(currentFile,"text/plain");
                 else if(currentFile.toString().contains(".3gp") || currentFile.toString().contains(".mpg") ||
                        currentFile.toString().contains(".mpeg") || currentFile.toString().contains(".mpe") ||
                        currentFile.toString().contains(".mp4") || currentFile.toString().contains(".avi"))
                    // Video files
                    openFile(currentFile,"video/*");

                 else {
                    //if you want you can also define the intent type for any other file
                    //additionally use else clause below, to manage other unknown extensions
                    //in this case, Android will show all applications installed on the device
                    //so you can choose which application to use
                    openFile(currentFile,"*/*");
            }
            }
        });

        //delete the item when it's long clicked
        tempList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list_item = filesPathsList.get(position);
                list_item_name = filesNamesList.get(position);
                if (mActionMode != null){
                    return  false;
                }
                mActionMode = MainActivity.this.startActionMode(mActionModeCallBack);
                return true;
            }
        });
    }

    /**Helper method to open the file according to it's format */
    private void openFile(File currentFile, String fileFormat) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(currentFile),fileFormat);
        startActivity(intent);
    }

    @Override
    public Loader<File[]> onCreateLoader(int id, Bundle args) {
        return new FilesLoader(MainActivity.this, path);
    }

    @Override
    public void onLoadFinished(Loader<File[]> loader, File[] data) {

        filesNamesList.clear();
        filesPathsList.clear();
        directoryAdapter.clear();
        for(File f:data){
            String temp = f.getName();
            if(!temp.startsWith(".")) {
                filesNamesList.add(f.getName());
                filesPathsList.add(f.getPath());
            }
        }
        Collections.sort(filesNamesList);
        Collections.sort(filesPathsList);
        directoryAdapter.addAll(filesNamesList);
    }

    @Override
    public void onLoaderReset(Loader<File[]> loader) {
        //Clear the Adapter
        directoryAdapter.clear();
    }

    private ActionMode.Callback mActionModeCallBack = new ActionMode.Callback(){
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.setTitle(list_item_name);
            MenuInflater inflater = mode.getMenuInflater();
            // inflate contextual menu
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            switch (item.getItemId()) {
                case R.id.delete_menu:
                    confirmDelete();
                    mode.finish();
                    return true;

                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            mActionMode = null;
        }
    };

    private void confirmDelete() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Confirm delete");
        alert.setMessage("Are you sure you want to delete this file?");
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                File deleteFile = new  File(list_item);
                boolean isDeleted = deleteFile.delete();
                directoryAdapter.remove(list_item_name);

                directoryAdapter.notifyDataSetChanged();
                dialog.dismiss();
                if(isDeleted){
                    Toast.makeText(MainActivity.this,"File has been deleted",Toast.LENGTH_LONG).show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Do nothing
                dialog.dismiss();
            }
        });

        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        if (id == R.id.action_refresh){
            getSupportLoaderManager().initLoader(0, null, this).forceLoad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}