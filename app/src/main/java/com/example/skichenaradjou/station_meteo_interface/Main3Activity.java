package com.example.skichenaradjou.station_meteo_interface;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Main3Activity extends AppCompatActivity {

    LineGraphSeries series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        int temperature[] = {1, 2, 5, 3, 6, 8, 5, 6, 9, 7};


        GraphView graph = findViewById(R.id.graph);

        series = new LineGraphSeries();
        for(int i = 0; i<10; i++)
        {
            series.appendData(new DataPoint(i, temperature[i]), true, 20);
        }
        graph.addSeries(series);
        series.setColor(Color.rgb(226, 91, 34));
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(10,255, 10, 12));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
    }
}
