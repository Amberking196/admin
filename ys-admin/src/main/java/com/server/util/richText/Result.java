package com.server.util.richText;

import java.io.Serializable;

import lombok.Data;

/**
 * 富文本 返回类  author name: why
 *
 */
@Data
public class Result implements Serializable {

	/** 错误码. */
	private Integer errno;

	/** 具体的内容. */
	private String[] data;
}