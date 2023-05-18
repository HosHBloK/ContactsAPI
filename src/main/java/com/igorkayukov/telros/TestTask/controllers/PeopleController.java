package com.igorkayukov.telros.TestTask.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.igorkayukov.telros.TestTask.dto.PersonContactInfoDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDTO;
import com.igorkayukov.telros.TestTask.dto.PersonDetailedInfoDTO;
import com.igorkayukov.telros.TestTask.exceptions.NotValidPersonContactInfoDTOException;
import com.igorkayukov.telros.TestTask.exceptions.NotValidPersonDetailedInfoDTOException;
import com.igorkayukov.telros.TestTask.exceptions.NotValidPersonPhotoException;
import com.igorkayukov.telros.TestTask.exceptions.PersonNotFoundException;
import com.igorkayukov.telros.TestTask.exceptions.PersonPhotoNotFoundException;
import com.igorkayukov.telros.TestTask.services.PeopleService;
import com.igorkayukov.telros.TestTask.util.ErrorMessage;
import com.igorkayukov.telros.TestTask.util.ErrorResponse;
import com.igorkayukov.telros.TestTask.validators.PersonContactInfoDTOValidator;

@RestController
@RequestMapping("/people")
public class PeopleController {

	private final PeopleService peopleService;
	private final PersonContactInfoDTOValidator personContactInfoDTOValidator;

	@Autowired
	public PeopleController(PeopleService peopleService, PersonContactInfoDTOValidator personContactInfoDTOValidator) {
		this.peopleService = peopleService;
		this.personContactInfoDTOValidator = personContactInfoDTOValidator;
	}
	
	

	// CREATE METHODS

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> addPerson(@RequestBody @Valid PersonContactInfoDTO personContactInfoDTO,
		BindingResult bindingResult) {

		personContactInfoDTOValidator.validate(personContactInfoDTO, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonContactInfoDTOException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.save(peopleService.convertToPerson(personContactInfoDTO));

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/add/detailed_info/{id}")
	public ResponseEntity<HttpStatus> addPersonDetailedInfo(
		@RequestBody @Valid PersonDetailedInfoDTO personDetailedInfoDTO, BindingResult bindingResult,
		@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonDetailedInfoDTOException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateDetailedInfo(id, peopleService.convertToPerson(personDetailedInfoDTO));

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping(value = "/add/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<HttpStatus> addPersonPhoto(@RequestPart("file") MultipartFile file,
		@PathVariable("id") int id) throws IOException {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (file.getSize() == 0) {
			throw new NotValidPersonPhotoException("Photo file should not be empty!");
		}

		if (file.getSize() > 5 * 1024 * 1024) {
			throw new NotValidPersonPhotoException("File size should not exceed 5MB!");
		}

		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
			throw new NotValidPersonPhotoException("Only .jpeg and .png files are allowed!");
		}

		peopleService.addPersonPhoto(file.getBytes(), file.getOriginalFilename(), id);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	
	
	// READ METHODS

	@PostMapping("/get/contact_info/{id}")
	public PersonContactInfoDTO getPersonContactInfo(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		return peopleService.convertToPersonContactInfoDTO(peopleService.findOne(id));
	}

	@PostMapping("/get/detailed_info/{id}")
	public PersonDetailedInfoDTO getPersonDetailedInfo(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		return peopleService.convertToPersonDetailedInfoDTO(peopleService.findOne(id));
	}

	@PostMapping("/get/photo/{id}")
	public ResponseEntity<byte[]> getPersonPhoto(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (peopleService.findOne(id).getPhotoName() == null) {
			throw new PersonPhotoNotFoundException("Photo of person with this id is not found!");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", peopleService.findOne(id).getPhotoName());

		return ResponseEntity.ok().headers(headers).body(peopleService.findOne(id).getPhoto());
	}

	@PostMapping("/get/{id}")
	public PersonDTO getPerson(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}
		
		return peopleService.convertToPersonDTO(peopleService.findOne(id));
	}

	@PostMapping("/get/detailed_info")
	public List<PersonDetailedInfoDTO> getPeopleDetailedInfo() {

		return peopleService.findAll().stream().map(peopleService::convertToPersonDetailedInfoDTO).collect(Collectors
			.toList());
	}

	@PostMapping("/get/contact_info")
	public List<PersonContactInfoDTO> getPeopleContactInfo() {

		return peopleService.findAll().stream().map(peopleService::convertToPersonContactInfoDTO).collect(Collectors
			.toList());
	}

	@PostMapping("/get")
	public List<PersonDTO> getPeople() {

		return peopleService.findAll().stream().map(peopleService::convertToPersonDTO).collect(Collectors.toList());
	}
	
	

	// UPDATE METHODS

	@PatchMapping("/update/{id}")
	public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid PersonContactInfoDTO personContactInfoDTO,
		BindingResult bindingResult, @PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		personContactInfoDTOValidator.validate(personContactInfoDTO, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonContactInfoDTOException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateContactInfo(id, peopleService.convertToPerson(personContactInfoDTO));

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/* 
	 * At the moment this method is the same as "add", which could also be used instead of an update request,
	 * but if in the future you need to change the update logic, you won't have to change the request
	 * on the client.
	 */

	@PatchMapping("/update/detailed_info/{id}")
	public ResponseEntity<HttpStatus> updatePersonDetailedInfo(
		@RequestBody @Valid PersonDetailedInfoDTO personDetailedInfoDTO, BindingResult bindingResult,
		@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonDetailedInfoDTOException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateDetailedInfo(id, peopleService.convertToPerson(personDetailedInfoDTO));

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/* 
	 * At the moment this method is the same as "add", which could also be used instead of an update request,
	 * but if in the future you need to change the update logic, you won't have to change the request
	 * on the client.
	 */

	@PostMapping(value = "/update/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<HttpStatus> updatePersonPhoto(@RequestPart("file") MultipartFile file,
		@PathVariable("id") int id) throws IOException {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (file.getSize() == 0) {
			throw new NotValidPersonPhotoException("Photo file should not be empty!");
		}
		
		if (file.getSize() > 5 * 1024 * 1024) {
			throw new NotValidPersonPhotoException("File size should not exceed 5MB!");
		}

		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
			throw new NotValidPersonPhotoException("Only .jpeg and .png files are allowed!");
		}

		peopleService.addPersonPhoto(file.getBytes(), file.getOriginalFilename(), id);

		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	

	// DELETE METHODS

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<HttpStatus> deletePerson(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		peopleService.delete(id);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping("/delete/detailed_info/{id}")
	public ResponseEntity<HttpStatus> deletePersonDetailedInfo(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		peopleService.deleteDetailedInfo(id);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@DeleteMapping("/delete/photo/{id}")
	public ResponseEntity<HttpStatus> deletePersonPhoto(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		peopleService.deletePhoto(id);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(PersonNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(PersonPhotoNotFoundException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(NotValidPersonContactInfoDTOException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(NotValidPersonDetailedInfoDTOException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(NotValidPersonPhotoException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	private ResponseEntity<ErrorResponse> handleCreateException(IOException ex) {

		ErrorResponse response = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}