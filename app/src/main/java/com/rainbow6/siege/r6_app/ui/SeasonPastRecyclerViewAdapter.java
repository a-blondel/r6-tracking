package com.rainbow6.siege.r6_app.ui;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainbow6.siege.r6_app.R;
import com.rainbow6.siege.r6_app.db.entity.SeasonEntity;
import com.rainbow6.siege.r6_app.ui.TabSeasons.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.rainbow6.siege.r6_app.ui.PlayerListAdapter.getDrawable;

public class SeasonPastRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final List<SeasonEntity> mValues;
    private final OnListFragmentInteractionListener mListener;

    public SeasonPastRecyclerViewAdapter(List<SeasonEntity> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_past_item, parent, false);

        if (viewType == TYPE_HEADER) {
            return new ViewHolderHeader(view);
        } else {
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder holder2 = (ViewHolder) holder;

            holder2.seasonEntity = mValues.get(position - 1);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());

            if (holder2.seasonEntity.getUpdateDate() != null) {
                holder2.seasonPastDate.setText(sdf.format(holder2.seasonEntity.getUpdateDate()));
            } else {
                holder2.seasonPastDate.setText(R.string.LabelNoStats);
            }
            holder2.seasonPastRegion.setText(holder2.seasonEntity.getRegion());
            holder2.seasonPastSeason.setText(TabSettings.SEASONS_LIST[TabSettings.SEASONS_ID_LIST.indexOf(holder2.seasonEntity.getSeason())]);
            holder2.seasonPastMaxMmr.setText(String.valueOf((int) Math.floor(holder2.seasonEntity.getMaxMmr())));

            holder2.seasonPastWonLost.setText(holder2.seasonEntity.getWins() + "/" + holder2.seasonEntity.getLosses() + "(" + holder2.seasonEntity.getAbandons() + ")");

            // Context use is not pretty
            holder2.seasonPastMaxRank.setImageResource(getDrawable(holder2.seasonPastMaxRank.getContext(), "rank_" + (holder2.seasonEntity.getMaxRank())));

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

            final ViewHolderHeader holder2 = (ViewHolderHeader) holder;
            holder2.seasonPastDate.setText(R.string.LabelDate);
            holder2.seasonPastRegion.setText(R.string.LabelRegion);
            holder2.seasonPastSeason.setText(R.string.LabelSeason);
            holder2.seasonPastMaxMmr.setText(R.string.LabelMmr);
            holder2.seasonPastWonLost.setText(R.string.LabelWL);

            holder2.seasonPastMaxRank.setImageResource(getDrawable(holder2.seasonPastMaxRank.getContext(), "rank_0"));
            holder2.seasonPastMaxRank.setVisibility(View.INVISIBLE);

            holder2.seasonPastDate.setTypeface(bold);
            holder2.seasonPastRegion.setTypeface(bold);
            holder2.seasonPastSeason.setTypeface(bold);
            holder2.seasonPastMaxMmr.setTypeface(bold);
            holder2.seasonPastWonLost.setTypeface(bold);
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
        public final TextView seasonPastDate;
        public final TextView seasonPastRegion;
        public final TextView seasonPastSeason;
        public final TextView seasonPastMaxMmr;
        public final TextView seasonPastWonLost;
        public final ImageView seasonPastMaxRank;
        public SeasonEntity seasonEntity;

        public ViewHolder(View view) {
            super(view);
            mView = view;

            seasonPastDate = (TextView) mView.findViewById(R.id.seasonPastDate);
            seasonPastRegion = (TextView) mView.findViewById(R.id.seasonPastRegion);
            seasonPastSeason = (TextView) mView.findViewById(R.id.seasonPastSeason);
            seasonPastMaxMmr = (TextView) mView.findViewById(R.id.seasonPastMaxMmr);
            seasonPastWonLost = (TextView) mView.findViewById(R.id.seasonPastWonLost);
            seasonPastMaxRank = (ImageView) mView.findViewById(R.id.seasonPastMaxRank);
        }

        /*@Override
        public String toString() {
            return super.toString() + " '" + seasonPastDate.getText() + "'";
        }*/
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView seasonPastDate;
        public final TextView seasonPastRegion;
        public final TextView seasonPastSeason;
        public final TextView seasonPastMaxMmr;
        public final TextView seasonPastWonLost;
        public final ImageView seasonPastMaxRank;

        public ViewHolderHeader(View view) {
            super(view);
            mView = view;

            seasonPastDate = (TextView) mView.findViewById(R.id.seasonPastDate);
            seasonPastRegion = (TextView) mView.findViewById(R.id.seasonPastRegion);
            seasonPastSeason = (TextView) mView.findViewById(R.id.seasonPastSeason);
            seasonPastMaxMmr = (TextView) mView.findViewById(R.id.seasonPastMaxMmr);
            seasonPastWonLost = (TextView) mView.findViewById(R.id.seasonPastWonLost);
            seasonPastMaxRank = (ImageView) mView.findViewById(R.id.seasonPastMaxRank);
        }
    }
}
