package com.example.audiowork;

import android.content.Context;

import java.util.Map;

public interface Command {
    String name();

    void execWithCallback(Context ctc, Map params, CommandCallback callback);
}
