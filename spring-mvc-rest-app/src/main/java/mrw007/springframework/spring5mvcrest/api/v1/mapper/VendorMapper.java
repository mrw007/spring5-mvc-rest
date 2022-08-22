package mrw007.springframework.spring5mvcrest.api.v1.mapper;

import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;
import mrw007.springframework.spring5mvcrest.models.Vendor;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VendorMapper {
    VendorMapper INSTANCE = Mappers.getMapper(VendorMapper.class);

    VendorDTO vendorToVendorDTO(Vendor vendor);

    Vendor vendorDTOToVendor(VendorDTO vendorDTO);
}
