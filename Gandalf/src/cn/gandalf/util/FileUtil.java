package cn.gandalf.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class FileUtil {

	public static final long B = 1;
	public static final long KB = B * 1024;
	public static final long MB = KB * 1024;
	public static final long GB = MB * 1024;
	private static final int BUFFER = 8192;

	/**
	 * ��ʽ���ļ���С<b> ���е�λ
	 * 
	 * @param size
	 * @return
	 */
	public static String formatFileSize(long size) {
		StringBuilder sb = new StringBuilder();
		String u = null;
		double tmpSize = 0;
		if (size < KB) {
			sb.append(size).append("B");
			return sb.toString();
		} else if (size < MB) {
			tmpSize = getSize(size, KB);
			u = "KB";
		} else if (size < GB) {
			tmpSize = getSize(size, MB);
			u = "MB";
		} else {
			tmpSize = getSize(size, GB);
			u = "GB";
		}
		return sb.append(twodot(tmpSize)).append(u).toString();
	}

	/**
	 * ������λС��
	 * 
	 * @param d
	 * @return
	 */
	public static String twodot(double d) {
		return String.format("%.2f", d);
	}

	public static double getSize(long size, long u) {
		return (double) size / (double) u;
	}

	/**
	 * sd�������ҿ���
	 * 
	 * @return
	 */
	public static boolean isSdCardMounted() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * �ݹ鴴���ļ�Ŀ¼
	 * 
	 * @param path
	 * */
	public static void CreateDir(String path) {
		if (!isSdCardMounted())
			return;
		File file = new File(path);
		if (!file.exists()) {
			try {
				file.mkdirs();
			} catch (Exception e) {
				Log.e("hulutan", "error on creat dirs:" + e.getStackTrace());
			}
		}
	}

	/**
	 * ��ȡ�ļ�
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readTextFile(File file) throws IOException {
		String text = null;
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			text = readTextInputStream(is);
			;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return text;
	}

	/**
	 * �����ж�ȡ�ļ�
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readTextInputStream(InputStream is) throws IOException {
		StringBuffer strbuffer = new StringBuffer();
		String line;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is));
			while ((line = reader.readLine()) != null) {
				strbuffer.append(line).append("\r\n");
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return strbuffer.toString();
	}

	/**
	 * ���ı�����д���ļ�
	 * 
	 * @param file
	 * @param str
	 * @throws IOException
	 */
	public static void writeTextFile(File file, String str) throws IOException {
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(new FileOutputStream(file));
			out.write(str.getBytes());
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * ��ȡһ���ļ��д�С
	 * 
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File f) {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {

		if (file.exists()) { // �ж��ļ��Ƿ����
			if (file.isFile()) { // �ж��Ƿ����ļ�
				file.delete(); // delete()���� ��Ӧ��֪�� ��ɾ������˼;
			} else if (file.isDirectory()) { // �����������һ��Ŀ¼
				File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
				for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
					deleteFile(files[i]); // ��ÿ���ļ� ������������е���
				}
			}
			file.delete();
		}
	}
}