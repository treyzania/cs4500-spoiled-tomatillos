import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.northeastern.cs4500.RtApplication;
import edu.northeastern.cs4500.data.AuthKeyRepository;
import edu.northeastern.cs4500.data.FriendRequestRepository;
import edu.northeastern.cs4500.data.NotificationRepository;
import edu.northeastern.cs4500.data.ReviewRepository;
import edu.northeastern.cs4500.data.SessionRepository;
import edu.northeastern.cs4500.data.TitleRepository;
import edu.northeastern.cs4500.data.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RtApplication.class})
@WebAppConfiguration
public class ApplicationTest {

	@Autowired
	private WebApplicationContext webappContext;
	
	private MockMvc mock;
	
	@Autowired
	private AuthKeyRepository akRepo;
	
	@Autowired
	private FriendRequestRepository frRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	@Autowired
	private NotificationRepository notifRepo;
	
	@Autowired
	private SessionRepository sessionRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private TitleRepository titleRepo;
	
	@Before
	public void setup() throws Exception {
		this.mock = MockMvcBuilders.webAppContextSetup(webappContext).build();
		this.mock.perform(
				MockMvcRequestBuilders.post("/api/user/create")
					.param("username", "foo")
					.param("password", "12345678"));
	}
	
	@Test
	public void testNothing() {
		// OK
	}
	
	@Test
	public void testAddUser() throws Exception {
		MockHttpServletResponse res1 = this.mock.perform(MockMvcRequestBuilders.get("/api/user/1")).andReturn().getResponse();
		Assert.assertEquals(200, res1.getStatus());
		MockHttpServletResponse res2 = this.mock.perform(MockMvcRequestBuilders.get("/api/user/by-name").param("name", "foo")).andReturn().getResponse();
		Assert.assertEquals(200, res2.getStatus());
	}
	
}
