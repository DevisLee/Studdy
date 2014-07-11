package lee.studdy.apkplug;

import java.io.File;


/**
 * 过滤出文件
 *
 */
public class isFileFilter extends AbFileFilter{

	public isFileFilter(AbFileFilter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	
	public boolean isaccept(File dir, String filename) {
		// TODO Auto-generated method stub
		return !dir.isDirectory();
	}
}
