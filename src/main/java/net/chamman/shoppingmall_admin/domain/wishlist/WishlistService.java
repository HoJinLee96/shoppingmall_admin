package net.chamman.shoppingmall_admin.domain.wishlist;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistService {
	
	private final WishlistRepository wishlistRepository;

}
