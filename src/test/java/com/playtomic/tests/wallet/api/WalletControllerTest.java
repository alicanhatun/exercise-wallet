package com.playtomic.tests.wallet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.request.ChargeRequest;
import com.playtomic.tests.wallet.response.WalletResponse;
import com.playtomic.tests.wallet.service.wallet.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WalletService walletService;

    @Test
    void findByIdentifier_existsIdentifier_returnsOk() throws Exception {

        String identifier = "identifier";
        WalletResponse walletResponse = new WalletResponse(identifier, BigDecimal.ZERO);

        Mockito.when(walletService.findByIdentifier(identifier)).thenReturn(walletResponse);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/{identifier}", identifier)
                                                                .contentType(MediaType.APPLICATION_JSON))
                                 .andExpect(MockMvcResultMatchers.status()
                                                                 .isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WalletResponse returnedWalletResponse = objectMapper.readValue(content, WalletResponse.class);

        Assertions.assertEquals(walletResponse.getIdentifier(), returnedWalletResponse.getIdentifier());
        verify(walletService, times(1)).findByIdentifier(identifier);
    }

    @Test
    void chargeWallet_existsIdentifier_returnsOk() throws Exception {

        String identifier = "identifier";
        ChargeRequest chargeRequest = new ChargeRequest("card", BigDecimal.ZERO);

        mvc.perform(MockMvcRequestBuilders.put("/charge/{identifier}", identifier)
                                          .content(objectMapper.writeValueAsString(chargeRequest))
                                          .contentType(MediaType.APPLICATION_JSON))
           .andExpect(MockMvcResultMatchers.status()
                                           .isOk()).andReturn();


        verify(walletService, times(1)).chargeWallet(identifier, chargeRequest);
    }
}
