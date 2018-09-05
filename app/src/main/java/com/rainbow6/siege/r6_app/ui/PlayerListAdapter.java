package com.rainbow6.siege.r6_app.ui;

import android.content.Context;
import android.database.Observable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerNameItemView;
        private final ImageView playerRankImageItemView;

        private PlayerViewHolder(View itemView) {
            super(itemView);
            playerNameItemView = itemView.findViewById(R.id.playerNameText);
            playerRankImageItemView = itemView.findViewById(R.id.playerRankImage);
        }
    }

    private final LayoutInflater mInflater;
    private List<PlayerEntity> mPlayerEntities; // Cached copy of players
    private OnItemClicked onClick;
    private Context mContext;

    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public PlayerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, final int position) {
        if (mPlayerEntities != null) {
            PlayerEntity current = mPlayerEntities.get(position);
            holder.playerNameItemView.setText(current.getNameOnPlatform());
//            holder.playerRankImageItemView.setImageResource(getDrawable(mContext, "rank_" + current.getRank()));
        } else {
            // Covers the case of data not being ready yet.
            holder.playerNameItemView.setText("No PlayerEntity");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(position);
            }
        });
    }

    public static int getDrawable(Context context, String name)
    {
        return context.getResources().getIdentifier(name,
                "drawable", context.getPackageName());
    }

    public void setPlayers(List<PlayerEntity> playerEntities) {
        mPlayerEntities = playerEntities;
        notifyDataSetChanged();
    }

    public PlayerEntity getPlayer(int position) {
        return mPlayerEntities.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mPlayerEntities has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlayerEntities != null)
            return mPlayerEntities.size();
        else return 0;
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}