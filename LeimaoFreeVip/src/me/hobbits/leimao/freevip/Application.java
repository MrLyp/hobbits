package me.hobbits.leimao.freevip;

import me.hobbits.leimao.freevip.net.HttpManager;
import me.hobbits.leimao.freevip.util.ShareUtils;
import cn.gandalf.util.AsyncImageLoader;
import android.content.Context;

public class Application extends android.app.Application {

	@Override
	public void onCreate() {
		super.onCreate();
		onAppStart();
	}

	private void onAppStart() {
		Context mContext = getApplicationContext();
		HttpManager.init(mContext);
		AsyncImageLoader.init(mContext);
	}

}
