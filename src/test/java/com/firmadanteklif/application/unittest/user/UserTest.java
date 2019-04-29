package com.firmadanteklif.application.unittest.user;

import com.firmadanteklif.application.domain.entity.SiteUser;
import com.firmadanteklif.application.repository.UserRepository;
import com.firmadanteklif.application.security.UserDetailsServiceImpl;
import com.firmadanteklif.application.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRepository.class)
@ContextConfiguration(classes = {UserDetailsServiceImpl.class})
public class UserTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @Test
    public void registerNewUserTest() {
        final String email = "kasimgul@gmail.com";
        final SiteUser user = createBasicUser(email);

        Mockito.when(userService.register(user))
                .thenReturn(user);

        SiteUser siteUser = userService.register(user);
        assertEquals(email, siteUser.getEmail());
    }

    @Test
    public void createUserTest() {

        final String email = "kasimgul@gmail.com";
        final SiteUser user = createBasicUser(email);

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        Optional<SiteUser> found = userRepository.findByEmail(user.getEmail());

        if(found.isPresent()) {
            SiteUser fetched = found.get();
            assertEquals(email, fetched.getEmail());
        }
        else {
            fail();
        }
    }

    private SiteUser createBasicUser(String email) {
        SiteUser user = new SiteUser();
        user.setEmail(email);
        user.setPassword("ksm123");
        return user;
    }
}
