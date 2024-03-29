package it.apperol.group.worlddatabank.myadapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import it.apperol.group.worlddatabank.R;
import it.apperol.group.worlddatabank.myfragments.WelcomeFragment;
import it.apperol.group.worlddatabank.itemlist.MyIndicatorItem;
import it.apperol.group.worlddatabank.myactivities.CountryActivity;
import it.apperol.group.worlddatabank.myactivities.IndicatorActivity;
import it.apperol.group.worlddatabank.myactivities.PlotActivity;
import it.apperol.group.worlddatabank.mythreads.FetchData;
import it.apperol.group.worlddatabank.myviews.MyTextView;

public class MyIndicatorAdapter extends RecyclerView.Adapter<MyIndicatorAdapter.ViewHolder> implements Filterable {

    public static String indicatorID;
    public static  String indicatorName;

    private List<MyIndicatorItem> myIndicatorItems;
    private List<MyIndicatorItem> myIndicatorItemsFull;
    private static Context context;

    public static JSONArray ja;

    public static MyIndicatorItem myIndicatorItemText;

    public MyIndicatorAdapter(List<MyIndicatorItem> myIndicatorItems, Context context) {
        this.myIndicatorItems = myIndicatorItems;
        this.context = context;
        myIndicatorItemsFull = new ArrayList<>(myIndicatorItems);
    }


    @NonNull
    @Override
    public MyIndicatorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.indicator_item, viewGroup, false);
        return new MyIndicatorAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyIndicatorAdapter.ViewHolder viewHolder, int i) {
        final MyIndicatorItem myIndicatorItem = myIndicatorItems.get(i);

        viewHolder.myTvIndicatorName.setText(myIndicatorItem.getIndicatorName());
        viewHolder.myTvIndicatorID.setText(String.format(context.getResources().getString(R.string.indicator_id), myIndicatorItem.getIndicatorID()));

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            IndicatorActivity.recyclerView.setBackgroundColor(context.getResources().getColor(R.color.backgroundDark));
        } else {
            IndicatorActivity.recyclerView.setBackgroundColor(context.getResources().getColor(R.color.backgroundLight));
        }

        viewHolder.llIndicator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indicatorID = myIndicatorItem.getIndicatorID();
                myIndicatorItemText = myIndicatorItem;
                indicatorName = myIndicatorItem.getIndicatorName();
                if(WelcomeFragment.count == 0) {
                    FetchData process = new FetchData(IndicatorActivity.indicatorActivityContext.getResources().getString(R.string.fetch_country_url) + MyCountryAdapter.countryIso2Code + "/indicator/" + myIndicatorItem.getIndicatorID() + "?format=json", IndicatorActivity.indicatorActivityContext, 3);
                    process.execute();
                }
                else if(WelcomeFragment.count == 1){
                    Intent countryIntent = new Intent(context, CountryActivity.class);
                    context.startActivity(countryIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return myIndicatorItems.size();
    }

    @Override
    public Filter getFilter() {
        return indicatorFilter;
    }

    private Filter indicatorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyIndicatorItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(myIndicatorItemsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (MyIndicatorItem item : myIndicatorItemsFull) {
                    if (item.getIndicatorName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            myIndicatorItems.clear();
            myIndicatorItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder{

        public MyTextView myTvIndicatorName;
        public MyTextView myTvIndicatorID;
        public LinearLayout llIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myTvIndicatorName = itemView.findViewById(R.id.myTvIndicatorName);
            myTvIndicatorID = itemView.findViewById(R.id.myTvIndicatorID);
            llIndicator = itemView.findViewById(R.id.llIndicator);
        }
    }

    public static void fetchDataControl() {
        ja = FetchData.ja;

        if(ja != null) {
            Intent plotIntent = new Intent(context, PlotActivity.class);
            context.startActivity(plotIntent);
        }
    }
}
