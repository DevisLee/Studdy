package lee.studdy.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(Plugin.PLUGIN_CLASS)
public class Plugin extends AVObject {

	static final String PLUGIN_CLASS = "Plugin";

	private static final String NAME_KEY = "name";//插件名称
	public String getName() {
		return this.getString(NAME_KEY);
	}
	public void setName(String name) {
		this.put(NAME_KEY, name);
	}
	
	private static final String FILENAME_KEY = "fileName";//插件名称
	public String getFileName() {
		return this.getString(FILENAME_KEY);
	}
	public void setFileName(String name) {
		this.put(FILENAME_KEY, name);
	}
	
	private static final String IMAGE_URL_KEY = "imageUrl";//图片路径
	public String getImageUrl() {
		return this.getString(IMAGE_URL_KEY);
	}
	public void setImageUrl(String imageUrl) {
		this.put(IMAGE_URL_KEY, imageUrl);
	}
	
	private static final String FILE_URL_KEY = "fileUrl";//文件路径
	public String getFileUrl() {
		return this.getString(FILE_URL_KEY);
	}
	public void setFileUrl(String fileUrl) {
		this.put(FILE_URL_KEY, fileUrl);
	}

}
