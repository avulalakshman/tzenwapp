package com.heraizen.dhi.tzen.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.heraizen.dhi.tzen.domain.College;
import com.heraizen.dhi.tzen.domain.Message;
import com.heraizen.dhi.tzen.domain.TimeTableOutputWrapper;
import com.heraizen.dhi.tzen.domain.WorkHours;
import com.heraizen.dhi.tzen.dto.CollegeDTO;
import com.heraizen.dhi.tzen.service.FileUPloadService;
import com.heraizen.dhi.tzen.service.TimeTableOutputWrapperService;
import com.heraizen.dhi.tzen.service.TimeTableService;
import com.heraizen.dhi.tzen.service.TzenService;
import com.spaneos.ga.tt.domain.LabInfo;
import com.spaneos.ga.tt.domain.StudentGroup;
import com.spaneos.ga.tt.domain.Teacher;
import com.spaneos.ga.tt.domain.TimeTableOutput;
import com.spaneos.ga.tt.ext.ConstraintsRequirement;
import com.spaneos.ga.tt.ext.TimeTableInputExt;
import com.spaneos.ga.tt.ext.domain.ConstraintInfo;
import com.spaneos.ga.tt.ext.domain.Department;

@RestController
public class TzenController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TzenController.class);

	@Autowired
	private TzenService tzenService;
	@Autowired
	private FileUPloadService fileUPloadService;
	@Autowired
	private TimeTableService timeTableService;

	@Autowired
	private TimeTableOutputWrapperService timeTableOutputWrapperService;

	@PostMapping("/college")
	public College addCollecge(@RequestBody College college) {
		LOGGER.info("College with name is going to add:{}", college.getName());
		college = tzenService.addCollege(college);
		LOGGER.info("College is added with Id:{} and with Name:{}", college.getCid(), college.getName());
		return college;
	}

	@GetMapping("/college/collegedto/{cid}/{deptId}")
	public CollegeDTO getCollege(@PathVariable String cid, @PathVariable String deptId) {
		System.out.println(cid + " " + deptId);
		CollegeDTO collegeDto = tzenService.getCollegeDto(cid, deptId);
		return collegeDto;
	}

	@GetMapping("/college/{cid}/staff")
	public List<Teacher> getTeachersList(@PathVariable String cid) {
		List<Teacher> teachers = tzenService.getFacultyList(cid);
		return teachers;
	}

	@PostMapping("/college/{cid}/staff/addstaff")
	public ResponseEntity<Message> addFaculty(@RequestBody Teacher teacher, @PathVariable String cid) {
		LOGGER.info("Staff with name {} is going to add to :{}", teacher.getName(), cid);
		Long count = tzenService.addFaculty(cid, teacher);
		return new ResponseEntity<>(new Message("Total faculty added : " + count), HttpStatus.OK);
	}

	@PostMapping("/college/{cid}/staff/addstafflist")
	public ResponseEntity<Message> addFacultyList(@RequestBody List<Teacher> teachers, @PathVariable String cid) {
		LOGGER.info("Staff with size {} is going to add to :{}", teachers.size(), cid);
		Long count = tzenService.addFacultyList(cid, teachers);
		return new ResponseEntity<>(new Message("Total faculty add  : " + count), HttpStatus.OK);
	}

	@GetMapping("/college/{cid}/staff/getstaff/{id}")
	public ResponseEntity<Teacher> getFaculty(@PathVariable String cid, @PathVariable String id) {
		LOGGER.info("Staff with Id {} is going to get for college :{}", cid, id);
		Teacher faculty = tzenService.getFaculty(cid, id);
		return new ResponseEntity<>(faculty, HttpStatus.OK);
	}

	@DeleteMapping("/college/{cid}/staff/deletestaff/{id}")
	public ResponseEntity<Message> deleteFaculty(@PathVariable String cid, @PathVariable String id) {
		LOGGER.info("Staff with id {} is going to delete for college id:{} ", cid, id);
		boolean isDeleted = tzenService.deleteFaculty(cid, id);
		return new ResponseEntity<Message>(new Message(isDeleted ? "Faculty deleted" : "Faculty couldn't delete"),
				HttpStatus.OK);
	}

	@GetMapping("/college/{cid}/lab")
	public List<LabInfo> getLabList(@PathVariable String cid) {
		List<LabInfo> labs = tzenService.getLabInfoList(cid);
		return labs;
	}

	@PostMapping("/college/{cid}/lab/addlab")
	public ResponseEntity<Message> addLab(@RequestBody LabInfo labInfo, @PathVariable String cid) {
		LOGGER.info("Lab with name {} is going to add to :{}", labInfo.getLabRoomName(), cid); // Needs to complete
		Long count = tzenService.addLabInfo(cid, labInfo);
		return new ResponseEntity<>(new Message("Total Lab(s) added : " + count), HttpStatus.OK);
	}

	@PostMapping("/college/{cid}/lab/addlablist")
	public ResponseEntity<Message> addLabList(@RequestBody List<LabInfo> labs, @PathVariable String cid) {
		LOGGER.info("Labs with size {} is going to add to :{}", labs.size(), cid);
		Long count = tzenService.addLabInfoList(cid, labs);
		return new ResponseEntity<>(new Message("Total faculty add  : " + count), HttpStatus.OK);
	}

	@GetMapping("/college/{cid}/lab/getlab/{id}")
	public ResponseEntity<LabInfo> getLab(@PathVariable String cid, @PathVariable String id) {
		LOGGER.info("Lab with Id {} is going to get for college :{}", cid, id);
		LabInfo labInfo = tzenService.getLabInfo(cid, id);
		return new ResponseEntity<>(labInfo, HttpStatus.OK);
	}

	@DeleteMapping("/college/{cid}/lab/deletelab/{id}")
	public ResponseEntity<Message> deleteLabInfo(@PathVariable String cid, @PathVariable String id) {
		LOGGER.info("Lab with id {} is going to delete for college id:{} ", cid, id);
		boolean isDeleted = tzenService.deleteLabInfo(cid, id);
		return new ResponseEntity<Message>(new Message(isDeleted ? "Faculty deleted" : "Faculty couldn't delete"),
				HttpStatus.OK);
	}

	// Department

	@PostMapping("/college/{cid}/dept/adddept")
	public ResponseEntity<Message> addDept(@RequestBody Department dept, @PathVariable String cid) {
		LOGGER.info("Dept with name {} is going to add to :{}", dept.getDeptName(), cid); // Needs to complete
		Long count = tzenService.addDept(cid, dept);
		return new ResponseEntity<>(new Message("Total Dept(s) added : " + count), HttpStatus.OK);
	}

	@PostMapping("/college/{cid}/timing")
	public ResponseEntity<List<WorkHours>> addFaculty(@RequestBody List<WorkHours> workHours,
			@PathVariable String cid) {
		List<WorkHours> workHrsList = tzenService.addWorkHours(cid, workHours);
		return new ResponseEntity<>(workHrsList, HttpStatus.OK);
	}

	@GetMapping("/college/{cid}/timing")
	public ResponseEntity<List<WorkHours>> getTiming(@PathVariable String cid) {
		List<WorkHours> workHrsList = tzenService.getWorkHours(cid);
		return new ResponseEntity<>(workHrsList, HttpStatus.OK);
	}

	@GetMapping("/timetableoutput")
	public TimeTableOutput timeTableOutput() {
		TimeTableOutput timeTableOutput = timeTableService.getTimeTableOutput();
		return timeTableOutput;

	}

	@PostMapping("/addtimetableoutput")
	public TimeTableOutput saveTimeTableOutput(@RequestBody TimeTableOutput timeTableOutput) {
		System.out.println("Timetableoutput: " + timeTableOutput);
		System.out.println(timeTableOutputWrapperService);
		TimeTableOutputWrapper tw = timeTableOutputWrapperService.addTimeTableOutput(timeTableOutput);
		System.out.println(tw);
//		return tw.getTimeTableOutput();
		return null;
	}

	@DeleteMapping("/deletecollege/{cid}")
	public String deleteCollege(@PathVariable String cid) {
		LOGGER.info("College with id :{}  going to delete", cid);
		cid = tzenService.deleteCollege(cid);
		LOGGER.info("College with id :{} deleted sucessfully", cid);
		return cid;
	}

	@GetMapping("/allcolleges")
	public List<College> getColleges() {
		return tzenService.getColleges();
	}

	@RequestMapping(value = "/college/{cid}/uploadlabinfo", method = RequestMethod.POST)
	public List<LabInfo> handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String cid) {
		Optional<String> isUploaded = fileUPloadService.uploadFile(file);
		List<LabInfo> labDetails = new ArrayList<LabInfo>();

		if (file.getName().endsWith(".json")) {
			labDetails = null; // TODO
		} else {
			if (isUploaded.isPresent()) {
				labDetails = tzenService.getLabDetails(isUploaded.get());
			}
		}
		return labDetails;
	}

	@RequestMapping(value = "/uploadteacherinfo", method = RequestMethod.POST)
	public List<Teacher> uploadTeacherInfo(@RequestParam("file") MultipartFile file) {
		Optional<String> isUploaded = fileUPloadService.uploadFile(file);
		List<Teacher> facultyList = new ArrayList<>();
		if (file.getName().endsWith(".json")) {
			facultyList = null; // TODO
		} else {
			if (isUploaded.isPresent()) {
				facultyList = tzenService.getTeachers(isUploaded.get());
			}
		}
		return facultyList;
	}

	@RequestMapping(value = "/college/uploadstudentgroup/{cid}/{id}", method = RequestMethod.POST)
	public CollegeDTO uploadStudentGroup(@RequestParam("file") MultipartFile file, @PathVariable String cid,
			@PathVariable String id) {
		Optional<String> isUploaded = fileUPloadService.uploadFile(file);
		List<StudentGroup> studentGroups = new ArrayList<>();

		if (file.getName().endsWith(".json")) {
			studentGroups = null; // TODO
		} else {
			if (isUploaded.isPresent()) {
				studentGroups = tzenService.getStudentGroups(isUploaded.get());
			}
		}
		CollegeDTO college = tzenService.addStuGroups(cid, id, studentGroups);

		return college;
	}

	@GetMapping("/getcollege/{cid}")
	public College getCollecgeHours(@PathVariable("cid") String cid) {
		College college = tzenService.getCollege(cid);
		return college;
	}

	@PostMapping("/college/updatestugroup")
	public CollegeDTO updateAndGet(@RequestBody CollegeDTO collegeDTO) {
		CollegeDTO retCollegeDTO = tzenService.updateAndGetCollegeDto(collegeDTO);
		return retCollegeDTO;
	}

	@PostMapping("/college/{cid}/{deptId}/updateconstraints")
	public CollegeDTO updateConstraints(@PathVariable("cid") String cid, @PathVariable("deptId") String deptId,
			@RequestBody ConstraintsRequirement constraintsRequirement) {
		CollegeDTO collegeDTO = tzenService.updateConstraints(cid, deptId, constraintsRequirement);
		System.out.println(constraintsRequirement.getSubjPrefs());
		System.out.println(collegeDTO);
		return collegeDTO;
	}

	@GetMapping("/college/constraints")
	public List<ConstraintInfo> getConstraints() {
		return tzenService.getConstraints();

	}

	@GetMapping("/college/ttinput/{cid}/{deptId}")
	public TimeTableInputExt getTtInput(@PathVariable String cid, @PathVariable String deptId) {

		TimeTableInputExt ttExt = tzenService.getTtInput(cid, deptId);

		return ttExt;
	}

}
