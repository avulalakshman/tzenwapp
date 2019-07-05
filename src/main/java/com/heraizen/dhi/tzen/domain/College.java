package com.heraizen.dhi.tzen.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

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
public class College {

		@Id
		private String cid;
		private String name;
		private String code;
		private String shortName;
		private List<WorkHours> workHrs=new ArrayList<>();
		private List<LabInfo> labsInfo=new ArrayList<>();
		private List<Teacher> teachersInfo=new ArrayList<>();
		private List<Department> departments=new ArrayList<>();
			
}
