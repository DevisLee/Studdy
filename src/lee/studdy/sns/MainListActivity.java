package lee.studdy.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import android.os.Bundle;
import lee.studdy.R;
import lee.studdy.common.BaseActivity;

public class MainListActivity extends BaseActivity {

	List<AVObject> topics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sns_main_list);
		setActivity(this);
		initView();
		initData();

	}

	private void initView() {
		
	}

	private void initData() {
		topics = new ArrayList<AVObject>();
		showProgress("请稍候", "正在拼命加载数据……");
		AVQuery<AVObject> query = AVQuery.getQuery("Topic");
		query.orderByDescending("updatedAt");
		query.limit(10);
		query.findInBackground(new FindCallback<AVObject>() {
			public void done(List<AVObject> objects, AVException e) {
				if (objects != null && e == null) {
					topics = objects;

				} else {
					showToast("获取数据失败~~~~~(>_<)~~~~~!");
				}
				cancelProgress();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
