package com.heraizen.dhi.tzen.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heraizen.dhi.tzen.domain.TimeTableOutputWrapper;
import com.heraizen.dhi.tzen.domain.TimeTableStatus;
import com.heraizen.dhi.tzen.repo.TimeTableWrapperRepo;
import com.spaneos.ga.tt.domain.TimeTableOutput;

@Service
public class TimeTableOutputWrapperServiceImpl implements TimeTableOutputWrapperService {
	@Autowired
	private TimeTableWrapperRepo timeTableWrapperRepo;

	private static final Logger LOGGER = LoggerFactory.getLogger(TimeTableOutputWrapperServiceImpl.class.getName());

	@Override
	public TimeTableOutputWrapper addTimeTableOutput(TimeTableOutput timeTableOutput) {

		TimeTableOutputWrapper obj = null;
		try {
			LOGGER.info("Timetable output: {}",timeTableOutput);
			obj = TimeTableOutputWrapper.builder().createAt(LocalDateTime.now())
					.timeTableStatus(TimeTableStatus.GENERATED).timeTableOutput(timeTableOutput).build();
			LOGGER.info("Timetable wrapper objec: {}",obj.getTimeTableOutput());
			obj = timeTableWrapperRepo.save(obj);
			LOGGER.info("Timetable wrapper object :{} is added", obj.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return obj;
	}

}
