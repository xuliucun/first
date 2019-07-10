package com.bgtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fbtest.BootstrapApplication;
import com.fbtest.model.TAuthUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

////SpringBoot1.4版本之前用的是SpringJUnit4ClassRunner.class
//@RunWith(SpringRunner.class)
////SpringBoot1.4版本之前用的是@SpringApplicationConfiguration(classes = Application.class)
//@SpringBootTest(classes=BootstrapApplication.class)
////测试环境使用，用来表示测试环境使用的ApplicationContext将是WebApplicationContext类型的
//@WebAppConfiguration

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootstrapApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class TAuthUserControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @Before
  public void setUp() throws Exception{
    //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
  }


  @Test
  public void queryUserById() throws Exception{
      MvcResult mvcResult=mockMvc.perform(
              MockMvcRequestBuilders.get("/user/101")
      ).andReturn();
      int status=mvcResult.getResponse().getStatus();                 //得到返回代码
      String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
      System.out.println(content);
      //Assert.assertEquals(200,status); //断言，判断返回代码是否正确
  }

  @Test
  public void addUser() throws Exception {
      String example="{\"employeeNo\":\"003sf0595\",\"name\":\"总部3057\",\"userEmail\":\"888888888\",\"userPhone\":\"199883\",\"userGender\":null,\"userHeadImage\":null,\"status\":\"1\",\"password\":\"bcb15f821479b4d5772bd0ca866c00ad5f926e3580720659cc80d39c9d09802a\",\"remark\":\"ESB系统管理员\",\"lastLoginIp\":null,\"lastLoginTime\":null,\"createTime\":1552959837000,\"updateTime\":1560995107000,\"depId\":1}";
//      ObjectMapper mapper = new ObjectMapper();
//      TAuthUser tAuthUser=mapper.readValue(example,TAuthUser.class);
//      example=mapper.writeValueAsString(tAuthUser);
      MvcResult mvcResult= mockMvc.perform(
              MockMvcRequestBuilders.post("/user")
                      .contentType(MediaType.APPLICATION_JSON)//请求类型
                      .content(example)//body
                      .accept(MediaType.APPLICATION_JSON)//响应类型
      ).andDo(MockMvcResultHandlers.print())
       .andReturn();
      String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
      System.out.println(content);
  }

}
