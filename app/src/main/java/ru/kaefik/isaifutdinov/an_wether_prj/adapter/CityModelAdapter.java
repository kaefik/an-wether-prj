package ru.kaefik.isaifutdinov.an_wether_prj.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.kaefik.isaifutdinov.an_wether_prj.R;
import ru.kaefik.isaifutdinov.an_wether_prj.city.CityModel;

public class CityModelAdapter extends BaseAdapter {

    private List<CityModel> listcity;
    private LayoutInflater layoutinflater;

    public CityModelAdapter(Context context, List<CityModel> listcity) {
        this.listcity = listcity;
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listcity.size();
    }

    @Override
    public Object getItem(int position) {
        return listcity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutinflater.inflate(R.layout.itemcity_layout,parent,false);
        }

       CityModel cityModel = getCityModel(position);

        TextView textView = (TextView) view.findViewById(R.id.textItemCity);
        textView.setText(cityModel.getName());

        return view;
    }


    public CityModel getCityModel(int position) {
        return (CityModel) getItem(position);
    }

}
