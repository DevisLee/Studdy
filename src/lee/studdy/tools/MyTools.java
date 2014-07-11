package lee.studdy.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import lee.studdy.animation.Rotate3dAnimation;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;

public class MyTools {

	// 切换视图的动画效果
	public static LayoutAnimationController getgridlayoutAnim() {
		LayoutAnimationController controller;
		Animation anim = new Rotate3dAnimation(90f, 0f, 0.5f, 0.5f, 0.5f, false);// 角度
		anim.setDuration(300);// 动画时间
		controller = new LayoutAnimationController(anim, 0.1f);
		controller.setOrder(LayoutAnimationController.ORDER_NORMAL);// 出现方式 次序
		return controller;
	}

	// vibrate
	public static void vibrate(Activity context, int time) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(time);
	}

	/**
	 * 将byte数组转换成InputStream
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static InputStream byteTOInputStream(byte[] in) throws Exception {

		ByteArrayInputStream is = new ByteArrayInputStream(in);
		return is;
	}

	/*
	 * 从Assets中读取图片
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
}
