package com.firmadanteklif.application.integrationtest.user;

import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@WebMvcTest(UserRepository.class)
public class UserTest {

    @MockBean
    private UserRepository userRepository;

    @Test
    public void createUserTest() {

        final String email = "kasimgul@gmail.com";

        SiteUser user = new SiteUser();
        user.setEmail(email);
        user.setPassword("kasimgul123");

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
}
