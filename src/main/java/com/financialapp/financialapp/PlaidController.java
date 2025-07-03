package com.financialapp.financialapp;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/plaid")
public class PlaidController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private CurrentUserService currentUserService;

    @Autowired
    private PlaidItemRepository plaidItemRepository;

    @Autowired
    private PlaidService plaidService;

    @PostMapping("/create-link-token")
    public ResponseEntity<String> createLinkToken() {
        Integer userId = currentUserService.getCurrentUserId();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> user = Map.of("client_user_id", "user-" + userId);

        Map<String, Object> body = Map.of(
            "client_id", Env.PLAID_CLIENT_ID,
            "secret", Env.PLAID_SECRET,
            "user", user,
            "client_name", "FinancialApp",
            "products", List.of("transactions", "liabilities"),
            "country_codes", List.of("US", "CA"),
            "language", "en"
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://sandbox.plaid.com/link/token/create", request, String.class);

        return response;
    }

    @PostMapping("/exchange-public-token")
    public ResponseEntity<String> exchangePublicToken(@RequestBody Map<String, String> payload) {

        String publicToken = payload.get("public_token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of(
            "client_id", Env.PLAID_CLIENT_ID,
            "secret", Env.PLAID_SECRET,
            "public_token", publicToken
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(
            "https://sandbox.plaid.com/item/public_token/exchange", request, String.class);

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.getBody());

            String accessToken = node.get("access_token").asText();

            PlaidItem p = plaidItemRepository.findByUserAndAccessToken(currentUserService.getCurrentUser(), accessToken).orElse(new PlaidItem());
            p.setUser(currentUserService.getCurrentUser());
            p.setAccessToken(accessToken);
            p.setInstitutionId(payload.get("institution_id"));
            p.setInstitutionName(payload.get("institution_name"));
            plaidItemRepository.save(p);

            plaidService.refreshPlaidItem(p);
        } 
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token parse error");
        }
        return response;
    }
    
    @PostMapping("/refresh-transactions")
    public ResponseEntity<List<TransactionItem>> refreshTransactions(){
        List<TransactionItem> transactions = new ArrayList<>();
        for(PlaidItem plaidItem:currentUserService.getCurrentUser().getPlaidItems()){
            plaidService.refreshPlaidItem(plaidItem);
            for(AccountItem accountItem : plaidItem.getAccountItems()){
                transactions.addAll(accountItem.getTransactions());
            }
        }
        return ResponseEntity.ok(transactions);
    }
}