package vladi_yaki_project.com;

import java.io.Serializable;

import vladi_yaki_project.domain.RaceData;

public class RaceMsg implements Serializable {
	private RaceMsgType msgType;
	private RaceData data;
	private String msg;
	
	public RaceMsg(RaceMsgType msgType, RaceData data, String msg) {
		this.msgType = msgType;
		this.data = data;
		this.msg = msg;
	}
	
	public RaceMsgType getMsgType() {
		return msgType;
	}
	public void setMsgType(RaceMsgType msgType) {
		this.msgType = msgType;
	}
	public RaceData getData() {
		return data;
	}
	public void setData(RaceData data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
