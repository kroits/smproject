package kssproject.com.smproject.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by b3216 on 2017-07-17.
 */

public class StoreData {

    private static  StoreData mStoreData = new StoreData();

    private StoreData(){}
    private List<String> date = new ArrayList<String>();
    private List<Long> calorie = new ArrayList<Long>();
    private List<Double> weight = new ArrayList<Double>();
    public static StoreData getInstance(){return mStoreData;}

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<Long> getCalorie() {
        return calorie;
    }

    public void setCalorie(List<Long> calorie) {
        this.calorie = calorie;
    }

    public List<Double> getWeight() {
        return weight;
    }

    public void setWeight(List<Double> weight) {
        this.weight = weight;
    }

    public void dataClear(){
        date.clear();
        weight.clear();
        calorie.clear();
    }

    public void storeNoneData(int counter){
        Date date = DateUtil.getInstance().getDate();
        Date date2 = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date2.setDate(date.getDate()+1);
        for(int i = 0 ; i < counter-1; i++) {
            date2.setDate(date2.getDate()-1);
            String updateStrDate = dateFormat.format(date2);
            this.date.add(updateStrDate);
            this.calorie.add(0L);
            this.weight.add(0D);
        }
    }
}
