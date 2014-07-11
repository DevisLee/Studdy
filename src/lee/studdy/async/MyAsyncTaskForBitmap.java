package lee.studdy.async;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lee.studdy.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public class MyAsyncTaskForBitmap extends AsyncTask<String, String, ArrayList<Bitmap>> {

	private List<Map<String, Object>> pluginList;
	private Handler handler;
	private Context context;

	public MyAsyncTaskForBitmap(Context context, List<Map<String, Object>> pluginList, Handler handler){
		super();
		this.pluginList = pluginList;
		this.handler = handler;
		this.context = context;
	}

	@Override
	protected ArrayList<Bitmap> doInBackground(String... params) {
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		Bitmap defaultBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		try {
			for(int i=0; i<pluginList.size(); i++){
				if (params[0] != null) {
					Log.i("TTT", "doInBackground " + params[0]);
					bitmaps.add(getImage(params[0]));// 根据每个URL分别下载网络图片存入bitmap数组中
				} else {
					bitmaps.add(defaultBitmap);
				}
			}
			
			return bitmaps;
		} catch (Exception e) {
			Log.i("TTT", "doInBackground error");
			return null;
		}
	}

	public Bitmap getImage(String path) throws Exception {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			// 访问URL指向的网络
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			// conn.setConnectTimeout(8000);
			conn.connect();
			// 把获取的流转换成BITMAP
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap; // 返回一个bitmap
	}

	@Override
	protected void onPostExecute(ArrayList<Bitmap> result) {
//		super.onPostExecute(result);
		Log.e("TTTT", "onPostExecute");
	}

}
