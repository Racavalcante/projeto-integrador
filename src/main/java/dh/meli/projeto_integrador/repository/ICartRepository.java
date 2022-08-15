package dh.meli.projeto_integrador.repository;

import dh.meli.projeto_integrador.enumClass.PurchaseOrderStatusEnum;
import dh.meli.projeto_integrador.model.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface ICartRepository will manage data persistence for Cart object instances.
 * Will read, save, update and delete data through the GET, POST, PUT and DELETE requests.
 * @author Gabriela Azevedo
 * @version 0.0.1
 */
public interface ICartRepository extends CrudRepository<Cart, Long> {

    /**
     * Method for to find a Cart by their status
     * @param status PurchaseOrderStatusEnum
     * @return an object of type Product filtered by status
     */
   List<Cart> findByStatus(PurchaseOrderStatusEnum status);
}
