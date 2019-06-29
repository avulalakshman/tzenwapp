package com.heraizen.dhi.tzen.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spaneos.ga.tt.domain.TimeTableOutput;
import com.spaneos.ga.tt.ext.ConstraintsContainer;
import com.spaneos.ga.tt.ext.JsonUtil;

@Service
public class TimeTableServiceImpl implements TimeTableService{
	private final String path = "/opt/tt-output/ttoutput-cs-jss.json"; 
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableOutput.class);
	
	
	@Override
	public TimeTableOutput getTimeTableOutput() {
		TimeTableOutput timeTableOutput = JsonUtil.readTimeTableOutput(path);
		LOGGER.info("Time table output :{}",timeTableOutput);
		return timeTableOutput;
	}
	


}
