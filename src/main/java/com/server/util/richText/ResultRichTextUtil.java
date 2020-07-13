package com.server.util.richText;

/**
 * 富文本 返回对象 工具类   author name: why
 *
 */
public class ResultRichTextUtil {

	public static Result success(String[] object) {
		Result result = new Result();
		result.setErrno(0);
		result.setData(object);
		return result;
	}

	public static Result success() {
		return success(null);
	}

}