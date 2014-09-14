package cn.gandalf.exception;

import java.lang.reflect.Field;

public class ECode {
	public final static int SERVER_RETURN_SUCCESS = 0;
	public final static int CANCELED = -100;
	public final static int SUCCESS = 1;
	public final static int FAIL = -1;
	public final static int JSON_PARSER_ERROR = 10;
	public final static int SERVER_RETURN_NULL = 11;
	public final static int SERVER_RETURN_ERROR = 12;
	public final static int SERVER_RETURN_EMPTY_SET = 15;
	public final static int SERVER_RETURN_EMPTY_SET_FIRST_TIME = 16;
	public final static int CANNOT_INSTANTIATE = 17;
	public final static int CANNOT_LOCATE = 18;
	public final static int SUCCESS_LAST_TIME = 2;
	public static String codeStr(int code) {
		Field[] fs = ECode.class.getFields();
		for (Field f : fs) {
			try {
				if (f.getInt(null) == code)
					return f.getName();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private static int mCustomCodeStart = 60000;

	public static int getCustomCode() {
		return mCustomCodeStart++;
	}
}
