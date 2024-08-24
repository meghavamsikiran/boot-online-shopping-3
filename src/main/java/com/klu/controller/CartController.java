package com.klu.controller;

import com.klu.entity.Cart;
import com.klu.entity.LineItem;
import com.klu.entity.Product;
import com.klu.exception.CartNotFoundException;
import com.klu.exception.ProductNotFoundException;
import com.klu.repository.CartRepository;
import com.klu.service.CartService;
import com.klu.service.LineItemService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/shop/cart")
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LineItemService lineItemService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestParam("customerId") long customerId, @RequestBody Product productRequest) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);
        if (existingCart == null) {
            Cart newCart = new Cart();
            newCart.getCustomer().setId(customerId);

            LineItem newLineItem = new LineItem();
            newLineItem.setQuantity(1);
            newLineItem.setUnitPrice(productRequest.getPrice());
            newLineItem.setItemTotal(newLineItem.getQuantity() * productRequest.getPrice());
            newLineItem.setProduct(productRequest);
            newLineItem.setCart(newCart);

            newCart.getLineItemList().add(newLineItem);
            cartService.save(newCart);
            return ResponseEntity.ok("Product added to new cart");
        } else {
            LineItem lineItemExists = existingCart.getLineItemList().stream()
                .filter(li -> li.getProduct().getId() == productRequest.getId())
                .findFirst()
                .orElse(null);

            if (lineItemExists != null) {
                lineItemExists.setQuantity(lineItemExists.getQuantity() + 1);
                lineItemExists.setItemTotal(lineItemExists.getItemTotal() + productRequest.getPrice());
            } else {
                lineItemExists = new LineItem();
                lineItemExists.setQuantity(1);
                lineItemExists.setUnitPrice(productRequest.getPrice());
                lineItemExists.setItemTotal(productRequest.getPrice());
                lineItemExists.setProduct(productRequest);
                lineItemExists.setCart(existingCart);
                existingCart.getLineItemList().add(lineItemExists);
            }
            lineItemService.save(lineItemExists);
            cartService.save(existingCart);
            return ResponseEntity.ok("Product added to existing cart");
        }
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<String> removeFromCart(@RequestParam("customerId") long customerId, @RequestParam("productId") long productId) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);
        if (existingCart == null) {
            throw new CartNotFoundException("Cart not found for the customer");
        }

        LineItem lineItemToRemove = existingCart.getLineItemList().stream()
                .filter(lineItem -> lineItem.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);

        if (lineItemToRemove != null) {
            if (lineItemToRemove.getQuantity() > 1) {
                lineItemToRemove.setQuantity(lineItemToRemove.getQuantity() - 1);
                lineItemToRemove.setItemTotal(lineItemToRemove.getQuantity() * lineItemToRemove.getUnitPrice());
                lineItemService.save(lineItemToRemove);
                cartService.save(existingCart);
                return ResponseEntity.ok("Quantity decreased by 1");
            } else {
                existingCart.getLineItemList().remove(lineItemToRemove);
                lineItemService.delete(lineItemToRemove.getLineItemId());
                if (existingCart.getLineItemList().isEmpty()) {
                    cartRepository.delete(existingCart);
                    return ResponseEntity.ok("Cart removed");
                } else {
                    cartService.save(existingCart);
                    return ResponseEntity.ok("Last item removed from cart");
                }
            }
        } else {
            throw new ProductNotFoundException("Product not found in cart");
        }
    }

    @DeleteMapping("/removeCart")
    public ResponseEntity<String> removeCart(@RequestParam("customerId") long customerId) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);

        if (existingCart == null) {
            throw new CartNotFoundException("Cart not found for the customer");
        }

        existingCart.getLineItemList().forEach(lineItem -> lineItemService.delete(lineItem.getLineItemId()));
        cartRepository.delete(existingCart);
        return ResponseEntity.ok("Cart removed");
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Cart>> findAllCarts() {
        List<Cart> carts = cartService.findAll();
        return ResponseEntity.ok(carts);
    }

    @GetMapping("/viewByQuantity")
    public ResponseEntity<Object> viewByQuantity(@RequestParam("customerId") long customerId, @RequestParam("quantity") int quantity) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);
        if (existingCart == null) {
            throw new CartNotFoundException("No cart found for the customer");
        }

        List<LineItem> lineItems = existingCart.getLineItemList().stream()
                .filter(lineItem -> lineItem.getQuantity() == quantity)
                .collect(Collectors.toList());

        if (lineItems.isEmpty()) {
        	return ResponseEntity.badRequest().body("No line items found with the specified quantity");
        }

        return ResponseEntity.ok(lineItems);
    }

    @GetMapping("/viewByPrice")
    public ResponseEntity<Object> viewByPrice(@RequestParam("customerId") long customerId, @RequestParam("price") double price) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);
        if (existingCart == null) {
            throw new CartNotFoundException("No cart found for the customer");
        }

        List<LineItem> lineItems = existingCart.getLineItemList().stream()
                .filter(lineItem -> lineItem.getUnitPrice() == price)
                .collect(Collectors.toList());

        if (lineItems.isEmpty()) {
            return ResponseEntity.ok("No line items found with the specified price");
        }

        return ResponseEntity.ok(lineItems);
    }

    @PutMapping("/updateCart")
    public ResponseEntity<String> updateCart(@RequestParam("customerId") long customerId, @RequestParam("productId") long productId, @RequestParam("quantity") int quantity) {
        Cart existingCart = cartService.findByCustomer_Id(customerId);
        if (existingCart == null) {
            throw new CartNotFoundException("Cart not found for the customer");
        }

        LineItem lineItemToUpdate = existingCart.getLineItemList().stream()
                .filter(lineItem -> lineItem.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);

        if (lineItemToUpdate != null) {
            lineItemToUpdate.setQuantity(quantity);
            lineItemToUpdate.setItemTotal(quantity * lineItemToUpdate.getUnitPrice());
            lineItemService.save(lineItemToUpdate);
            cartService.save(existingCart);
            return ResponseEntity.ok("Cart updated");
        } else {
            throw new ProductNotFoundException("Product not found in cart");
        }
    }
}
