package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.WalletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(value = "develop", profiles = "develop")
class WalletControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void findByIdentifier_nonExistsIdentifier_returnsOk() throws Exception {

        String identifier = UUID.randomUUID().toString();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/{identifier}", identifier)
                                                                .contentType(MediaType.APPLICATION_JSON))
                                 .andExpect(MockMvcResultMatchers.status()
                                                                 .isBadRequest()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertEquals(WalletNotFoundException.WALLET_NOT_FOUND_MESSAGE, content);
    }

    @Test
    void findByIdentifier_existsIdentifier_returnsOk() throws Exception {

        String identifier = UUID.randomUUID().toString();

        mvc.perform(MockMvcRequestBuilders.post("/{identifier}", identifier)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isOk()).andReturn();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/{identifier}", identifier)
                                                                .contentType(MediaType.APPLICATION_JSON))
                                 .andExpect(MockMvcResultMatchers.status()
                                                                 .isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WalletResponse walletResponse = objectMapper.readValue(content, WalletResponse.class);

        Assertions.assertNotNull(walletResponse);
        Assertions.assertNotNull(walletResponse.getIdentifier());
        Assertions.assertEquals(walletResponse.getIdentifier(), identifier);
    }

    @Test
    void chargeWallet_nonExistsIdentifier_returnBadRequest() throws Exception {

        String identifier = UUID.randomUUID().toString();
        ChargeRequest chargeRequest = new ChargeRequest("card", BigDecimal.ONE);

        mvc.perform(MockMvcRequestBuilders.put("/charge/{identifier}", identifier)
                                          .content(objectMapper.writeValueAsString(chargeRequest))
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isBadRequest()).andReturn();
    }

    @Test
    void chargeWallet_badAmount_returnBadRequest() throws Exception {

        String identifier = UUID.randomUUID().toString();
        ChargeRequest chargeRequest = new ChargeRequest("card", BigDecimal.ONE);

        mvc.perform(MockMvcRequestBuilders.post("/{identifier}", identifier)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isOk()).andReturn();

        mvc.perform(MockMvcRequestBuilders.put("/charge/{identifier}", identifier)
                                          .content(objectMapper.writeValueAsString(chargeRequest))
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isBadRequest()).andReturn();
    }

    @Test
    void chargeWallet_correctRequest_returnOk() throws Exception {

        String identifier = UUID.randomUUID().toString();
        ChargeRequest chargeRequest = new ChargeRequest("card", BigDecimal.TEN);

        mvc.perform(MockMvcRequestBuilders.post("/{identifier}", identifier)
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isOk()).andReturn();

        mvc.perform(MockMvcRequestBuilders.put("/charge/{identifier}", identifier)
                                          .content(objectMapper.writeValueAsString(chargeRequest))
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isOk()).andReturn();
    }

}
