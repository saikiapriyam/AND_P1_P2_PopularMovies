package apps.orchotech.com.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import apps.orchotech.com.popularmovies.R;
import apps.orchotech.com.popularmovies.activities.DetailsActivity;
import apps.orchotech.com.popularmovies.network.AllMoviesBean;
import apps.orchotech.com.popularmovies.network.ImageLoader;
import apps.orchotech.com.popularmovies.network.PopularMoviesBean;
import apps.orchotech.com.popularmovies.network.ReviewsBean;
import apps.orchotech.com.popularmovies.utils.AppConstants;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by PriyamSaikia on 17-05-2016.
 */
public class GridLayoutAdapter extends RecyclerView.Adapter<GridLayoutAdapter.FavouritesListViewHolder> {
    private static final String TAG = GridLayoutAdapter.class.getSimpleName();
    Context mContext;
    ArrayList<?> mArrayList;
    Object mObj;

    public GridLayoutAdapter(Context context, ArrayList<?> arrayList, boolean isTwoPane) {
        mContext = context;
        mArrayList = arrayList;
        mObj = arrayList.get(0);
        //todo: check
        //selecting the first item at initialisation time.
        if (isTwoPane) {
            if (mObj instanceof AllMoviesBean)
                ((CallBack) mContext).onItemSelected(0, ((AllMoviesBean) mObj).getId());
        }
    }

    @Override
    public FavouritesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.staggered_item_trailer_list, null);
        Log.i(TAG,"onCreateFavViewHolder");
        return new FavouritesListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FavouritesListViewHolder holder, final int position) {
        if (mObj instanceof PopularMoviesBean) {
            Log.i(TAG,"instance on PopularMoviesBean");
            final PopularMoviesBean item = (PopularMoviesBean) (mArrayList.get(position));
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.loadImage(mContext, item.getPoster_path(), holder.imv_item_trailer);
            holder.tv_trailer_name.setText(item.getTitle());
            holder.ll_item_trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailsActivity.class);
                    intent.putExtra(AppConstants.MOVIE_ID, item.getId());
                    mContext.startActivity(intent);
                }
            });
        } else if (mObj instanceof ReviewsBean) {
            Log.i(TAG,"instance of ReviewsBean");
            ReviewsBean reviewsBean = (ReviewsBean) mArrayList.get(position);
            holder.tv_trailer_name.setText(reviewsBean.getContent());
            holder.imv_item_trailer.setVisibility(View.GONE);
        } else if (mObj instanceof AllMoviesBean) {
            Log.i(TAG,"instance of AllMoviesBean");
            final AllMoviesBean bean = (AllMoviesBean) mArrayList.get(position);
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.loadImage(mContext, bean.getPoster_link(), holder.imv_item_trailer);
            holder.tv_trailer_name.setText(bean.getName());
            holder.ll_item_trailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CallBack) mContext).onItemSelected(position, bean.getId());
                }
            });
        }else{
            Log.i(TAG,"instance of no bean");
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    public class FavouritesListViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_trailer_name)
        TextView tv_trailer_name;
        @Bind(R.id.imv_item_trailer)
        ImageView imv_item_trailer;
        @Bind(R.id.rl_item_trailer)
        RelativeLayout ll_item_trailer;

        public FavouritesListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CallBack {
        void onItemSelected(int position, String movieId);
    }
}