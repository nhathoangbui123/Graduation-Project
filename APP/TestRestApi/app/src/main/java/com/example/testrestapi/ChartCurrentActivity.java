package com.example.testrestapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChartCurrentActivity extends AppCompatActivity {
    private final String TAG = ChartCurrentActivity.class.getSimpleName();
    private Toolbar toolbar;
    private Spinner spinnerYear;
    private Spinner spinnerMonth;

    private LineChart mLineChart;
    private BarChart mBarChart;

    private ArrayList<Entry> yValuesLineChart = new ArrayList();
    private ArrayList<BarEntry> yValuesBarChart = new ArrayList();

    private Calendar myCal;
    private int daysInMonth ;
    private int currentMonth;
    private int currentDay;
    private int currentYear;
    private int userPromptYear ;
    private int userPromptMonth ;

    private String [] yearList = new String[5];
    private String [] monthList = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    private Date today;
    private Calendar todayCal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_current);

        int tempYear;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chart Current Total");

        mLineChart = findViewById(R.id.lineChart);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(false);

        mBarChart = findViewById(R.id.BarChart);
        mBarChart.setDrawBarShadow(false);
        mBarChart.setDrawValueAboveBar(true);
        mBarChart.setPinchZoom(false);
        mBarChart.setDrawGridBackground(true);

        today = new Date();
        todayCal = Calendar.getInstance();
        todayCal.setTime(today);
        currentYear = todayCal.get(Calendar.YEAR);
        currentMonth = todayCal.get(Calendar.MONTH);
        currentDay = todayCal.get(Calendar.DAY_OF_MONTH);

        for(int yearCount = 0 ; yearCount <5 ;yearCount++){
            tempYear = currentYear - yearCount;
            yearList[yearCount] =  Integer.toString(tempYear);
            Log.i(TAG, "onCreate:  current Year " +  yearList[yearCount] );
        }

        spinnerYear = findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapterYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userPromptYear = Integer.parseInt(parent.getItemAtPosition(position).toString());
                Log.i(TAG, "spinnerYear onItemSelected:  userPromptYear " +  userPromptYear );
                //drawLineGraph("Device1");
//                drawBarGraph("Device1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerMonth = findViewById(R.id.spinnerMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userPromptMonth= position + 1;
                Log.i(TAG, "spinnerMonth onItemSelected:  userPromptMonth " +  userPromptMonth );
                //drawLineGraph("Device1");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.i(TAG, "onDataChange: HElooooooooooooo im hereeeeee beforeeeee ");

        drawLineGraph("Device1");
        drawBarGraph("Device1");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ChartCurrentActivity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void  drawLineGraph( String path){
        yValuesLineChart.clear();
        float[] yValLineC;

        myCal = new GregorianCalendar(userPromptYear, userPromptMonth -1, 1);
        daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.i(TAG ,"drawLineGraph: Current Month "+ currentMonth );
        Log.i(TAG ,"drawLineGraph: Current date "+ currentDay );
        Log.i(TAG ,"drawLineGraph: days in month "+ daysInMonth );
        Log.i(TAG ,"drawLineGraph: user prompt month "+ userPromptMonth );
        if((currentMonth + 1) == userPromptMonth){
            yValLineC = new float[currentDay];
        }else{
            yValLineC = new float[daysInMonth];
        }

        Arrays.fill(yValLineC, 0);

        yValLineC[0] = 1;
        yValLineC[1] = 2;
        yValLineC[2] = 3;
        yValLineC[3] = 5;
        yValLineC[4] = 1;
        yValLineC[5] = 5;
        yValLineC[6] = 2;
        yValLineC[7] = 3;
        yValLineC[8] = 1;
        yValLineC[9] = 2;
        yValLineC[10] = 1;
        yValLineC[11] = 3;
        yValLineC[12] = 2;
        yValLineC[13] = 2;
        yValLineC[14] = 3;
        yValLineC[15] = 4;
        yValLineC[16] = 5;
        yValLineC[17] = 1;
        yValLineC[18] = 6;
        yValLineC[19] = 4;
        yValLineC[20] = 5;
        yValLineC[21] = 6;
        yValLineC[22] = 4;
        yValLineC[23] = 2;
        yValLineC[24] = 1;
        yValLineC[25] = 3;
        yValLineC[26] = 0;
        yValLineC[27] = 4;
        yValLineC[28] = 7;
        yValLineC[29] = 1;
        //yValLineC[30] = 100;
//        for(int i=1;i<=daysInMonth;i++){
//            //yValLineC[i-1] = Float.parseFloat(Objects.requireNonNull("100"));
//            yValLineC[i-1] = i;
//        }

        for(int counter = 0; counter < yValLineC.length ; counter++  ){
            yValuesLineChart.add(new Entry((counter +1), yValLineC[counter] ));
        }
        LineDataSet set1 = new LineDataSet(yValuesLineChart, "Daily Current (A)");

        set1.setColor(0xE1134AD4);
        set1.setLineWidth(4f);
        set1.setValueTextSize(10f);
        set1.setValueTextColor(Color.BLACK);
        set1.setDrawValues(false);

        set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        set1.setCubicIntensity(0.5f);

        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataset = new ArrayList<>();
        dataset.add(set1);

        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue);
        set1.setFillDrawable(drawable);

        YAxis yl = mLineChart.getAxisLeft();
        yl.setAxisMinimum(0f);



        LineData data = new LineData(dataset);
        mLineChart.setTouchEnabled(true);

        Description description = new Description();
        description.setText("");
        mLineChart.setDescription(description);

        IMarker marker = new MyMarkerView(this, R.layout.tvcontent);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(true);
        mLineChart.setMarker(marker);
        mLineChart.setData(data);
        mLineChart.invalidate();
    }

    private void drawBarGraph(String path){
        yValuesBarChart.clear();

        float[] yValBarC;
        if(currentYear == userPromptYear) {
            yValBarC = new float[currentMonth +1];
        }else{
            yValBarC = new float[12];
        }
        Arrays.fill(yValBarC, 0);

        myCal = new GregorianCalendar(userPromptYear, userPromptMonth -1, 1);
        daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        yValBarC[0] = 0;
        yValBarC[1] = 0;
        yValBarC[2] = 0;
        yValBarC[3] = 0;
        yValBarC[4] = 2;
        yValBarC[5] = 4;
        yValBarC[6] = 0;
        yValBarC[7] = 0;
        yValBarC[8] = 0;
        yValBarC[9] = 0;
        yValBarC[10] = 0;
        yValBarC[11] = 0;
//        for(int i=1;i<5;i++){
//            yValBarC[i-1] = Float.parseFloat(Objects.requireNonNull("1"));
//        }

        for(int counter = 0; counter < yValBarC.length ; counter++  ){
            yValuesBarChart.add(new BarEntry((counter +1), yValBarC[counter] ));
        }

        BarDataSet set1 = new BarDataSet(yValuesBarChart, "Monthly Current (A)");
        set1.setColors(0x80134AD4);

        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);

        BarData data = new BarData(set1);
        data.setBarWidth(0.9f);

        mBarChart.setData(data);
        mBarChart.invalidate();

    }
}