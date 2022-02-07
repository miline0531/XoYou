package com.redrover.xoyou.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.CommonActivity;

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public class WebViewActivity extends CommonActivity {
    private String TAG = WebViewActivity.class.getSimpleName();

    private WebView webView = null;

    @Override
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());  // 새 창 띄우기 않기
        webView.setWebChromeClient(new WebChromeClient());

        webView.getSettings().setLoadWithOverviewMode(true);  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.getSettings().setUseWideViewPort(true);  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.getSettings().setSupportZoom(false);  // 줌 설정 여부
        webView.getSettings().setBuiltInZoomControls(false);  // 줌 확대/축소 버튼 여부

        webView.getSettings().setJavaScriptEnabled(true); // 자바스크립트 사용여부
        webView.addJavascriptInterface(new AndroidBridge(), "HybridApp");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true); // javascript가 window.open()을 사용할 수 있도록 설정
        webView.getSettings().setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView.getSettings().setDomStorageEnabled(true);  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출
//      webView.loadUrl("http://www.naver.com");
        webView.loadUrl("https://www.genericit.co.kr/addr2.html");

    }

    public class AndroidBridge {
        @JavascriptInterface
        public void invokeAction(final String arg){
            String test = arg;
            Intent i = new Intent(WebViewActivity.this,AddLocationActivity.class);
            i.putExtra("arg",arg);
            setResult(RESULT_OK,i);
            finish();
        }
    }
}