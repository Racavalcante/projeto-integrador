package dh.meli.projeto_integrador.service;

import dh.meli.projeto_integrador.exception.InternalServerErrorException;
import dh.meli.projeto_integrador.model.Batch;
import dh.meli.projeto_integrador.model.OrderEntry;
import dh.meli.projeto_integrador.repository.IBatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Class responsible for business rules and communication with the Batch Repository layer;
 * @author Diovana Valim, Thiago Almeida;
 * @version 0.0.2
 */
@Service
public class BatchService implements IBatchService {

    /**
     * Dependency Injection of the Agent Repository.
     */
    @Autowired
    private IBatchRepository batchRepository;

    /**
     * Method to save a new batch;
     * @param batch of type Batch. Batch instance;
     * @return an object of type Batch;
     */
    @Transactional
    @Override
    public Batch createBatch(Batch batch) {
        try {
            return batchRepository.save(batch);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    /**
     * Method to find all batches that belongs to a given Order Entry;
     * @param orderEntry of type OrderEntry. OrderEntry instance;
     * @return a List of objects of type Batch;
     */
    public List<Batch> findAllByOrderEntry(OrderEntry orderEntry){
        return batchRepository.findByOrderEntry(orderEntry);
    };
}
