package dh.meli.projeto_integrador.util;

import dh.meli.projeto_integrador.dto.dtoInput.BatchDto;
import dh.meli.projeto_integrador.dto.dtoInput.OrderEntryDto;
import dh.meli.projeto_integrador.dto.dtoInput.SectionDto;
import dh.meli.projeto_integrador.dto.dtoOutput.ProductOutputDto;
import dh.meli.projeto_integrador.dto.dtoOutput.ProductStockDto;
import dh.meli.projeto_integrador.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Generators {

    public static List<BatchDto> createInboundOrder() {
        Batch batch = createBatch();

        List<BatchDto> batches = new ArrayList<BatchDto>();

        batches.add(new BatchDto(batch));

        return batches;
    }

    public static Batch createBatch() {
        Warehouse warehouse = new Warehouse();

        warehouse.setId(1);
        warehouse.setName("Armazém 01");
        warehouse.setAddress("Rua Almeida 259");

        Section section = new Section();

        section.setId(1);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(warehouse);

        HashSet<Section> sections = new HashSet<Section>();

        sections.add(section);

        warehouse.setSections(sections);

        Product product = new Product();
        product.setId(1);
        product.setPrice(12.20);
        product.setName("Maçã");
        product.setType("Fresco");

        HashSet<Product> products = new HashSet<Product>();

        products.add(product);

        OrderEntry orderEntry = new OrderEntry();

        orderEntry.setId(1);
        orderEntry.setOrderDate(LocalDate.now());
        orderEntry.setSection(section);

        HashSet<OrderEntry> orderEntries = new HashSet<OrderEntry>();

        orderEntries.add(orderEntry);

        section.setOrderEntries(orderEntries);

        Batch batch01 = new Batch();

        batch01.setId(1);
        batch01.setCurrentTemperature(10);
        batch01.setMinimumTemperature(0);
        batch01.setInitialQuantity(100);
        batch01.setCurrentQuantity(100);
        batch01.setManufacturingDate(LocalDate.now());
        batch01.setManufacturingTime(LocalTime.now());
        batch01.setDueDate(LocalDate.now());
        batch01.setProduct(product);
        batch01.setOrderEntry(orderEntry);

        HashSet<Batch> batches = new HashSet<Batch>();

        batches.add(batch01);

        orderEntry.setBatches(batches);

        return batch01;
    }


    public static OrderEntryDto createOrderEntryDto() {
        BatchDto batchDto = new BatchDto();

        batchDto.setBatchId(0);
        batchDto.setProductId(0);
        batchDto.setCurrentQuantity(100);
        batchDto.setInitialQuantity(120);
        batchDto.setCurrentTemperature(10);
        batchDto.setMinimumTemperature(0);
        batchDto.setManufacturingDate(LocalDate.now());
        batchDto.setManufacturingTime(LocalTime.now());
        batchDto.setDueDate(LocalDate.now());

        Set<BatchDto> batchDtoSet = new HashSet<BatchDto>();

        batchDtoSet.add(batchDto);

        SectionDto sectionDto = new SectionDto();

        sectionDto.setSectionId(0);
        sectionDto.setWarehouseId(0);

        OrderEntryDto orderEntryDto = new OrderEntryDto();

        orderEntryDto.setSection(sectionDto);
        orderEntryDto.setAgentId(0);
        orderEntryDto.setBatchStock(batchDtoSet);
        orderEntryDto.setOrderDate(LocalDate.now());

        return orderEntryDto;
    }

    public static OrderEntry getOrderEntry() {
        Batch batch = createBatch();

        return batch.getOrderEntry();
    }

    public static Product getProduct() {
        Batch batch = createBatch();

        return batch.getProduct();
    }

    public static Warehouse getWarehouse() {
        Batch batch = createBatch();

        return batch.getOrderEntry().getSection().getWarehouse();
    }

    public static Section getSection() {
        Batch batch = createBatch();

        return batch.getOrderEntry().getSection();
    }

    public static Section getUnavailableSection() {
        Batch batch = createBatch();

        Section section = batch.getOrderEntry().getSection();

        section.setProductType("Congelado");

        return section;
    }

    public static Agent getAgent() {
        Batch batch = createBatch();

        Set<Agent> agents = batch.getOrderEntry().getSection().getWarehouse().getAgents();
        return new ArrayList<Agent>(agents).get(0);
    }

    public static Agent getUnavailableAgent() {
        Batch batch = createBatch();

        Set<Agent> agents = batch.getOrderEntry().getSection().getWarehouse().getAgents();

        Agent agent = new ArrayList<Agent>(agents).get(0);

        Warehouse agentWarehouse = agent.getWarehouse();

        agentWarehouse.setId(50);

        return agent;
    }

    public static Batch getBatch() {
        return createBatch();
    }

    public static ProductOutputDto validProductDto1() {
        return ProductOutputDto.builder()
                .name("Maçã")
                .type("Fresco")
                .price(20.1)
                .build();
    }

    public static ProductOutputDto validProductDto2() {
        return ProductOutputDto.builder()
                .name("Uva")
                .type("Fresco")
                .price(20.1)
                .build();
    }

    public static Product validProduct1() {
        return Product.builder()
                .name("Maçã")
                .type("Fresco")
                .price(20.1)
                .build();
    }

    public static Product validProduct2() {
        return Product.builder()
                .name("sorvete")
                .type("Congelado")
                .price(20.1)
                .build();
    }

    public static List<ProductOutputDto> productDtoList() {
        List<ProductOutputDto> productList = new ArrayList<>();
        productList.add(validProductDto1());
        productList.add(validProductDto2());

        return productList;
    }

    public static List<Product> productList() {
        List<Product> productList = new ArrayList<>();
        productList.add(validProduct1());
        productList.add(validProduct2());

        return productList;
    }

    public static List<Product> emptyProductDtoList() {
        return new ArrayList<>();
    }

    public static Batch createBatchDueDate60Days() {
        Warehouse warehouse = new Warehouse();

        warehouse.setId(0);
        warehouse.setName("Armazém 01");
        warehouse.setAddress("Rua Almeida 259");

        Section section = new Section();

        section.setId(0);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(warehouse);

        HashSet<Section> sections = new HashSet<Section>();

        Agent agent = new Agent();
        agent.setId(0);
        agent.setName("João Maria");
        agent.setPhoneNumber("48 999343899");
        agent.setEmailAddress("joaomaria@gmail.com");
        agent.setWarehouse(warehouse);

        HashSet<Agent> agents = new HashSet<Agent>();
        agents.add(agent);

        sections.add(section);

        warehouse.setSections(sections);
        warehouse.setAgents(agents);

        Product product = new Product();
        product.setPrice(12.20);
        product.setId(1);
        product.setName("Maçã");
        product.setType("Fresco");

        OrderEntry orderEntry = new OrderEntry();

        orderEntry.setId(1);
        orderEntry.setOrderDate(LocalDate.now());
        orderEntry.setSection(section);

        HashSet<OrderEntry> orderEntries = new HashSet<OrderEntry>();

        orderEntries.add(orderEntry);

        section.setOrderEntries(orderEntries);

        Batch batch01 = new Batch();

        batch01.setId(1);
        batch01.setCurrentTemperature(10);
        batch01.setMinimumTemperature(0);
        batch01.setInitialQuantity(100);
        batch01.setCurrentQuantity(100);
        batch01.setManufacturingDate(LocalDate.now());
        batch01.setManufacturingTime(LocalTime.now());
        batch01.setDueDate(LocalDate.now().plusDays(60));
        batch01.setProduct(product);
        batch01.setOrderEntry(orderEntry);

        HashSet<Batch> batches = new HashSet<Batch>();

        batches.add(batch01);

        orderEntry.setBatches(batches);

        return batch01;
    }

    public static Batch createBatchDueDate90Days() {
        Warehouse warehouse = new Warehouse();

        warehouse.setId(0);
        warehouse.setName("Armazém 01");
        warehouse.setAddress("Rua Almeida 259");

        Section section = new Section();

        section.setId(0);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(warehouse);

        HashSet<Section> sections = new HashSet<Section>();

        Agent agent = new Agent();
        agent.setId(0);
        agent.setName("João Maria");
        agent.setPhoneNumber("48 999343899");
        agent.setEmailAddress("joaomaria@gmail.com");
        agent.setWarehouse(warehouse);

        HashSet<Agent> agents = new HashSet<Agent>();
        agents.add(agent);

        sections.add(section);

        warehouse.setSections(sections);
        warehouse.setAgents(agents);

        Product product = new Product();
        product.setPrice(12.20);
        product.setId(1);
        product.setName("Maçã");
        product.setType("Fresco");

        OrderEntry orderEntry = new OrderEntry();

        orderEntry.setId(1);
        orderEntry.setOrderDate(LocalDate.now());
        orderEntry.setSection(section);

        HashSet<OrderEntry> orderEntries = new HashSet<OrderEntry>();

        orderEntries.add(orderEntry);

        section.setOrderEntries(orderEntries);

        Batch batch01 = new Batch();

        batch01.setId(2);
        batch01.setCurrentTemperature(10);
        batch01.setMinimumTemperature(0);
        batch01.setInitialQuantity(100);
        batch01.setCurrentQuantity(10);
        batch01.setManufacturingDate(LocalDate.now());
        batch01.setManufacturingTime(LocalTime.now());
        batch01.setDueDate(LocalDate.now().plusDays(90));
        batch01.setProduct(product);
        batch01.setOrderEntry(orderEntry);

        HashSet<Batch> batches = new HashSet<Batch>();

        batches.add(batch01);

        orderEntry.setBatches(batches);

        return batch01;
    }

    public static Batch createBatchDueDate21Days() {
        Warehouse warehouse = new Warehouse();

        warehouse.setId(0);
        warehouse.setName("Armazém 01");
        warehouse.setAddress("Rua Almeida 259");

        Section section = new Section();

        section.setId(0);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(warehouse);

        HashSet<Section> sections = new HashSet<Section>();

        Agent agent = new Agent();
        agent.setId(0);
        agent.setName("João Maria");
        agent.setPhoneNumber("48 999343899");
        agent.setEmailAddress("joaomaria@gmail.com");
        agent.setWarehouse(warehouse);

        HashSet<Agent> agents = new HashSet<Agent>();
        agents.add(agent);

        sections.add(section);

        warehouse.setSections(sections);
        warehouse.setAgents(agents);

        Product product = new Product();
        product.setPrice(12.20);
        product.setId(1);
        product.setName("Fresco");
        product.setType("Fruta");

        OrderEntry orderEntry = new OrderEntry();

        orderEntry.setId(1);
        orderEntry.setOrderDate(LocalDate.now());
        orderEntry.setSection(section);

        HashSet<OrderEntry> orderEntries = new HashSet<OrderEntry>();

        orderEntries.add(orderEntry);

        section.setOrderEntries(orderEntries);

        Batch batch01 = new Batch();

        batch01.setId(1);
        batch01.setCurrentTemperature(10);
        batch01.setMinimumTemperature(0);
        batch01.setInitialQuantity(100);
        batch01.setCurrentQuantity(80);
        batch01.setManufacturingDate(LocalDate.now());
        batch01.setManufacturingTime(LocalTime.now());
        batch01.setDueDate(LocalDate.now().plusDays(21));
        batch01.setProduct(product);
        batch01.setOrderEntry(orderEntry);

        HashSet<Batch> batches = new HashSet<Batch>();

        batches.add(batch01);

        orderEntry.setBatches(batches);

        return batch01;
    }


    public static List<Batch> validBatchList() {
        List<Batch> batchList = new ArrayList<>();
        batchList.add(createBatchDueDate21Days());
        batchList.add(createBatchDueDate60Days());
        batchList.add(createBatchDueDate90Days());
        return batchList;
    }

    public static List<Batch> notAbleBatchList() {
        List<Batch> batchList = new ArrayList<>();
        batchList.add(createBatchDueDate21Days());
        return batchList;
    }

    public static List<Batch> emptyBatchList() {
        return new ArrayList<>();
    }

    public static ProductStockDto getProductStockDtos() {
        return new ProductStockDto(getProduct(), validBatchList());

    }

    public static Warehouse createWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1);
        warehouse.setName("Armazém 01");
        warehouse.setAddress("Rua Almeida 259");
        return warehouse;
    }

    public static Section createSectionFresco(){
        Section section = new Section();
        section.setId(1);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(createWarehouse());
        return section;
    }

    public static Section createSectionRefrigerado(){
        Section section = new Section();
        section.setId(2);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(2000);
        section.setProductType("Fresco");
        section.setWarehouse(createWarehouse());
        return section;
    }

    public static Section createSectionCongelado(){
        Section section = new Section();
        section.setId(3);
        section.setCurrentProductLoad(130);
        section.setMaxProductLoad(1000);
        section.setProductType("Fresco");
        section.setWarehouse(createWarehouse());
        return section;
    }

    public static Agent createAgent(){
        Agent agent = new Agent();
        agent.setId(1);
        agent.setName("João Maria");
        agent.setPhoneNumber("48 999343899");
        agent.setEmailAddress("joaomaria@gmail.com");
        agent.setWarehouse(createWarehouse());
        return agent;
    }

    public static OrderEntry createOrderEntry(){
        OrderEntry orderEntry = new OrderEntry();
        orderEntry.setId(1);
        orderEntry.setOrderDate(LocalDate.now());
        orderEntry.setSection(createSectionFresco());
        return orderEntry;
    }
}
