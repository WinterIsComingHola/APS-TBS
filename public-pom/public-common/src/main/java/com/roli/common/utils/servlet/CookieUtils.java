package com.roli.common.utils.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author xuxinyu
 * @date 2018/4/17 下午8:36
 */
public class CookieUtils {


    /**
     * 获取当前请求所有cookie
     *
     * @return
     */
    public static Cookie[] getCookies(HttpServletRequest request) {
        return request.getCookies();
    }

    /**
     * 获取指定name的cookie
     *
     * @param name
     * @param request
     * @return
     */
    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = getCookies(request);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * 获取指定name的cookie值
     *
     * @param name
     * @param request
     * @return
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie[] cookies = getCookies(request);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 删除cookies
     *
     * liuhb
     * @param request
     * @param response
     * @param name
     * @param path 必须设置，否则无效
     * @since
     *
     */
    public static String deleteCookie(HttpServletRequest request, HttpServletResponse response, String name, String path) {
        String value = null;
        Cookie[] cookies = getCookies(request);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    value = cookie.getValue();
                    cookie.setMaxAge(0);
                    cookie.setPath(path);
                    response.addCookie(cookie);
                }
            }
        }
        return value;
    }

    /**
     *
     * 删除cookie
     *
     * @param cookie
     *            需要删除的某个cookie
     * @param response
     *            响应对象
     * @param domain
     * @param path
     *
     */
    public static void deleteCookie(Cookie cookie, HttpServletResponse response, String domain, String path) {
        if (cookie != null) {
            cookie.setMaxAge(0);
            cookie.setValue(null);
            cookie.setPath(path);
            cookie.setDomain(domain);
            response.addCookie(cookie);
        }
    }

    /**
     *
     * 新增cookie
     *
     * @param cookie
     *            需要保存的cookie
     * @param response
     *            响应对象
     *
     */
    public static void addCookie(Cookie cookie, HttpServletResponse response) {
        response.addCookie(cookie);
    }

    /**
     * 新增cookie
     *
     * liuhb
     * @param response
     * @param name
     * @param value
     * @param maxAge 不设置过期时间就是会话
     * @param path 这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
     * @since
     *
     */
    public static void addCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 新增cookie
     *
     * liuhb
     * @param response
     * @param name
     * @param value
     * @param domain
     * @param maxAge 不设置过期时间就是会话
     * @param path 这个路径即该工程下都可以访问该cookie 如果不设置路径，那么只有设置该cookie路径及其子路径可以访问
     * @since
     *
     */
    public static void addCookie(HttpServletResponse response, String name, String value, String domain, Integer maxAge, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 新增cookie
     *
     * @param response
     * @param name
     * @param value
     * @param domain
     * @param maxAge
     * @param isHttpOnly
     */
    public static void addCookie(HttpServletResponse response, String name, String value, String domain,
                                 Integer maxAge, boolean isHttpOnly, String path) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setHttpOnly(isHttpOnly);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    public static void addCookie(HttpServletResponse response, String name, String value, String domain,
                                 Integer maxAge, boolean isHttpOnly, String path, boolean isSecure) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setHttpOnly(isHttpOnly);
        if (maxAge != null) {
            cookie.setMaxAge(maxAge);
        }
        cookie.setPath(path);
        cookie.setSecure(isSecure);
        response.addCookie(cookie);
    }


}
