package com.mei.jwt;

import com.mei.jwt.entity.Role;
import com.mei.jwt.service.RoleService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;

@SpringBootTest
class JwtApplicationTests {

    @Test
    void contextLoads() {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
        //tqn03zzkf5sEYGdDWFCxR/4d1IlrWIejiJInwj5e+Lk=
    }

    @Autowired
    private RoleService roleService;

    @Test
    public void test(){
        Role role = roleService.selectRoleByUsername("mei");
        System.out.println(role);
    }

}
