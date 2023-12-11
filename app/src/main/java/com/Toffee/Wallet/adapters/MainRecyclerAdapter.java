package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.AppsResp;
import com.Toffee.Wallet.Responsemodel.ContestResp;
import com.Toffee.Wallet.Responsemodel.GameResp;
import com.Toffee.Wallet.Responsemodel.HomeModal;
import com.Toffee.Wallet.Responsemodel.HomeResp;
import com.Toffee.Wallet.activities.Games;
import com.Toffee.Wallet.restApi.ApiClient;
import com.Toffee.Wallet.restApi.ApiInterface;
import com.Toffee.Wallet.utils.Const;
import com.Toffee.Wallet.utils.Pref;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder> {

    private Context context;
    private List<HomeModal> allCategoryList;
    public static AppsAdapter appadapter;
    public static List<AppsResp> appsResps=new ArrayList<>();
    public MainRecyclerAdapter(Context context, List<HomeModal> allCategoryList) {
        this.context = context;
        this.allCategoryList = allCategoryList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(context).inflate(R.layout.main_recycler_row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {

        holder.categoryTitle.setText(allCategoryList.get(position).getCategory().get(position).getTitle());

        if (allCategoryList.get(position).getCategory().get(position).getCat_type().equalsIgnoreCase("game")) {
            Const.TOOLBAR_TITLE=allCategoryList.get(position).getCategory().get(position).getTitle();
            holder.seeAll.setVisibility(View.VISIBLE);
            holder.seeAll.setOnClickListener(v -> {
                context.startActivity(new Intent(context, Games.class));
            });
        }

        setCatItemRecycler(holder.itemRecycler, position, allCategoryList.get(position).getCategory(),
                allCategoryList.get(position).getHomeItemList());

    }

    @Override
    public int getItemCount() {
        return allCategoryList.size();
    }

    public static final class MainViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle, seeAll;
        RecyclerView itemRecycler;
        RelativeLayout layout;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.cat_title);
            itemRecycler = itemView.findViewById(R.id.item_recycler);
            seeAll = itemView.findViewById(R.id.seeAll);
            layout = itemView.findViewById(R.id.layout);

        }
    }

    private void setCatItemRecycler(RecyclerView recyclerView, int posi, List<HomeResp> category, List<HomeResp> categoryItemList) {
        HomeResp cat = category.get(posi);

        if (cat.getCat_type() != null && cat.getCat_type().equalsIgnoreCase("game")) {
            loadPlayzone(recyclerView);
        }
        else if (cat.getCat_type() != null && cat.getCat_type().equalsIgnoreCase("game_task")) {
            loadGameTask(recyclerView);
        }
        else if (cat.getCat_type() != null && cat.getCat_type().equalsIgnoreCase("cpi")) {
            loadCpi(recyclerView);
        }
        else if (cat.getCat_type() != null && cat.getCat_type().equalsIgnoreCase("contest")) {
           loadContest(recyclerView);
        }
        else {
           try {
               HomeAdapter itemRecyclerAdapter = new HomeAdapter(context, cat.getCat_theme(), categoryItemList,cat.getView_mode() );
               if (cat.getView_mode() == 0) {
                   recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
               } else if (cat.getView_mode() == 1) {
                   recyclerView.setLayoutManager(new LinearLayoutManager(context));
               } else {
                   recyclerView.setLayoutManager(new GridLayoutManager(context, cat.getView_mode()));
               }
               recyclerView.setAdapter(itemRecyclerAdapter);
           }catch (Exception e){}
        }

    }

    private void loadPlayzone(RecyclerView recyclerView) {
        try {
            ApiClient.restAdapter(context).create(ApiInterface.class).funGame("0", "8").enqueue(new Callback<GameResp>() {
                @Override
                public void onResponse(Call<GameResp> call, Response<GameResp> response) {
                    if (response.isSuccessful() && response.body().getData().size() != 0) {
                        GameAdapter adapter = new GameAdapter(context, response.body().getData(), true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<GameResp> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }

    private void loadContest(RecyclerView recyclerView) {
        try {
            Objects.requireNonNull(ApiClient.restAdapter(context)).create(ApiInterface.class).getContest(Pref.User_id(context),4).enqueue(new Callback<ContestResp>() {
                @Override
                public void onResponse(Call<ContestResp> call, Response<ContestResp> response) {
                    if (response.isSuccessful() && response.body().getCode() == 201) {
                        ContestAdapter adapter = new ContestAdapter(context, response.body().getData());
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(adapter);
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<ContestResp> call, Throwable t) {
                }
            });
        } catch (Exception ignored) {}
    }

    private void loadGameTask(RecyclerView recyclerView) {
        try {
            ApiClient.restAdapter(context).create(ApiInterface.class).funGame("1", "8").enqueue(new Callback<GameResp>() {
                @Override
                public void onResponse(Call<GameResp> call, Response<GameResp> response) {
                    if (response.isSuccessful() && response.body().getData().size() != 0) {
                        GameAdapter adapter = new GameAdapter(context, response.body().getData(), false);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<GameResp> call, Throwable t) {

                }
            });
        } catch (Exception e) {
        }
    }

    private void loadCpi(RecyclerView recyclerView) {
        try {
            Call<List<AppsResp>> call = Objects.requireNonNull(ApiClient.restAdapter(context)).create(ApiInterface.class).cpaoffers();
            call.enqueue(new Callback<List<AppsResp>>() {
                @Override
                public void onResponse(Call<List<AppsResp>> call, Response<List<AppsResp>> response) {
                    if (response.isSuccessful() && response.body().size() != 0) {
                        appsResps.clear();
                        appsResps.addAll(response.body());
                        appadapter = new AppsAdapter(context,appsResps);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        recyclerView.setAdapter(appadapter);
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<List<AppsResp>> call, Throwable t) {
                }
            });
        } catch (Exception e) {
        }
    }


}
