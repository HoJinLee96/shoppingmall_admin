package net.chamman.shoppingmall_admin.security.crypto;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AesService {

	private final AesProvider aesProvider;
	
    public <T extends Cryptable<T>> T encrypt(T payload) {
        return payload.encrypt(aesProvider);
    }
    
    public <T extends Cryptable<T>> T decrypt(T payload) {
        return payload.decrypt(aesProvider);
    }
    
}
