package ir.mirrajabi.searchdialog.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.R;
import ir.mirrajabi.searchdialog.StringsHelper;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;


public class SearchDialogAdapter<T extends Searchable> extends RecyclerView.Adapter<SearchDialogAdapter.ViewHolder> {
	protected Context mContext;
	private List<T> mItems = new ArrayList<>();
	private LayoutInflater mLayoutInflater;
	private int mLayout;
	private SearchResultListener mSearchResultListener;
	private AdapterViewBinder<T> mViewBinder;
	private String mSearchTag;
	private boolean mHighlightPartsInCommon = true;
	
	private BaseSearchDialogCompat mSearchDialog;
	
	public SearchDialogAdapter(Context context, @LayoutRes int layout, List<T> items) {
		this(context, layout, null, items);
	}
	
	public SearchDialogAdapter(
		Context context, AdapterViewBinder<T> viewBinder,
		@LayoutRes int layout, List<T> items
	) {
		this(context, layout, viewBinder, items);
	}
	
	public SearchDialogAdapter(
		Context context, @LayoutRes int layout,
		@Nullable AdapterViewBinder<T> viewBinder,
		List<T> items
	) {
		this.mContext = context;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.mItems = items;
		this.mLayout = layout;
		this.mViewBinder = viewBinder;
	}
	
	public List<T> getItems() {
		return mItems;
	}
	
	public void setItems(List<T> objects) {
		this.mItems = objects;
		notifyDataSetChanged();
	}
	
	public T getItem(int position) {
		return mItems.get(position);
	}
	
	@Override
	public int getItemCount() {
		if (mItems != null) {
			return mItems.size();
		} else {
			return 0;
		}
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public SearchDialogAdapter<T> setViewBinder(AdapterViewBinder<T> viewBinder) {
		this.mViewBinder = viewBinder;
		return this;
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View convertView = mLayoutInflater.inflate(R.layout.search_item, parent, false);
		convertView.setTag(new ViewHolder(convertView));
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		return viewHolder;
	}
	
	@Override
	public void onBindViewHolder(SearchDialogAdapter.ViewHolder holder, int position) {
		initializeViews(getItem(position), holder, position);
	}
	
	private void initializeViews(
		final T object, final ViewHolder holder,
		final int position
	) {
		if (mViewBinder != null) {
			mViewBinder.bind(holder, object, position);
		}
		TextView text = holder.getViewById(R.id.title);
		TextView text2 = holder.getViewById(R.id.subtitle);
		View divider = holder.getViewById(R.id.divider);
		text.setTextColor(getColor(R.color.searchDialogResultColor));
		if (mSearchTag != null && mHighlightPartsInCommon) {
			text.setText(StringsHelper.highlightLCS(object.getTitle(), getSearchTag(),
				getColor(R.color.searchDialogResultHighlightColor)
			));
		} else {
			text.setText(object.getTitle());
		}

		if(object.getSubtitle().length() > 0){
			text2.setText(object.getSubtitle());
			text2.setVisibility(View.VISIBLE);
		}
		else{
			text2.setVisibility(View.GONE);
		}

		if(position == mItems.size()-1){
			divider.setVisibility(View.GONE);
		}
		else{
			divider.setVisibility(View.VISIBLE);
		}

		if (mSearchResultListener != null) {
			holder.getBaseView().setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mSearchResultListener.onSelected(mSearchDialog, object, position);
				}
			});
		}
	}
	
	public SearchResultListener getSearchResultListener() {
		return mSearchResultListener;
	}
	
	public void setSearchResultListener(SearchResultListener searchResultListener) {
		this.mSearchResultListener = searchResultListener;
	}
	
	public String getSearchTag() {
		return mSearchTag;
	}
	
	public SearchDialogAdapter setSearchTag(String searchTag) {
		mSearchTag = searchTag;
		return this;
	}
	
	public boolean isHighlightPartsInCommon() {
		return mHighlightPartsInCommon;
	}
	
	public SearchDialogAdapter setHighlightPartsInCommon(boolean highlightPartsInCommon) {
		mHighlightPartsInCommon = highlightPartsInCommon;
		return this;
	}
	
	public SearchDialogAdapter setSearchDialog(BaseSearchDialogCompat searchDialog) {
		mSearchDialog = searchDialog;
		return this;
	}
	
	@SuppressWarnings("deprecation")
	private int getColor(@ColorRes int colorResId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return mContext.getResources().getColor(colorResId, null);
		} else {
			return mContext.getResources().getColor(colorResId);
		}
	}
	
	public interface AdapterViewBinder<T> {
		void bind(ViewHolder holder, T item, int position);
	}
	
	
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private View mBaseView;
		
		public ViewHolder(View view) {
			super(view);
			mBaseView = view;
		}
		
		public View getBaseView() {
			return mBaseView;
		}
		
		public <T> T getViewById(@IdRes int id) {
			return (T) mBaseView.findViewById(id);
		}
		
		public void clearAnimation(@IdRes int id) {
			mBaseView.findViewById(id).clearAnimation();
		}
	}
}