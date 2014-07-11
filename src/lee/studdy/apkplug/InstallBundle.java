package lee.studdy.apkplug;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apkplug.Bundle.BundleControl;
import org.apkplug.Bundle.installCallback;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import android.content.Context;
import android.util.Log;
public class InstallBundle {
	//下载网络插件
	public void installOnlinePlugin(Context context,BundleContext mBundleContext,String fileName, InputStream inputStream, installCallback callback){
		File f1=null;
	        try {
//				InputStream in=context.getAssets().open("BundleDemoOSGIService2.apk");
				f1=new File(context.getFilesDir(),fileName+".apk");
//				if(!f1.exists()){
//					Log.e("TTTT", "f1.exists");
					copy(inputStream, f1);
					// startlevel设置为2插件不会自启 isCheckVersion不检测插件版本覆盖更新
					this.install(mBundleContext,"file:"+f1.getAbsolutePath(),callback,2,false);
//				}else{
//					
//				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	}
	
	
	private void copy(InputStream is, File outputFile)
	        throws IOException
	    {
	        OutputStream os = null;

	        try
	        {
	            os = new BufferedOutputStream(
	                new FileOutputStream(outputFile),4096);
	            byte[] b = new byte[4096];
	            int len = 0;
	            while ((len = is.read(b)) != -1)
	                os.write(b, 0, len);
	        }
	        finally
	        {
	            if (is != null) is.close();
	            if (os != null) os.close();
	        }
	    }
	/**
	  * 安装本地插件服务调用
	  * 详细接口参见 http://www.apkplug.com/javadoc/bundledoc1.5.3/
	  * org.apkplug.Bundle 
     *      接口 BundleControl
	  * @param path
	  * @param callback   安装插件的回掉函数
	  * @throws Exception
	  */
	 private void install(BundleContext mcontext,String path,installCallback callback,int startlevel,boolean isCheckVersion) throws Exception{
		 System.out.println("安装 :"+path);
			ServiceReference reference=mcontext.getServiceReference(BundleControl.class.getName());
	    	if(null!=reference){
	    		BundleControl service=(BundleControl) mcontext.getService(reference);
	    		if(service!=null){
	    			//插件启动级别为1(会自启) 并且不检查插件版本是否相同都安装
	    			service.install(mcontext, path,callback);//, startlevel,isCheckVersion);
	    		}
	    		mcontext.ungetService(reference);
	    	}
	}
}
