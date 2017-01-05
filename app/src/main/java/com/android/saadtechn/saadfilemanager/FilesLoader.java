package com.android.saadtechn.saadfilemanager;

import android.content.Context;
import android.os.Environment;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;

/**
 * Created by Mohamed_Saad on 12/21/2016.
 */
public class FilesLoader extends AsyncTaskLoader<File[]> {

    /** Query File Path*/
    private String mFilesPath;

    public FilesLoader(Context context,String filePath){
        super(context);
        mFilesPath = filePath;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    /**
     * This is on a background thread.
     */
    @Override
    public File[] loadInBackground() {
        if (mFilesPath == null) {
            return null;
        }

        File root = new File(mFilesPath);
        File[] files = root.listFiles();

        return files;
    }

}
