package com.heraizen.dhi.tzen.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heraizen.dhi.tzen.domain.TimeTableOutputWrapper;
import com.heraizen.dhi.tzen.repo.TimeTableWrapperRepo;
import com.spaneos.ga.tt.domain.TimeTableOutput;

@Service
public class TimeTableServiceImpl implements TimeTableService{
	@Autowired
	private TimeTableWrapperRepo ttRepo;

	private final String path = "/opt/tt-output/ttoutput-cs-jss.json"; 
	private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableOutput.class);
	
	
	@Override
	public TimeTableOutput getTimeTableOutput(String cid,String deptId) {
		//TimeTableOutput timeTableOutput = JsonUtil.readTimeTableOutput(path);
		
		TimeTableOutput timeTableOutput=null;
		TimeTableOutputWrapper twrapper = ttRepo.findByCidAndDeptId(cid,deptId);
		if (twrapper !=null) {
			timeTableOutput = twrapper.getTimeTableOutput();
		}
		LOGGER.info("Time table output :{}",timeTableOutput);
		return timeTableOutput;
	}
	


}
