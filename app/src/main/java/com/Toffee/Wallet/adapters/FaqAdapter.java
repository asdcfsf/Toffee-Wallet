package com.Toffee.Wallet.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.FaqResp;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<FaqResp> faqModelList;
    Activity ctx;

    public FaqAdapter(Context ctx, List<FaqResp> faqModelList) {
        this.inflater = LayoutInflater.from(ctx);
        this.faqModelList = faqModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_faq, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.faq.setText(faqModelList.get(position).getQuestion());
        holder.answer.setText("\uD83D\uDC49\uD83C\uDFFB "+faqModelList.get(position).getAnswer());
            }

    @Override
    public int getItemCount() {
        return faqModelList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView faq, answer;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            faq = itemView.findViewById(R.id.faq);
            answer = itemView.findViewById(R.id.answer);
        }
    }
}




