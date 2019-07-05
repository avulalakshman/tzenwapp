package com.heraizen.dhi.tzen.dto;

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
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CollegeWithDeptDTO {
	private String cid;
	private String name;
	private String code;
	private String shortName;
	private List<WorkHours> workHrs;
	private List<LabInfo> labsInfo;
	private List<Teacher> teachersInfo;
	private Department department;
}
