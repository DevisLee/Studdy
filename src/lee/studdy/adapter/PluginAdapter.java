package lee.studdy.adapter;

import java.util.List;
import java.util.Map;

import lee.studdy.R;
import lee.studdy.common.Constant;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PluginAdapter extends BaseAdapter {

	private List<Map<String, Object>> pluginList;
	private Context mContext;
	private LayoutInflater mInflater;

	public PluginAdapter(Context context, List<Map<String, Object>> data) {
		pluginList = data;
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return (pluginList != null) ? pluginList.size() + 2 : 2;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("TTTT", "getView" + position);
		ListViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.plugin_listview_item, null);

			convertView.setLayoutParams(new ListView.LayoutParams(Constant.SCREEN_WIDTH * 2 / 5,
					Constant.SCREEN_WIDTH * 2 / 5));

			viewHolder = new ListViewHolder();
			viewHolder.imageViewIcon = (ImageView) convertView.findViewById(R.id.imageview);
			viewHolder.appName = (TextView) convertView.findViewById(R.id.textview);
			viewHolder.rootView = (RelativeLayout) convertView.findViewById(R.id.plugin_item_layout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ListViewHolder) convertView.getTag();
		}
		if (position == 0) {
			viewHolder.imageViewIcon.setImageResource(R.drawable.logo);
			viewHolder.appName.setText(Constant.NICKNAME);
			viewHolder.rootView.setBackgroundResource(R.drawable.button_click_with_stroke);
		} else if (position == 1) {
			viewHolder.imageViewIcon.setImageResource(R.drawable.topic);
			viewHolder.appName.setText("你问我答");
			viewHolder.rootView.setBackgroundResource(R.drawable.button_click_with_stroke);
		} else {
			int currentPosition = position - 2;
			if (pluginList.get(currentPosition).get("image") != null) {
				viewHolder.imageViewIcon.setImageBitmap((Bitmap) pluginList.get(currentPosition).get("image"));
			} else {
				viewHolder.imageViewIcon.setImageResource(R.drawable.logo);
			}
			viewHolder.appName.setText((String) pluginList.get(currentPosition).get("name"));
			viewHolder.rootView.setBackgroundResource(R.drawable.button_click);
		}
		return convertView;
	}

	private final class ListViewHolder {
		public ImageView imageViewIcon;
		public TextView appName;
		public RelativeLayout rootView;
	}

}
