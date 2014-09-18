package cn.gandalf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

public class PersistenceManager {

	private static PersistenceManager instance = null;
	private Context mContext;

	private PersistenceManager(Context context) {
		mContext = context.getApplicationContext();
	}

	public static synchronized PersistenceManager getInstance(Context context) {
		if (instance == null) {
			instance = new PersistenceManager(context);
		}
		return instance;
	}

	public void putObject(String key, Object value) {
		putObject(key, value, PreferenceManager.TIME_FOREVER);
	}

	public void putObject(String key, Object value, long period) {
		save(key, value);
		PreferenceManager.getInstance(mContext).putPeriod(key, period);
	}

	public Object getObject(String key) {
		if (PreferenceManager.getInstance(mContext).checkPeriod(key)) {
			return load(key);
		} else {
			delete(key);
		}
		return null;
	}

	private void save(String key, Object value) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = mContext.openFileOutput(key, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Object load(String key) {
		Object value = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = mContext.openFileInput(key);
			ois = new ObjectInputStream(fis);
			value = ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return value;
	}

	private void delete(String key) {
		File file = mContext.getFileStreamPath(key);
		if (file.exists()) {
			file.delete();
		}
	}

}
