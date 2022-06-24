package com.apiloja.controller;

import com.apiloja.dto.productdto.ProductOfCartDTO;
import com.apiloja.service.product.ProductOfCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin()
@Tag(name = "Produtos de Carrinho")
@RequestMapping(value = "/cart/")
public class ProductOfCartController {


    final ProductOfCartService productOfCartService;

    public ProductOfCartController(ProductOfCartService productOfCartService) {
        this.productOfCartService = productOfCartService;
    }

    @Operation(summary = "Listar produtos do carrinho", description = "Retorna lista de produtos do carrinho do usuário autenticado.")
    @GetMapping("/shoppingcart")
    public ResponseEntity<List<ProductOfCartDTO>> getShopingCart() {

        return ResponseEntity.status(HttpStatus.OK).body(productOfCartService.getShopingCart());
    }

    @Operation(summary = "Salvar produto no carrinho", description = "Salva produto no carrinho do usuário autenticado.")
    @PostMapping("/shoppingcart")
    public ResponseEntity<ProductOfCartDTO> postShopingCart(@RequestBody ProductOfCartDTO productOfCartDTO) {

        return  ResponseEntity.status(HttpStatus.CREATED).body(productOfCartService.postShopingCart(productOfCartDTO));

    }

    @Operation(summary = "Deletar produto do carrinho", description = "Deleta produto de carrinho do usuário autenticado.")
    @DeleteMapping("/shoppingcart/{id}")
    public ResponseEntity<Boolean> deleteShopingCart(@PathVariable Long id){

        if(productOfCartService.deleteShopingCart(id))
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);


    }

    @Operation(summary = "Editar produto no carrinho", description = "Edita produto no carrinho do usuário autenticado.")
    @PutMapping("/shoppingcart/{id}")
    public ResponseEntity<Boolean> putShopingCart(@PathVariable Long id, @RequestBody ProductOfCartDTO productOfCartDTO) {

        if(productOfCartService.putShopingCart(id, productOfCartDTO))
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(true);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
    }

}
