package lee.studdy.adapter;

import java.util.List;
import java.util.Map;

import lee.studdy.R;
import lee.studdy.async.MyAsyncTask;
import lee.studdy.common.Constant;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OnlinePluginListAdapter extends BaseAdapter {

	private List<Map<String, Object>> pluginList;
	private Context mContext;
	private LayoutInflater mInflater;

	public OnlinePluginListAdapter(Context context, List<Map<String, Object>> data) {
		pluginList = data;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return (pluginList != null) ? pluginList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("TTTT", "getView" + position);
		GridViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.get_plugin_ol_gridview_item, null);

			convertView.setLayoutParams(new ListView.LayoutParams(Constant.SCREEN_WIDTH * 1 / 4,
					Constant.SCREEN_HEIGHT * 1 / 5));

			viewHolder = new GridViewHolder();
			viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.icon);
			viewHolder.isNewImageView = (ImageView) convertView.findViewById(R.id.isnew);
			viewHolder.appName = (TextView) convertView.findViewById(R.id.title);
			viewHolder.appContent = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (GridViewHolder) convertView.getTag();
		}
		// AVFile imageFile = (AVFile)
		pluginList.get(position).get("imageUrl");
		String imageFile = (String) pluginList.get(position).get("imageUrl");
		if (imageFile != null) {
			new MyAsyncTask(viewHolder.imageViewIcon).execute(imageFile);

		} else {
			viewHolder.imageViewIcon.setImageResource(R.drawable.studdy_icon);
		}

		if ((Boolean) pluginList.get(position).get("isNew")) {
			viewHolder.isNewImageView.setVisibility(View.VISIBLE);
		} else {
			viewHolder.isNewImageView.setVisibility(View.INVISIBLE);
		}

		viewHolder.appName.setText((String) pluginList.get(position).get("name"));
		viewHolder.appContent.setText((String) pluginList.get(position).get("fileSize"));

		// convertView = mInflater.inflate(R.layout.get_plugin_ol_gridview_item,
		// null);
		//
		// convertView.setLayoutParams(new
		// ListView.LayoutParams(Constant.SCREEN_WIDTH * 1 / 4,
		// Constant.SCREEN_HEIGHT * 1 / 5));
		//
		// ImageView imageView = (ImageView)
		// convertView.findViewById(R.id.icon);
		// TextView name = (TextView) convertView.findViewById(R.id.title);
		// TextView size = (TextView) convertView.findViewById(R.id.content);
		//
		// String imageFile = (String) pluginList.get(position).get("imageUrl");
		// if (imageFile != null) {
		// new MyAsyncTask(imageView).execute(imageFile);
		// } else {
		// imageView.setImageResource(R.drawable.studdy_icon);
		// }
		//
		// name.setText((String) pluginList.get(position).get("name"));
		// size.setText((String) pluginList.get(position).get("fileSize"));

		return convertView;
	}

	private final class GridViewHolder {
		public ImageView imageViewIcon;
		public ImageView isNewImageView;
		public TextView appName;
		public TextView appContent;
	}

}
