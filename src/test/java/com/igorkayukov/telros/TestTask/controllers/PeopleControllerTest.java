package com.igorkayukov.telros.TestTask.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.igorkayukov.telros.TestTask.dto.PersonContactInfoDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDetailedInfoDTO;
import com.igorkayukov.telros.TestTask.models.Person;
import com.igorkayukov.telros.TestTask.services.PeopleService;
import com.igorkayukov.telros.TestTask.validators.PersonContactInfoDTOValidator;

@ExtendWith(MockitoExtension.class)
public class PeopleControllerTest {

	@Mock
	private PeopleService peopleService;
	@Mock
	private PersonContactInfoDTOValidator personContactInfoDTOValidator;

	@InjectMocks
	private PeopleController peopleController;

	private MockMvc mockMvc;

	@BeforeEach
	private void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(peopleController).build();
	}

	// ADD TESTS

	@Test
	public void addPerson_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO("abc", "abc@abc", "123");

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(post("/people/add")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void addPerson_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO();

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(post("/people/add")
		.contentType(MediaType.APPLICATION_JSON)
		.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void addPersonDetailedInfo_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO("abc", "abc", "2000-01-01");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(post("/people/add/detailed_info/1")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void addPersonDetailedInfo_returnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO();

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(post("/people/add/detailed_info/1")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void addPersonDetailedInfo_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO();

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(post("/people/add/detailed_info/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void addPersonPhoto_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[1]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/add/photo/1")
        .file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void addPersonPhoto_returnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[1]);

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/add/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void addPersonPhoto_returnsErrorResponse_whenFileSizeIsZero() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[0]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/add/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void addPersonPhoto_returnsErrorResponse_whenFileHaveWrongContentType() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "", new byte[1]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/add/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	// GET TESTS

	@Test
	public void getPersonContactInfo_renturnsJson_whenNoErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO("abc", "abc@abc", "123");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());
		Mockito.when(peopleService.convertToPersonContactInfoDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		 MvcResult result = mockMvc.perform(post("/people/get/contact_info/1"))
        .andExpect(status().isOk())
        .andReturn();
		 //@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		PersonContactInfoDTO response = new ObjectMapper().readValue(responseBody, PersonContactInfoDTO.class);
		assertEquals(dto.toString(), response.toString());
	}

	@Test
	public void getPersonContactInfo_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(post("/people/get/contact_info/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void getPersonPhoto_renturnsFile_whenNoErrors() throws Exception {

		Person person = new Person();
		person.setPhoto("Test photo data".getBytes());
		person.setPhotoName("test.jpg");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(person);

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.post("/people/get/photo/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"test.jpg\""))
            .andExpect(MockMvcResultMatchers.content().bytes(person.getPhoto()));
		//@formatter:on
	}

	@Test
	public void getPersonPhoto_renturnsErrorResponse_whenNoPhoto() throws Exception {

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(post("/people/get/photo/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void getPersonPhoto_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(post("/people/get/photo/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void getPersonDetailedInfo_renturnsJson_whenNoErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO("abc", "abc", "2000-01-01");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());
		Mockito.when(peopleService.convertToPersonDetailedInfoDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		 MvcResult result = mockMvc.perform(post("/people/get/detailed_info/1"))
        .andExpect(status().isOk())
        .andReturn();
		 //@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		PersonDetailedInfoDTO response = new ObjectMapper().readValue(responseBody, PersonDetailedInfoDTO.class);
		assertEquals(dto.toString(), response.toString());
	}

	@Test
	public void getPersonDetailedInfo_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(post("/people/get/detailed_info/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void getPerson_renturnsJson_whenNoErrors() throws Exception {

		PersonDTO dto = new PersonDTO(1, "abc", "abc", "abc", Date.from(Instant.now()), "abc", "abc");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());
		Mockito.when(peopleService.convertToPersonDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		 MvcResult result = mockMvc.perform(post("/people/get/1"))
        .andExpect(status().isOk())
        .andReturn();
		 //@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		PersonDTO response = new ObjectMapper().readValue(responseBody, PersonDTO.class);
		assertEquals(dto.toString(), response.toString());
	}

	@Test
	public void getPerson_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(post("/people/get/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void getPeople_renturnsJson_whenNoErrors() throws Exception {

		PersonDTO dto = new PersonDTO(1, "abc", "abc", "abc", Date.from(Instant.now()), "abc", "abc");
		List<PersonDTO> expectedList = Arrays.asList(dto);

		Mockito.when(peopleService.findAll()).thenReturn(List.of(new Person()));
		Mockito.when(peopleService.convertToPersonDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		 MvcResult result = mockMvc.perform(post("/people/get"))
        .andExpect(status().isOk())
        .andReturn();
		 //@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		List<PersonDTO> actualList = new ObjectMapper().readValue(responseBody, new TypeReference<List<PersonDTO>>() {
		});
		assertEquals(expectedList.get(0).toString(), actualList.get(0).toString());
	}

	@Test
	public void getPeopleContactInfo_renturnsJson_whenNoErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO("abc", "abc@abc", "123");
		List<PersonContactInfoDTO> expectedList = Arrays.asList(dto);

		Mockito.when(peopleService.findAll()).thenReturn(List.of(new Person()));
		Mockito.when(peopleService.convertToPersonContactInfoDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		MvcResult result = mockMvc.perform(post("/people/get/contact_info"))
			.andExpect(status().isOk())
			.andReturn();
		//@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		List<PersonContactInfoDTO> actualList = new ObjectMapper().readValue(responseBody,
			new TypeReference<List<PersonContactInfoDTO>>() {
			});
		assertEquals(expectedList.get(0).toString(), actualList.get(0).toString());
	}

	@Test
	public void getPeopleDetailedInfo_renturnsJson_whenNoErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO("abc", "abc", "2000-01-01");
		List<PersonDetailedInfoDTO> expectedList = Arrays.asList(dto);

		Mockito.when(peopleService.findAll()).thenReturn(List.of(new Person()));
		Mockito.when(peopleService.convertToPersonDetailedInfoDTO(any(Person.class))).thenReturn(dto);

		//@formatter:off 
		MvcResult result = mockMvc.perform(post("/people/get/detailed_info"))
			.andExpect(status().isOk())
			.andReturn();
		//@formatter:on

		String responseBody = result.getResponse().getContentAsString();
		List<PersonDetailedInfoDTO> actualList = new ObjectMapper().readValue(responseBody,
			new TypeReference<List<PersonDetailedInfoDTO>>() {
			});
		assertEquals(expectedList.get(0).toString(), actualList.get(0).toString());
	}

	// UPDATE TESTS

	@Test
	public void updatePerson_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO("abc", "abc@abc", "123");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/1")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void updatePerson_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO();

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/1")
		.contentType(MediaType.APPLICATION_JSON)
		.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePerson_returnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		PersonContactInfoDTO dto = new PersonContactInfoDTO();

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePersonPhoto_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[1]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/update/photo/1")
        .file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void updatePersonPhoto_returnsErrorResponse_whenFileSizeIsZero() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[0]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/update/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePersonPhoto_returnsErrorResponse_whenFileHaveWrongContentType() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "", new byte[1]);

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/update/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePersonPhoto_returnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		MockMultipartFile file = new MockMultipartFile("file", "abc.png", "image/png", new byte[1]);

		//@formatter:off 
		mockMvc.perform(MockMvcRequestBuilders.multipart("/people/update/photo/1")
		.file(file))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePersonDetailedInfo_returnsOk_whenBindingResultHasNoErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO("abc", "abc", "2000-01-01");

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/detailed_info/1")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void updatePersonDetailedInfo_returnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO();

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/detailed_info/1")
		.contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void updatePersonDetailedInfo_returnsErrorResponse_whenBindingResultHasErrors() throws Exception {

		PersonDetailedInfoDTO dto = new PersonDetailedInfoDTO();

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		String json = new ObjectMapper().writeValueAsString(dto);

		//@formatter:off 
		mockMvc.perform(patch("/people/update/detailed_info/1")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void deletePersonDetailedInfo_returnsOk_whenNoErrors() throws Exception {

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/detailed_info/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void deletePersonDetailedInfo_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/detailed_info/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void deletePerson_returnsOk_whenNoErrors() throws Exception {

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void deletePerson_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

	@Test
	public void deletePersonPhoto_returnsOk_whenNoErrors() throws Exception {

		Mockito.when(peopleService.findOne(anyInt())).thenReturn(new Person());

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/photo/1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.errors").doesNotExist());
		//@formatter:on
	}

	@Test
	public void deletePersonPhoto_renturnsErrorResponse_whenNoPersonWithGivenId() throws Exception {

		//@formatter:off 
		mockMvc.perform(delete("/people/delete/photo/1"))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").exists())
		.andExpect(jsonPath("$.timestamp").exists());
		//@formatter:on
	}

}
