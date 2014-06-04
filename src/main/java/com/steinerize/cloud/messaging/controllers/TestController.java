package com.steinerize.cloud.messaging.controllers;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/param")
public class TestController {
	
	private static final Log LOG = LogFactory.getLog(TestController.class);

	public static class Response {
		public Response(String data) {
			this.data = data;
			this.date = new Date();
		}
		public String data;
		public Date date;
	}
	
	public static class Request {
		public String text;
		public int number;
	}
	
	@RequestMapping("test")
	public @ResponseBody Response test(HttpServletResponse res) {
		LOG.debug("test(HttpServletResponse)");
		return new Response("data is ok");
	}
	
	@RequestMapping(value = "say", method = RequestMethod.POST)
	public @ResponseBody Response test(@RequestBody Request body) {
		LOG.debug("say(Request)");
		return new Response("you really said: " + body.text + ", " + body.number);
	}
}
