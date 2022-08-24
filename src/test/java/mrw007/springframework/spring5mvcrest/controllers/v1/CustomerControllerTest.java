package mrw007.springframework.spring5mvcrest.controllers.v1;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import mrw007.springframework.spring5mvcrest.api.v1.model.CustomerDTO;
import mrw007.springframework.spring5mvcrest.services.CustomerService;
import mrw007.springframework.spring5mvcrest.services.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
class CustomerControllerTest {

    public static final long ID_1 = 1L;
    public static final String CUSTOMER_1_FIRST_NAME = "Joe";
    public static final String CUSTOMER_1_LAST_NAME = "Newman";

    public static final long ID_2 = 2L;
    public static final String CUSTOMER_2_FIRST_NAME = "Mary";
    public static final String CUSTOMER_2_LAST_NAME = "Lachappele";
    public static final String CUSTOMERS_BASE_URL = "/api/v1/customers/";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .apply(documentationConfiguration(restDocumentation))
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
    void testGetAllCustomers() throws Exception {
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(ID_1);
        customerDTO1.setFirstName(CUSTOMER_1_FIRST_NAME);
        customerDTO1.setLastName(CUSTOMER_1_LAST_NAME);

        CustomerDTO customerDTO2 = new CustomerDTO();
        customerDTO2.setId(ID_2);
        customerDTO2.setFirstName(CUSTOMER_2_FIRST_NAME);
        customerDTO2.setLastName(CUSTOMER_2_LAST_NAME);

        List<CustomerDTO> customers = Arrays.asList(customerDTO1, customerDTO2);

        when(customerService.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get(CUSTOMERS_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers", hasSize(2)));
    }

    @Test
    void testGetCustomerById() throws Exception {
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setId(ID_1);
        customerDTO1.setFirstName(CUSTOMER_1_FIRST_NAME);
        customerDTO1.setLastName(CUSTOMER_1_LAST_NAME);
        customerDTO1.setCustomerUrl(CUSTOMERS_BASE_URL + ID_1);

        when(customerService.getCustomerByID(anyLong())).thenReturn(customerDTO1);

        mockMvc.perform(get(CUSTOMERS_BASE_URL + "/{customerId}", ID_1)
                        .param("isHappy", "yes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(ID_1)))
                .andExpect(jsonPath("$.firstName", equalTo(CUSTOMER_1_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(CUSTOMER_1_LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + ID_1)))
                .andDo(document("v1/customers-getById",
                        pathParameters(
                                parameterWithName("customerId").description("ID of desired Customer")
                        ),
                        requestParameters(
                                parameterWithName("isHappy").description("Is Customer Happy query Param")
                        ),
                        responseFields(
                                fieldWithPath("id").description("ID of Customer"),
                                fieldWithPath("firstName").description("Customer's First Name"),
                                fieldWithPath("lastName").description("Customer's Last Name"),
                                fieldWithPath("customerUrl").description("Customer's URL")
                        )
                ));
    }

    @Test
    void testCreateNewCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(CUSTOMER_1_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_1_LAST_NAME);

        CustomerDTO returnCustomerDTO = new CustomerDTO();
        returnCustomerDTO.setId(ID_1);
        returnCustomerDTO.setFirstName(customerDTO.getFirstName());
        returnCustomerDTO.setLastName(customerDTO.getLastName());
        returnCustomerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + ID_1);

        when(customerService.createNewCustomer(customerDTO)).thenReturn(returnCustomerDTO);

        mockMvc.perform(post(CUSTOMERS_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo(CUSTOMER_1_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(CUSTOMER_1_LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + ID_1)))
                .andDo(document("v1/customers-new",
                        requestFields(
                                fieldWithPath("id").ignored(),
                                fieldWithPath("firstName").description("Customer's First Name"),
                                fieldWithPath("lastName").description("Customer's Last Name"),
                                fieldWithPath("customerUrl").ignored()
                        ),
                        responseFields(
                                fieldWithPath("id").description("ID of Customer"),
                                fieldWithPath("firstName").description("Customer's First Name"),
                                fieldWithPath("lastName").description("Customer's Last Name"),
                                fieldWithPath("customerUrl").description("Customer's URL")
                        )
                ));
    }

    @Test
    void testUpdateCustomerById() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(CUSTOMER_1_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_1_LAST_NAME);

        CustomerDTO returnCustomerDTO = new CustomerDTO();
        returnCustomerDTO.setFirstName(customerDTO.getFirstName());
        returnCustomerDTO.setLastName(customerDTO.getLastName());
        returnCustomerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + ID_1);

        when(customerService.updateCustomerById(anyLong(), any(CustomerDTO.class))).thenReturn(returnCustomerDTO);

        mockMvc.perform(put(CUSTOMERS_BASE_URL + ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(CUSTOMER_1_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(CUSTOMER_1_LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + ID_1)));

    }

    @Test
    void testPatchCustomer() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(CUSTOMER_1_FIRST_NAME);
        customerDTO.setLastName(CUSTOMER_1_LAST_NAME);

        CustomerDTO returnCustomerDTO = new CustomerDTO();
        returnCustomerDTO.setFirstName(customerDTO.getFirstName());
        returnCustomerDTO.setLastName(customerDTO.getLastName());
        returnCustomerDTO.setCustomerUrl(CUSTOMERS_BASE_URL + ID_1);

        when(customerService.patchCustomer(anyLong(), any(CustomerDTO.class))).thenReturn(returnCustomerDTO);

        mockMvc.perform(patch(CUSTOMERS_BASE_URL + ID_1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(customerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo(CUSTOMER_1_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", equalTo(CUSTOMER_1_LAST_NAME)))
                .andExpect(jsonPath("$.customerUrl", equalTo(CUSTOMERS_BASE_URL + ID_1)));

    }

    @Test
    void testDeleteCustomerById() throws Exception {
        mockMvc.perform(delete(CUSTOMERS_BASE_URL + ID_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(customerService, times(1)).deleteCustomerById(anyLong());
    }

    @Test
    public void testNotFoundException() throws Exception {

        when(customerService.getCustomerByID(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get(CUSTOMERS_BASE_URL + "/222")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}