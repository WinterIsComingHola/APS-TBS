package com.roli.common.utils.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码加码工具类.
 */
public class EncodeUtils {

	private static final String DEFAULT_URL_ENCODING = "UTF-8"; // hex

	/**
	 * Hex编码.
	 */
	public static String hexEncodeString(String input) {
		return hexEncodeString(input, DEFAULT_URL_ENCODING);
	}

	/**
	 * Hex编码.
	 */
	public static String hexEncodeString(String input, String charset) {
		try {
			if (input == null)
				return "";
			return hexEncode(input.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Hex string encode exception", e);
		}
	}

	/**
	 * Hex编码.
	 */
	public static String hexEncode(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码.
	 */
	public static String hexDecodeString(String input) {
		return hexDecodeString(input, DEFAULT_URL_ENCODING);
	}

	/**
	 * Hex解码.
	 */
	public static String hexDecodeString(String input, String charset) {
		try {
			return new String(hexDecode(input), charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Hex string encode exception", e);
		}
	}

	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String input) {
		if (input == null)
			return null;
		return urlEncode(input, DEFAULT_URL_ENCODING);
	}

	/**
	 * URL 编码.
	 */
	public static String urlEncode(String input, String encoding) {
		if (input == null) {
			return null;
		}
		try {
			String str = URLEncoder.encode(input, encoding);
			return str.replaceAll("\\+", "%20"); // 空格被替换成+,js是%20,所以此处将+替换成%20
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String input) {
		if (input == null) {
			return null;
		}
		return urlDecode(input, DEFAULT_URL_ENCODING);
	}

	/**
	 * URL 解码.
	 */
	public static String urlDecode(String input, String encoding) {
		if (input == null) {
			return null;
		}
		try {
			return URLDecoder.decode(input, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml11(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}

}
