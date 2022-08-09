package dh.meli.projeto_integrador.service;

import dh.meli.projeto_integrador.dto.dtoInput.CartDto;
import dh.meli.projeto_integrador.dto.dtoInput.ProductDto;
import dh.meli.projeto_integrador.dto.dtoOutput.TotalPriceDto;
import dh.meli.projeto_integrador.exception.ForbiddenException;
import dh.meli.projeto_integrador.exception.ResourceNotFoundException;
import dh.meli.projeto_integrador.model.*;
import dh.meli.projeto_integrador.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

/**
 * Class responsible for business rules and communication with the Cart Repository layer
 * @author Gabriela Azevedo
 * @version 0.0.1
 */
@Service
public class CartService implements ICartService {

    /**
     * Dependency Injection of the Cart Repository.
     */
    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IBatchRepository batchRepository;

    /**
     * Dependency Injection of the BatchCart Repository.
     */
    @Autowired
    private IProductCartRepository productCartRepository;

    /**
     * Dependency Injection of the Batch Repository.
     */
    @Autowired
    private IProductRepository productRepository;

    /**
     * Dependency Injection of the Customer Repository.
     */
    @Autowired
    private ICustomerRepository customerRepository;

    /**
     * Method that receives an object of type CartDto, build the cart object and saves on the Cart table.
     * @param cartDto
     * @return
     */
    public Cart buildCart(CartDto cartDto) {
        Customer customerById = customerRepository.findById(cartDto.getBuyerId()).get();
        Cart cart = Cart.builder()
                .date(cartDto.getDate())
                .status(cartDto.getOrderStatus())
                .customer(customerById)
                .build();

        return cartRepository.save(cart);
    }

    /**
     * Method that receives an object of type Cart and a List of objects of type ProductDto and saves the data on the BatchCart table.
     * @param savedCart an object of type Cart
     * @param productsList a list of objects of type ProductDto
     */
    public void buildProductCart(Cart savedCart, List<ProductDto> productsList) {
        productsList.forEach(product -> {
            Product productById = productRepository.findById(product.getProductId()).get();
            Batch batchById = batchRepository.findByProduct(productById);

                if (product.getQuantity() > batchById.getCurrentQuantity()) {
                   throw new ForbiddenException(String.format("The product: %s does not have enough quantity in stock.", productById.getName()));
                }

            ProductCart productCart = ProductCart.builder()
                    .cart(savedCart)
                    .product(productById)
                    .quantity(product.getQuantity())
                    .build();
            productCartRepository.save(productCart);
        });
    }

    /**
     * Method that receives a list of type ProductDto and calculates the total price of the cart products.
     * @param productsList List of objects of type ProductDto
     * @return an object of type TotalPriceDto with an attribute totalPrice of type Double.
     */
    public TotalPriceDto totalCartPrice(List<ProductDto> productsList) {
        TotalPriceDto total = new TotalPriceDto(0.0);

        productsList.forEach(product -> {
            Product productById = productRepository.findById(product.getProductId()).get();
            Batch batchById = batchRepository.findByProduct(productById);
            total.setTotalPrice(batchById.getProduct().getPrice() + total.getTotalPrice());
        });
        return total;
    }

    /**
     * Method that calls the other methods of this class and persists the info of the carts on the database and returns the total price for the user.
     * @param cartDto an object of type CartDto
     * @return an object of type TotalPriceDto with an attribute totalPrice of type Double.
     */
    @Override
    @Transactional
    public TotalPriceDto createCart(CartDto cartDto) {
        Cart savedCart = buildCart(cartDto);
        List<ProductDto> productsList = cartDto.getProducts();
        buildProductCart(savedCart, productsList);
        return totalCartPrice(productsList);
    }
}
