package com.firmadanteklif.application.integrationtest.user;

import com.firmadanteklif.application.Application;
import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.domain.enums.VerificationEvent;
import com.firmadanteklif.application.domain.dto.VerificationMessage;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@TestPropertySource(locations = "classpath:application.properties")
public class UserIT {

    @Autowired
    protected UserService userService;

    @Autowired
    protected UserRepository userRepository;

    @Before
    public void setup() {

    }

    @After
    public void destroy() {
        userRepository.deleteAll();
    }

    @Test
    public void createUserTest() {
        final String email = "kasimgul@gmail.com";
        final SiteUser user = createBasicUser(email);

        SiteUser created = userService.register(user);
        assertNotNull(created.getUuid());
    }

    @Test
    public void generateActivationNeededMessageTest() {
        final String email = "kasimgul@gmail.com";
        VerificationMessage message = userService.generateActivationNeededMessage(email);
        assertEquals(message.getEvent(), VerificationEvent.REGISTER.name());
        assertEquals("Giris yapmadan önce Email adresinizi onaylamaniz gerekiyor", message.getMessage());
    }

    @Test
    public void getUserTest() {
        final String email = "kasimgul@gmail.com";
        final SiteUser user = createBasicUser(email);

        SiteUser created = userService.register(user);
        Optional<SiteUser> optional = userService.findUserByEmail(email);
        if(!optional.isPresent()) {
            fail();
        }
        SiteUser fetched = optional.get();
        assertEquals(created.getEmail(), fetched.getEmail());
    }


    /***********     Helper Methods      ***********/

    private SiteUser createBasicUser(String email) {
        SiteUser user = new SiteUser();
        user.setEmail(email);
        user.setPassword("ksm123");
        return user;
    }
}
