package com.financialapp.financialapp;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class utilityControllers {
 
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }


    
    @Autowired
    private PlaidService plaidService;
    @Autowired
    private PlaidItemRepository plaidItemRepository;
 
    @PostMapping("/test")
    public ResponseEntity<String> testCall() {
        try {
            PlaidItem dummy = plaidItemRepository.findAll().get(0);
            plaidService.refreshPlaidItem(dummy);
            return ResponseEntity.ok("Plaid item refresh completed.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("Refresh failed: " + e.getMessage());
        }
    }
    @GetMapping("/getUserRole")
    public String getCurrentUserRoles(Authentication authentication) {
        if(authentication==null){
            return "Not Signed in";
        }
        String x =  authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  
                .collect(Collectors.toList()).get(0);
        System.out.println(x);
        return x;
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
