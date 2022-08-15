package dh.meli.projeto_integrador.integration;

import dh.meli.projeto_integrador.model.Cart;
import dh.meli.projeto_integrador.model.Customer;
import dh.meli.projeto_integrador.model.Product;
import dh.meli.projeto_integrador.model.ProductCart;
import dh.meli.projeto_integrador.repository.ICartRepository;
import dh.meli.projeto_integrador.repository.ICustomerRepository;
import dh.meli.projeto_integrador.repository.IProductCartRepository;
import dh.meli.projeto_integrador.repository.IProductRepository;
import dh.meli.projeto_integrador.util.Generators;
import com.fasterxml.jackson.databind.ObjectMapper;
import dh.meli.projeto_integrador.dto.dtoInput.CartDto;
import dh.meli.projeto_integrador.dto.dtoOutput.TotalPriceDto;
import dh.meli.projeto_integrador.dto.dtoOutput.UpdateStatusDto;
import dh.meli.projeto_integrador.model.Batch;
import dh.meli.projeto_integrador.repository.IBatchRepository;
import dh.meli.projeto_integrador.utils.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CartIntegrationTest {

    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private IProductCartRepository productCartRepository;

    @Autowired
    private IProductRepository productRepository;


    @Autowired
    private IBatchRepository batchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        productCartRepository.deleteAll();
        cartRepository.deleteAll();
    }

    @Test
    public void findCartById_ReturnListOfProducts_whenFindAllSuccess() throws Exception {
        Cart cart = Generators.validCartWhitoutProductCart();
        Customer customer = Generators.validCustomer1();
        Product product1 = Generators.validProduct1();
        Product product2 = Generators.validProduct2();
        ProductCart productCart1 = Generators.validProductCart1();
        ProductCart productCart2 = Generators.validProductCart2();

        Customer savedCustomer = customerRepository.save(customer);
        Cart savedCart = cartRepository.save(cart);
        Iterable<Cart> allcarts = cartRepository.findAll();
        Product savedproduct1 = productRepository.save(product1);
        Product savedproduct2 =productRepository.save(product2);
        productCart1.setCart(savedCart);
        productCart2.setCart(savedCart);
        productCart1.setProduct(savedproduct1);
        productCart2.setProduct(savedproduct2);
        ProductCart savedProductCart1 = productCartRepository.save(productCart1);
        ProductCart savedProductCart2 = productCartRepository.save(productCart2);

        ResultActions response = mockMvc.perform(
                get("/api/v1/fresh-products/orders/{id}", savedCart.getId())
                        .contentType(MediaType.APPLICATION_JSON));


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.total",
                        CoreMatchers.is(Generators.validCartDto().getTotal())))
                .andExpect(jsonPath("$.customerEmail",
                        CoreMatchers.is(Generators.validCartDto().getCustomerEmail())));
    }

    @Test
    public void findAll_ReturnEmptylist_whenProductsDoesntExists() throws Exception {

        ResultActions response = mockMvc.perform(
                get("/api/v1/fresh-products//orders/{id}", Generators.validCart1().getId())
                        .contentType(MediaType.APPLICATION_JSON));


        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(String.format("Cart not found with this id"))))
                .andExpect(jsonPath("$.status",
                        CoreMatchers.is(404)))
                .andExpect(jsonPath("$.title",
                        CoreMatchers.is("Resource Not Found")));
    }

    @Test
    public void create_returnTotalPrice_whenPurchaseOrder() throws Exception {
        CartDto cartDto = GenerateCartDto.newCartDto();
        TotalPriceDto totalPriceDto = GenerateTotalPrice.newTotalPrice();
        Customer customer = GenerateCustomer.newCustomer1();
        customerRepository.save(customer);
        Product product = GenerateProduct.newProduct1();
        Product newProduct = productRepository.save(product);
        Batch batch = GenerateBatch.newBatch1();
        batch.setProduct(newProduct);
        batchRepository.save(batch);
        cartDto.getProducts().get(0).setProductId(newProduct.getId());

        ResultActions response = mockMvc.perform(post("/api/v1/fresh-products/orders")
                .content(objectMapper.writeValueAsString(cartDto))
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice", CoreMatchers.is(totalPriceDto.getTotalPrice())));
    }

    @Test
    public void update_returnCartStatus_whenCartAlreadyExists() throws Exception{
        Customer customer = GenerateCustomer.newCustomer1();
        customerRepository.save(customer);
        Cart cart = GenerateCart.newCartWithId1();
        cartRepository.save(cart);
        UpdateStatusDto updateStatusDto = GenerateUpdateStatusDto.newUpdateStatusDto();

        ResultActions response = mockMvc.perform(put("/api/v1/fresh-products/{id}", GenerateCart.newCartWithId1().getId())
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.message",
                        CoreMatchers.is(updateStatusDto.getMessage())));
    }

}


