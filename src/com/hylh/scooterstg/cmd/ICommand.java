package com.hylh.scooterstg.cmd;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

public interface ICommand {
	abstract public void send(String trcd, int seq, JSONObject keys, Context c, Handler h, int type);
	abstract public void abort( int seq );
}
