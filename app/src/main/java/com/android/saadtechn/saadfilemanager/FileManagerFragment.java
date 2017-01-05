package com.android.saadtechn.saadfilemanager;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A {@link FileManagerFragment} subclass.
 */
public class FileManagerFragment extends Fragment {

    private List<String> fileList;
    private List<String> fileDirectories;
    private FilesAdapter directoryList;
    private ListView tempList;
    private File currentFile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Inflate the layout for this fragment
        fileList = new ArrayList<String>();
        fileDirectories = new ArrayList<String>();
        Intent intent = getActivity().getIntent();
        String currentDir = intent.getExtras().getString("KeyName");
        //get the file name from the file string
        currentFile = new File(currentDir);

        //set the folder name as the title of the activity
        getActivity().setTitle(currentFile.getName());
        File [] files = currentFile.listFiles();
        fileList.clear();
        for(File f:files) {
            fileList.add(f.getName());
            fileDirectories.add(f.getPath());
        }
        Collections.sort(fileList);
        Collections.sort(fileDirectories);

        tempList = (ListView)rootView.findViewById(R.id.list_item_files);
        directoryList =new FilesAdapter(getActivity(),fileList);

        tempList.setAdapter(directoryList);
        tempList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFile = new File(fileDirectories.get(position));
                // Check what kind of file you are trying to open, by comparing the url with extensions.
                // When the if condition is matched, plugin sets the correct intent (mime) type,
                if (currentFile.canExecute()) {
                    getFiles();

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



        return rootView;
    }
    private void openFile(File currentFile, String fileFormat) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(currentFile),fileFormat);
        startActivity(intent);
    }
    private void getFiles(){

        getActivity().setTitle(currentFile.getName());
        File [] files = currentFile.listFiles();
        fileList.clear();
        fileDirectories.clear();
        for(File f:files) {
            fileList.add(f.getName());
            fileDirectories.add(f.getPath());
        }
        Collections.sort(fileList);
        Collections.sort(fileDirectories);

        directoryList = new FilesAdapter(getActivity(),fileList);

        tempList.setAdapter(directoryList);

    }
}
