package mrw007.springframework.spring5mvcrest.services;

import mrw007.springframework.spring5mvcrest.api.v1.mapper.VendorMapper;
import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;
import mrw007.springframework.spring5mvcrest.models.Vendor;
import mrw007.springframework.spring5mvcrest.repositories.VendorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    public static final String VENDORS_BASE_URL = "/api/v1/vendors/";


    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public List<VendorDTO> getAllVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(vendor -> {
                    VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
                    vendorDTO.setVendorUrl(VENDORS_BASE_URL + vendor.getId());
                    return vendorDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public VendorDTO getVendorByID(Long id) {
        Optional<Vendor> vendorOptional = vendorRepository.findById(id);
        if (vendorOptional.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendorOptional.get());
        vendorDTO.setVendorUrl(VENDORS_BASE_URL + vendorDTO.getId());
        return vendorDTO;
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO VendorDTO) {
        return saveAndReturnDTO(vendorMapper.vendorDTOToVendor(VendorDTO));
    }

    @Override
    public VendorDTO updateVendorById(Long id, VendorDTO VendorDTO) {
        Vendor vendor = vendorMapper.vendorDTOToVendor(VendorDTO);
        vendor.setId(id);
        return saveAndReturnDTO(vendor);
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id).map(vendor -> {
            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }
            return saveAndReturnDTO(vendor);
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.deleteById(id);
    }

    private VendorDTO saveAndReturnDTO(Vendor vendor) {
        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnDTO.setVendorUrl(VENDORS_BASE_URL + savedVendor.getId());
        return returnDTO;
    }
}
