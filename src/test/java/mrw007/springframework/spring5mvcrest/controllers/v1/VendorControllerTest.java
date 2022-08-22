package mrw007.springframework.spring5mvcrest.controllers.v1;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import mrw007.springframework.spring5mvcrest.api.v1.model.VendorDTO;
import mrw007.springframework.spring5mvcrest.services.ResourceNotFoundException;
import mrw007.springframework.spring5mvcrest.services.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static mrw007.springframework.spring5mvcrest.controllers.v1.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VendorControllerTest {

    public static final long ID_1 = 1L;
    public static final String VENDOR_NAME_1 = "Vendor 1";
    public static final long ID_2 = 2L;
    public static final String VENDOR_NAME_2 = "Vendor";
    public static final String VENDORS_BASE_URL = "/api/v1/vendors/";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(vendorController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
        deserializationSetup();
    }

    private void deserializationSetup() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        Configuration.setDefaults(new Configuration.Defaults() {

            private final JsonProvider jsonProvider = new JacksonJsonProvider(objectMapper);
            private final MappingProvider mappingProvider = new JacksonMappingProvider(objectMapper);

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }


    @Test
    void getAllVendors() throws Exception {
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setId(ID_1);
        vendorDTO1.setName(VENDOR_NAME_1);

        VendorDTO vendorDTO2 = new VendorDTO();
        vendorDTO2.setId(ID_2);
        vendorDTO2.setName(VENDOR_NAME_2);

        List<VendorDTO> vendors = Arrays.asList(vendorDTO1, vendorDTO2);

        when(vendorService.getAllVendors()).thenReturn(vendors);

        mockMvc.perform(get(VENDORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vendors", hasSize(2)));
    }

    @Test
    void getVendorById() throws Exception {
        VendorDTO vendorDTO1 = new VendorDTO();
        vendorDTO1.setId(ID_1);
        vendorDTO1.setName(VENDOR_NAME_1);
        vendorDTO1.setVendorUrl(VENDORS_BASE_URL + ID_1);

        when(vendorService.getVendorByID(ID_1)).thenReturn(vendorDTO1);

        mockMvc.perform(get(VENDORS_BASE_URL + ID_1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(ID_1)))
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME_1)));
    }

    @Test
    void createNewVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(ID_1);
        vendorDTO.setName(VENDOR_NAME_1);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setId(vendorDTO.getId());
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(VENDORS_BASE_URL + ID_1);

        when(vendorService.createNewVendor(any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(post(VENDORS_BASE_URL)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME_1)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VENDORS_BASE_URL + ID_1)));
    }

    @Test
    void updateVendorById() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(ID_1);
        vendorDTO.setName(VENDOR_NAME_1);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setId(vendorDTO.getId());
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(VENDORS_BASE_URL + ID_1);

        when(vendorService.updateVendorById(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(put(VENDORS_BASE_URL + ID_1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME_1)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VENDORS_BASE_URL + ID_1)));
    }

    @Test
    void patchVendor() throws Exception {
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setId(ID_1);
        vendorDTO.setName(VENDOR_NAME_1);

        VendorDTO returnDTO = new VendorDTO();
        returnDTO.setId(vendorDTO.getId());
        returnDTO.setName(vendorDTO.getName());
        returnDTO.setVendorUrl(VENDORS_BASE_URL + ID_1);

        when(vendorService.patchVendor(anyLong(), any(VendorDTO.class))).thenReturn(returnDTO);

        mockMvc.perform(patch(VENDORS_BASE_URL + ID_1)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(vendorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(VENDOR_NAME_1)))
                .andExpect(jsonPath("$.vendorUrl", equalTo(VENDORS_BASE_URL + ID_1)));
    }

    @Test
    void deleteVendorById() throws Exception {
        mockMvc.perform(delete(VENDORS_BASE_URL + ID_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(vendorService, times(1)).deleteVendorById(anyLong());
    }

    @Test
    void testNotFoundException() throws Exception {
        when(vendorService.getVendorByID(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(VENDORS_BASE_URL + "/222")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}