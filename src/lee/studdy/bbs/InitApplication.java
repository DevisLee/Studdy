package lee.studdy.bbs;

import com.avos.avoscloud.*;

import android.app.Application;

public class InitApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    // 初始化应用 Id 和 应用 Key，您可以在应用设置菜单里找到这些信息
    AVOSCloud.initialize(this, "ys5pu8f2dy6n80ctd6n69igm9266o43kpa9ovgcalb790gkv",
        "nqcgmyj54v4cpxxbhy61z18kvwus3h33b2c8nt4ybdswnl66");
    // 启用崩溃错误报告
    AVAnalytics.enableCrashReport(getApplicationContext(), true);
    // 注册子类
    AVObject.registerSubclass(Topic.class);
  }

}
