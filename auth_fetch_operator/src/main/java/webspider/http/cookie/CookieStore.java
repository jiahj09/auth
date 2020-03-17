package webspider.http.cookie;


import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/17 9:57
 */
public class CookieStore {
    private CopyOnWriteArrayList<Cookie> cookies;

    public boolean isContain(Cookie cookie) {
        if (cookies == null) return false;
        else {
            boolean containFlag = false;
            for (Cookie co : cookies) {
                if (co.equals(cookie)) {
                    containFlag = true;
                    break;
                }
            }
            return containFlag;
        }
    }

    public void addCookie(Cookie cookie) {
        if (isContain(cookie) || cookie == null) return;
        else cookies.add(cookie);
    }

    public void deleteCookieByName(Cookie cookie) {
        if (cookies == null) return;
        else {
            for (Cookie coo : cookies) {
                if (coo.getName().equalsIgnoreCase(cookie.getName())) {
                    cookies.remove(coo);
                }
            }
        }
    }

    public void addCookies(List<Cookie> cookieList) {
        for (Cookie cookie : cookieList) {
            addCookie(cookie);
        }
    }

    public void clear() {
        if (cookies == null) return;
        else cookies.clear();
    }


    public List<Cookie> getCookies() {
        return cookies.subList(0, cookies.size());
    }

    public Map<String, String> toMapCookie() {
        if (this.cookies == null || this.cookies.size() < 1) return null;
        Map<String, String> result = new HashMap<>();
        cookies.forEach(coo -> {
            result.put(coo.getName(), coo.getValue());
        });
        return result;
    }

    public static List<Cookie> parseHeader2Cookies(List<String> setCookieHeaders) {
        if (setCookieHeaders == null) return null;
        List<Cookie> result = new ArrayList<>();
        setCookieHeaders.forEach(s -> {
            result.add(parseHeader2Cookie(s));
        });
        return result;
    }

    public static Cookie parseHeader2Cookie(String origin) {
        //sendflag=20200317100827823356;domain=.10086.cn;secure;HTTPOnly;
        Cookie cookie = new Cookie();
        boolean nameFlag = true;
        while (true) {
            int i = origin.indexOf(";");
            if (i == -1 || i == origin.length() - 1) break;
            String substring = origin.substring(0, i).trim();
            if (substring.contains("=")) {
                int indexOf = substring.indexOf("=");
                String key = substring.substring(0, indexOf);
                String value = substring.substring(indexOf + 1);
                if (nameFlag) {
                    cookie.setName(key);
                    cookie.setValue(value);
                    nameFlag = false;
                } else if (key.startsWith("domain")) {
                    cookie.setCookieDomain(value);
                } else if (key.startsWith("path")) {
                    cookie.setCookiePath(value);
                }
            }
            origin = origin.substring(i + 1).trim();
        }
        cookie.setCookieExpiryDate(new Date(System.currentTimeMillis() + (1000 * 60 * 30)));
        if (cookie.getCookiePath() == null) cookie.setCookiePath("/");
        return cookie;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = new CopyOnWriteArrayList<Cookie>() {{
            addAll(cookies);
        }};
    }
}