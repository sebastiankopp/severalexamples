package de.sebastiankopp.severalexamples.bootysoap.jms.entity;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;

import java.time.Instant;
import java.util.UUID;

public class ReadyMsg {
	private Instant tstamp;
	private String msg;
	private UUID msgId;
	
	public ReadyMsg() {
	}

	public ReadyMsg(String msg) {
		this.tstamp = now();
		this.msg = msg;
		this.msgId = randomUUID();
	}

	public Instant getTstamp() {
		return tstamp;
	}

	public void setTstamp(Instant tstamp) {
		this.tstamp = tstamp;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public UUID getMsgId() {
		return msgId;
	}

	public void setMsgId(UUID msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		return "ReadyMsg [tstamp=" + tstamp + ", msg=" + msg + ", msgId=" + msgId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		result = prime * result + ((msgId == null) ? 0 : msgId.hashCode());
		result = prime * result + ((tstamp == null) ? 0 : tstamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadyMsg other = (ReadyMsg) obj;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		if (msgId == null) {
			if (other.msgId != null)
				return false;
		} else if (!msgId.equals(other.msgId))
			return false;
		if (tstamp == null) {
			if (other.tstamp != null)
				return false;
		} else if (!tstamp.equals(other.tstamp))
			return false;
		return true;
	}
	
	
	
}
