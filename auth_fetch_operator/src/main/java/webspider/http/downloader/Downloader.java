package webspider.http.downloader;


import webspider.http.Request;
import webspider.http.Response;

/**
 * @author JIUN·LIU
 * @data 2020/2/12 14:18
 */
public interface Downloader {
    Response download(Request request);
}
