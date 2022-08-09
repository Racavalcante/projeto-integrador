package dh.meli.projeto_integrador.dto.dtoOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * Method Getter implemented by Lombok lib for get access the private attributes of the
 * ListProductByWarehouse Class
 */
@Getter
/**
 * Method Setter implemented by Lombok lib for set the private attributes of the
 * ListProductByWarehouse Class
 */
@Setter
/**
 * Method Constructor with all arguments implemented by Lombok lib
 */
@AllArgsConstructor
/**
 * Class used to create a Data Transfer Object for ListProductByWarehouse POJO
 * @author Diovana Valim
 * @version 0.0.1
 * @see java.lang.Object
 */
public class ListProductByWarehouse {
    private long productId;

    private Set<TotalProductByWarehouseDto> warehouses;
}
