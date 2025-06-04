package com.financialapp.financialapp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class testingapisec {
    
    private List<user> users = new ArrayList<>(List.of(
            new user(1,"josh"),
            new user(2,"terrance")

    ));

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
    @GetMapping("/test")
    public List<user> getUsers(){
        return this.users;
    }
    @PostMapping("/test")
    public String addUser(@RequestBody user userr){
        this.users.add(userr);
        return "ok";
    }
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){

        System.out.println(request.getAttribute("_csrf"));

        return (CsrfToken) request.getAttribute("_csrf");
    }


    public static class user{
        private int id;
        private String name;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public user(int id, String name) {
            this.id = id;
            this.name = name;
        }
        
    }

}
