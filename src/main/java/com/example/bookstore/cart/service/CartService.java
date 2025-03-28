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
import java.util.Optional;
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


        Optional<Cart> existingCart = cartRepository.findByUserAndInventory(user, inventory);

        if (existingCart.isPresent()) {

            Cart cart = existingCart.get();
            cart.updateQuantity(cart.getQuantity() + dto.getQuantity());
        } else {

            Cart newCart = Cart.builder()
                    .user(user)
                    .inventory(inventory)
                    .quantity(dto.getQuantity())
                    .build();
            cartRepository.save(newCart);
        }
    }




    @Transactional(readOnly = true)
    public List<CartDto> findAll(User user) {
        List<Cart> carts = cartRepository.findByUser(user);

        System.out.println("Service - findAll Cart 개수: " + carts.size());

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
                            ) : null,
                            cart.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<CartDto> findCartsByIds(List<Long> cartIds) {
        List<Cart> carts = cartRepository.findAllById(cartIds);
        return carts.stream().map(cart -> new CartDto(
                cart.getCartId(),
                new InventoryForUserDto(
                        cart.getInventory().getInventoryId(),
                        cart.getInventory().getTitle(),
                        cart.getInventory().getIsbn(),
                        cart.getInventory().getAuthors() != null ? cart.getInventory().getAuthors().split(",") : new String[]{},
                        cart.getInventory().getPublisher(),
                        cart.getInventory().getSalePrice(),
                        cart.getInventory().getThumbnail()
                ),
                cart.getQuantity()
        )).collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<CartDto> findBooksByInventoryIds(List<Long> inventoryIds) {
        List<Inventory> inventories = inventoryRepository.findAllById(inventoryIds);

        return inventories.stream()
                .map(inventory -> new CartDto(
                        null,
                        new InventoryForUserDto(
                                inventory.getInventoryId(),
                                inventory.getTitle(),
                                inventory.getIsbn(),
                                inventory.getAuthors() != null ? inventory.getAuthors().split(",") : new String[]{},
                                inventory.getPublisher(),
                                inventory.getSalePrice(),
                                inventory.getThumbnail()
                        ),
                        1 // 기본 수량 1
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public void updateById(UpdateCartDto dto) {
        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new IllegalArgumentException("장바구니 아이템을 찾을 수 없습니다."));
        cart.updateQuantity(dto.getQuantity());
    }

    
    @Transactional
    public void deleteById(Long cartId) {

        cartRepository.deleteById(cartId);
    }
}
