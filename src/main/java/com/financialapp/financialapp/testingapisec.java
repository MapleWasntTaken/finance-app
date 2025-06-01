package com.financialapp.financialapp;

import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testingapisec {
    

    /**@GetMapping("/")
    public String home(){
        return ("<h1>Welcome</h1>");
    }**/
    @GetMapping("/user")
    public String user(){
        return ("<h1>Welcome user or admin</h1>");
    }
    @GetMapping("/admin")
    public String admin(){
        return ("<h1>Welcome admin</h1>");
    }
    @GetMapping("/getUserRole")
    public String getCurrentUserRoles(Authentication authentication) {
        String x =  authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  
                .collect(Collectors.toList()).get(0);
        System.out.println(x);
        return x;
    }

}
