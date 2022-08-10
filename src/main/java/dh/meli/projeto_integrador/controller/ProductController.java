package dh.meli.projeto_integrador.controller;

import dh.meli.projeto_integrador.dto.dtoOutput.ProductOutputDto;
import dh.meli.projeto_integrador.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Class responsible for intermediating the requests sent by the user with the responses provided by the Service;
 * @author Rafael Cavalcante
 * @version 0.0.1
 */
@RestController
@RequestMapping("/api/v1")
public class ProductController {

    /**
     * Dependency Injection of the ProductService.
     */
    @Autowired
    private ProductService productService;

    /**
     * A get method that when called will return in the body request a list of products present in the Database
     * @return Response Entity of type List of productDto and the corresponding HttpStatus ;
     */
    @GetMapping("/fresh-products")
    public ResponseEntity<List<ProductOutputDto>> listAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * A get method that when called will return in the body request a list of products of a specified category, present in the Database
     * @param category a String received by the URL request to determine the type of product returned
     * @return Response Entity of type List of productDto and the corresponding HttpStatus ;
     */
    @GetMapping("/fresh-products/{category}")
    public ResponseEntity<List<ProductOutputDto>> listProductByCategory(@PathVariable String category){
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
}
