package lee.studdy.bbs;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import lee.studdy.R;
import lee.studdy.common.BaseActivity;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.SaveCallback;

public class DetailCommentActivity extends BaseActivity {

	private LinearLayout mainLayout;
	private Dialog progressDialog;
	private Topic topic;
	private volatile List<AVObject> comments;
	private Intent intent;
	private EditText mEditText;
	private AVRelation<AVObject> relation;
	AVObject newComment;

	private OnClickListener sendClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (arg0.getId() == R.id.send) {
				if (topic != null) {
					if (mEditText.getText().toString().length() == 0) {
						Toast.makeText(DetailCommentActivity.this, "回复内容不能为空", Toast.LENGTH_SHORT).show();
						return;
					}
					showProgress("", "正在提交回复……");
					closeInputMethod();
					newComment = new AVObject("Comment");
					newComment.put("commentUser", intent.getStringExtra("userName"));
					newComment.put("Detail", mEditText.getText().toString());
					newComment.saveInBackground(new SaveCallback() {
						public void done(AVException e) {
							cancelProgress();
							if (e == null) {
								// 保存成功
								relation.add(newComment);
								topic.saveInBackground(new SaveCallback() {
									public void done(AVException e) {
										if (e == null) {
											// 保存成功
											LinearLayout commentLayout = (LinearLayout) LayoutInflater.from(
													DetailCommentActivity.this).inflate(R.layout.one_comment, null);
											mainLayout.addView(commentLayout);
											TextView commentUser = (TextView) commentLayout
													.findViewById(R.id.commentUser);
											commentUser.setText(newComment.getString("commentUser"));
											TextView commentDetail = (TextView) commentLayout
													.findViewById(R.id.commentDetail);
											commentDetail.setText(newComment.getString("Detail"));
											Toast.makeText(DetailCommentActivity.this, "回复成功", Toast.LENGTH_SHORT)
													.show();
											mEditText.setText("");
										} else {
											// 保存失败，输出错误信息
											Toast.makeText(DetailCommentActivity.this, "回复失败", Toast.LENGTH_SHORT)
													.show();
											Log.i("SEND", "err is  " + e.toString());
										}
									}
								});
							} else {
								// 保存失败，输出错误信息
								Toast.makeText(DetailCommentActivity.this, "回复失败", Toast.LENGTH_SHORT).show();
								Log.i("SEND", "err is  " + e.toString());
							}
						}
					});
				}
			}
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		@Override
		protected Void doInBackground(Void... params) {
			String objectId = intent.getStringExtra("objectId");
			AVQuery<AVObject> topicQuery = new AVQuery<AVObject>(Topic.CLASS);
			try {
				topic = (Topic) topicQuery.get(objectId);
			} catch (AVException e) {
				// e.getMessage()
			}
			relation = topic.getRelation("myComm");
			AVQuery<AVObject> commentQuery = relation.getQuery();
			commentQuery.orderByAscending("updatedAt");
			commentQuery.limit(10);
			try {
				comments = commentQuery.find();
				if (comments == null) {
					Log.i("yifan", "doInBackground todos is null");
					return null;
				}
			} catch (AVException exception) {
				Log.e("yifan", "Query todos failed.", exception);
				comments = Collections.emptyList();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			DetailCommentActivity.this.progressDialog = ProgressDialog.show(DetailCommentActivity.this, "",
					"Loading...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Void result) {
			DetailCommentActivity.this.progressDialog.dismiss();
			TextView title = (TextView) findViewById(R.id.title);
			title.setText(topic.getString("Title"));
			TextView question = (TextView) findViewById(R.id.question);
			question.setText(topic.getString("Question"));
			TextView user_time = (TextView) findViewById(R.id.user_time);
			Date date = topic.getUpdatedAt();
			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd HH");
			user_time.setText(topic.getString("userName") + " 于 " + dateformat.format(date) + "时 发布");
			if (comments != null && !comments.isEmpty()) {
				for (AVObject comment : comments) {
					LinearLayout commentLayout = (LinearLayout) LayoutInflater.from(DetailCommentActivity.this)
							.inflate(R.layout.one_comment, null);
					mainLayout.addView(commentLayout);
					TextView commentUser = (TextView) commentLayout.findViewById(R.id.commentUser);
					commentUser.setText(comment.getString("commentUser"));
					TextView commentDetail = (TextView) commentLayout.findViewById(R.id.commentDetail);
					commentDetail.setText(comment.getString("Detail"));
				}
			}
		}
	}
	
	

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.comment_detail);
//		setTitle(R.string.create_todo);
		setActivity(this);
		intent = getIntent();
		Button button = (Button) findViewById(R.id.send);
		button.setOnClickListener(sendClickListener);
		mEditText = (EditText) findViewById(R.id.commentToSent);
		mainLayout = (LinearLayout) DetailCommentActivity.this.findViewById(R.id.comment);
		new RemoteDataTask().execute();
	}
}
