package com.example.hackaz.schedule;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hackaz.DownloadTask;
import com.example.hackaz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ScheduleDayActivity extends AppCompatActivity {
    private String day;
    private ArrayList<String> daySchedule;
    ListView scheduleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_day);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            day = extras.getString("day");
            //The key argument here must match that used in the other activity
            Log.i("day", day);
        }


        DownloadTask task = new DownloadTask();
        String result = null;

        try {
            result = task.execute("http://hackarizona.org/2017/masterschedule2017.json").get();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException f) {
            f.printStackTrace();
        }

        addJSONContent(result);
        setUpListView();

    }

    private void addJSONContent(String result) {
        daySchedule = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(result);

            String dayInfo = jsonObject.getString(day);

            JSONArray events = new JSONArray(dayInfo);

            //add each events info to the schedule
            for (int i = 0; i < events.length(); i++){
                JSONObject event = events.getJSONObject(i);

                daySchedule.add("\n" + event.getString("eventtitle") + " " + event.getString("subtitle") + "\n" +
                        event.getString("time") + " - " + event.getString("location") + "\n" );
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    //adds the list view and json listview content
    private void setUpListView() {

        // Get ListView object from xml
        scheduleView = (ListView) findViewById(R.id.schedule_day_list);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, daySchedule){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

            /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(15);
                return view;
            }
        };
        scheduleView.setAdapter(adapter);
    }
}