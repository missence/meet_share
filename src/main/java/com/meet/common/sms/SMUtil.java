package com.meet.common.sms;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.meet.common.constants.OrderConstants;
import com.meet.exception.BusinessException;

/**
 * 短信Util
 * @author bzhx
 */
public class SMUtil {

	public static void sendBatchSM(String mobiles, String identiyCode) throws BusinessException {
		/**
		 *  Step 1. 获取主题引用
		 */
	        CloudAccount account = new CloudAccount(OrderConstants.SMS_API_KEY,OrderConstants.SMS_SECRET_KEY, OrderConstants.SMS_ENDPOINT);
	        MNSClient client = account.getMNSClient();
	        CloudTopic topic = client.getTopicRef(OrderConstants.SMS_TOPIC);
	        /**
	         * Step 2. 设置SMS消息体（必须）
	         *
	         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
	         */
	        RawTopicMessage msg = new RawTopicMessage();
	        msg.setMessageBody("sms-message");
	        /**
	         * Step 3. 生成SMS消息属性
	         */
	        MessageAttributes messageAttributes = new MessageAttributes();
	        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
	        // 3.1 设置发送短信的签名（SMSSignName）
	        batchSmsAttributes.setFreeSignName(OrderConstants.SMS_SIGNNAME);
	        // 3.2 设置发送短信使用的模板（SMSTempateCode）
	        batchSmsAttributes.setTemplateCode(OrderConstants.SMS_TEMPLATE_ID);
	        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
	        batchSmsAttributes.setType("singleContent");
	        
	        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
	        smsReceiverParams.setParam("code",identiyCode);
	        // 3.4 增加接收短信的号码
	        batchSmsAttributes.addSmsReceiver(mobiles, smsReceiverParams);
	        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
	        try {
	            /**
	             * Step 4. 发布SMS消息
	             */
	            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
	            System.out.println("MessageId: " + ret.getMessageId());
	            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
	        } catch (ServiceException se) {
	            System.out.println(se.getErrorCode() + se.getRequestId());
	            System.out.println(se.getMessage());
	            se.printStackTrace();
	            throw new BusinessException("調用短信接口異常:"+se.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new BusinessException("調用短信接口異常:"+e.getMessage());
	        }
	        client.close();
	}
	public static void main(String[] args) throws BusinessException {
		sendBatchSM("18910432851", "12345");
	}
}
