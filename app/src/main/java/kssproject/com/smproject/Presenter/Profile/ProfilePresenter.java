package kssproject.com.smproject.Presenter.Profile;

/**
 * Created by b3216 on 2017-07-30.
 */

public interface ProfilePresenter {
    void setView(ProfilePresenter.View view);

    void onConfirm(String name, Integer age, Float height, Float weight, Float goalweight,String sex);

    void start();
    public interface View {
        void storeData();
        void startActivity(Class MainActivity);
    }
}
