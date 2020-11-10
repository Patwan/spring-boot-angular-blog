package com.pwebk.SpringBootBlogApplication.service;

import com.pwebk.SpringBootBlogApplication.exceptions.SpringRedditException;
import com.pwebk.SpringBootBlogApplication.model.RefreshToken;
import com.pwebk.SpringBootBlogApplication.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    //This class implements the logic to manage our Refresh Tokens.

    private final RefreshTokenRepository refreshTokenRepository;

    //The first method we have is generateRefreshToken() which creates a random 128 bit UUID String and we are using that as our token.
    // We are setting the createdDate as Instant.now(). And then we are saving the token to our MySQL Database.

    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRepository.save(refreshToken);
    }

    // we have validateRefreshToken() which queries the DB with the given token. If the token is not found it throws an Exception with message
    // – “Invalid refresh Token”

    void validateRefreshToken(String token) {
        refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new SpringRedditException("Invalid refresh Token"));
    }

    //we have deleteRefreshToken() which as name suggests deletes the refresh token from the database.
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }
}
