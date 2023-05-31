package com.igorkayukov.telros.TestTask.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonContactInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Request.PersonDetailedInfoRequest;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonContactInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonDetailedInfoResponse;
import com.igorkayukov.telros.TestTask.dto.Person.Response.PersonResponse;
import com.igorkayukov.telros.TestTask.errors.ErrorMessage;
import com.igorkayukov.telros.TestTask.exceptions.NotValidPersonRequestException;
import com.igorkayukov.telros.TestTask.exceptions.PersonNotFoundException;
import com.igorkayukov.telros.TestTask.exceptions.PersonPhotoNotFoundException;
import com.igorkayukov.telros.TestTask.services.PeopleService;
import com.igorkayukov.telros.TestTask.validators.PersonContactInfoRequestValidator;

@RestController
@RequestMapping("/people")
public class PeopleController {

	private final PeopleService peopleService;
	private final PersonContactInfoRequestValidator personContactInfoDTOValidator;

	@Autowired
	public PeopleController(PeopleService peopleService, PersonContactInfoRequestValidator personContactInfoDTOValidator) {
		this.peopleService = peopleService;
		this.personContactInfoDTOValidator = personContactInfoDTOValidator;
	}

	// CREATE METHODS

	@PostMapping("/add")
	public ResponseEntity<HttpStatus> addPerson(@RequestBody @Valid PersonContactInfoRequest request,
		BindingResult bindingResult) {

		personContactInfoDTOValidator.validate(request, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonRequestException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.save(request);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping("/add/detailed_info/{id}")
	public ResponseEntity<HttpStatus> addPersonDetailedInfo(@RequestBody @Valid PersonDetailedInfoRequest request,
		BindingResult bindingResult, @PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonRequestException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateDetailedInfo(id, request);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	@PostMapping(value = "/add/photo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<HttpStatus> addPersonPhoto(@RequestPart("file") MultipartFile file,
		@PathVariable("id") int id) throws IOException {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (file.getSize() == 0) {
			throw new NotValidPersonRequestException("Photo file should not be empty!");
		}

		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
			throw new NotValidPersonRequestException("Only .jpeg and .png files are allowed!");
		}

		peopleService.addPersonPhoto(file.getBytes(), file.getOriginalFilename(), id);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	// READ METHODS

	@PostMapping("/get/contact_info/{id}")
	public PersonContactInfoResponse getPersonContactInfo(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		return peopleService.getPersonContactInfoResponse(id);
	}

	@PostMapping("/get/detailed_info/{id}")
	public PersonDetailedInfoResponse getPersonDetailedInfo(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		return peopleService.getPersonDetailedInfoResponse(id);
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
	public PersonResponse getPerson(@PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		return peopleService.getPersonResponse(id);
	}

	@PostMapping("/get/detailed_info")
	public List<PersonDetailedInfoResponse> getPeopleDetailedInfo() {

		return peopleService.getPersonDetailedInfoResponseList();
	}

	@PostMapping("/get/contact_info")
	public List<PersonContactInfoResponse> getPeopleContactInfo() {

		return peopleService.getPersonContactInfoResponseList();
	}

	@PostMapping("/get")
	public List<PersonResponse> getPeople() {

		return peopleService.getPersonResponseList();
	}

	// UPDATE METHODS

	@PatchMapping("/update/{id}")
	public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid PersonContactInfoRequest request,
		BindingResult bindingResult, @PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		personContactInfoDTOValidator.validate(request, bindingResult);

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonRequestException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateContactInfo(id, request);

		return ResponseEntity.ok(HttpStatus.OK);
	}

	/* 
	 * At the moment this method is the same as "add", which could also be used instead of an update request,
	 * but if in the future you need to change the update logic, you won't have to change the request
	 * on the client.
	 */

	@PatchMapping("/update/detailed_info/{id}")
	public ResponseEntity<HttpStatus> updatePersonDetailedInfo(@RequestBody @Valid PersonDetailedInfoRequest request,
		BindingResult bindingResult, @PathVariable("id") int id) {

		if (peopleService.findOne(id) == null) {
			throw new PersonNotFoundException("Person with this id is not found!");
		}

		if (bindingResult.hasErrors()) {
			throw new NotValidPersonRequestException(ErrorMessage.getErrorMessage(bindingResult));
		}

		peopleService.updateDetailedInfo(id, request);

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
			throw new NotValidPersonRequestException("Photo file should not be empty!");
		}

		if (!file.getContentType().equals("image/jpeg") && !file.getContentType().equals("image/png")) {
			throw new NotValidPersonRequestException("Only .jpeg and .png files are allowed!");
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
}