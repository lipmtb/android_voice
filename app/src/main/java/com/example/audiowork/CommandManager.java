package com.example.audiowork;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map commandMap = new HashMap<String, Command>();
    private static CommandManager commandManager;

    public static CommandManager getInstance() {
        if (commandManager == null) {
            synchronized (CommandManager.class) {
                commandManager = new CommandManager();
            }
        }
        return commandManager;
    }

    public void exec(Context ctx, final String commandName, final String param, CommandCallback callback) {
        Command tempCommand = (Command) commandMap.get(commandName);
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(param, Map.class);
        Log.d("命令执行:", tempCommand.name());
        if (tempCommand != null) {
            tempCommand.execWithCallback(ctx, map, callback);
        } else {
            Log.d("找不到命令", commandName);
        }
    }

    public void registCommand(Command newCommand) {
        commandMap.put(newCommand.name(), newCommand);
    }

}
