package com.heraizen.dhi.tzen.repo;

import java.util.List;

import com.heraizen.dhi.tzen.domain.College;
import com.heraizen.dhi.tzen.domain.WorkHours;
import com.heraizen.dhi.tzen.dto.CollegeDTO;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.ext.ConstraintsRequirement;
import com.spaneos.ga.tt.ext.domain.Department;

public interface CollegeDao {
	
	
		public Long addTeacher(String cid, Teacher teacher);

		Long addTeachers(String cid, List<Teacher> teachers);

		public Teacher getFaculty(String cid, String id);

		public boolean deleteFaculty(String cid, String id);

		public List<Teacher> getFacultyList(String cid);
		
		public List<LabInfo> getLabInfoList(String cid);

		public Long addLabInfo(String cid, LabInfo labInfo);

		public Long addLabInfoList(String cid, List<LabInfo> labs);

		public LabInfo getLabInfo(String cid, String id);

		public boolean deleteLabInfo(String cid, String id);

		public List<WorkHours> addWorkHours(String cid, List<WorkHours> workHours);

		public List<WorkHours> getWorkHours(String cid);

		public Long addDept(String cid, Department dept);

		public CollegeDTO getCollegeDto(String cid, String deptId);

	
		public CollegeDTO updateAndGetCollegeDto(CollegeDTO collegeDTO);

		public CollegeDTO addStuGroups(String cid, String id, List<StudentGroup> studentGroups);

		public CollegeDTO updateConstraints(String cid, String deptId, ConstraintsRequirement constraintsRequirement);

		public CollegeDTO addStuGroups(String cid, String id, Department dept);
		
		
}
