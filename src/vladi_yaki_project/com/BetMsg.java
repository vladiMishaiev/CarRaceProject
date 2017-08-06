package vladi_yaki_project.com;

import java.io.Serializable;

import vladi_yaki_project.domain.Bet;

public class BetMsg implements Serializable{
	private BetMsgType msgType;
	private Bet data;
	private String msg;	
	public BetMsg(BetMsgType msgType, Bet data, String msg) {
		this.msgType = msgType;
		this.data = data;
		this.msg = msg;
	}
	public BetMsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(BetMsgType msgType) {
		this.msgType = msgType;
	}
	public Bet getData() {
		return data;
	}
	public void setData(Bet data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
	
}
