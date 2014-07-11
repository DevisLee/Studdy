package lee.studdy.bbs;

import lee.studdy.R;
import lee.studdy.common.BaseActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

public class CreateTopicActivity extends Activity {
	private Intent intent;
	private EditText title;
	private EditText question;
	private static final String TAG = "CreateTopicActivity";
	private OnClickListener createTopicClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (title.getText().toString().length() == 0) {
				Toast.makeText(CreateTopicActivity.this, "主题不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (question.getText().toString().length() == 0) {
				Toast.makeText(CreateTopicActivity.this, "问题不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
//			showProgress("", "正在创建问题……");
//			closeInputMethod();
			AVObject newTopic = new AVObject(Topic.CLASS);
			newTopic.put("userName", intent.getStringExtra("userName"));
			newTopic.put("Title", title.getText().toString());
			newTopic.put("Question", question.getText().toString());
			newTopic.saveInBackground(new SaveCallback() {
				public void done(AVException e) {
//					cancelProgress();
					if (e == null) {
						// 保存成功
						Toast.makeText(CreateTopicActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
						Intent result = new Intent(CreateTopicActivity.this, TopicListActivity.class);
						result.putExtra("success", true);
						setResult(RESULT_OK, result);
						finish();
					} else {
						Toast.makeText(CreateTopicActivity.this, "创建失败", Toast.LENGTH_SHORT).show();
						Log.i(TAG, "Send err is  " + e.toString());
					}
				}
			});

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.topic_create);
		setTitle("新建一个问题：");
		// setActivity(this);
		intent = getIntent();

		Button button = (Button) findViewById(R.id.createTopic);
		button.setOnClickListener(createTopicClickListener);
		title = (EditText) findViewById(R.id.title_detail);
		question = (EditText) findViewById(R.id.question_detail);
	}
}
