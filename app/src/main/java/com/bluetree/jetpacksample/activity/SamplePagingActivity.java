package com.bluetree.jetpacksample.activity;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bluetree.jetpacksample.R;
import com.bluetree.jetpacksample.bean.SamMusicBean;
import com.bluetree.jetpacksample.viewmodel.PagingViewModel;

/**
 * 参考：https://blog.csdn.net/huangxin388/article/details/88314543
 */
public class SamplePagingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private PagingViewModel pagingViewModel;
    private MyPagingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_paging);


        pagingViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(PagingViewModel.class);

        //step3、获取ViewModel中的LiveData（LiveData<T> ，T必须要是一个PagedList<>类型），注册观察者
        pagingViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<SamMusicBean>>() {
            @Override
            public void onChanged(@Nullable PagedList<SamMusicBean> musicBeans) {
                //step4、通过submitList()函数触发adapter数据更新
                adapter.submitList(musicBeans);
            }
        });

        recyclerView = findViewById(R.id.rv);

        //step1、adapter换成PagingAdapter
         adapter = new MyPagingAdapter( new DiffUtil.ItemCallback<SamMusicBean>() {
            @Override
            public boolean areItemsTheSame(@NonNull SamMusicBean musicBean, @NonNull SamMusicBean t1) {
                return musicBean.getText().equals(t1.getText());//比对唯一标识码
//                return false;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull SamMusicBean musicBean, @NonNull SamMusicBean t1) {
                return musicBean.equals(t1);//比对对象
//                return false;
            }
        });
        recyclerView.setAdapter(adapter);

    }

    class MyPagingAdapter extends PagedListAdapter<SamMusicBean, RecyclerView.ViewHolder> {

        protected MyPagingAdapter(@NonNull DiffUtil.ItemCallback<SamMusicBean> diffCallback) {
            super(diffCallback);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = LayoutInflater.from(SamplePagingActivity.this).inflate(android.R.layout.simple_list_item_1, null);
            RecyclerView.ViewHolder viewholder = new RecyclerView.ViewHolder(view) {
            };
            return viewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            //step2、获取list的方法必须换成PagingAdapter的getItem()函数
            String str = getItem(i).getText();
            ((TextView) viewHolder.itemView.findViewById(android.R.id.text1)).setText(str);
        }
    }


}
