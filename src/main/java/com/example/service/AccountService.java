package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.Optional;


@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void register(Account newAccount) {
        accountRepository.save(newAccount);
    }

    public boolean isAccountExist(String username){
        return accountRepository.findByUsername(username).isPresent();
    }

    public boolean isAccountExist(int id){
        return accountRepository.findById(id).isPresent();
    }

    public Account login(String username, String password){
        Optional<Account> accoutOptional = accountRepository.findByUsernameAndPassword(username,password);
        if(accoutOptional.isPresent()){
            Account account = accoutOptional.get();
            return account;
        }
        return null;
    }

    
}