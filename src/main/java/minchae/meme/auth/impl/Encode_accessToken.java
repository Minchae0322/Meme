package minchae.meme.auth.impl;

import minchae.meme.auth.Encode;

import java.util.UUID;

public class Encode_accessToken implements Encode {
    @Override
    public String encodeValue(String value) {
        return UUID.randomUUID().toString();
    }

    @Override
    public String encodeValue(Long value) {
        return UUID.randomUUID().toString();
    }
}
