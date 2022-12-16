package com.mobiloitte.microservice.wallet.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.Mnemonic;

public interface MnemonicDao extends JpaRepository<Mnemonic, Long> {

	Optional<Mnemonic> findByIsDeletedFalse();

	Optional<Mnemonic> findByCoinName(String string);

}
