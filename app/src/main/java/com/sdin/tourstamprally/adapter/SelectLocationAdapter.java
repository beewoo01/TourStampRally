package com.sdin.tourstamprally.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sdin.tourstamprally.R;

import java.util.List;

public class SelectLocationAdapter extends BaseAdapter {

    private List<String> data;
    private LayoutInflater inflater;

    public SelectLocationAdapter(Context context, List<String> data) {
        clearItem();
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (data != null)
            return data.size();

        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = inflater.inflate(R.layout.spinner_title_item, parent, false);

            if (data != null){
                String text = data.get(position);
                TextView textView = convertView.findViewById(R.id.spinnerTitleText);
                textView.setText(text);
            }
        }
        return convertView;
    }

    public void clearItem(){
        if (data != null){
            data.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);

        }

        ((TextView)convertView.findViewById(R.id.spinnerText)).setText(data.get(position));

        return convertView;
    }
}
