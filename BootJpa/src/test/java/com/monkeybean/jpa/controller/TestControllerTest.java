package com.monkeybean.jpa.controller;

import com.monkeybean.jpa.MainApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by MonkeyBean on 2018/9/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
@WebAppConfiguration
public class TestControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before()
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getUserListInfoTest() throws Exception {
        String responseStr = mockMvc.perform(
                get("/test/info/list/user")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .sessionAttr("")
        ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("UserListInfoTest result: " + responseStr);
    }

    @Test
    public void addNewUserTest() throws Exception {
        Random r = new Random();
        int id = r.nextInt(10000);
        String name = "test" + id;
        String requestBody = "{\"userId\":" + id + ", \"userName\": \"" + name + "\"}";
        String responseStr = mockMvc.perform(
                post("/test/user/new/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        System.out.println("addNewUser result: " + responseStr);
    }
}