package com.meet.wechat.pojo.xmlmessage;

public class XMLVoiceMessage extends XMLMessage{

	private String mediaId;
	
	public XMLVoiceMessage(String toUserName, String fromUserName,String mediaId) {
		super(toUserName, fromUserName, "voice");
		this.mediaId = mediaId;
	}


	@Override
	public String subXML() {
		return "<Voice><MediaId><![CDATA["+mediaId+"]]></MediaId></Voice>";
	}

	
}
