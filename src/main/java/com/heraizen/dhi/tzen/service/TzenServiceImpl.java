package com.heraizen.dhi.tzen.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.heraizen.dhi.tzen.domain.College;
import com.heraizen.dhi.tzen.domain.TimeTableOutputWrapper;
import com.heraizen.dhi.tzen.domain.WorkHours;
import com.heraizen.dhi.tzen.dto.CollegeDTO;
import com.heraizen.dhi.tzen.parser.FacultyTemplateParser;
import com.heraizen.dhi.tzen.parser.LabTemplateParser;
import com.heraizen.dhi.tzen.parser.StudentGroupTemplateParser;
import com.heraizen.dhi.tzen.repo.CollegeDao;
import com.heraizen.dhi.tzen.repo.CollegeRepo;
import com.heraizen.dhi.tzen.repo.TimeTableWrapperRepo;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.ext.ConstraintsContainer;
import com.spaneos.ga.tt.ext.ConstraintsContainer.ConstraintLoadException;
import com.spaneos.ga.tt.ext.ConstraintsRequirement;
import com.spaneos.ga.tt.ext.TimeTableExtSchedulerFactory;
import com.spaneos.ga.tt.ext.TimeTableInputExt;
import com.spaneos.ga.tt.ext.domain.ConstraintInfo;
import com.spaneos.ga.tt.ext.domain.Department;

@Service
public class TzenServiceImpl implements TzenService {

	@Autowired
	private LabTemplateParser labTemplateParser;
	@Autowired
	private FacultyTemplateParser facultyTemplateParser;
	@Autowired
	private StudentGroupTemplateParser studentGroupTemplateParser;
	@Autowired
	private TimeTableWrapperRepo timeTableWrapperRepo;
	@Autowired
	private CollegeRepo collegeRepo;
	@Autowired
	private CollegeDao collegeDao;
	
	
	
	@Override
	public List<ConstraintInfo> getConstraints(){
		   return  ConstraintsContainer.getInstance().getAvailableConstraintsInfo();
		
	}

	@Override
	public List<LabInfo> getLabDetails(String filePath) {
		List<LabInfo> labList = labTemplateParser.parse(filePath);
		System.out.println("Lab details:" + labList);
		return labList;
	}

	@Override
	public College getCollege(String cid) {
		College college = collegeRepo.findById(cid.trim()).orElseGet(null);
		return college;
	}

	@Override
	public College addCollege(College college) {
		try {
			return collegeRepo.save(college);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public List<Teacher> getTeachers(String filePath) {
		List<Teacher> facultyList = facultyTemplateParser.parse(filePath);
		return facultyList;
	}

	@Override
	public Teacher getFaculty(String cid, String id) {
		Teacher teacher = collegeDao.getFaculty(cid, id);
		return teacher;
	}

	@Override
	public List<Teacher> getFacultyList(String cid) {
		List<Teacher> teachers = collegeDao.getFacultyList(cid);
		return teachers;
	}

	@Override
	public boolean deleteFaculty(String cid, String id) {
		boolean isDeleted = collegeDao.deleteFaculty(cid, id);
		return isDeleted;
	}

	@Override
	public List<LabInfo> getLabInfoList(String cid) {
		List<LabInfo> labsInfo = collegeDao.getLabInfoList(cid);
		return labsInfo;
	}

	@Override
	public Long addLabInfo(String cid, LabInfo labInfo) {
		Long count = collegeDao.addLabInfo(cid, labInfo);
		return count;
	}

	@Override
	public Long addLabInfoList(String cid, List<LabInfo> labs) {
		Long labsCount = collegeDao.addLabInfoList(cid, labs);
		return labsCount;
	}

	@Override
	public LabInfo getLabInfo(String cid, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteLabInfo(String cid, String id) {
		boolean res = collegeDao.deleteLabInfo(cid, id);
		return res;
	}

	@Override
	public List<StudentGroup> getStudentGroups(String filePath) {
		List<StudentGroup> studentGroups = studentGroupTemplateParser.processStudentGroup(filePath);
		return studentGroups;
	}

	@Override
	public List<College> getColleges() {
		List<College> colleges = collegeRepo.findAll();
//		log.info("Total colleges found :{}"+colleges.size());
		return colleges;
	}

	@Override
	public College deleteCollege(String cid) {
		Optional<College> optCollege=collegeRepo.findById(cid);
		College c = null;
		if(optCollege.isPresent()) {
			 c = optCollege.get();
			c.setDepartments(new ArrayList<Department>());
			c.setLabsInfo(new ArrayList<>());
			c.setShortName("");
			c.setTeachersInfo(new ArrayList<>());
			c.setWorkHrs(new ArrayList<>());
			c.setCode("");
			c = collegeRepo.save(c); 
			
		}
		return c;
	}

	@Override
	public Long addFaculty(String cid, Teacher teacher) {

		Long count = collegeDao.addTeacher(cid, teacher);
		return count;
	}

	@Override
	public Long addFacultyList(String cid, List<Teacher> teachers) {
		teachers.forEach(e -> {
			String uid = UUID.randomUUID().toString();
			e.setId(uid);
		});
		Long count = collegeDao.addTeachers(cid, teachers);

		return count;

	}

	@Override
	public List<WorkHours> addWorkHours(String cid, List<WorkHours> workHours) {
		List<WorkHours> workHrsList = collegeDao.addWorkHours(cid, workHours);
		return workHrsList;
	}

	@Override
	public List<WorkHours> getWorkHours(String cid) {
		List<WorkHours> workHrsList = collegeDao.getWorkHours(cid);
		return workHrsList;
	}

	@Override
	public Long addDept(String cid, Department dept) {
		Long count = collegeDao.addDept(cid, dept);
		return count;
	}

	@Override
	public CollegeDTO getCollegeDto(String cid, String deptId) {
		CollegeDTO collegeDto = collegeDao.getCollegeDto(cid, deptId);
		return collegeDto;
	}

	@Override
	public CollegeDTO updateAndGetCollegeDto(CollegeDTO collegeDTO) {
		CollegeDTO collegeDto = collegeDao.updateAndGetCollegeDto(collegeDTO);
		return collegeDto;
	}

	@Override
	public CollegeDTO addStuGroups(String cid, String id, List<StudentGroup> studentGroups) {
		CollegeDTO collegeDto = collegeDao.addStuGroups(cid,id,studentGroups);
		return collegeDto;
	}

	@Override
	public CollegeDTO updateConstraints(String cid, String deptId, ConstraintsRequirement constraintsRequirement) {
		CollegeDTO collegeDto = collegeDao.updateConstraints(cid,deptId,constraintsRequirement);
		return collegeDto;
	}
	
	public int getFromIdx(WorkHours wh , int i) {
		
		int retVal=0;
		switch (i) {
			case 0: retVal = wh.getMon();break;
			case 1: retVal = wh.getTue();break;
			case 2: retVal = wh.getWed();break;
			case 3: retVal = wh.getThu();break;
			case 4: retVal = wh.getFri();break;
			case 5: retVal = wh.getSat();break;
			case 6: retVal = wh.getSun();break;
		}
		return retVal;
	}

	@Override
	public TimeTableInputExt getTtInput(String cid, String deptId) {
		CollegeDTO collegeDto = collegeDao.getCollegeDto(cid, deptId);
		
		TimeTableInputExt ttExt = new TimeTableInputExt();
		Department department = collegeDto.getDepartment();
		List<WorkHours> workHours = collegeDto.getWorkHrs();
		
		int workHrs[][] = new int[7][workHours.size()];
		
		for(int i=0;i<7;i++) {
			for ( int j=0; j<workHours.size();j++) {
				workHrs[i][j] = getFromIdx(workHours.get(j), i);
			}
		}
		ttExt.setWorkHrs(workHrs);
		ttExt.setConstraints(department.getConstraints());
		ttExt.setTeachersInfo(collegeDto.getTeachersInfo());
		ttExt.setStudentGroups(department.getStudentGrpList());
		ttExt.setLabsInfo(collegeDto.getLabsInfo());

		return ttExt;
	}

	@Override
	public CollegeDTO addStuGroups(final String cid, String id, Department dept) {
		CollegeDTO college =  collegeDao.addStuGroups(cid,id,dept);
		return college;
	}

	@Override
	public void generateTimeTable(final String cid,String deptId,TimeTableInputExt ttInput) {
		try {
			new TimeTableExtSchedulerFactory().withTimeTableInput(ttInput).buildScheduler().scheduleAsync().thenApply(e->{
				TimeTableOutputWrapper obj = timeTableWrapperRepo.findByCidAndDeptId(cid, deptId);
				if (obj!=null) {
					obj.setTimeTableOutput(e);
					timeTableWrapperRepo.save(obj);
				}else {
					TimeTableOutputWrapper ttow = new TimeTableOutputWrapper();
					ttow.setCid(cid);
					ttow.setDeptId(deptId);
					ttow.setTimeTableOutput(e);
					timeTableWrapperRepo.save(ttow);
				}
				
				return true;
			});
		} catch (ConstraintLoadException e) {
			e.printStackTrace();
		}
		
	}

}
