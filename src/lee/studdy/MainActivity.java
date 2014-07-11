package lee.studdy;

import java.io.File;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lee.studdy.adapter.MyViewPagerAdapter;
import lee.studdy.adapter.OnlinePluginListAdapter;
import lee.studdy.adapter.PluginAdapter;
import lee.studdy.apkplug.FilebrowerDialog;
import lee.studdy.apkplug.InstallBundle;
import lee.studdy.apkplug.SharedPreferencesFactory;
import lee.studdy.apkplug.apkFilter;
import lee.studdy.apkplug.isFilesFilter;
import lee.studdy.async.MyAsyncTask;
import lee.studdy.bbs.TopicListActivity;
import lee.studdy.common.BaseActivity;
import lee.studdy.common.Constant;
import lee.studdy.myview.PathView;
import lee.studdy.tools.MyTools;

import org.apkplug.Bundle.BundleControl;
import org.apkplug.Bundle.installCallback;
import org.apkplug.app.FrameworkFactory;
import org.apkplug.app.FrameworkInstance;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.SaveCallback;

public class MainActivity extends BaseActivity implements lee.studdy.myview.PathView.OnItemClickListener {

	private ListView listView;// 已安装的插件列表
	private PluginAdapter adapter;// 列表适配器
	private FrameworkInstance frame = null;// 插件化框架
	private List<org.osgi.framework.Bundle> bundles = null;// 插件集合
	private List<Map<String, Object>> pluginList;
	private AlertDialog alertDialog;
	// 时间 日期
	private TextView timeTextView;
	private TextView dateTextView;
	private final String[] weekStrings = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	private Timer timeTimer;
	private FrameLayout rootLayout;// 根布局
	private SharedPreferences sharedPreferences;
	// 当前背景图片序号
	private int currentPosition = 0;
	// 背景图片列表
	private final int[] IMAGESTRINGS = { R.drawable.b_house, R.drawable.b_bed, R.drawable.b_haimianbaobao,
			R.drawable.b_tusiji, R.drawable.b_road, R.drawable.b_bridge, R.drawable.b_sun, R.drawable.b_lengtu };
	// 每次安装新插件 自定定位到最后
	private boolean installNew = false;

	// 网络插件图片集合
	// private List<Map<String, Bitmap>> onlinePluginBitmapList = null;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setActivity(this);
		setContentView(R.layout.activity_main);
		sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
		rootLayout = (FrameLayout) findViewById(R.id.rootview);

		currentPosition = sharedPreferences.getInt("position", 0);
		rootLayout.setBackgroundResource(IMAGESTRINGS[currentPosition]);

		initTime();
		// 定时更新时间
		timeTimer = new Timer();
		timeTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						Time t = new Time();
						t.setToNow(); // 取得系统时间。
						int hour = t.hour; // 0-23
						int minute = t.minute;
						if (timeTextView != null) {
							timeTextView.setText(hour + ":" + (minute > 9 ? "" : "0") + minute);
						}
					}
				});
			}
		}, 1000, 1000);

		frame = ((MyApplication) this.getApplication()).getFrame();

		initListView();
		ListenerBundleEvent();
		setupView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AVAnalytics.onResume(this);
		AVAnalytics.setSessionContinueMillis(10 * 1000);
		setDateAndWeek();
	}

	private void initTime() {
		timeTextView = (TextView) findViewById(R.id.time);
		dateTextView = (TextView) findViewById(R.id.date);
		timeTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showCartoonToast("好好学习！天天向上！", 0);
			}
		});
		setDateAndWeek();
	}

	private void setDateAndWeek() {
		// 保存更新时间
		Time t = new Time();
		t.setToNow(); // 取得系统时间。
		int year = t.year;
		int month = t.month + 1;
		int day = t.monthDay;
		int hour = t.hour; // 0-23
		int minute = t.minute;
		int week = t.weekDay;
		if (timeTextView != null) {
			timeTextView.setText(hour + ":" + (minute > 9 ? "" : "0") + minute);
		}
		if (dateTextView != null) {
			dateTextView.setText(year + "-" + (month > 9 ? "" : "0") + month + "-" + (day > 9 ? "" : "0") + day + "  "
					+ weekStrings[week]);
		}
	}

	private void initListView() {
		pluginList = new ArrayList<Map<String, Object>>();

		bundles = new java.util.ArrayList<org.osgi.framework.Bundle>();
		BundleContext context = frame.getSystemBundleContext();
		for (int i = 0; i < context.getBundles().length; i++) {
			if (context.getBundles()[i].getBundleId() == 0) {// 去掉systembundle
				continue;
			}
			// 获取已安装插件
			// if(context.getBundles()[i].getBundleId()!=0&&!context.getBundles()[i].getSymbolicName().equals("com.example.bundledemotheme"))
			bundles.add(context.getBundles()[i]);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", context.getBundles()[i].getBundle_icon());
			map.put("name", context.getBundles()[i].getName());
			pluginList.add(map);
		}

		listView = (ListView) findViewById(R.id.listview_plugin);
		adapter = new PluginAdapter(this, pluginList);
		// 动画
		listView.setLayoutAnimation(MyTools.getgridlayoutAnim());
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					showCartoonToast("你好！" + Constant.NICKNAME + "！", 100);
				} else if (position == 1) {
					// 打开你问我答社区板块
					startActivity(new Intent(MainActivity.this, TopicListActivity.class));
				} else {
					int thisPosition = position - 2;
					AVAnalytics.onEvent(MainActivity.this, (String) pluginList.get(thisPosition).get("name"), "open");
					org.osgi.framework.Bundle bundle = bundles.get(thisPosition);
					if (bundle.getState() != bundle.ACTIVE) {
						// 判断插件是否已启动
						try {
							bundle.start();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (bundle.getBundleActivity() != null) {
						Intent i = new Intent();
						i.setClassName(MainActivity.this, bundle.getBundleActivity().split(",")[0]);
						i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						MainActivity.this.startActivity(i);
					} else {
						Toast.makeText(MainActivity.this, "该插件没有配置BundleActivity", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {

				} else if (position == 1) {

				} else {
					final int myPosition = position - 2;
					final org.osgi.framework.Bundle bundle = bundles.get(myPosition);
					AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(MainActivity.this);
					alertbBuilder.setMessage("是否卸载插件？").setPositiveButton("卸载", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							AVAnalytics.onEvent(MainActivity.this, (String) pluginList.get(myPosition).get("name"),
									"uninstall");
							// 直接使用 Bundle.uninstall()卸载
							try {
								bundle.uninstall();
							} catch (Exception e) {
								e.printStackTrace();
							}
							dialog.cancel();
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create();
					alertbBuilder.show();
				}
				return false;
			}
		});
	}

	// path菜单初始化
	private void setupView() {
		PathView mPathView = (PathView) findViewById(R.id.mPathView);
		ImageButton startMenu = new ImageButton(this);
		startMenu.setBackgroundResource(R.drawable.start_menu_btn);
		mPathView.setStartMenu(startMenu);

		int[] drawableIds = new int[] { R.drawable.menu_contact, R.drawable.menu_background, R.drawable.menu_native,
				R.drawable.menu_net };
		View[] items = new View[drawableIds.length];
		for (int i = 0; i < drawableIds.length; i++) {
			ImageButton button = new ImageButton(this);
			button.setBackgroundResource(drawableIds[i]);
			items[i] = button;
		}
		mPathView.setItems(items);
		mPathView.setOnItemClickListener(this);
	}

	/**
	 * 安装插件回调函数
	 */
	class myinstallCallback implements installCallback {

		@Override
		public void callback(int arg0, org.osgi.framework.Bundle arg1) {
			if (arg0 == installCallback.stutas5 || arg0 == installCallback.stutas7) {
				// info.setText(String.format("插件安装 %s ：\n %s",
				// stutasToStr(arg0), showBundle(arg1)));
				showCartoonToast(String.format("插件安装 %s ：\n", stutasToStr(arg0)), 0);
				return;
			} else {
				String info1 = "安装状态:" + arg0;
				// info.setText("插件安装失败 ：" + this.stutasToStr(arg0));
				showCartoonToast("插件安装失败 ：" + this.stutasToStr(arg0), 0);
			}

		}

		/**
		 * 信息由 http://www.apkplug.com/javadoc/bundledoc1.5.3/ org.apkplug.Bundle
		 * 接口 installCallback 提供
		 * 
		 * @param stutas
		 * @return
		 */
		private String stutasToStr(int stutas) {
			if (stutas == installCallback.stutas) {
				return "缺少SymbolicName";
			} else if (stutas == installCallback.stutas1) {
				return "已是最新版本";
			} else if (stutas == installCallback.stutas2) {
				return "版本号不正确";
			} else if (stutas == installCallback.stutas3) {
				return " 版本相等";
			} else if (stutas == installCallback.stutas4) {
				return "无法获取正确的证书";
			} else if (stutas == installCallback.stutas5) {
				return "更新成功";
			} else if (stutas == installCallback.stutas6) {
				return "证书不一致";
			} else if (stutas == installCallback.stutas7) {
				return "安装成功";
			}
			return "状态信息不正确";
		}
	}

	/**
	 * 用户长按apk文件以后回调事件
	 */
	class clickfile implements FilebrowerDialog.ClickFile {

		public void ClickFile(FilebrowerDialog fd, int ClickType, String[] filepath) {
			// 用户长按文件后的回掉函数
			fd.close();
			// 记录当前文件路径 下次直接显示这个文件夹
			SharedPreferencesFactory.getInstance(MainActivity.this).putString("sd", new File(filepath[0]).getParent());
			try {
				installNew = true;
				// 调用osgi插件安装服务安装插件
				install("file:" + filepath[0], new myinstallCallback());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	/**
	 * 监听插件安装事件，当有新插件安装或卸载时成功也更新一下
	 */
	private void ListenerBundleEvent() {
		frame.getSystemBundleContext().addBundleListener(new SynchronousBundleListener() {

			public void bundleChanged(BundleEvent event) {
				// 把插件列表清空
				bundles.clear();
				pluginList.clear();

				BundleContext context = frame.getSystemBundleContext();
				for (int i = 0; i < context.getBundles().length; i++) {
					if (context.getBundles()[i].getBundleId() == 0) {// 去掉systembundle
						continue;
					}
					bundles.add(context.getBundles()[i]);

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("image", context.getBundles()[i].getBundle_icon());
					map.put("name", context.getBundles()[i].getName());
					pluginList.add(map);

				}
				// 安装完刷新
				if (installNew) {
					installNew = false;
					listView.setLayoutAnimation(MyTools.getgridlayoutAnim());// 用了在打开插件第一次会卡顿
					listView.setSelection(pluginList.size() - 1);
				}

				adapter.notifyDataSetChanged();
				adapter.notifyDataSetInvalidated();
			}

		});
	}

	/**
	 * 安装本地插件服务调用 详细接口参见 http://www.apkplug.com/javadoc/bundledoc1.5.3/
	 * org.apkplug.Bundle 接口 BundleControl
	 * 
	 * @param path
	 * @param callback
	 *            安装插件的回掉函数
	 * @throws Exception
	 */
	public void install(String path, installCallback callback) throws Exception {
		System.out.println("安装 :" + path);
		BundleContext mcontext = frame.getSystemBundleContext();
		ServiceReference reference = mcontext.getServiceReference(BundleControl.class.getName());
		if (null != reference) {
			BundleControl service = (BundleControl) mcontext.getService(reference);
			if (service != null) {
				// 插件启动级别为1(会自启) 并且不检查插件版本是否相同都安装
				service.install(mcontext, path, callback, 1, false);
			}
			mcontext.ungetService(reference);
		}
	}

	@Override
	public void onItemClick(View view, int position) {
		switch (position) {
		case 3:
			showProgress("请稍候", "正在拼命加载数据……");
			AVQuery<AVObject> query = AVQuery.getQuery("Plugin");
			query.findInBackground(new FindCallback<AVObject>() {
				public void done(List<AVObject> objects, AVException e) {
					if (objects != null && e == null) {
						int count = objects.size();
						if (count == 0) {
							showToast("暂时木有可用的插件列表~~~~~(>_<)~~~~~");
						} else {
							List<Map<String, Object>> onlinePluginList = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < count; i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								String name = objects.get(i).getString("name");
								map.put("name", name);
								map.put("fileName", objects.get(i).getString("fileName"));

								AVFile imageFile = (AVFile) objects.get(i).getAVFile("imageUrl");
								map.put("imageUrl", imageFile.getThumbnailUrl(false, 80, 80));

								AVFile file = (AVFile) objects.get(i).getAVFile("fileUrl");
								map.put("fileUrl", file);
								map.put("fileSize", file.getSize() / 1000 + "KB");
								boolean isNew = true;
								for (int j = 0; j < pluginList.size(); j++) {
									if (name.equals(pluginList.get(j).get("name"))) {
										isNew = false;
									}
								}
								map.put("isNew", isNew);

								onlinePluginList.add(map);
							}

							initOnlinePluginList(onlinePluginList, objects);
						}
					} else {
						showToast("获取数据失败~~~~~(>_<)~~~~~!");
						cancelProgress();
					}
					cancelProgress();
				}
			});

			break;
		case 2:
			// FilebrowerDialog 是一个选择本地文件的对话框
			// 初始化文件显示对话框
			final FilebrowerDialog fildia = new FilebrowerDialog(MainActivity.this, SharedPreferencesFactory
					.getInstance(MainActivity.this).getString("sd",
							Environment.getExternalStorageDirectory().getAbsolutePath())
			// 上次读取的文件夹路径
					, new apkFilter(new isFilesFilter(null))); // 过滤除.apk外的文件
			// 设置用户选择文件监听事件（长按）
			fildia.setClickfile(new clickfile());
			// 显示对话框
			fildia.show();
			break;

		case 1:
			// 弹出设置窗口
			initSettingDialog();
			break;
		case 0:
			// 弹出反馈窗口
			initFeedBackDialog();
			break;
		default:
			break;
		}
		// cancelProgress();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0x11:
			int position = data.getIntExtra("position", 0);

			Editor editor = sharedPreferences.edit();
			editor.putInt("position", position);
			editor.commit();

			rootLayout.setBackgroundResource(IMAGESTRINGS[position]);

			Log.e("TTTT", position + " result");
			break;

		default:
			break;
		}
	}

	public Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0x11:
				showCartoonToast((String) msg.obj, 0);
				cancelProgress();
				break;
			case 0x12:
				cancelProgress();
				break;
			case 0x31:
				Editor editor = sharedPreferences.edit();
				editor.putInt("position", currentPosition);
				editor.commit();
				rootLayout.setBackgroundResource(IMAGESTRINGS[currentPosition]);
				break;
			default:
				break;
			}
		}

	};

	// 主菜单列表窗口
	private void initOnlinePluginList(final List<Map<String, Object>> list, final List<AVObject> objects) {
		View view = getLayoutInflater().inflate(R.layout.get_plugin_ol, null);
		// 必须在view加载以后才能初始化gridview 否则会有空指针异常
		GridView gridView = (GridView) view.findViewById(R.id.add_plugin_gridview);
		OnlinePluginListAdapter adapter = new OnlinePluginListAdapter(this, list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!(Boolean) list.get(position).get("isNew")) {
					showToast("这个插件已经下载过了哦！");
					return;
				}
				showProgress("", "正在后台拼命下载安装插件……");
				AVAnalytics.onEvent(MainActivity.this, (String) objects.get(position).get("name"), "install");
				final int index = position;
				Log.e("TTTTT", "index = " + index);
				AVFile avFile = objects.get(position).getAVFile("fileUrl");
				if (avFile == null) {
					Log.e("TTTTT", "avFile == null");
					return;
				}
				installNew = true;
				avFile.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, AVException e) {
						if (data != null && e == null) {
							BundleContext context = frame.getSystemBundleContext();
							InstallBundle ib = new InstallBundle();
							Log.e("TTTTT", "try");
							try {
								ib.installOnlinePlugin(MainActivity.this, context,
										(String) objects.get(index).get("fileName"),
										(InputStream) MyTools.byteTOInputStream(data), new installCallback() {
											@Override
											public void callback(int arg0, Bundle arg1) {
												Log.e("TTTTT", "callback");
												String messageString = new String();
												if (arg0 == installCallback.stutas5 || arg0 == installCallback.stutas7) {
													Log.d("", String.format("插件安装成功-->%s", arg1.getName()));
													messageString = String.format("插件安装成功-->%s", arg1.getName());
												} else {
													Log.d("", "插件安装失败 " + arg1.getName());
													messageString = "插件安装失败 ：" + arg1.getName();
													cancelProgress();
													installNew = false;
												}
												Message msg = myHandler.obtainMessage();
												msg.what = 0x11;
												msg.obj = messageString;
												myHandler.sendMessage(msg);
												Log.e("TTTTT", "sendMessage");
											}
										});
							} catch (Exception e1) {
								Log.e("TTTTT", "Exception!~~~~~(>_<)~~~~");
								cancelProgress();
								e1.printStackTrace();
							}
						} else {
							Log.e("TTTTT", "获取数据失败!~~~~~(>_<)~~~~");
							cancelProgress();
							showToast("获取数据失败!~~~~~(>_<)~~~~~");
						}
					}
				});
				alertDialog.dismiss();
			}
		});

		alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("下载网络插件")
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).setView(view).create();
		alertDialog.show();
	}

	// 设置窗口
	private void initSettingDialog() {

		View view = getLayoutInflater().inflate(R.layout.settings, null);
		ImageView cancel = (ImageView) view.findViewById(R.id.cancel);
		ImageView confirm = (ImageView) view.findViewById(R.id.confirm);

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myHandler.sendEmptyMessage(0x31);
				alertDialog.dismiss();
			}
		});

		ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
		viewPager.setAdapter(new MyViewPagerAdapter(MainActivity.this, IMAGESTRINGS));
		viewPager.setCurrentItem(currentPosition);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				currentPosition = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		viewPager.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		// 选择弹出框
		alertDialog = new AlertDialog.Builder(MainActivity.this).setView(view).create();
		alertDialog.show();
	}

	// 反馈窗口
	private void initFeedBackDialog() {
		View view = getLayoutInflater().inflate(R.layout.about_us, null);
		final EditText nameEditText = (EditText) view.findViewById(R.id.name);
		final EditText contentEditText = (EditText) view.findViewById(R.id.content);
		Button submit = (Button) view.findViewById(R.id.submit);
		Button back = (Button) view.findViewById(R.id.back);

		nameEditText.setText(Constant.NICKNAME);

		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (nameEditText.getText().toString().length() > 0 && contentEditText.getText().toString().length() > 0) {

					AVObject feedBack = new AVObject("FeedBack");
					feedBack.put("name", nameEditText.getText().toString());
					feedBack.put("content", contentEditText.getText().toString());
					feedBack.saveInBackground(new SaveCallback() {
						public void done(AVException e) {
							if (e == null) {
								// 保存成功
								showToast("提交成功，祝您一帆风顺！");
								alertDialog.dismiss();
							} else {
								// 保存失败，输出错误信息
								showToast("Sorry!提交失败了~");
							}
						}
					});
				} else {
					showToast("昵称或者建议为空，请输入相应内容再提交，谢谢");
				}

			}
		});

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});

		// 选择弹出框
		alertDialog = new AlertDialog.Builder(MainActivity.this).setView(view).create();
		alertDialog.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		AVAnalytics.onPause(this);
	}

	@Override
	protected void onDestroy() {
		if (alertDialog != null) {
			alertDialog.cancel();
			alertDialog = null;
		}
		if (myHandler != null) {
			myHandler.removeCallbacksAndMessages(myHandler);
			myHandler = null;
		}
		if (bundles != null && listView != null && adapter != null) {
			bundles.removeAll(bundles);
			adapter.notifyDataSetChanged();
			listView = null;
			adapter = null;
			bundles = null;

		}
		if (timeTimer != null) {
			timeTimer.cancel();
			timeTimer.purge();
			timeTimer = null;
		}
		super.onDestroy();
	}

}
