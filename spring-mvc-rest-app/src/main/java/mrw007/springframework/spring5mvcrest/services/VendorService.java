package mrw007.springframework.spring5mvcrest.services;


import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;

import java.util.List;

public interface VendorService {
    List<VendorDTO> getAllVendors();

    VendorDTO getVendorByID(Long id);

    VendorDTO createNewVendor(VendorDTO VendorDTO);

    VendorDTO updateVendorById(Long id, VendorDTO VendorDTO);

    VendorDTO patchVendor(Long id, VendorDTO VendorDTO);

    void deleteVendorById(Long id);
}
