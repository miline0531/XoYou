package kr.co.genericit.mybase.xoyou2.network.interfaces;

/**
 * Retrofit Response Listener
 */
public interface ActionResultListener<T> {
    void onResponseResult(T result);

    void onResponseError(String message);
}
