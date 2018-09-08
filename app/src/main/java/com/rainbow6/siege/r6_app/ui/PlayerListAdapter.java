package com.rainbow6.siege.r6_app.ui;

import android.app.Application;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.db.entity.PlayerEntity;
import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.repository.SeasonRepository;
import com.rainbow6.siege.r6_app.repository.StatsRepository;

import java.util.List;

import static com.rainbow6.siege.r6_app.service.UbiService.REGION_EMEA;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.COUNT_1;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_0;
import static com.rainbow6.siege.r6_app.viewmodel.PlayerViewModel.SKIP_1;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private final TextView playerName;
        private final ImageView playerRank;
        private final TextView playerMmr;
        private final TextView playerKd;

        private PlayerViewHolder(View itemView) {
            super(itemView);
            playerName = itemView.findViewById(R.id.playerName);
            playerRank = itemView.findViewById(R.id.playerRank);
            playerMmr = itemView.findViewById(R.id.playerMmr);
            playerKd = itemView.findViewById(R.id.playerKd);
        }
    }

    private static final String FORMAT_PRECISION_KD = "%.3f";
    private final LayoutInflater mInflater;
    private List<PlayerEntity> mPlayerEntities; // Cached copy of players
    private OnItemClicked onClick;
    private Context mContext;
    private SeasonRepository seasonRepository;
    private StatsRepository statsRepository;

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

            // Bad practice ?
            seasonRepository = new SeasonRepository((Application) mContext.getApplicationContext());
            statsRepository = new StatsRepository((Application) mContext.getApplicationContext());

            SeasonEntity seasonEntity = seasonRepository.getLastSeasonEntityByProfileIdAndRegionId(current.getProfileId(),REGION_EMEA, SKIP_0, COUNT_1);
            SeasonEntity seasonEntityPrevious = seasonRepository.getLastSeasonEntityByProfileIdAndRegionId(current.getProfileId(),REGION_EMEA, SKIP_1, COUNT_1);
            StatsEntity statsEntity = statsRepository.getLastStatsEntityByProfileId(current.getProfileId());


            holder.playerName.setText(current.getNameOnPlatform());
            holder.playerRank.setImageResource(getDrawable(mContext, "rank_" + seasonEntity.getRank()));

            int mmr = (int) Math.floor(seasonEntity.getMmr());
            int diffMmr = 0;
            if(seasonEntityPrevious != null) {
                diffMmr = (int) Math.floor(seasonEntity.getMmr()) - (int) Math.floor(seasonEntityPrevious.getMmr());
            }

            holder.playerMmr.setText(String.valueOf(mmr + " (" + diffMmr +")"));
            holder.playerKd.setText(String.format(FORMAT_PRECISION_KD, statsEntity.getKillsRanked() / (double) statsEntity.getDeathRanked()));
        } else {
            // Covers the case of data not being ready yet.
            holder.playerName.setText("Nobody");
            holder.playerRank.setImageResource(getDrawable(mContext, "rank_0"));
            holder.playerMmr.setText("2500");
            holder.playerKd.setText("0");
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