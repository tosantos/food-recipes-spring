package org.olivetree.foodrecipesspring.service;

import org.olivetree.foodrecipesspring.domain.Account;
import org.olivetree.foodrecipesspring.domain.VerificationToken;
import org.olivetree.foodrecipesspring.domain.VerificationTokenPK;
import org.olivetree.foodrecipesspring.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private static final long TOKEN_EXPIRY_IN_MINUTES = 30L;
    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public void createVerificationToken(String token, Account account) {
        VerificationTokenPK pk = new VerificationTokenPK();
        pk.setToken(token);
        pk.setUsername(account.getUsername());

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setTokenId(pk);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_IN_MINUTES));

        verificationTokenRepository.saveAndFlush(verificationToken);
    }
}
