package lee.studdy.entity;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(User.USER_CLASS)
public class User extends AVObject {

	static final String USER_CLASS = "User";

	private static final String NICKNAME_KEY = "nickname";//插件名称
	public String getNickname() {
		return this.getString(NICKNAME_KEY);
	}
	public void setNickname(String nickname) {
		this.put(NICKNAME_KEY, nickname);
	}
	

}
