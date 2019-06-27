package com.bluetree.jetpacksample.activity;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.databinding.ActivitySampleBindDataBinding;
import com.bluetree.jetpacksample.bean.SamUserBean;
import com.bluetree.jetpacksample.bean.SamUserBean2;
import com.bluetree.jetpacksample.bean.SamUserBean3;

public class SampleBindDataActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    /**
     * 这个binding是编译的时候自动生成的，根据layout文件生成的，R.layout.activity_sample_bind_data
     * ，编译器会自动生成一个ActivitySampleBindDataBinding文件在build目录下
     */
    private ActivitySampleBindDataBinding binding;

    private SamUserBean user;
    private SamUserBean2 user2;
    private SamUserBean3 user3;

    private ListView lv;
    String[] strArr = {
            "更新整体数据"
            ,"使用 BaseObservable"
            ,"使用 ObservableFields"
            ,"双向绑定，@={user3.sex}"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sample_bind_data);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_bind_data);
        user = new SamUserBean("Test", "User");


        lv = findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,strArr));
        lv.setOnItemClickListener(this);
    }

    /**
     * 在布局文件中，
     * @param s
     * @return
     */
    public static String increaseNumber(String s) {
        return "xml可以调用public static 函数："+s;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //整体更新，通过重新setXX(bean)
                user.setFirstName(user.getFirstName() + 1);
                binding.setUser(user);
                break;
            case 1:
//                BaseObservable 是一个基类，需要我们的数据 bean 继承这个基类，然后给属性的 get 方法
//                添加@Bindable 这个注解，然后在属性的 set 方法中添加上 DataBinding 更新某个字段的方法
                if(user2==null){
                    user2 = new SamUserBean2("test2", "user2");
                    binding.setUser2(user2);
                }
                //在 activity 中操作数据 bean 的 set 方法就可以同步把数据更新到 view 中了
                user2.setFirstName(user2.getFirstName()+"2");
                break;
            case 2:
/*ObservableFields 是一个对属性添加 DataBinding 更新功能的代理类，
针对不同的数据类型有不同类型的 ObservableFields ：ObservableBoolean、 ObservableByte ObservableChar、ObservableShort、ObservableInt、ObservableLong、ObservableFloat、ObservableDouble、 ObservableParcelable 等。
这种方式不是主流类型，使用不便，不能扩展属性
*/
                if (user3 == null) {
                    user3 = new SamUserBean3();
                    binding.setUser3(user3);
                    user3.firstName.set("name");
                }
                user3.firstName.set(user3.firstName.get() + "3");
                user3.sex.set(!user3.sex.get());
                break;
            case 3:
                if (user3 == null) {
                    user3 = new SamUserBean3();
                    binding.setUser3(user3);
                    user3.firstName.set("name");
                }
                Toast.makeText(this, ""+user3.sex.get(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
