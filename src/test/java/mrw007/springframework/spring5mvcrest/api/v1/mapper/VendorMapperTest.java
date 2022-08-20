package mrw007.springframework.spring5mvcrest.api.v1.mapper;

import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;
import mrw007.springframework.spring5mvcrest.models.Vendor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VendorMapperTest {
    public static final long ID = 1L;
    public static final String VENDOR_NAME = "Vendor";
    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    void vendorToVendorDTO() {
        Vendor vendor = new Vendor();
        vendor.setId(ID);
        vendor.setName(VENDOR_NAME);

        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        assertEquals(ID, vendorDTO.getId());
        assertEquals(VENDOR_NAME, vendorDTO.getName());
    }

    @Test
    void vendorDTOToVendor() {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(VENDOR_NAME);

        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        assertEquals(VENDOR_NAME, vendor.getName());
    }
}