package com.project.repository.interfaces;

import com.project.domain.Client;

public interface IClientRepository extends IRepository<Long, Client>{
    Client loginClient(String username, String password);

    Client findByUserName(String username);

    Client findByEmail(String email);
}