package com.example.audiowork;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.util.Map;

public class BaseWebView extends WebView {
    protected Context context;

    public BaseWebView(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        this.context = context;
        JavaScriptInterface jsInterface = new JavaScriptInterface(this.context);
        addJavascriptInterface(jsInterface, "utilWeb");
        Log.d("注册goback", CommandManager.getInstance().toString());
        // 注册goBack命令
        CommandManager.getInstance().registCommand(new GoBackCommand("goBack"));
        // 注册录音命令
        CommandManager.getInstance().registCommand(new RecordVoice("recordVoice"));
    }

    private class JavaScriptInterface {
        private Context activityContext;

        public JavaScriptInterface(Context context) {
            this.activityContext = context;
        }

        @JavascriptInterface
        public void postNative(final String commandName, final String param) {
            Log.d("commandName:", commandName);
            Log.d("param:", param);
            CommandManager.getInstance().exec(this.activityContext, commandName, param, new CommandCallback() {
                @Override
                public void onResult(Map params) {
                    handleResultWithCallback(params);
                }
            });
        }
    }

    private void handleResultWithCallback(Map params) {
        String id = (String) params.get("callbackId");
        String response = new Gson().toJson(params);
        Log.d("回调结果response：", response);
        Log.d("回调结果callbackId：", id);
        if (params.get("startGoBack") != null) {
            // 返回上个activity
            ((AiChatActivity) this.context).finish();
        }
        // id不为空时需要回调
        if (id != null) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    evaluateJavascript("javascript:" + "window." + id + "(" + response + ")", null);
                }
            });
        }
    }
}
