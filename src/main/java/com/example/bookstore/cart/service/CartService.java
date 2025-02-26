package com.example.bookstore.cart.service;

import com.example.bookstore.cart.domain.Cart;
import com.example.bookstore.cart.dto.AddCartDto;
import com.example.bookstore.cart.dto.CartDto;
import com.example.bookstore.cart.dto.UpdateCartDto;
import com.example.bookstore.cart.repository.CartRepository;
import com.example.bookstore.inventory.domain.Inventory;
import com.example.bookstore.inventory.dto.InventoryForUserDto;
import com.example.bookstore.inventory.repository.InventoryRepository;
import com.example.bookstore.user.domain.User;
import com.example.bookstore.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void save(AddCartDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        Inventory inventory = inventoryRepository.findById(dto.getInventoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

        Cart cart = Cart.builder()
                .user(user)  // ✅ User 객체 직접 조회 후 저장
                .inventory(inventory)  // ✅ Inventory 객체 직접 조회 후 저장
                .quantity(dto.getQuantity())
                .build();
        cartRepository.save(cart);
    }


    @Transactional(readOnly = true)
    public List<CartDto> findAll(User user) {
        List<Cart> carts = cartRepository.findByUser(user);

        System.out.println("Cart 개수: " + carts.size());

        return carts.stream()
                .map(cart -> {
                    System.out.println("Cart ID: " + cart.getCartId());
                    System.out.println("Inventory: " + (cart.getInventory() != null ? cart.getInventory().getInventoryId() : "NULL"));

                    return new CartDto(
                            cart.getCartId(),
                            cart.getInventory() != null ? new InventoryForUserDto(
                                    cart.getInventory().getInventoryId(),
                                    cart.getInventory().getTitle(),
                                    cart.getInventory().getIsbn(),
                                    cart.getInventory().getAuthors() != null ? cart.getInventory().getAuthors().split(",") : new String[]{},
                                    cart.getInventory().getPublisher(),
                                    cart.getInventory().getSalePrice(),
                                    cart.getInventory().getThumbnail()
                            ) : null, // ✅ Inventory가 없으면 null
                            cart.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }




    /** 📌 장바구니 상품 수량 수정 */
    @Transactional
    public void updateById(UpdateCartDto dto) {
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다."));
        cart.updateQuantity(dto.getQuantity());
    }

    /** 📌 장바구니 상품 삭제 */
    @Transactional
    public void deleteById(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}
