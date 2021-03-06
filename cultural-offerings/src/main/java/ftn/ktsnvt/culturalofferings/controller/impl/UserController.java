package ftn.ktsnvt.culturalofferings.controller.impl;

import ftn.ktsnvt.culturalofferings.controller.api.UserApi;
import ftn.ktsnvt.culturalofferings.dto.ChangePasswordDTO;
import ftn.ktsnvt.culturalofferings.dto.ChangeUserDataDTO;
import ftn.ktsnvt.culturalofferings.dto.UserDTO;
import ftn.ktsnvt.culturalofferings.helper.DTOValidationHelper;
import ftn.ktsnvt.culturalofferings.mapper.UserMapper;
import ftn.ktsnvt.culturalofferings.model.User;
import ftn.ktsnvt.culturalofferings.model.exceptions.RequestBodyBindingFailedException;
import ftn.ktsnvt.culturalofferings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Controller
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Override
    @PreAuthorize("hasAuthority('USER:read')")
    public ResponseEntity<List<UserDTO>> findAll() {
        return new ResponseEntity<>(
                userService.findAll().stream().map(x -> userMapper.toDto(x)).collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Override
    @PreAuthorize("hasAuthority('USER:read')")
    public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable) {
        Page<User> page = userService.findAll(pageable);

        List<UserDTO> usersDTO = page
                .toList()
                .stream()
                .map(x -> userMapper.toDto(x))
                .collect(Collectors.toList());

        Page<UserDTO> pageUsersDTO = new PageImpl<>(usersDTO,page.getPageable(),page.getTotalElements());
        return new ResponseEntity<>(pageUsersDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Page<UserDTO>> findAllAdmins(Pageable pageable) {
        Page<User> page = userService.findAllAdmins(pageable);

        List<UserDTO> usersDTO = page
                .toList()
                .stream()
                .map(x -> userMapper.toDto(x))
                .collect(Collectors.toList());

        Page<UserDTO> pageUsersDTO = new PageImpl<>(usersDTO,page.getPageable(),page.getTotalElements());
        return new ResponseEntity<>(pageUsersDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<UserDTO> findOne(Long id) {
        User user = userService.findOne(id);
        return new ResponseEntity<>(
                userMapper.toDto(user),
                HttpStatus.OK
        );
    }

    @Override
    @PreAuthorize("hasAuthority('USER:write')")
    public ResponseEntity<UserDTO> create(@Valid UserDTO body, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    User.class
            );
        }
        User user = userService.create(userMapper.toEntity(body));
        return new ResponseEntity<>(
                userMapper.toDto(user),
                HttpStatus.CREATED
        );
    }

    @Override
    @PreAuthorize("hasAuthority('USER:write')")
    public ResponseEntity<UserDTO> update(@Valid UserDTO body, BindingResult bindingResult, Long id) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    User.class
            );
        }
        User user = userService.update(userMapper.toEntity(body), id);
        return new ResponseEntity<>(
                userMapper.toDto(user),
                HttpStatus.OK
        );
    }

    @Override
    @PreAuthorize("hasAuthority('USER:write')")
    public ResponseEntity<Void> delete(Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @Override
    public ResponseEntity<Void> changePassword(@Valid ChangePasswordDTO body, BindingResult bindingResult) {
		DTOValidationHelper.validateDTO(bindingResult);
		userService.changePassword(body.getOldPassword(), body.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Void> changeUserData(ChangeUserDataDTO body, BindingResult bindingResult) {
		DTOValidationHelper.validateDTO(bindingResult);
		userService.ChangePersonalData(body.getFirstName(), body.getLastName());
        return new ResponseEntity<>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ChangeUserDataDTO> getUserData() {
		ChangeUserDataDTO dto = userService.getUserData();
		return new ResponseEntity<>(dto, HttpStatus.OK);
	}

}
