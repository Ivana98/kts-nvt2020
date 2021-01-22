package ftn.ktsnvt.culturalofferings.unit.cultural_offering;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import ftn.ktsnvt.culturalofferings.model.CulturalOffering;
import ftn.ktsnvt.culturalofferings.repository.CulturalOfferingRepository;
import ftn.ktsnvt.culturalofferings.service.CulturalOfferingService;
import ftn.ktsnvt.culturalofferings.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CulturalOfferingServiceTest {
    @Autowired
    private CulturalOfferingService service;

    @MockBean
    private CulturalOfferingRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CulturalOfferingService culturalOfferingService;

    @Before
    public void setup() {
        List<CulturalOffering> co = new ArrayList<>();
        co.add(new CulturalOffering());
        co.add(new CulturalOffering());
        given(repository.findAll()).willReturn(co);

        Pageable pageable = PageRequest.of(0, 2);
        Page<CulturalOffering> page = new PageImpl<>(co, pageable, 2);
        given(repository.findAll(pageable)).willReturn(page);

        CulturalOffering id1 = new CulturalOffering();
        id1.setDescription("dasdasdasd");
        CulturalOffering id1_saved = new CulturalOffering();
        id1_saved.setId(1L);
        given(repository.findById(1L)).willReturn(Optional.of(id1_saved));
        given(repository.save(id1)).willReturn(id1_saved);
    }

    @Test
    public void testFindAll() {
        List<CulturalOffering> found = service.findAll();
        verify(repository, times(1)).findAll();

        assertEquals(2, found.size());
    }

    @Test
    public void testFindAllPageable() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<CulturalOffering> found = service.findAll(pageable);

        verify(repository, times(1)).findAll(pageable);
        assertEquals(2, found.getNumberOfElements());
    }

    @Test
    public void testFindById() {
        CulturalOffering found = service.findOne(1L);

        verify(repository, times(1)).findById(1L);
        assertEquals(Long.valueOf(1L), found.getId());
    }

    @Test
    public void testCreate() {
        CulturalOffering id1 = new CulturalOffering();
        id1.setName("dasdasdasd");
        CulturalOffering id1_saved = new CulturalOffering();
        id1_saved.setId(1L);
        given(repository.findByName(id1.getName())).willReturn(Optional.ofNullable(null));
        given(repository.save(id1)).willReturn(id1_saved);
        id1_saved = service.create(id1);

        verify(repository, times(1)).save(id1);
        assertEquals(id1_saved.getId(), Long.valueOf(1L));
    }

    @Test
    public void testUpdate() throws Exception {
        CulturalOffering s1 = new CulturalOffering();
        s1.setDescription("nesto");
        CulturalOffering s1_updated = new CulturalOffering();
        s1_updated.setDescription("nesto");
        s1_updated.setId(1l);

        // This was previously in the database
        CulturalOffering s2_saved = new CulturalOffering();
        s2_saved.setDescription("");
        s2_saved.setId(1L);
        assertEquals("", s2_saved.getDescription());

        given(repository.findById(1L)).willReturn(Optional.of(s2_saved));
        given(repository.save(s1)).willReturn(s1_updated);

        CulturalOffering s2_updated = service.update(s1, 1L);
        assertEquals("nesto", s2_updated.getDescription());

        // verify(culturalContentCategoryRepository, times(1)).findById(CATEGORY_ID);
        // verify(culturalContentCategoryRepository, times(1)).findByNameAndIdNot(NEW_CATEGORY,CATEGORY_ID);

        // assertEquals(NEW_CATEGORY, created.getName());
    }
}
