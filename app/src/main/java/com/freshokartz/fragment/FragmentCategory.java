package com.freshokartz.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.freshokartz.ActivityCategoryDetails;
import com.freshokartz.ActivityMain;
import com.freshokartz.CatApi;
import com.freshokartz.Category.PostList;
import com.freshokartz.Category.Result;
import com.freshokartz.R;
import com.freshokartz.adapter.AdapterCategory;
import com.freshokartz.adapter.CatAdapter;
import com.freshokartz.connection.API;
import com.freshokartz.connection.RestAdapter;
import com.freshokartz.connection.callbacks.CallbackCategory;
import com.freshokartz.model.Category;
import com.freshokartz.utils.NetworkCheck;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCategory extends Fragment {

    private View root_view;
    private RecyclerView recyclerView;
    private Call<CallbackCategory> callbackCall;
    private AdapterCategory adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.fragment_category, null);
        // initComponent();
        recyclerView = root_view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //requestListCategory();
        getCatData();
        return root_view;
    }

    private void initComponent() {
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //set data and list adapter
        adapter = new AdapterCategory(getActivity(), new ArrayList<Category>());
        recyclerView.setAdapter(adapter);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setVisibility(View.GONE);

        adapter.setOnItemClickListener(new AdapterCategory.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Category obj) {
                Snackbar.make(root_view, obj.name, Snackbar.LENGTH_SHORT).show();
                ActivityCategoryDetails.navigate(getActivity(), obj);
            }
        });
    }


    private void requestListCategory() {
        API api = RestAdapter.createAPI();
        callbackCall = api.getListCategory();
        callbackCall.enqueue(new Callback<CallbackCategory>() {
            @Override
            public void onResponse(Call<CallbackCategory> call, Response<CallbackCategory> response) {
                CallbackCategory resp = response.body();
                if (resp != null && resp.status.equals("success")) {
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setItems(resp.categories);
                    ActivityMain.getInstance().category_load = true;
                    ActivityMain.getInstance().showDataLoaded();
                } else {
                    onFailRequest();
                }
            }

            @Override
            public void onFailure(Call<CallbackCategory> call, Throwable t) {
                Log.e("onFailure", t.getMessage());
                if (!call.isCanceled()) onFailRequest();
            }

        });
    }

    private void onFailRequest() {
        if (NetworkCheck.isConnect(getActivity())) {
            showFailedView(R.string.msg_failed_load_data);
        } else {
            showFailedView(R.string.no_internet_text);
        }
    }

    private void showFailedView(@StringRes int message) {
        ActivityMain.getInstance().showDialogFailed(message);
    }

    private void getCatData() {

        Call<PostList> result = CatApi.getService().getPostList();
        result.enqueue(new Callback<PostList>() {
            List<Result> newMain ;

            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList ujju = response.body();
                for (int i = 0; i < ujju.getCount(); i++) {
                    if (ujju.getResults().get(i).getCategoryTreeId() > 4) {
                        //  main.add(ujju.getResults().get(i));
                    }
                }
                Log.i("retr", ujju.toString());
                List<Result> main=ujju.getResults();
                Log.i("retr", "sd");
                for (int i = 0; i < 5; i++) {
                    if (main.get(i).getCategoryTreeId() < 4) {
                        main.remove(main.get(i));
                    }
                        Log.i("humble", main.get(i).getName().toString());
                }

                recyclerView.setAdapter(new CatAdapter(getContext(),main));
//                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "FAILED", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
