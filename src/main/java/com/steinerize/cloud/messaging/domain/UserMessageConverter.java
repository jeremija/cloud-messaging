//package com.steinerize.cloud.messaging.domain;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.steinerize.cloud.messaging.util.GenericHandler;
//
//public class UserMessageConverter {
//	
//	private static final Log LOG = LogFactory.getLog(UserMessageConverter.class);
//	
//	private static interface UserProcessor {
//		public void process(PushMessage message, User user);
//	}
//	
//	private static GenericHandler<Device, User> handlers = 
//			new GenericHandler<>();
//	
//	public static PushMessage toPushMessage(UserMessage userMessage, 
//			List<User> users) 
//	{
//		PushMessage pushMessage = new PushMessage(userMessage.data);
//		
//		for(User user : users) {
//			UserProcessor processor = processors.get(user.device);
//			if (processor == null) {
//				LOG.error("No userProcessor for device " + user.device);
//				continue;
//			}
//			processor.process(pushMessage, user);
//		}
//		
//		return pushMessage;
//	}
//	
//}
