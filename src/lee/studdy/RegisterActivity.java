package lee.studdy;

import lee.studdy.common.BaseActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

public class RegisterActivity extends BaseActivity {
	private Context mContext;
	private Button btn_complete;
	private EditText accountEditText;
	private EditText passwordEditText;
	private EditText passwordAgainEditText;
	private EditText nicknameEditText;
	
	private boolean clickableFlag = true;
	
//	private TitleBarView mTitleBarView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_userinfo);
		setActivity(this);
		mContext=this;
		findView();
		initTitleView();
		init();
	}
	
	private void findView(){
//		mTitleBarView=(TitleBarView) findViewById(R.id.title_bar);
		btn_complete=(Button) findViewById(R.id.register_complete);
		accountEditText = (EditText) findViewById(R.id.account);
		passwordEditText = (EditText) findViewById(R.id.password);
		passwordAgainEditText = (EditText) findViewById(R.id.passwordAgain);
		nicknameEditText = (EditText) findViewById(R.id.name);
	}
	
	private void init(){
		btn_complete.setOnClickListener(completeOnClickListener);
	}
	
	private void initTitleView(){
//		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE,View.GONE, View.GONE);
//		mTitleBarView.setTitleText(R.string.title_register_info);
//		mTitleBarView.setBtnLeft(R.drawable.boss_unipay_icon_back, R.string.back);
//		mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {	
//			@Override
//			public void onClick(View v) {
//				finish();
//				
//			}
//		});
	}
	
	private OnClickListener completeOnClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(clickableFlag){
				if(accountEditText.getText().toString().length() <= 0){
					showToast("请输入账户");
					return;
				}
				if(passwordEditText.getText().toString().length() <= 0){
					showToast("请输入密码");
					return;
				}
				if(!passwordAgainEditText.getText().toString().equals(passwordEditText.getText().toString())){
					showToast("两次输入的密码不匹配，请重新输入");
					passwordAgainEditText.setText("");
					passwordEditText.setText("");
					return;
				}
				if(nicknameEditText.getText().toString().length() <= 0){
					showToast("请输入昵称");
					return;
				}
				showProgress("", "正在注册新用户……");
				clickableFlag = false;
				registNewUser(accountEditText.getText().toString(), passwordEditText.getText().toString(), nicknameEditText.getText().toString());
			}
		}
	};
	
	private void registNewUser(String username, String password, String nickname){
		AVUser user = new AVUser();
		user.setUsername(username);
		user.setPassword(password);
//		user.setEmail(email);
		// 其他属性可以像其他AVObject对象一样使用put方法添加
//		user.put("phone", "12345678");
		user.put("nickname", nickname);

		user.signUpInBackground(new SignUpCallback() {
		    public void done(AVException e) {
		    	cancelProgress();
		        if (e == null) {
		            // successfully
		        	showToast("注册成功");
		        	Intent intent = new Intent();
		        	intent.putExtra("account", accountEditText.getText().toString());
		        	setResult(0x11, intent);
		        	finish();
		        } else {
		            // failed
		        	showToast("注册失败 "+e.getLocalizedMessage() );
		        }
		        clickableFlag = true;
		    }
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	

}
