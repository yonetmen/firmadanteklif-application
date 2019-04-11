package com.firmadanteklif.application.integrationtest.user;

import com.firmadanteklif.application.Application;
import com.firmadanteklif.application.entity.SiteUser;
import com.firmadanteklif.application.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class
)
@TestPropertySource(locations = "classpath:application.properties")
public class UserIT {

    @Autowired
    protected UserRepository userRepository;

    @Test
    public void createUserTest() {
        SiteUser user = new SiteUser();
        user.setEmail("kasim@gmail.com");
        user.setPassword("kasim123");

        SiteUser created = userRepository.save(user);
        System.out.println("USER ID: " + created.getUuid());
        assertNotNull(created.getUuid());
    }
}
