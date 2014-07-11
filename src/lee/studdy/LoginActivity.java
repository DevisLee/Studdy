package lee.studdy;

import lee.studdy.common.BaseActivity;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import lee.studdy.common.Constant;

import com.avos.avoscloud.AVAnonymousUtils;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

public class LoginActivity extends BaseActivity {

	private Context mContext;
	private RelativeLayout rl_user;
	private Button mLogin;
	private Button mNotLogin;
	private Button register;
	private EditText acountEditText;
	private EditText passwordEditText;

	private boolean clickableFlag = true;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setActivity(this);
		mContext = this;
		findView();
		init();
	}

	private void findView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		mLogin = (Button) findViewById(R.id.login);
		mNotLogin = (Button) findViewById(R.id.notlogin);
		register = (Button) findViewById(R.id.register);
		acountEditText = (EditText) findViewById(R.id.account);
		passwordEditText = (EditText) findViewById(R.id.password);
	}

	private void init() {
		sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
		String accountString = sharedPreferences.getString("account", "");
		if (accountString != null && !accountString.equals("")) {
			acountEditText.setText(accountString);
			passwordEditText.requestFocus();
		}
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.login_anim);
		anim.setFillAfter(true);
		rl_user.startAnimation(anim);

		mNotLogin.setOnClickListener(loginOnClickListener);
		mLogin.setOnClickListener(loginOnClickListener);
		register.setOnClickListener(registerOnClickListener);
	}

	private OnClickListener loginOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (clickableFlag) {// 按键屏蔽
				switch (v.getId()) {
				case R.id.login:
					if (acountEditText.getText().toString().length() <= 0) {
						showToast("请输入账户");
						return;
					}
					if (passwordEditText.getText().toString().length() <= 0) {
						showToast("请输入密码");
						return;
					}
					clickableFlag = false;
					showProgress("", "正在登录……");
					// 登录
					AVUser.logInInBackground(acountEditText.getText().toString(),
							passwordEditText.getText().toString(), new LogInCallback<AVUser>() {
								public void done(AVUser user, AVException e) {
									if (user != null) {
										// 登录成功
										showToast("登录成功");
										// 保存当前用户名
										if (sharedPreferences != null) {
											Editor editor = sharedPreferences.edit();
											editor.putString("account", acountEditText.getText().toString());
											editor.commit();
										}
										Constant.ACCOUNT = user.getString("username");
										Constant.NICKNAME = user.getString("nickname");
										// 进入主程序
										Intent intent = new Intent(mContext, MainActivity.class);
										startActivity(intent);
										cancelProgress();
										finish();
									} else {
										// 登录失败
										showToast("登录失败 " + e.getLocalizedMessage());
										cancelProgress();
									}
									clickableFlag = true;
								}
							});

					break;
				case R.id.notlogin:
					clickableFlag = false;
					showProgress("", "正在进入游客模式……");
					AVAnonymousUtils.logIn(new LogInCallback<AVUser>() {
						@Override
						public void done(AVUser user, AVException e) {
							Constant.ACCOUNT = "0";
							Constant.NICKNAME = "游客";
							Intent intent = new Intent(mContext, MainActivity.class);
							startActivity(intent);
							clickableFlag = true;
							cancelProgress();
							finish();
						}
					});

					break;
				default:
					break;
				}
			}
		}
	};

	private OnClickListener registerOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(mContext, RegisterActivity.class);
			startActivityForResult(intent, 0);

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0x11) {
			String accountString = data.getStringExtra("account");
			acountEditText.setText(accountString);
			passwordEditText.requestFocus();
			passwordEditText.setText("");
			// 保存当前用户名
			if (sharedPreferences != null) {
				Editor editor = sharedPreferences.edit();
				editor.putString("account", accountString);
				editor.commit();
			}

		}
	}

}