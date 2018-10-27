package com.rainbow6.siege.r6_app.ui;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.db.entity.StatsEntity;
import com.rainbow6.siege.r6_app.repository.SeasonRepository;
import com.rainbow6.siege.r6_app.repository.StatsRepository;
import com.rainbow6.siege.r6_app.ui.TabSeasons.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SeasonHistoryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<SeasonEntity> mValues;
    private final OnListFragmentInteractionListener mListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public SeasonHistoryRecyclerViewAdapter(List<SeasonEntity> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_history_item, parent, false);

        if (viewType == TYPE_HEADER) {
            return new ViewHolderHeader(view);
        } else {
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder holder2 = (ViewHolder)holder;

            holder2.seasonEntity = mValues.get(position - 1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

            if(holder2.seasonEntity.getUpdateDate() != null){
                holder2.seasonHistoryDate.setText(sdf.format(holder2.seasonEntity.getUpdateDate()));
            }else{
                holder2.seasonHistoryDate.setText(R.string.LabelNoStats);
            }
            holder2.seasonHistoryRegion.setText(holder2.seasonEntity.getRegion());
            holder2.seasonHistorySeason.setText(String.valueOf(holder2.seasonEntity.getSeason()));
            holder2.seasonHistoryMmr.setText(String.valueOf((int) Math.floor(holder2.seasonEntity.getMmr())));

            SeasonRepository seasonRepository = new SeasonRepository((Application) holder2.mView.getContext().getApplicationContext());

            SeasonEntity previousSeasonEntity = seasonRepository.getSeasonEntityByProfileIdAndRegionIdAndSeasonAndLessThanDate(
                    holder2.seasonEntity.getProfileId(), holder2.seasonEntity.getRegion(), holder2.seasonEntity.getSeason(), holder2.seasonEntity.getUpdateDate());

            if (previousSeasonEntity != null) {
                holder2.seasonHistoryWonLost.setText(String.valueOf(holder2.seasonEntity.getWins() - previousSeasonEntity.getWins()) +
                        "/" + String.valueOf(holder2.seasonEntity.getLosses() - previousSeasonEntity.getLosses())
                        + "(" + String.valueOf(holder2.seasonEntity.getAbandons() - previousSeasonEntity.getAbandons()) + ")");
            }else{
                holder2.seasonHistoryWonLost.setText(String.valueOf(holder2.seasonEntity.getWins()) + "/" + String.valueOf(holder2.seasonEntity.getLosses()));
            }

            StatsRepository statsRepository = new StatsRepository((Application) holder2.mView.getContext().getApplicationContext());

            StatsEntity statsEntityGreaterThanSeasonDate = statsRepository.getStatsEntityByProfileIdAndGreaterThanDate(holder2.seasonEntity.getProfileId(), holder2.seasonEntity.getUpdateDate());
            StatsEntity statsEntityLessThanSeasonDate = statsRepository.getStatsEntityByProfileIdAndLessThanDate(holder2.seasonEntity.getProfileId(), holder2.seasonEntity.getUpdateDate());

            String diffKd = "0/0";
            if(statsEntityGreaterThanSeasonDate != null && statsEntityLessThanSeasonDate != null){
                int diffK = statsEntityGreaterThanSeasonDate.getKillsRanked() - statsEntityLessThanSeasonDate.getKillsRanked();
                int diffD = statsEntityGreaterThanSeasonDate.getDeathRanked() - statsEntityLessThanSeasonDate.getDeathRanked();
                diffKd = diffK + "/" + diffD;
            }

            holder2.seasonHistoryKd.setText(diffKd);

            holder2.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(holder2.seasonEntity);
                    }
                }
            });

        } else if (holder instanceof ViewHolderHeader) {

            Typeface bold = Typeface.create("san-serif", Typeface.BOLD);

            final ViewHolderHeader holder2 = (ViewHolderHeader)holder;
            holder2.seasonHistoryDate.setText(R.string.LabelDate);
            holder2.seasonHistoryRegion.setText(R.string.LabelRegion);
            holder2.seasonHistorySeason.setText(R.string.LabelSeason);
            holder2.seasonHistoryMmr.setText(R.string.LabelMmr);
            holder2.seasonHistoryWonLost.setText(R.string.LabelWL);
            holder2.seasonHistoryKd.setText(R.string.LabelKd);

            holder2.seasonHistoryDate.setTypeface(bold);
            holder2.seasonHistoryRegion.setTypeface(bold);
            holder2.seasonHistorySeason.setTypeface(bold);
            holder2.seasonHistoryMmr.setTypeface(bold);
            holder2.seasonHistoryWonLost.setTypeface(bold);
            holder2.seasonHistoryKd.setTypeface(bold);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView seasonHistoryDate;
        public final TextView seasonHistoryRegion;
        public final TextView seasonHistorySeason;
        public final TextView seasonHistoryMmr;
        public final TextView seasonHistoryWonLost;
        public final TextView seasonHistoryKd;
        public SeasonEntity seasonEntity;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            seasonHistoryDate = (TextView) mView.findViewById(R.id.seasonHistoryDate);
            seasonHistoryRegion = (TextView) mView.findViewById(R.id.seasonHistoryRegion);
            seasonHistorySeason = (TextView) mView.findViewById(R.id.seasonHistorySeason);
            seasonHistoryMmr = (TextView) mView.findViewById(R.id.seasonHistoryMmr);
            seasonHistoryWonLost = (TextView) mView.findViewById(R.id.seasonHistoryWonLost);
            seasonHistoryKd = (TextView) mView.findViewById(R.id.seasonHistoryKd);
        }

        /*@Override
        public String toString() {
            return super.toString() + " '" + seasonHistoryDate.getText() + "'";
        }*/
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView seasonHistoryDate;
        public final TextView seasonHistoryRegion;
        public final TextView seasonHistorySeason;
        public final TextView seasonHistoryMmr;
        public final TextView seasonHistoryWonLost;
        public final TextView seasonHistoryKd;

        public ViewHolderHeader(View view) {
            super(view);
            mView = view;

            seasonHistoryDate = (TextView) mView.findViewById(R.id.seasonHistoryDate);
            seasonHistoryRegion = (TextView) mView.findViewById(R.id.seasonHistoryRegion);
            seasonHistorySeason = (TextView) mView.findViewById(R.id.seasonHistorySeason);
            seasonHistoryMmr = (TextView) mView.findViewById(R.id.seasonHistoryMmr);
            seasonHistoryWonLost = (TextView) mView.findViewById(R.id.seasonHistoryWonLost);
            seasonHistoryKd = (TextView) mView.findViewById(R.id.seasonHistoryKd);
        }
    }
}
