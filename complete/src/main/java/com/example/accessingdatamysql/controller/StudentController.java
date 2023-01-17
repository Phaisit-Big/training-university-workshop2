package com.example.accessingdatamysql.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.accessingdatamysql.model.Student;
import com.example.accessingdatamysql.service.StudentService;

@Controller
public class StudentController {


	@Autowired    
	private StudentService studentService;

	@Autowired
	@Qualifier(value="messageSource1")   
	private MessageSource messageSource;


	/*
	 * @return list all students in a JSONArray with JSONObjects transfermed from Student models
	 */
	@RequestMapping(path="/students", method=RequestMethod.GET) 
	public @ResponseBody ResponseEntity<Object> getAllStudents() {

		Iterable<Student> resultList = studentService.findAll();

		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}

	/*
	 * @param student - the Student model with name and optional email  
	 * @return a JSONObject with description "SAVED ID: <student id>!" and the saved Student model.
	 *         The Student model contains four fields as follows:
	 * <ul>
	 *   <li>id - student id</li>
	 *   <li>isActive - true as a default value for new student</li>
	 *   <li>name - parametered name</li>
	 *   <li>email - parametered email</li>
	 * </ul>
	 */
	@RequestMapping(path="/students", method=RequestMethod.POST) 
	public @ResponseBody ResponseEntity<Object> addNewStudent(@RequestHeader HttpHeaders headers,
																//@RequestParam(name="lang", defaultValue="th") String lang,
																@RequestBody Student student) {

		Student resultStudent = studentService.save(student);
	
		List<Locale> locales = headers.getAcceptLanguageAsLocales();
		Locale locale = new Locale("th", "TH"); 						// LocaleContextHolder.getLocale(); 
		if (null != locales && 0 < locales.size()) {
			locale = locales.get(0);
		}
		//Locale locale = new Locale(lang);

		String resultMessage = messageSource.getMessage("users.add.rs", new Object[] {resultStudent.getId()}, locale);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("description", resultMessage);
		resultMap.put("student", resultStudent);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(resultMap);
	}


	/*
	 * @return the number of students
	 */
	@RequestMapping(path="/students/count", method=RequestMethod.GET) 
	public @ResponseBody ResponseEntity<Object> countStudents(@RequestParam(name="isActive", defaultValue="true") boolean isActive) {

		int count = studentService.countStudents(isActive);

		return ResponseEntity.status(HttpStatus.OK).body(count);
	}
}
