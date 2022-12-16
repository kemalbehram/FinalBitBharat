package com.mobiloitte.socket.serviceimpl;

import java.util.Map;

import com.google.gson.Gson;
import com.mobiloitte.socket.enums.MessageType;

public class SocketResponseImpl {
		private static Gson gson = new Gson();
		private MessageType messageType;
		private Map<String, Object> params;

		public MessageType getMessageType() {
			return messageType;
		}

		public void setMessageType(MessageType messageType) {
			this.messageType = messageType;
		}

		public void setParams(Map<String, Object> params) {
			this.params = params;
		}

		public Map<String, Object> getParams() {
			return params;
		}

		public int getParamsLength() {
			String json = gson.toJson(params);
			return json.length();
		}


}
