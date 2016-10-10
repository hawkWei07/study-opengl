package cn.hawk.lessoneight;

/**
 * Created by kehaowei on 2016/10/10.
 */

public interface ErrorHandler {
    enum ErrorType {
        BUFFER_CREATION_ERROR
    }

    void handleError(ErrorType errorType, String cause);

}
