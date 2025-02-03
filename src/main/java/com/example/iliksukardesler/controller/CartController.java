package com.example.iliksukardesler.controller;

import com.example.iliksukardesler.model.Cart;
import com.example.iliksukardesler.service.CartService;
import com.example.iliksukardesler.service.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CartService cartService;

    public CartController(JwtTokenProvider jwtTokenProvider, CartService cartService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartService = cartService;
    }

    @PostMapping("/add/{productId}")
    public ResponseEntity<Cart> addProductToCart(@PathVariable Long productId, @RequestHeader("Authorization") String authHeader ){
        String token = authHeader.replace("Bearer ","");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Cart updatedCart = cartService.addProductToCart(username,productId);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeProductFromCart (@PathVariable Long productId, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Cart updatedCart = cartService.removeProductFromCart(username,productId);
        return ResponseEntity.ok(updatedCart);
    }

    @GetMapping
    public ResponseEntity<Cart> getCart(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ","");
        String username = jwtTokenProvider.getUsernameFromToken(token);
        Cart cart = cartService.getCart(username);
        return ResponseEntity.ok(cart);
    }





}
