package com.github.ttwd80.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@ResponseBody
	@RequestMapping("/index")
	public Map<String, Integer> index(@RequestParam("value") final Integer value) {
		final Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		map.put("value", value);
		map.put("square", value * value);
		map.put("square", value * value * value);
		logger.info("{}, {}, {}", value, value * value, value * value * value);
		return map;
	}

}
