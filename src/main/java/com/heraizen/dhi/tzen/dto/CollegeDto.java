package com.heraizen.dhi.tzen.dto;

import java.util.ArrayList;
import java.util.List;

import com.heraizen.dhi.tzen.domain.WorkHours;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.ext.domain.Department;

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
public class CollegeDto {
	
	private String cid;
	private String name;
	private String code;
	private String shortName;
	private List<WorkHours> workHrs=new ArrayList<>();
	private List<LabInfo> labsInfo=new ArrayList<>();
	private List<Teacher> teachersInfo=new ArrayList<>();
	private List<Department> departments=new ArrayList<>();
	
}
