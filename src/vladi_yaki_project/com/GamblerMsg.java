package vladi_yaki_project.com;
import java.io.Serializable;

import vladi_yaki_project.domain.Gambler;

public class GamblerMsg implements Serializable{
	private GamblerMsgType msgType;
	private Gambler data;
	private String msg;

	public GamblerMsg(GamblerMsgType msgType, Gambler data, String msg) {
		this.msgType = msgType;
		this.data = data;
		this.msg = msg;
	}

	public GamblerMsgType getMsgType() {
		return msgType;
	}

	public void setMsgType(GamblerMsgType msgType) {
		this.msgType = msgType;
	}

	public Gambler getData() {
		return data;
	}

	public void setData(Gambler data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
	
}
