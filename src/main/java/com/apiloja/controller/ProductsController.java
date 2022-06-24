package com.apiloja.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.apiloja.dto.productdto.EvaluationProductDTO;
import com.apiloja.dto.productdto.ProductDTO;
import com.apiloja.model.products.Product;

import com.apiloja.service.product.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;

import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController()
@CrossOrigin()
@Tag(name = "Produtos")
@RequestMapping(value = "/products/")
public class ProductsController {

    final ProductService  productService;

    public ProductsController(ProductService productService) {

        this.productService = productService;

    }
    //@PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Listar de produtos", description = "Retorna paginas contendo listas de todos produtos, apenas para administrador.")
    @GetMapping("/all/")
    public ResponseEntity<Page<ProductDTO>> getAll(@PageableDefault(page = 0, size = 10, sort = "name") Pageable paging) {

        //https://howtodoinjava.com/spring-boot2/pagination-sorting-example/

        return ResponseEntity.status(HttpStatus.OK).body(productService.getAll(paging));
    }

    @Operation(summary = "Listar produtos para a home page", description = "Retorna 5 produtos de cada categoria de produtos.")
    @GetMapping("/home")
    public ResponseEntity<Map<String, List<Product>>> getHome( Pageable paging) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.getHome());

    }

    @Operation(summary = "Busca rápida de produtos.", description = "Retorna uma lista de 4 produtos. É usado para o autocompletar de busca.")
    @GetMapping("/fast/{nameProduct}")
    public ResponseEntity<List<ProductDTO>> faindFast(@PathVariable String nameProduct){

        return ResponseEntity.status(HttpStatus.OK).body(productService.faindFast(nameProduct));
    }

    @Operation(summary = "Listar produtos por categoria.", description = "Retorna paginas de produtos de uma categoria.")
    @GetMapping("/category/{category}/")
    public ResponseEntity<Page<ProductDTO>> getProductForCategory(
            @SortDefault(sort = "status")
            @PageableDefault(page = 0, size = 10) Pageable page,
            @PathVariable String category) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductForCategory(page, category));

    }

    @Operation(summary = "Listar produtos por busca.", description = "Retorna paginas de produtos que incluem o nome buscado.")
    @GetMapping("/search/{name}/")
    public ResponseEntity<Page<ProductDTO>> getProductForName(
            @SortDefault(sort = "status")
            @PageableDefault(size = 10, page = 0) Pageable page,
            @PathVariable String name) {

        return ResponseEntity.status(HttpStatus.OK).body(productService.getProductForName(name, page));

    }

    @Operation(summary = "Buscar por id", description = "Retorna um produto por id.")
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProduct(@PathVariable Long id) {

        if (productService.getProduct(id).isPresent()) {
            var productDTO = new ProductDTO();
            BeanUtils.copyProperties(productService.getProduct(id).get(), productDTO);

            return ResponseEntity.status(HttpStatus.OK).body(productDTO);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

    }

    @Operation(summary = "Salvar produto", description = "Salva produto, somente administrador.")
    @PostMapping("/")
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody ProductDTO productDTO) {

        try {
            var product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.saveProduct(product));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Operation(summary = "Editar produto", description = "Edita produto, somente administrador.")
    @PutMapping("/{id}")
    public ResponseEntity<Product> editProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long id) {
        try {
            var product = new Product();
            BeanUtils.copyProperties(productDTO, product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productService.editProduct(product, id));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Salvar avaliação de produto", description = "Salvar avaliação de produto já entregue")
    @PostMapping("/evaluation/")
    public ResponseEntity<String> postEvaluation(@RequestBody EvaluationProductDTO evaluationProductDTO){

        if( productService.salveEvaluation(evaluationProductDTO))
            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliação salva");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao salvar avalição");
    }

    @Operation(summary = "Ediatr avaliação de produto", description = "Edita avaliação de produto já entregue")
    @PutMapping("/evaluation/")
    public ResponseEntity<String> putEvaluation(@RequestBody EvaluationProductDTO evaluationProductDTO){

        if( productService.salveEvaluation(evaluationProductDTO))
            return ResponseEntity.status(HttpStatus.CREATED).body("Avaliação editada");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao editar avalição");
    }

    @Operation(summary = "Listar avaliações de produto", description = "Retornar uma lista de avaliações de produtos")
    @GetMapping("/evaluation/{productId}")
    public ResponseEntity<List<EvaluationProductDTO>> getEvaluations(@PathVariable Long productId){
      return  ResponseEntity.status(HttpStatus.OK).body(productService.getEvaluations(productId));
    }

    @Operation(summary = "Listar avaliações de produto por usuário", description = "Retornar uma lista de avaliações de produtos do usuário logado")
    @GetMapping("/evaluation/byusers/")
    public ResponseEntity<List<EvaluationProductDTO>> getEvaluationsByUser(){
        return  ResponseEntity.status(HttpStatus.OK).body(productService.getEvaluationsByUser());
    }

    @Operation(summary = "Apagar avaliação por usuário", description = "Apaga avaliação feita por usuário.")
    @DeleteMapping("/evaluation/byusers/{id}")
    public ResponseEntity<String> deleteEvaluation(@PathVariable Long id) {

        if(productService.deleteEvaluation(id)){
            return ResponseEntity.status(HttpStatus.OK).body("Avaliação apagada");
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro");
        }
    }

    @Operation(summary = "Apagar produto", description = "Apaga produto, somente administrador.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable Long id) {

        if(productService.deleteProduct(id)){
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }




}
