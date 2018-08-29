package com.rainbow6.siege.r6_app.Views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.Entities.Player;
import com.rainbow6.siege.r6_app.R;

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
    private List<Player> mPlayers; // Cached copy of players

    public PlayerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        if (mPlayers != null) {
            Player current = mPlayers.get(position);
            holder.playerNameItemView.setText(current.getNameOnPlatform());
//            holder.playerRankImageItemView.set
        } else {
            // Covers the case of data not being ready yet.
            holder.playerNameItemView.setText("No Player");
        }
    }

    public void setPlayers(List<Player> players) {
        mPlayers = players;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mPlayers has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mPlayers != null)
            return mPlayers.size();
        else return 0;
    }
}