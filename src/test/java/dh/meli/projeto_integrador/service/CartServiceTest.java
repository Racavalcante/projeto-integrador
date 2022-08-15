package dh.meli.projeto_integrador.service;

import dh.meli.projeto_integrador.dto.dtoInput.CartDto;

import dh.meli.projeto_integrador.dto.dtoInput.ProductDto;
import dh.meli.projeto_integrador.dto.dtoOutput.CartOutputDto;
import dh.meli.projeto_integrador.dto.dtoOutput.UpdateStatusDto;

import dh.meli.projeto_integrador.exception.ResourceNotFoundException;
import dh.meli.projeto_integrador.model.Cart;
import dh.meli.projeto_integrador.model.Customer;
import dh.meli.projeto_integrador.repository.ICartRepository;
import dh.meli.projeto_integrador.repository.ICustomerRepository;
import dh.meli.projeto_integrador.repository.IProductRepository;
import dh.meli.projeto_integrador.util.Generators;
import dh.meli.projeto_integrador.utils.GenerateCart;
import dh.meli.projeto_integrador.utils.GenerateCustomer;


import dh.meli.projeto_integrador.dto.dtoOutput.TotalPriceDto;
import dh.meli.projeto_integrador.exception.ForbiddenException;
import dh.meli.projeto_integrador.model.*;
import dh.meli.projeto_integrador.repository.*;
import dh.meli.projeto_integrador.utils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CartServiceTest {

    @InjectMocks
    CartService cartService;

    @Mock
    ICartRepository cartRepository;

    @Mock
    ICustomerRepository customerRepository;

    @Mock
    IProductRepository productRepository;

    @Mock
    IProductCartRepository productCartRepository;

    @Mock
    IBatchRepository batchRepository;

    @Test
    void createCart() {
        Cart newCartWithId = GenerateCart.newCartWithId1();
        Customer newCustomer = GenerateCustomer.newCustomer1();
        Product newProduct = GenerateProduct.newProduct1();
//        Batch newBatch = GenerateBatch.newBatch1();
        ProductDto newProductDto = GenerateProduct.newProductDto();
        CartDto newCartDto = GenerateCart.newCartDto1();
        ProductCart newProductCart = GenerateProductCart.newProductCart1();

        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(newCustomer));
        BDDMockito.when(cartRepository.save(ArgumentMatchers.any(Cart.class)))
                .thenReturn(newCartWithId);
        BDDMockito.when(productRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(newProduct));
        BDDMockito.when(batchRepository.findTotalQuantityByProductId(ArgumentMatchers.anyLong()))
                .thenReturn(20);
        BDDMockito.when(productCartRepository.save(ArgumentMatchers.any(ProductCart.class)))
                .thenReturn(newProductCart);


        TotalPriceDto result = cartService.createCart(newCartDto);

        assertThat(result.getTotalPrice()).isEqualTo(newProduct.getPrice() * newProductDto.getQuantity());
        verify(customerRepository, atLeastOnce()).findById(1L);
        verify(cartRepository, atLeastOnce()).save(ArgumentMatchers.any(Cart.class));
        verify(productCartRepository, atLeastOnce()).save(ArgumentMatchers.any(ProductCart.class));
    }

    @Test
    void create_returnException_whenQuantityIsNotValid() {
        Cart newCartWithId = GenerateCart.newCartWithId1();
        Customer newCustomer = GenerateCustomer.newCustomer1();
        Product newProduct = GenerateProduct.newProduct1();
        Batch newBatch = GenerateBatch.newBatchInvalidQuantity();
        CartDto cartDto = GenerateCartDto.newCartDto();

        BDDMockito.when(customerRepository.findById(ArgumentMatchers.anyLong()))
            .thenReturn(Optional.of(newCustomer));
        BDDMockito.when(cartRepository.save(ArgumentMatchers.any(Cart.class)))
                .thenReturn(newCartWithId);
        BDDMockito.when(productRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(newProduct));
        BDDMockito.when(batchRepository.findByProduct(ArgumentMatchers.any(Product.class)))
                .thenReturn(newBatch);

        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            cartService.createCart(cartDto);
        });

        assertThat(exception.getMessage()).isEqualTo("The product: [Morango] does not have enough quantity in stock.");
        verify(cartRepository, never()).save(GenerateCart.newCart1());
    }

    @Test
    @DisplayName("Test update with success")
    void updateStatusCartWithSuccess() {
        BDDMockito.when(cartRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(GenerateCart.newCartWithId1()));

        UpdateStatusDto result = cartService.updateStatusCart(GenerateCart.newCartWithId1().getId());

        assertThat(result.getMessage()).isEqualTo("Cart Finished successfully");
        verify(cartRepository, atLeastOnce()).findById(1L);
    }


    @Test
    @DisplayName("Tests function when not found cart")
    void updateStatusCartNotFound() {

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartService.updateStatusCart(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Cart not found with this id");
        verify(cartRepository, never()).save(GenerateCart.newCart1());
    }

    @Test
    @DisplayName("Tests exception when cart status is already finished")
    void update_returnException_WhenCartStatusAlreadyFinished() {
        BDDMockito.when(cartRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(GenerateCart.newCartWithId2()));

            ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
                cartService.updateStatusCart(1L);
        });

        assertThat(exception.getMessage()).isEqualTo("Cart already Finished");
        verify(cartRepository, never()).save(GenerateCart.newCart1());
    }

    @Test
    void getCartById_WhenCartIsNotFound_ReturnException() {
        BDDMockito.when(cartRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        long id = 0;
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            cartService.getCartById(id);
        });

        assertThat(exception.getMessage()).isEqualTo("Cart not found with this id");

        verify(cartRepository, atLeastOnce()).findById(id);
    }

    @Test
    void getCartById_WhenCartIsFound_ReturnACartOutputDto() {
        BDDMockito.when(cartRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Generators.validCart1()));

        BDDMockito.when(customerRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(Generators.validCustomer1()));

        BDDMockito.when(productRepository.findById(1L))
                .thenReturn(Optional.ofNullable(Generators.validProduct1()));

        BDDMockito.when(productRepository.findById(2L))
                .thenReturn(Optional.ofNullable(Generators.validProduct2()));

        long id = 1;
         CartOutputDto cart = cartService.getCartById(id);


        assertThat(cart.getCustomerEmail()).isEqualTo(Generators.validCustomer1().getEmailAddress());
        assertThat(cart.getStatus()).isEqualTo(Generators.validCart1().getStatus());
        assertThat(cart.getDate()).isEqualTo(Generators.validCart1().getDate());
        assertThat(cart.getProducts().size()).isEqualTo(2);
        assertThat(cart.getProducts().get(0).getName()).isEqualTo(Generators.validProduct1().getName());
        assertThat(cart.getProducts().get(0).getQuantity()).isEqualTo(Generators.validProductCart1().getQuantity());
        assertThat(cart.getProducts().get(0).getType()).isEqualTo(Generators.validProduct1().getType());
        assertThat(cart.getProducts().get(0).getPrice()).isEqualTo(Generators.validProduct1().getPrice());
        assertThat(cart.getProducts().get(0).getSubtotal())
                .isEqualTo(Generators.validProduct1().getPrice() * Generators.validProductCart1().getQuantity());
        assertThat(cart.getTotal())
                .isEqualTo(Generators.validProduct1().getPrice() * Generators.validProductCart1().getQuantity()
                + Generators.validProduct2().getPrice() * Generators.validProductCart2().getQuantity());

        verify(cartRepository, atLeastOnce()).findById(id);
        verify(customerRepository, atLeastOnce()).findById(id);
        verify(productRepository, atLeastOnce()).findById(1L);
        verify(productRepository, atLeastOnce()).findById(2L);
    }
}
