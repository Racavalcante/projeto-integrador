package dh.meli.projeto_integrador.dto.dtoOutput;

import dh.meli.projeto_integrador.enumClass.PurchaseOrderStatusEnum;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
 * Class used to create a Data Transfer Object for Output Cart POJO
 * @author Rafael Cavalcante
 * @version 0.0.1
 * @see java.lang.Object
 */
public class CartOutputDto {
    private String customerEmail;
    private PurchaseOrderStatusEnum status;
    private LocalDate date;
    private List<CartProductsOutputDto> products;
    private Double total;
}
