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

// адаптер для отображения списка городов на экране в MainActivity
public class CityModelAdapter extends BaseAdapter {

    private List<CityModel> mListcity;
    private LayoutInflater mLayoutInflater;

    public CityModelAdapter(Context context, List<CityModel> listcity) {
        this.mListcity = listcity;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mListcity.size();
    }

    @Override
    public Object getItem(int position) {
        return mListcity.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //определение как будет выглядеть на экране элемент списка ListView
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.itemcity_layout, parent, false);
        }
        CityModel cityModel = getCityModel(position);
        TextView textView = (TextView) view.findViewById(R.id.textItemCity);
        textView.setText(cityModel.getName() + "   " + cityModel.getTemp() + " C");
        return view;
    }

    public CityModel getCityModel(int position) {
        return (CityModel) getItem(position);
    }

}
