package lee.studdy.bbs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lee.studdy.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class TopicAdapter extends BaseAdapter {

  Context mContext;
  List<Topic> todos;

  public TopicAdapter(Context context, List<Topic> todos) {
    mContext = context;
    this.todos = todos;
  }

  @Override
  public int getCount() {
    return todos != null ? todos.size() : 0;
  }

  @Override
  public Object getItem(int position) {
    if (todos != null)
      return todos.get(position);
    else
      return null;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @SuppressLint("InflateParams")
@Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(mContext).inflate(R.layout.topic_row, null);
    }
    TextView title = (TextView) convertView.findViewById(R.id.row_title);
    TextView question = (TextView) convertView.findViewById(R.id.row_Question);
    TextView user_time = (TextView) convertView.findViewById(R.id.row_user_time);
    Topic topic = todos.get(position);
    if (topic != null) {
    	title.setText("主题： " + topic.getString("Title"));
    	question.setText("问题： " +topic.getString("Question"));
		Date date = topic.getUpdatedAt();
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH");
		user_time.setText(topic.getString("userName") + " 于 " + dateformat.format(date) + "时 发布");
    }
    return convertView;
  }

}
