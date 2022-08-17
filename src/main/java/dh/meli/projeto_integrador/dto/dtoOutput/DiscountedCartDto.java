package dh.meli.projeto_integrador.dto.dtoOutput;

import lombok.*;

/**
 * Method Getter implemented by Lombok lib for get access the private attributes of the Product Class
 */
@Getter
/**
 * Method Setter implemented by Lombok lib for set the private attributes of the Product Class
 */
@Setter
/**
 * Method Default Constructor implemented by Lombok lib
 */
@NoArgsConstructor
/**
 * Method Constructor with all arguments implemented by Lombok lib
 */
@AllArgsConstructor
/**
 * Method builder implemented by Lombok lib
 */
@Builder
/**
 * Class used to create a Data Transfer Object for used on CartOutputDto POJO
 * @author Rafael Cavalcante
 * @version 0.0.1
 * @see java.lang.Object
 */
public class DiscountedCartDto {
    private CartOutputDto cart;
    private String discount;
    private Double newPrice;
}
