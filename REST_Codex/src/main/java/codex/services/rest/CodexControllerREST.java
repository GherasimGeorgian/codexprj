package codex.services.rest;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    @PostMapping("/user/save")
    public ResponseEntity<Client> saveClient(@RequestBody Client client){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        return ResponseEntity.created(uri).body(service.adaugaClient(client));
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
