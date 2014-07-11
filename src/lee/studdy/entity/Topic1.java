package lee.studdy.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(Topic1.TOPIC_CLASS)
public class Topic1 extends AVObject {

	static final String TOPIC_CLASS = "Topic";

	private static final String TITLE_KEY = "title";
	public String getTitle() {
		return this.getString(TITLE_KEY);
	}
	public void setTitle(String title) {
		this.put(TITLE_KEY, title);
	}
	
	private static final String CONTENT_KEY = "content";
	public String getContent() {
		return this.getString(CONTENT_KEY);
	}
	public void setContent(String name) {
		this.put(CONTENT_KEY, name);
	}
	
	private static final String CREATOR_KEY = "creator";
	public String getCreator() {
		return this.getString(CREATOR_KEY);
	}
	public void setCreator(String creator) {
		this.put(CREATOR_KEY, creator);
	}
	
	private static final String HIT_KEY = "hit";
	public String getHit() {
		return this.getString(HIT_KEY);
	}
	public void setHit(String hit) {
		this.put(HIT_KEY, hit);
	}
	
}
