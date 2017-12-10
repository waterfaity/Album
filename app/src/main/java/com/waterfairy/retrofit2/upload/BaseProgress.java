package com.waterfairy.retrofit2.upload;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/9
 * des  :
 */

public interface BaseProgress {
//    void setTotalLen(long contentLength);

    void onDownloading(boolean upload, long contentLength, long bytesWritten);
}
