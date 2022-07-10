package com.example.testrestapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

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

public class ChartCostActivity extends AppCompatActivity {
    private final String TAG = ChartCostActivity.class.getSimpleName();
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
        setContentView(R.layout.activity_chart_cost);
        int tempYear;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Chart Cost Total");

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
                drawLineGraph(userPromptMonth);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.i(TAG, "onDataChange: HElooooooooooooo im hereeeeee beforeeeee ");

        drawLineGraph(6);
        drawBarGraph("Device1");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ChartCostActivity.this, MainActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void  drawLineGraph(int month){
        yValuesLineChart.clear();
        float[] yValLineC;

        myCal = new GregorianCalendar(userPromptYear, userPromptMonth -1, 1);
        daysInMonth = myCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.i(TAG ,"drawLineGraph: Current Month "+ currentMonth );
        Log.i(TAG ,"drawLineGraph: Current date "+ currentDay );
        Log.i(TAG ,"drawLineGraph: days in month "+ daysInMonth );
        Log.i(TAG ,"drawLineGraph: user prompt month "+ userPromptMonth );
//        if((currentMonth + 1) == userPromptMonth){
//            yValLineC = new float[currentDay];
//        }else{
            yValLineC = new float[daysInMonth];
       // }

        Arrays.fill(yValLineC, 0);
        if(month==6){
            yValLineC[0] = 3500;
            yValLineC[1] = 3500;
            yValLineC[2] = 7000;
            yValLineC[3] = 3500;
            yValLineC[4] = 10500;
            yValLineC[5] = 35000;
            yValLineC[6] = 14000;
            yValLineC[7] = 17500;
            yValLineC[8] = 24500;
            yValLineC[9] = 3500;
            yValLineC[10] = 3500;
            yValLineC[11] = 3500;
            yValLineC[12] = 7000;
            yValLineC[13] = 7000;
            yValLineC[14] = 3500;
            yValLineC[15] = 3500;
            yValLineC[16] = 35000;
            yValLineC[17] = 17500;
            yValLineC[18] = 28000;
            yValLineC[19] = 35000;
            yValLineC[20] = 3500;
            yValLineC[21] = 7000;
            yValLineC[22] = 35000;
            yValLineC[23] = 10500;
            yValLineC[24] = 14000;
            yValLineC[25] = 17500;
            yValLineC[26] = 21000;
            yValLineC[27] = 3500;
            yValLineC[28] = 7000;
            yValLineC[29] = 3500;
        }else if(month==7){
            yValLineC[0] = 3500;
            yValLineC[1] = 3500;
            yValLineC[2] = 7000;
            yValLineC[3] = 3500;
            yValLineC[4] = 3500;
            yValLineC[5] = 3500;
            yValLineC[6] = 3500;
            yValLineC[7] = 3500;
            yValLineC[8] = 7000;
            yValLineC[9] = 3500;
            yValLineC[10] = 3500;
            yValLineC[11] = 0;
            yValLineC[12] = 0;
            yValLineC[13] = 0;
            yValLineC[14] = 0;
            yValLineC[15] = 0;
            yValLineC[16] = 0;
            yValLineC[17] = 0;
            yValLineC[18] = 0;
            yValLineC[19] = 0;
            yValLineC[20] = 0;
            yValLineC[21] = 0;
            yValLineC[22] = 0;
            yValLineC[23] = 0;
            yValLineC[24] = 0;
            yValLineC[25] = 0;
            yValLineC[26] = 0;
            yValLineC[27] = 0;
            yValLineC[28] = 0;
            yValLineC[29] = 0;
        }

        //yValLineC[30] = 100;
//        for(int i=1;i<=daysInMonth;i++){
//            //yValLineC[i-1] = Float.parseFloat(Objects.requireNonNull("100"));
//            yValLineC[i-1] = i;
//        }

        for(int counter = 0; counter < yValLineC.length ; counter++  ){
            yValuesLineChart.add(new Entry((counter +1), yValLineC[counter] ));
        }
        LineDataSet set1 = new LineDataSet(yValuesLineChart, "Daily Cost (VND)");

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
        yValBarC[4] = 0;
        yValBarC[5] = 388500;
        yValBarC[6] = 45500;
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

        BarDataSet set1 = new BarDataSet(yValuesBarChart, "Monthly Cost (VND)");
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