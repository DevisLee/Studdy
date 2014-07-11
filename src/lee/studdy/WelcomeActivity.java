package lee.studdy;

import lee.studdy.common.BaseActivity;
import lee.studdy.common.Constant;
import lee.studdy.discrollview.DiscrollViewContent;
import lee.studdy.tools.MyTools;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class WelcomeActivity extends BaseActivity {
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity(this);
		// 首次登陆显示介绍界面
		// sharedPreferences = getSharedPreferences("data",
		// Context.MODE_PRIVATE);
		// if(sharedPreferences.getBoolean("isfirst", true)){
		// Editor editor = sharedPreferences.edit();
		// editor.putBoolean("isfirst", false);
		// editor.commit();
		setContentView(R.layout.activity_welcome);
		// }else{
		// startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
		// finish();
		// }

		// 计算屏幕分辨率比率
		WindowManager w = this.getWindowManager();
		Constant.SCREEN_HEIGHT = w.getDefaultDisplay().getHeight();
		Constant.SCREEN_WIDTH = w.getDefaultDisplay().getWidth();
		Log.e("TTTT", "SCREEN_HEIGHT = " + Constant.SCREEN_HEIGHT + "   SCREEN_WIDTH = " + Constant.SCREEN_WIDTH);

	}

	public void start(View view) {
		showToast("欢迎使用Studdy!");
		// vibrate
		MyTools.vibrate(this, 55);
		// play sound
		// MediaPlayer mp = MediaPlayer.create(this, R.raw.haliluya);// -1是为了去掉0
		// if (mp != null)
		// mp.start();
		// go go go
		startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
		finish();
	}

}
