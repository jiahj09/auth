package webspider.http.cookie;

/**
 * 功能：
 *
 * @Author:JIUNLIU
 * @data : 2020/3/17 9:57
 */

import java.util.Date;
import java.util.Objects;

public class Cookie {
    private String name;
    private String value;
    private String cookieDomain;
    private Date cookieExpiryDate;
    private String cookiePath;
    private boolean isSecure;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public Date getCookieExpiryDate() {
        return cookieExpiryDate;
    }

    public void setCookieExpiryDate(Date cookieExpiryDate) {
        this.cookieExpiryDate = cookieExpiryDate;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return isSecure == cookie.isSecure &&
                name.equals(cookie.name) &&
                Objects.equals(value, cookie.value) &&
                Objects.equals(cookieDomain, cookie.cookieDomain) &&
                Objects.equals(cookieExpiryDate, cookie.cookieExpiryDate) &&
                Objects.equals(cookiePath, cookie.cookiePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, cookieDomain, cookieExpiryDate, cookiePath, isSecure);
    }
}


