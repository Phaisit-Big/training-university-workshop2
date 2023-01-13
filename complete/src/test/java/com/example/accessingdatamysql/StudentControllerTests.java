
package com.example.accessingdatamysql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.accessingdatamysql.service.repo.StudentRepository;
import com.example.accessingdatamysql.service.repo.entity.StudentEntity;

@AutoConfigureMockMvc
@SpringBootTest(classes=MainApplication.class)
public class StudentControllerTests {

	@Autowired
	private MockMvc mockMvc;


    @MockBean
    private StudentRepository studentRepository;



	@Test
	public void getAllStudents_shouldReturnAllUsers() throws Exception {

		List<StudentEntity> mockUserList = new ArrayList<StudentEntity>();
		mockUserList.add(new StudentEntity(123, "name123", 0));
		mockUserList.add(new StudentEntity(456, "name456", 0));
		when(studentRepository.findAll()).thenReturn(mockUserList);

		MvcResult result = this.mockMvc.perform(get("/students"))//.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		JSONArray jsonArray = new JSONArray(content);

		assertEquals(2, jsonArray.length());
		
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		assertEquals(123, jsonObject.getInt("id"));
		assertEquals("name123", jsonObject.getString("name"));

	}


	@Test
	public void addNewStudent_shouldReturnSaved() throws Exception {

		String dummyRequestBody = """
							{"name":"name123", "email":"name123@gmail.com"}""";

		StudentEntity dummyStudentEntity = new StudentEntity(123, "name123", 1);                                         // 0: Expired, 1: Active (Default)
		dummyStudentEntity.setEmail("name123@gmail.com");							
		when(studentRepository.save(any())).thenReturn(dummyStudentEntity);


		MvcResult result = this.mockMvc.perform(post("/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dummyRequestBody))//.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		JSONObject jsonObject = new JSONObject(content);
		assertTrue(jsonObject.getString("description").matches(".+ ID: [0-9]+!"));

		JSONObject jsonUser = jsonObject.getJSONObject("student");
		assertEquals(123, jsonUser.getInt("id"));
		assertEquals("name123", jsonUser.getString("name"));
		assertEquals("name123@gmail.com", jsonUser.getString("email"));

	}


	@Test
	public void addNewStudentWithoutEmail_shouldReturnNullEmail() throws Exception {

		String jsonBody = """
							{"name":"name123"}""";
		
		when(studentRepository.save(any())).thenReturn(new StudentEntity(123, "name123", 0));


		MvcResult result = this.mockMvc.perform(post("/students")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonBody))//.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		JSONObject jsonObject = new JSONObject(content);
		assertTrue(jsonObject.getString("description").matches(".+ ID: [0-9]+!"));

		JSONObject jsonUser = jsonObject.getJSONObject("student");
		assertEquals(123, jsonUser.getInt("id"));
		assertEquals("name123", jsonUser.getString("name"));
		assertTrue(jsonUser.isNull("email"));


	}
}
