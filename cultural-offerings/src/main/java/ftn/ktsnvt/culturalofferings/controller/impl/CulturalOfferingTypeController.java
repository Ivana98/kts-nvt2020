package ftn.ktsnvt.culturalofferings.controller.impl;

import ftn.ktsnvt.culturalofferings.controller.api.CulturalOfferingTypeApi;
import ftn.ktsnvt.culturalofferings.dto.CulturalOfferingTypeDTO;
import ftn.ktsnvt.culturalofferings.dto.CulturalOfferingTypeUpsertDTO;
import ftn.ktsnvt.culturalofferings.mapper.CulturalOfferingTypeMapper;
import ftn.ktsnvt.culturalofferings.model.CulturalOfferingType;
import ftn.ktsnvt.culturalofferings.model.exceptions.RequestBodyBindingFailedException;
import ftn.ktsnvt.culturalofferings.service.CulturalOfferingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CulturalOfferingTypeController implements CulturalOfferingTypeApi {

    @Autowired
    private CulturalOfferingTypeService culturalOfferingTypeService;

    @Autowired
    private CulturalOfferingTypeMapper culturalOfferingTypeMapper;

    @Override
    // @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:read')")
    public ResponseEntity<List<CulturalOfferingTypeDTO>> findAll() {
        return new ResponseEntity<>(
                culturalOfferingTypeService
                        .findAll()
                        .stream()
                        .map(x -> culturalOfferingTypeMapper.toDto(x))
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }

    @Override
    // @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:read')")
    public ResponseEntity<Page<CulturalOfferingTypeDTO>> findAll(Pageable pageable){
        Page<CulturalOfferingType> page = culturalOfferingTypeService.findAll(pageable);

        List<CulturalOfferingTypeDTO> culturalOfferingTypesDTO = page
                .toList()
                .stream()
                .map(x -> culturalOfferingTypeMapper.toDto(x))
                .collect(Collectors.toList());

        Page<CulturalOfferingTypeDTO> pageCulturalOfferingTypesDTO = new PageImpl<>(culturalOfferingTypesDTO,page.getPageable(),page.getTotalElements());
        return new ResponseEntity<>(pageCulturalOfferingTypesDTO, HttpStatus.OK);

    }

    @Override
    // @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:read')")
    public ResponseEntity<CulturalOfferingTypeDTO> findOne(Long id) {
        CulturalOfferingType culturalOfferingType = culturalOfferingTypeService.findOne(id);
        return new ResponseEntity<>(
                culturalOfferingTypeMapper.toDto(culturalOfferingType),
                HttpStatus.OK
        );
    }

    public static final String pera = "pera";

    @Override
    @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:write')")
    public ResponseEntity<CulturalOfferingTypeDTO> create(@Valid CulturalOfferingTypeDTO body, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    CulturalOfferingTypeDTO.class
            );
        }
        CulturalOfferingType culturalOfferingType = culturalOfferingTypeService.create(culturalOfferingTypeMapper.toEntity(body));
        return new ResponseEntity<>(
                culturalOfferingTypeMapper.toDto(culturalOfferingType),
                HttpStatus.CREATED
        );
    }

    @Override
    @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:write')")
    public ResponseEntity<CulturalOfferingTypeDTO> create2(@Valid CulturalOfferingTypeUpsertDTO body, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    CulturalOfferingTypeDTO.class
            );
        }
        CulturalOfferingType culturalOfferingType = culturalOfferingTypeService.create(culturalOfferingTypeMapper.toEntityAddSubTypes(body));
        return new ResponseEntity<>(
                culturalOfferingTypeMapper.toDto(culturalOfferingType),
                HttpStatus.CREATED
        );
    }

    @Override
    @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:write')")
    public ResponseEntity<CulturalOfferingTypeDTO> update(@Valid CulturalOfferingTypeDTO body, BindingResult bindingResult, Long id) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    CulturalOfferingTypeDTO.class
            );
        }
        CulturalOfferingType culturalOfferingType = culturalOfferingTypeService.update(culturalOfferingTypeMapper.toEntity(body), id);
        return new ResponseEntity<>(
                culturalOfferingTypeMapper.toDto(culturalOfferingType),
                HttpStatus.OK
        );
    }

    @Override
    @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:write')")
    public ResponseEntity<CulturalOfferingTypeDTO> update2(@Valid CulturalOfferingTypeUpsertDTO body, BindingResult bindingResult, Long id) {
        if(bindingResult.hasErrors()){
            throw new RequestBodyBindingFailedException(
                    bindingResult.getFieldErrors().get(0).getField(),
                    bindingResult.getFieldErrors().get(0).getDefaultMessage(),
                    CulturalOfferingTypeUpsertDTO.class
            );
        }
        CulturalOfferingType culturalOfferingType = culturalOfferingTypeService.update(culturalOfferingTypeMapper.toEntityAddSubTypes(body), id);
        return new ResponseEntity<>(
                culturalOfferingTypeMapper.toDto(culturalOfferingType),
                HttpStatus.OK
        );
    }

    @Override
    @PreAuthorize("hasAuthority('CULTURAL_OFFERING_TYPE:write')")
    public ResponseEntity<Void> delete(Long id) {
        culturalOfferingTypeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}


