package com.android.saadtechn.saadfilemanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed_Saad on 12/22/2016.
 */
public class FilesAdapter extends ArrayAdapter<String> {

    private List<String> ListItems = new ArrayList<String>();

    /**
     * Create a new {@link FilesAdapter} object.
     *
     * @param context is the current context (i.e. Activity) that the adapter is being created in.
     * @param ListItems is the list of {@link String}s to be displayed.
     */
    public FilesAdapter(Context context, List<String> ListItems) {
        super(context, 0 ,ListItems);
    }

    public FilesAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_view, parent, false);
        }

        // Get the {@link String} object located at this position in the list
        String currentListItem = getItem(position);

        // Find the TextView in the list_item_view.xml
        TextView fileTextView = (TextView) listItemView.findViewById(R.id.list_item);
        //put the text from the arrayList into the ListItemView
        fileTextView.setText(currentListItem);

        // Return the whole list item layout (containing 1 TextViews and 1 ImageView)
        // so that it can be shown in the ListView.
        return listItemView;
    }
}
