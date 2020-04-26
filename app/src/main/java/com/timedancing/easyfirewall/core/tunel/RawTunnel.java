package com.timedancing.easyfirewall.core.tunel;

import android.util.Log;

import com.timedancing.easyfirewall.core.nat.NatSession;
import com.timedancing.easyfirewall.hooker.TcpHooker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * Created by zengzheying on 15/12/30.
 */
public class RawTunnel extends Tunnel {

	private TcpHooker tcpHooker;

	public RawTunnel(SocketChannel innerChannel, Selector selector) {
		super(innerChannel, selector);
	}

	public RawTunnel(InetSocketAddress serverAddress, Selector selector, NatSession session) throws IOException {
		super(serverAddress, selector);
		tcpHooker = new TcpHooker(session);
	}

	@Override
	protected void onConnected(ByteBuffer buffer) throws Exception {
		onTunnelEstablished();
	}

	@Override
	protected boolean isTunnelEstablished() {
		return true;
	}

	@Override
	protected void beforeSend(ByteBuffer buffer) throws Exception {
//		ByteBuffer byteBuffer = buffer.asReadOnlyBuffer();
//		Charset charset = Charset.forName("utf-8");
//		String s = charset.decode(byteBuffer).toString();
//		Log.e("zxj", "ByteBuffer: " + s );
	}

	@Override
	protected ByteBuffer afterReceived(ByteBuffer buffer) throws Exception {
		return tcpHooker.afterReceived(buffer);
	}

	@Override
	protected void onDispose() {

	}
}
