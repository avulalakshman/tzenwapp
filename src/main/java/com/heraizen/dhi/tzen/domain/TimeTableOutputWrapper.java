package com.heraizen.dhi.tzen.domain;

import org.springframework.data.annotation.Id;

import com.spaneos.ga.tt.domain.TimeTableOutput;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeTableOutputWrapper {
		@Id
		private String id;
	
		private String cid;
		private String deptId;
	
		private TimeTableOutput timeTableOutput;
}
