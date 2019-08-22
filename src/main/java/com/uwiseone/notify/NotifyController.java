package com.uwiseone.notify;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.uwiseone.notify.dao.NotifyDao;


public class NotifyController {
	private static Logger log = LoggerFactory.getLogger(NotifyController.class);

	private static final String SLACK_API_URL = "https://ids-gw-developer.slack.com/services/hooks/slackbot?token=sdA9qOQjfWtlexyQPv8zoSrW&channel=%23make-groupware";


	
	private static void sendMessage(String message) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(SLACK_API_URL);
        httpPost.setEntity(new StringEntity(message, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        System.out.println(httpResponse.getStatusLine().getStatusCode());
	}
	
    public static void main( String[] args ) {
		ApplicationContext context = new ClassPathXmlApplicationContext("/config/NotiBeans.xml");
		NotifyDao dao = (NotifyDao)context.getBean("notifyDao");
		
		List<Map<String, Object>> list = null;
		
		try {
			list = dao.getIssueList();
			
			log.debug("[RedMine Slack Api] Start");
			String contents = "";
			for(Map<String, Object> map : list) {
				contents = map.get("STR").toString();
				log.debug("내용 : " + contents);
				sendMessage(contents);
			}
			log.debug("[RedMine Slack Api] finished");
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
}
