package com.Toffee.Wallet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Toffee.Wallet.R;
import com.Toffee.Wallet.Responsemodel.SupportResp;

import java.util.List;


public class SupportAdapter extends RecyclerView.Adapter {
    Context ctx;
    List<SupportResp> list;
    LayoutInflater inflater;
    RecyclerView.ViewHolder viewHolder;

    public SupportAdapter(Context c, List<SupportResp> dataItems) {
        this.inflater = LayoutInflater.from(c);
        this.list = dataItems;
        this.ctx = c;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder = null;
        switch (viewType) {
            case 0:
                View v = inflater.inflate(R.layout.item_support, parent, false);
                viewHolder = new MyViewHolder(v);
                break;
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder_parent, int position) {
        if (getItemViewType(position) == 0) {
            ((MyViewHolder) holder_parent).bindData(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvmsg, date,ticket,status;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvmsg = itemView.findViewById(R.id.tvmsg);
            date = itemView.findViewById(R.id.date);
            ticket = itemView.findViewById(R.id.ticket);
            status = itemView.findViewById(R.id.status);
        }

        public void bindData(int posi) {
            viewHolder.setIsRecyclable(false);
            final SupportResp resp = list.get(posi);
            tvTitle.setText(resp.getSubject());
            tvmsg.setText(resp.getMessage());
            date.setText(itemView.getContext().getString(R.string.create_date)+resp.getCreated_at());
            ticket.setText(itemView.getContext().getString(R.string.ticket_id)+resp.getTicketID());

            if(resp.getStatus()==1){
                status.setText(itemView.getContext().getString(R.string.on_progress));
            }else if(resp.getStatus()==2){
                status.setText(itemView.getContext().getString(R.string.closed));
                status.setTextColor(ctx.getResources().getColor(R.color.green));
            }
        }
    }
}
