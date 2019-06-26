package com.diggoods.api.only_one;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.immersionbar.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ShowActivity extends AppCompatActivity {

    private ImageView show_icon;
    private RecyclerView show_recycler;
    private ProgressBar progressBar;
    private NestedScrollView show_nest;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        ImmersionBar.with(this).statusBarDarkFont(true, 0.2f).init();
        progressBar = findViewById(R.id.show_progress);
        show_icon = findViewById(R.id.show_icon);
        show_recycler = findViewById(R.id.show_recycler);
        show_nest = findViewById(R.id.show_nest);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        show_nest.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Observable<MyBean> query = Httputils.getInstanse().apiService.getQuery(name);
        query.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyBean myBean) {
                        List<MyBean.ResultBean> result = myBean.getResult();
                        if (result.size()>0){
                            toAdapter(result);
                        }else{
                            show_recycler.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ShowActivity.this,"请求的结果飞了",Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void toAdapter(List<MyBean.ResultBean> result) {
        final List<MyBean.ResultBean> list = new ArrayList<>();
        list.clear();
        list.addAll(result);
        MyAdapter myAdapter = new MyAdapter(R.layout.item_layout, list);
        LinearLayoutManager manager = new LinearLayoutManager(ShowActivity.this, LinearLayoutManager.VERTICAL, false);
        show_recycler.setLayoutManager(manager);
        show_recycler.setAdapter(myAdapter);
        show_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        show_nest.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String author = list.get(position).getAuthor();
                String pic = list.get(position).getPic();
                String url = list.get(position).getUrl();
                String title = list.get(position).getTitle();
                Intent toPlay = new Intent(ShowActivity.this, PlayActivity.class);
                toPlay.putExtra("author", author);
                toPlay.putExtra("pic", pic);
                toPlay.putExtra("title",title);
                toPlay.putExtra("url", url);
                startActivity(toPlay);
            }
        });
    }
}
