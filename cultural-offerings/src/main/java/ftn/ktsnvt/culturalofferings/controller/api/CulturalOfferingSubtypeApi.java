package ftn.ktsnvt.culturalofferings.controller.api;

import ftn.ktsnvt.culturalofferings.dto.CulturalOfferingSubTypeDTO;
import ftn.ktsnvt.culturalofferings.model.CulturalOfferingSubType;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;

@RequestMapping(value = "/cultural-offering-subtypes")
public interface CulturalOfferingSubtypeApi {

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<CulturalOfferingSubTypeDTO>> findAll();

    @RequestMapping(value = "/by-page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Page<CulturalOfferingSubTypeDTO>> findAll(Pageable pageable);

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CulturalOfferingSubTypeDTO> get(@PathVariable("id") Long id);

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CulturalOfferingSubTypeDTO> create(@RequestBody CulturalOfferingSubTypeDTO body);

    @RequestMapping(value= "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CulturalOfferingSubTypeDTO> update(@RequestBody CulturalOfferingSubTypeDTO body, @PathVariable("id") Long id);

    @RequestMapping(value= "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable("id") Long id);

}

