package lee.studdy.apkplug;

import java.io.File;


/**
 * 过滤出文件夹
 *
 */
public class isFilesFilter extends AbFileFilter{

	public isFilesFilter(AbFileFilter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	
	public boolean isaccept(File dir, String filename) {
		// TODO Auto-generated method stub
		return dir.isDirectory();
	}
    
}
