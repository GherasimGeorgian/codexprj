package com.project.service;

import com.project.domain.AccountType;
import com.project.domain.Client;
import com.project.repository.interfaces.IClientRepository;
import com.project.utils.events.ChangeEvent;
import com.project.utils.events.ChangeEventType;
import com.project.utils.observer.Observable;
import com.project.utils.observer.Observer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceCodex implements Observable<ChangeEvent>, UserDetailsService {
//public class ServiceCodex implements Observable<ChangeEvent> {
    private IClientRepository clientRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public ServiceCodex(IClientRepository clientRepository) {
        System.out.println("---Service created!!!!---");
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUserName(username);
        if(client == null){
            throw new UsernameNotFoundException("User not found in the database!");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(client.getAccountType().toString()));
        return new org.springframework.security.core.userdetails.User(client.getUserName(),client.getPassword(),authorities);
    }



    private int timesCalles = 0;
    private List<Observer<ChangeEvent>> observers=new ArrayList<>();

    @Override
    public void addObserver(Observer<ChangeEvent> e) {
        observers.add(e);

    }

    @Override
    public void removeObserver(Observer<ChangeEvent> e) {
        observers.remove(e);
    }
    @Override
    public void notifyObservers(ChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    public Client loginClient(String username, String password){
        return clientRepository.loginClient(username,password);
    }
    public Client adaugaClient(Client client){
        try {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
            client.setAccountType(AccountType.CLIENT);
            Client clientRez = clientRepository.save(client);
            notifyObservers(new ChangeEvent(ChangeEventType.CLIENT_SAVE, clientRez));
            return clientRez;
        }catch(Exception ex){
            return null;
        }
    }
    public List<Client> getClients(){
        timesCalles++;
        System.out.println("Apelat times: "+ timesCalles);
        return clientRepository.findAll();
    }
    public Client getClient(String username){
        Client client = clientRepository.findByUserName(username);
        return client;
    }

    public Client findByUserName(String username) {
        Client client = clientRepository.findByUserName(username);
        return client;
    }

    public Client getAccountByEmail(String email) {
        Client client = clientRepository.findByEmail(email);
        return client;
    }
}
