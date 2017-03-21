package com.puzzlegalaxy.slider.gui;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.swing.JTextArea;

import com.puzzlegalaxy.slider.gui.events.DebugRunnable;

public class DebugOutputStream extends OutputStream {

	private byte[] oneByte;
	private DebugRunnable appender;

	public DebugOutputStream(JTextArea txtara) {
		this(txtara, 1000);
	}

	public DebugOutputStream(JTextArea txtara, int maxlin) {
		if (maxlin < 1)
			throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value=" + maxlin + ")");
		oneByte=new byte[1];
		appender=new DebugRunnable(txtara, maxlin);
	}

	public synchronized void clear() {
		if (appender != null)
			appender.clear();
	}

	@Override
	public synchronized void close() {
		appender = null;
	}

	@Override
	public synchronized void flush() {
	}

	@Override
	public synchronized void write(int val) {
		oneByte[0] = (byte) val;
		write(oneByte, 0, 1);
	}

	@Override
	public synchronized void write(byte[] ba) {
		write(ba, 0, ba.length);
	}

	@Override
	public synchronized void write(byte[] ba,int str,int len) {
		if (appender != null)
			appender.append(bytesToString(ba, str, len));
	}

	static private String bytesToString(byte[] ba, int str, int len) {
		try {
			return new String(ba, str, len, "UTF-8");
		} catch (UnsupportedEncodingException thr) {
			return new String(ba, str, len);
		}
	}

}
