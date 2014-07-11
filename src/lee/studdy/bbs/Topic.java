package lee.studdy.bbs;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(Topic.CLASS)
public class Topic extends AVObject {

  static final String CLASS = "Topic";
  private static final String CONTENT_KEY = "userName";
  public String getContent() {	
    return this.getString(CONTENT_KEY);
  }

  public void setContent(String content) {
    this.put(CONTENT_KEY, content);
  }
}
