package com.example.demo.adress;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Storeadress;

public interface StoreAdressRepo extends JpaRepository<Storeadress, Integer> {

}
