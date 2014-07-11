package lee.studdy.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(FeedBack.FEEDBACK_CLASS)
public class FeedBack extends AVObject {

	static final String FEEDBACK_CLASS = "FeedBack";

	private static final String NAME_KEY = "name";//插件名称
	public String getName() {
		return this.getString(NAME_KEY);
	}
	public void setName(String name) {
		this.put(NAME_KEY, name);
	}
	
	private static final String CONTENT_KEY = "content";//插件名称
	public String getContent() {
		return this.getString(CONTENT_KEY);
	}
	public void setContent(String name) {
		this.put(CONTENT_KEY, name);
	}

}
