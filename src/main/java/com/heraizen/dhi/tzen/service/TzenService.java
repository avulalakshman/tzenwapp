package com.heraizen.dhi.tzen.service;

import java.util.List;

import com.heraizen.dhi.tzen.domain.College;
import com.heraizen.dhi.tzen.domain.WorkHours;
import com.heraizen.dhi.tzen.dto.CollegeDto;
import com.heraizen.dhi.tzen.dto.CollegeWithDeptDTO;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.ext.ConstraintsRequirement;
import com.spaneos.ga.tt.ext.TimeTableInputExt;
import com.spaneos.ga.tt.ext.domain.ConstraintInfo;
import com.spaneos.ga.tt.ext.domain.Department;

public interface TzenService {
	
		public CollegeDto createCollege(CollegeDto collegeDto);
		public CollegeDto getCollege(String cid);
		public List<Teacher> getFacultyList(String cid);
		
		
		
		public List<LabInfo> getLabDetails(String filePath);



		public List<Teacher> getTeachers(String string);

		public List<StudentGroup> getStudentGroups(String string);

		public List<College> getColleges();

		public College deleteCollege(String cid);

		public Long addFaculty(String cid, Teacher teacher);
		
		public Long addFacultyList(String cid, List<Teacher> teachers);

		public Teacher getFaculty(String cid, String id);

		public boolean deleteFaculty(String cid,String id);

		public List<LabInfo> getLabInfoList(String cid);

		public Long addLabInfo(String cid, LabInfo labInfo);

		public Long addLabInfoList(String cid, List<LabInfo> labs);

		public LabInfo getLabInfo(String cid, String id);

		public boolean deleteLabInfo(String cid, String id);

		public List<WorkHours> addWorkHours(String cid, List<WorkHours> workHours);

		public List<WorkHours> getWorkHours(String cid);

		public Long addDept(String cid, Department dept);

		public CollegeWithDeptDTO getCollegeDto(String cid, String deptId);

		public CollegeWithDeptDTO updateAndGetCollegeDto(CollegeWithDeptDTO collegeDTO);

		public CollegeWithDeptDTO addStuGroups(String cid, String id, List<StudentGroup> studentGroups);

		public CollegeWithDeptDTO updateConstraints(String cid, String deptId, ConstraintsRequirement constraintsRequirement);

		List<ConstraintInfo> getConstraints();

		public TimeTableInputExt getTtInput(String cid, String deptId);

		public CollegeWithDeptDTO addStuGroups(String cid, String id, Department dept);

		void generateTimeTable(String cid, String deptId, TimeTableInputExt ttInput);
}
