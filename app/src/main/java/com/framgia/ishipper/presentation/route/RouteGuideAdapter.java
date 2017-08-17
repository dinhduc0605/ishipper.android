package com.framgia.ishipper.presentation.route;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.net.data.ListRouteData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dinhduc on 11/11/2016.
 */

public class RouteGuideAdapter extends RecyclerView.Adapter<RouteGuideAdapter.PathGuideViewHolder> {
    private static final String REMOVE_BLANK_LINE_RG = "(?m)^[ \t]*\r?\n";
    private Context mContext;
    private List<ListRouteData.Step> mSteps;

    public RouteGuideAdapter(Context context, @NonNull ArrayList<ListRouteData.Step> steps) {
        mContext = context;
        mSteps = steps;
    }

    @Override
    public PathGuideViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_detail_path_guide, parent, false);
        return new PathGuideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PathGuideViewHolder holder, int position) {
        holder.bindData(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class PathGuideViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_path_guide_distance)
        TextView mTvDistance;
        @BindView(R.id.tv_path_guide_content)
        TextView mTvContent;
        @BindView(R.id.tv_path_guide_unit)
        TextView mTvUnit;

        public PathGuideViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(ListRouteData.Step step) {
            // distance format is: 0.5 km
            String distanceText = step.getDistance().getText();
            // get distance: 0.5
            String distanceNumber = distanceText.split(" ")[0];
            // get unit: km
            String unit = distanceText.split(" ")[1];
            mTvDistance.setText(distanceNumber);
            mTvUnit.setText(unit);
            mTvContent.setText(Html.fromHtml(step.getHtmlInstructions())
                    .toString().replaceAll(REMOVE_BLANK_LINE_RG, "")
            );
        }
    }

}
