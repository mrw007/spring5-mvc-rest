package mrw007.springframework.spring5mvcrest.services;

import mrw007.springframework.spring5mvcrest.api.v1.mapper.VendorMapper;
import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;
import mrw007.springframework.spring5mvcrest.models.Vendor;
import mrw007.springframework.spring5mvcrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VendorServiceTest {
    public static final long ID = 1L;
    public static final String VENDOR_NAME = "Vendor";
    public static final String VENDORS_BASE_URL = "/api/v1/vendors/";

    VendorService vendorService;
    @Mock
    VendorRepository vendorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
    }

    @Test
    void getAllVendors() {
        List<Vendor> vendors = Arrays.asList(new Vendor(), new Vendor(), new Vendor());
        when(vendorRepository.findAll()).thenReturn(vendors);

        List<VendorDTO> vendorDTOS = vendorService.getAllVendors();

        assertEquals(3, vendorDTOS.size());
    }

    @Test
    void getVendorByID() {
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(VENDOR_NAME);

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor));

        VendorDTO vendorDTO = vendorService.getVendorByID(ID);

        assertEquals(ID, vendorDTO.getId());
        assertEquals(VENDOR_NAME, vendorDTO.getName());
        assertEquals(VENDORS_BASE_URL + ID, vendorDTO.getVendorUrl());
    }

    @Test
    void createNewVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(vendorDTO.getName());

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO savedDto = vendorService.createNewVendor(vendorDTO);

        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals(VENDORS_BASE_URL + ID, savedDto.getVendorUrl());

    }

    @Test
    void updateVendorById() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(vendorDTO.getName());

        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO savedDto = vendorService.updateVendorById(ID, vendorDTO);

        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals(VENDORS_BASE_URL + ID, savedDto.getVendorUrl());
    }

    @Test
    void patchVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor savedVendor = new Vendor();
        savedVendor.setId(ID);
        savedVendor.setName(vendorDTO.getName());

        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(savedVendor));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(savedVendor);

        VendorDTO savedDto = vendorService.patchVendor(ID, vendorDTO);

        assertEquals(vendorDTO.getName(), savedDto.getName());
        assertEquals(VENDORS_BASE_URL + ID, savedDto.getVendorUrl());
    }

    @Test
    void deleteVendorById() {
        vendorService.deleteVendorById(ID);
        verify(vendorRepository, times(1)).deleteById(anyLong());
    }
}