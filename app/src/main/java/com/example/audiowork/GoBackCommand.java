package com.example.audiowork;

import android.content.Context;
import android.util.Log;

import java.util.Map;

public class GoBackCommand implements Command {
    private String name;

    public GoBackCommand(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public void execWithCallback(Context ctx, Map params, CommandCallback callback) {
        Log.d("GoBackCommand execWithCallback执行,param is:", params.toString());
        params.put("startGoBack","1");
        callback.onResult(params);
    }
}
