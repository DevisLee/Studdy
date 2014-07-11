package lee.studdy;

import lee.studdy.bbs.Topic;
import lee.studdy.entity.FeedBack;
import lee.studdy.entity.Plugin;
import lee.studdy.entity.User;

import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;

import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;

public class MyApplication extends Application {
	private FrameworkInstance frame = null;
	public FrameworkInstance getFrame() {
		return frame;
	}
	public void onCreate() {
		super.onCreate();

		// 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
		AVOSCloud.initialize(this, "ys5pu8f2dy6n80ctd6n69igm9266o43kpa9ovgcalb790gkv",
				"nqcgmyj54v4cpxxbhy61z18kvwus3h33b2c8nt4ybdswnl66");
		// 启用崩溃错误报告
		AVAnalytics.enableCrashReport(getApplicationContext(), true);
		// 注册子类
		AVObject.registerSubclass(Plugin.class);
		AVObject.registerSubclass(FeedBack.class);
		AVObject.registerSubclass(User.class);
		 // 注册子类
	    AVObject.registerSubclass(Topic.class);
	    
	    new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				try {
					// 初始化APKPLUG框架
					frame = FrameworkFactory.getInstance().start(null, getApplicationContext());
					// BundleContext context = frame.getSystemBundleContext();
					// 安装assets文件夹下的插件 该类替代了MyProperty.AutoStart()方法
					// ,1.6.7以上建议使用新方式
					// 启动云服务包括插件搜索 下载 更新功能
					// ApkplugCloudAgent.init(context);
				} catch (Exception e) {
					System.err.println("Could not create : " + e);
					e.printStackTrace();
					int nPid = android.os.Process.myPid();
					android.os.Process.killProcess(nPid);
				}
				
			}
		}).start();
		
	}

}
