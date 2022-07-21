package codex.services.rest;

import chat.persistance.UserRepository;
import chat.persistance.repository.jdbc.UserRepositoryJdbc;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.controller.Controller;
import com.project.domain.Client;
import com.project.repository.interfaces.IClientRepository;
import com.project.repository.orm.ClientORMRepository;
import com.project.repository.orm.RepositoryException;
import com.project.repository.orm.SessionFactoryInit;
import com.project.service.ServiceCodex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@ImportResource("classpath:beans.xml")
@RestController
@RequestMapping("/api")
public class CodexControllerREST {
    private static final String template = "Hello, %s!";

    @Autowired
    private ServiceCodex service;

    @GetMapping("/users")
    public ResponseEntity<List<Client>> getAll(){
        return ResponseEntity.ok().body(service.getClients());
    }
    @PostMapping("/create_account")
    public ResponseEntity<Client> saveClient(@RequestBody Client client){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/create_account").toUriString());
        return ResponseEntity.created(uri).body(service.adaugaClient(client));
    }

   @RequestMapping(value = "get_account_by_email/{email}", method = RequestMethod.GET)
    public ResponseEntity<Client> getAccountByEmail(@PathVariable String email){

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/get_account_by_email").toUriString());
        return ResponseEntity.created(uri).body(service.getAccountByEmail(email));
    }

    @RequestMapping(value = "user/get_photo/{username}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPhotoByEmail(@PathVariable String username){
        String url_photo = service.getUrlPhotoByUsername(username);

        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(url_photo, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] b = new byte[0];
        try {
            b = new byte[(int)f.length()];
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            f.readFully(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);


        return new ResponseEntity<byte[]>(b, headers, HttpStatus.OK);
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                Client client  = service.getClient(username);

                List<String> rolesClient = new ArrayList<String>(
                        Arrays.asList(client.getAccountType().toString()));
                String access_token = JWT.create()
                        .withSubject(client.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000 ))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",rolesClient)
                        .sign(algorithm);
                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch(Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }
        else{
            throw new RuntimeException("Refresh token is missing!");
        }
    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public ResponseEntity<?> getById(@PathVariable Long id){
//
//        Proba proba=probaRepositoryREST.findById(id);
//        if (proba==null)
//            return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
//        else
//            return new ResponseEntity<Proba>(proba, HttpStatus.OK);
//    }

//    @RequestMapping(method = RequestMethod.POST)
//    public Proba create(@RequestBody Proba proba){
//        probaRepositoryREST.save(proba);
//        return proba;
//
//    }

//    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//    public Proba update(@RequestBody Proba proba) {
//        System.out.println("Updating proba ...");
//        probaRepositoryREST.update(proba.getId(),proba);
//        return proba;
//
//    }

//    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
//    public ResponseEntity<?> delete(@PathVariable Long id){
//        System.out.println("Deleting user ... "+id);
//        try {
//            probaRepositoryREST.delete(id);
//            return new ResponseEntity<Proba>(HttpStatus.OK);
//        }catch (RepositoryException ex){
//            System.out.println("Ctrl Delete proba exception");
//            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }



    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }


}
