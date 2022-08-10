package dh.meli.projeto_integrador.service;

import dh.meli.projeto_integrador.dto.dtoOutput.ProductOutputDto;
import dh.meli.projeto_integrador.model.Product;

import java.util.List;

/**
 * Interface to specify service methods implemented on ProductService class.
 * @author Rafael Cavalcante
 * @version 0.0.1
 */
public interface IProductService {
    /**
     * Method for to get all products
     * @return a list of objects of type ProductOutputDto
     */
    List<ProductOutputDto> getAllProducts();

    /**
     * Method for to get products by category
     * @param category String
     * @return a list of objects of type ProductOutputDto
     */
    List<ProductOutputDto> getProductsByCategory(String category);

    /**
     * Method for to find product by id
     * @param id long
     * @return an object of type Product
     */
    Product findProduct(long id);
}
