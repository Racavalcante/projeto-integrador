package dh.meli.projeto_integrador.repository;

import dh.meli.projeto_integrador.model.Batch;
import dh.meli.projeto_integrador.model.Product;
import org.springframework.data.jpa.repository.Query;
import dh.meli.projeto_integrador.model.OrderEntry;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Interface IBatchRepository will manage data persistence for Batch object instances.
 * Will read, save, update and delete data through the GET, POST, PUT and DELETE requests.
 * @author Diovana Valim, Thiago Almeida
 * @version 0.0.2
 */
public interface IBatchRepository extends CrudRepository<Batch, Long> {

    /**
     * Method that find a batch by product
     * @param product an object of type Product
     * @return an object of type Batch
     */
    Batch findByProduct(Product product);

	/**
	 * Method that find a batch by product id
	 * @param id long that represents Product identifier
	 * @return a List of Batches
	 */
    @Query(value = "SELECT * FROM batch WHERE product_id = ?1", nativeQuery = true)
    List<Batch> findBatchByProductId(long id);
    
	/**
	 * Method to find all batches that belongs to a given Order Entry;
	 * @param orderEntry of type OrderEntry. OrderEntry instance;
	 * @return a List of objects of type Batch;
	 */
	List<Batch> findByOrderEntry(OrderEntry orderEntry);

	/**
	 * Method to find all batches that belongs to a given Order Entry;
	 * @param sectionId long that represents Section identifier
	 * @return a List of objects of type Batch;
	 */
	@Query(value = "SELECT * from batch " +
			"JOIN order_entry ON order_entry.id = batch.id " +
			"JOIN section ON section.id = order_entry.section_id  " +
			"WHERE section.id = ?1", nativeQuery = true)
	List<Batch> findBatchBySectionId(long sectionId);
}
