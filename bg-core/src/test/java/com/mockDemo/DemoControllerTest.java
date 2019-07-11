package com.mockDemo;


import com.fasterxml.jackson.databind.ObjectMapper;
//import com.htsec.BootstrapApplication;
//import com.htsec.rsrv.session.HtRsrvAuthenticationObject;
//import io.github.watertao.veigar.session.spi.AuthenticationObject;
import com.fbtest.BootstrapApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootstrapApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
public class DemoControllerTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private MockHttpSession mockHttpSession;

  @Before
  public void setUp() throws Exception{
    //MockMvcBuilders.webAppContextSetup(WebApplicationContext context)：指定WebApplicationContext，将会从该上下文获取相应的控制器并得到相应的MockMvc；
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();//建议使用这种
    /*
    mockHttpSession = new MockHttpSession();
    AuthenticationObject authObj=new HtRsrvAuthenticationObject();
    ((HtRsrvAuthenticationObject) authObj).setId(142);
    ((HtRsrvAuthenticationObject) authObj).setName("许留存");
    ((HtRsrvAuthenticationObject) authObj).setEmployee_no("xuliucun");
    ((HtRsrvAuthenticationObject) authObj).setDeptId("2272");
    mockHttpSession.setAttribute("net.bandle.veigar.rsrv.session.AuthenticationObject",authObj);
    //拦截器那边会判断用户是否登录，所以这里注入一个用户
    */
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
  public void query() throws Exception{//get
    MvcResult mvcResult=mockMvc.perform(
      MockMvcRequestBuilders.get("/auth/department")
        .param("departmentId","2289")
        .param("count","5")
        .param("parentId","1")
    ).andReturn();
    int status=mvcResult.getResponse().getStatus();                 //得到返回代码
    String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
    System.out.println("返回结果:"+content);
    //Assert.assertEquals(200,status); //断言，判断返回代码是否正确
  }

  @Test
  public void testPost() throws Exception {//post
    ObjectMapper mapper = new ObjectMapper();
//  TAuthUser tAuthUser=mapper.readValue(example,TAuthUser.class);
//  example=mapper.writeValueAsString(tAuthUser);
    String example="{\"token\":\"\",\"params\":{\"moduleId\":\"1\",\"systemId\":\"b68b213a93534e3a91aa0a00a2a4ec11\",\"moduleOperating\":\"1\"},\"pageSize\":10,\"pageNum\":1}";

    MvcResult mvcResult= mockMvc.perform(
      MockMvcRequestBuilders.post("/interface/query")
        .session(mockHttpSession)//传入session
        .contentType(MediaType.APPLICATION_JSON)//请求类型
        .content(example)//body
        .accept(MediaType.APPLICATION_JSON)//响应类型
    ).andDo(MockMvcResultHandlers.print())//打印执行情况
      .andReturn();//获得mock结果
    String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
    System.out.println("返回结果:"+content);
  }



}
