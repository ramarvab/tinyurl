package com.cloudflare.service;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

/**
 * This class generates random key whenever generate key is invoked.
 * Leverage apache commons RandomStringGenerator class.
 */
@Service
public class GenerateRandomKey {
    /**
     * Generates 8 bit random key which contains alphanumeric characters
     * eg: af45yt7j, uyf56ty
     * @return random string
     */
    public String generateKey() {
        RandomStringGenerator randomKey =
                new RandomStringGenerator.Builder()
                        .withinRange('0', 'z')
                        .filteredBy(CharacterPredicates.ASCII_ALPHA_NUMERALS, CharacterPredicates.DIGITS)
                        .build();
        return randomKey.generate(8);
    }
}
