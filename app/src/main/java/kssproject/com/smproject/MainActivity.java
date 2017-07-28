package kssproject.com.smproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kssproject.com.smproject.DataPackage.DateToday;
import kssproject.com.smproject.FireBase.ModifyDb;
import kssproject.com.smproject.FireBase.SelectDb;
import kssproject.com.smproject.FireBase.StoreDb;
import kssproject.com.smproject.MidStore.StoreData;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private Button button2;
    private EditText testText;
    private String TAG = "kss";
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference userRef = null;
    private SharedPreferences sp;
    private DateToday mDateToday =DateToday.getInstance();


    //// TODO: 2017-07-25  graph

    private LineChartView chartTop;
    private LineChartData lineData;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        key = getIntent().getStringExtra("UserKey");
        sp = getSharedPreferences("profile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        StoreDb.getInstance().DataSave(key,0.0,0);

        initView();

    }

    void initView() {
        button = (Button) findViewById(R.id.button2);
        button2 = (Button)findViewById(R.id.buttonBurn);
        chartTop = (LineChartView) findViewById(R.id.chart_top);
        testText = (EditText)findViewById(R.id.editText);

        if(key !=null) {
            databaseReference.child(key).child("information").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SelectDb.getInstance().SelectData(key);
                    generateInitialLineData();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyDb.getInstance().CalorieModify(key,mDateToday.getStrDate(),Integer.parseInt(testText.getText().toString()));
                Intent intentdata = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intentdata);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
//                removeSettingAll(getApplicationContext(),"Name");
            }
        });

    }

    //// TODO: 2017-07-25  graph
    private void generateInitialLineData() {
        StoreData storeData = StoreData.getInstance();
        int numValues = 0;

        if (storeData.getWeight().size() > 7) {
            numValues = storeData.getWeight().size() - 7;
        } else {
        }

        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<PointValue> values = new ArrayList<PointValue>();
        for (int i = 0, j = numValues; j < storeData.getWeight().size(); ++i, j++) {
            values.add(new PointValue(i, storeData.getCalorie().get(j)));
            axisValues.add(new AxisValue(i).setLabel(storeData.getDate().get(j).substring(5)));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_ORANGE).setCubic(false);


        line.setHasLabels(true);  // 그래프 y값 표시

        List<Line> lines = new ArrayList<Line>();
        line.setStrokeWidth(3);
        lines.add(line);
        lineData = new LineChartData(lines);
        lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        lineData.setAxisYLeft(new Axis().setName("Cal [kcal]").setHasLines(true).setMaxLabelChars(4));

        chartTop.setLineChartData(lineData);

        Viewport calorieView = chartTop.getMaximumViewport();
        calorieView.set(calorieView.left, calorieView.top, calorieView.right + 1, 0);
        calorieView.top = calorieView.top+400;

        chartTop.setMaximumViewport(calorieView);
        chartTop.setCurrentViewport(calorieView);



    }
}
