package lee.studdy.common;

import lee.studdy.R;
import lee.studdy.R.id;
import lee.studdy.R.layout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends Activity {

	private ProgressDialog myProgressDialog = null;
	private Activity myActivity = null;
	private static Toast toast = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// 设置无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	// 获取上下文对象
	public void setActivity(Activity _activity) {
		this.myActivity = _activity;
	}

	/******
	 * @MethodName: showExitAlertDialog
	 * @Description: 退出应用 提示框
	 * @Author: Libin
	 * @CreateDate: 2014-1-13
	 ******/
	public void showExitAlertDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(myActivity).setIcon(R.drawable.nonopanda_sit)
				.setTitle("亲！别离开我~").setPositiveButton("残忍退出", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
						System.exit(0);
					}
				}).setNegativeButton("再玩会儿", new AlertDialog.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						showCartoonToast("╭(╯3╰)╮ 么么哒~", 0);
					}
				}).create();
		alertDialog.show();
	}

	/******
	 * @MethodName: closeInputMethod
	 * @Description: 关闭软键盘
	 * @Author: Libin
	 * @CreateDate: 2014-1-13
	 ******/
	public void closeInputMethod() {
		try {
			((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/******
	 * @MethodName: showProgress
	 * @Description: 显示进度条
	 * @Author: Libin
	 * @CreateDate: 2014-1-13
	 ******/
	public void showProgress(String title, String msg) {
		try {
			if (myProgressDialog != null) {
				myProgressDialog.cancel();
			}
			myProgressDialog = ProgressDialog.show(myActivity, title, msg);
			myProgressDialog.setCancelable(false);
		} catch (Exception e) {
		} finally {
		}
	}

	/******
	 * @MethodName: showProgress
	 * @Description: 显示进度条
	 * @Author: Libin
	 * @CreateDate: 2014-1-13
	 ******/
	public void showProgress(String title, int msgId) {
		try {
			if (myProgressDialog != null) {
				myProgressDialog.cancel();
			}
			myProgressDialog = ProgressDialog.show(myActivity, title, myActivity.getResources().getString(msgId));
			myProgressDialog.setCancelable(true);
		} catch (Exception e) {
		} finally {
		}
	}

	/******
	 * @MethodName: cancelProgress
	 * @Description: 取消进度条
	 * @Author: Libin
	 * @CreateDate: 2014-1-13
	 ******/
	public void cancelProgress() {
		try {
			if (myProgressDialog != null) {
				myProgressDialog.dismiss();
				myProgressDialog.cancel();
			}
		} catch (Exception e) {
		} finally {
		}
	}
	
	/******
	 * @MethodName: cancelToast
	 * @Description: 取消当前toast
	 * @Author: Libin
	 * @CreateDate: 2013-12-24
	 ******/
	public static void cancelToast() {
		if (toast != null) {
			toast.cancel();
			toast = null;
		}
	}

	/******
	 * @MethodName: showToast
	 * @Description: 显示系统默认toast
	 * @Author: Libin
	 * @CreateDate: 2013-12-24
	 ******/
	public void showToast(String string) {
		toast = Toast.makeText(myActivity, string, Toast.LENGTH_SHORT);//
		toast.show();
	}

	/******
	 * @MethodName: showToast
	 * @Description: 显示系统默认toast 可改时间
	 * @Author: Libin
	 * @CreateDate: 2013-12-24
	 ******/
	public void showToast( String string, int time) {
		toast = Toast.makeText(myActivity, string, time);
		toast.show();
	}

	/******
	 * @MethodName: showCartoonToast
	 * @Description: 显示自定义toast
	 * @Author: Libin
	 * @CreateDate: 2013-12-24
	 ******/
	public void showCartoonToast(String string, int yOffset) { // yOffset表示对话框相对Y偏移量
		// 自定义的Toast
		LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.toast, null);
		TextView text = (TextView) view.findViewById(R.id.tvTextToast);
		text.setText(string);
		if (toast == null) {
			toast = Toast.makeText(myActivity, "", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, yOffset); // 位置，向右，向下
		}
		toast.setView(view);
		toast.show();
	}
	
	@Override
	public void onBackPressed() {
		showExitAlertDialog();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		Constant.ACCOUNT = savedInstanceState.getString("account");
		Constant.NICKNAME = savedInstanceState.getString("nickname");
		Constant.SCREEN_HEIGHT = savedInstanceState.getInt("screenheight");
		Constant.SCREEN_WIDTH = savedInstanceState.getInt("screenwidth");
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("account", Constant.ACCOUNT);
		outState.putString("nickname", Constant.NICKNAME);
		outState.putInt("screenheight", Constant.SCREEN_HEIGHT);
		outState.putInt("screenwidth", Constant.SCREEN_WIDTH);
	}
	
	
	
}
