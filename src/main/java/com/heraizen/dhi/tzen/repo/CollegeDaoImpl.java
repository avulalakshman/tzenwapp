package com.heraizen.dhi.tzen.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.heraizen.dhi.tzen.domain.College;
import com.heraizen.dhi.tzen.domain.WorkHours;
import com.heraizen.dhi.tzen.dto.CollegeWithDeptDTO;
import com.heraizen.dhi.tzen.service.exception.ResourceNotFound;
import com.mongodb.client.result.UpdateResult;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.ext.ConstraintsRequirement;
import com.spaneos.ga.tt.ext.domain.Department;

@Repository
public class CollegeDaoImpl implements CollegeDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CollegeRepo collegeRepo;

	
	private static final Logger LOG = LoggerFactory.getLogger(CollegeDaoImpl.class);
	
	@Override
	public Long addTeacher(String cid, Teacher teacher) {
		UpdateResult res = null;
		if (teacher.getId() == null) {
			res = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(cid)),
					new Update().push("teachersInfo", teacher), "college");
		} else {
			res = mongoTemplate.updateFirst(
					Query.query(Criteria.where("_id").is(cid))
							.addCriteria(Criteria.where("teachersInfo._id").is(teacher.getId())),
					new Update().set("teachersInfo.$", teacher), "college");
		}

		return res.getModifiedCount();
	}

	@Override
	public Long addTeachers(String cid, List<Teacher> teachers) {
		UpdateResult res = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(cid)),
				new Update().push("teachersInfo").each(teachers), "college");
		return res.getModifiedCount();
	}

	@Override
	public Teacher getFaculty(String cid, String id) {
		Teacher teacher = null;
		Optional<College> college = collegeRepo.findById(cid);
		if (college.isPresent()) {
			List<Teacher> teachers = college.get().getTeachersInfo();
			Optional<Teacher> optTeacher = teachers.stream().filter(t -> t.getId().equals(id)).findFirst();
			teacher = optTeacher.get();
		}
		return teacher;
	}

	@Override
	public boolean deleteFaculty(String cid, String id) {

		UpdateResult res = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(cid)),
				new Update().pull("teachersInfo", Query.query(Criteria.where("_id").is(id))), "college");

		long count = res.getMatchedCount();
		return count != 0;
	}

	@Override
	public List<Teacher> getFacultyList(String cid) {
		Optional<College> college = collegeRepo.findById(cid);
		List<Teacher> teachersList = null;
		if (college.isPresent()) {
			teachersList = college.get().getTeachersInfo();
		}
		return teachersList;
	}

	@Override
	public List<LabInfo> getLabInfoList(String cid) {
		Optional<College> college = collegeRepo.findById(cid);
		List<LabInfo> labsInfo = null;
		if (college.isPresent()) {
			labsInfo = college.get().getLabsInfo();
		}
		return labsInfo;
	}

	@Override
	public Long addLabInfo(String cid, LabInfo labInfo) {
		UpdateResult res = null;
		if (labInfo.getId() == null || labInfo.getId() == "") {
			String uid = UUID.randomUUID().toString();
			labInfo.setId(uid);
			res = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(cid)),
					new Update().push("labsInfo", labInfo), "college");
			System.out.println("New lab is going to add...");
		} else {
			res = mongoTemplate.updateFirst(
					Query.query(Criteria.where("_id").is(cid))
							.addCriteria(Criteria.where("labsInfo._id").is(labInfo.getId())),
					new Update().set("labsInfo.$", labInfo), "college");
		}

		return res.getModifiedCount();
	}

	@Override
	public Long addLabInfoList(String cid, List<LabInfo> labs) {
		labs.stream().forEach(e -> {
			if (e.getId() == null || e.getId() == "") {
				e.setId(UUID.randomUUID().toString());
			}
		});
		UpdateResult res = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(cid)),
				new Update().push("labsInfo").each(labs), "college");
		return res.getModifiedCount();
	}

	@Override
	public LabInfo getLabInfo(String cid, String id) {
		LabInfo labInfo = null;
		Optional<College> college = collegeRepo.findById(cid);
		if (college.isPresent()) {
			List<LabInfo> labsInfo = college.get().getLabsInfo();
			Optional<LabInfo> optLabInfo = labsInfo.stream().filter(t -> t.getId().equals(id)).findFirst();
			labInfo = optLabInfo.get();
		}
		return labInfo;
	}

	@Override
	public boolean deleteLabInfo(String cid, String id) {
		UpdateResult res = this.mongoTemplate.updateMulti(Query.query(Criteria.where("_id").is(cid.trim())),
				new Update().pull("labsInfo", Query.query(Criteria.where("_id").is(id))), "college");
		System.out.println(res);
		long count = res.getMatchedCount();
		return count != 0;
	}

	@Override
	public Long addDept(String cid, Department dept) {
		UpdateResult res = null;
		if (dept.getId() == null || dept.getId() == "") {
			String uid = UUID.randomUUID().toString();
			dept.setId(uid);
			res = mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(cid)),
					new Update().push("departments", dept), "college");
			System.out.println("New dept is going to add...");
		} else {
			res = mongoTemplate.updateFirst(
					Query.query(Criteria.where("_id").is(cid))
							.addCriteria(Criteria.where("departments._id").is(dept.getId())),
					new Update().set("departments.$", dept), "college");
		}
		return res.getMatchedCount();
	}

	@Override
	public List<WorkHours> addWorkHours(String cid, List<WorkHours> workHours) {
		Optional<College> optCollege = collegeRepo.findById(cid);
		College college = optCollege.orElseGet(null);
		college.setWorkHrs(workHours);
		college = collegeRepo.save(college);
		return college.getWorkHrs();
	}

	@Override
	public List<WorkHours> getWorkHours(String cid) {
		Optional<College> optCollege = collegeRepo.findById(cid);
		College college = optCollege.orElseGet(null);
		return college.getWorkHrs();
	}

	@Override
	public CollegeWithDeptDTO getCollegeDto(String cid, String deptId) {
		
				Optional<College> optCollege=collegeRepo.findById(cid);
				College college = optCollege.get();
				Department department = null;
				for(Department dept:college.getDepartments()) {
						if(dept.getId().equals(deptId)) {
							department = dept;
							break;
						}
				}
				System.out.println("Department "+department);
				
				CollegeWithDeptDTO collegeDTO = CollegeWithDeptDTO.builder().cid(college.getCid()).department(department).name(college.getName())
						.shortName(college.getShortName())
						.labsInfo(college.getLabsInfo())
						.teachersInfo(college.getTeachersInfo())
						.workHrs(college.getWorkHrs())
						.build();
				
			return collegeDTO;
				
	}

	@Override
	public CollegeWithDeptDTO updateAndGetCollegeDto(CollegeWithDeptDTO collegeDTO) {

		Optional<College> optCollege=collegeRepo.findById(collegeDTO.getCid());
		College college = optCollege.get();
		System.out.println(collegeDTO.getDepartment().getStudentGrpList());
		for(Department dept:college.getDepartments()) {
				if(dept.getId().equals(dept.getId())) {
					dept.setStudentGrpList(collegeDTO.getDepartment().getStudentGrpList());
					break;
				}
		}
		college = collegeRepo.save(college);

		CollegeWithDeptDTO retCollegeDTO = CollegeWithDeptDTO.builder().cid(college.getCid()).department(collegeDTO.getDepartment()).name(college.getName())
				.shortName(college.getShortName())
				.labsInfo(college.getLabsInfo())
				.teachersInfo(college.getTeachersInfo())
				.build();
		return retCollegeDTO;
	}

	@Override
	public CollegeWithDeptDTO addStuGroups(String cid, String deptId, List<StudentGroup> studentGroups) {
		
		
		
		Optional<College> optCollege=collegeRepo.findById(cid);
		College college = optCollege.get();
		Department department = null;
		for(Department dept:college.getDepartments()) {
				if(dept.getId().equals(deptId)) {
					
					if (dept.getStudentGrpList()==null ) {
						dept.setStudentGrpList(new ArrayList<>());
					}
					dept.getStudentGrpList().addAll(studentGroups);
					department = dept;
					break;
				}
		}
		college = collegeRepo.save(college);
		
		CollegeWithDeptDTO collegeDTO = CollegeWithDeptDTO.builder().cid(college.getCid()).department(department).name(college.getName())
				.shortName(college.getShortName())
				.labsInfo(college.getLabsInfo())
				.teachersInfo(college.getTeachersInfo())
				.build();
		
	return collegeDTO;
	}

	@Override
	public CollegeWithDeptDTO updateConstraints(String cid, String deptId, ConstraintsRequirement constraintsRequirement) {
		System.out.println("College id :"+cid+" department id :"+deptId);
		Optional<College> optCollege=collegeRepo.findById(cid);
		College college = optCollege.get();
		Department department = null;
		for(Department dept:college.getDepartments()) {
				if(dept.getId().equals(deptId)) {
					 	System.out.println("Department :"+deptId);
						dept.setConstraints(constraintsRequirement);
						department = dept;
						break;
				}
		}
		college = collegeRepo.save(college);
		CollegeWithDeptDTO collegeDTO = CollegeWithDeptDTO.builder().cid(college.getCid()).department(department).name(college.getName())
				.shortName(college.getShortName())
				.labsInfo(college.getLabsInfo())
				.teachersInfo(college.getTeachersInfo())
				.build();
		System.out.println(collegeDTO.getDepartment());
	return collegeDTO;
	}

	@Override
	public CollegeWithDeptDTO addStuGroups(String cid, String id, Department dept) {
		Optional<College> optCollege=collegeRepo.findById(cid);
		College college = optCollege.get();
		Department department = null;
		for(Department d:college.getDepartments()) {
				if(d.getId().equals(id)) {
					
					d.setStudentGrpList(dept.getStudentGrpList());
					d.setConstraints(dept.getConstraints());
					department = d;
					break;
				}
		}
		System.out.println("Department :"+department);
		System.out.println("Student Groups :"+department.getStudentGrpList());
		college = collegeRepo.save(college);
		CollegeWithDeptDTO collegeDTO = CollegeWithDeptDTO.builder().cid(college.getCid()).department(department).name(college.getName())
				.shortName(college.getShortName())
				.labsInfo(college.getLabsInfo())
				.teachersInfo(college.getTeachersInfo())
				.workHrs(college.getWorkHrs())
				.build();
		
	     return collegeDTO;
	}

	@Override
	public CollegeWithDeptDTO getCollegeDeptDTO(String cid,String deptId) {
		 AggregationOperation unwind = Aggregation.unwind("departments");
		 AggregationOperation match = Aggregation.match(Criteria.where("_id").is(cid).and("departments.id").is(deptId));
		 
		 Aggregation aggregation = Aggregation.newAggregation(unwind,match);
		
		 AggregationResults<CollegeWithDeptDTO> cdDto = this.mongoTemplate.aggregate(aggregation, College.class, CollegeWithDeptDTO.class);
		 List<CollegeWithDeptDTO> mappedResult = cdDto.getMappedResults();
		 if(mappedResult!=null && mappedResult.size()>0) {
			 return mappedResult.get(0);
		 }

		 throw new ResourceNotFound("There is no deparment for the given input" );
	}
	
	

}
