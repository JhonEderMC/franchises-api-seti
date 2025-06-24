package co.com.bancolombia.driven.adapter.mongo;

import co.com.bancolombia.driven.adapter.mongo.entity.FranchiseDocument;
import co.com.bancolombia.driven.adapter.mongo.mapper.FranchiseMapper;
import co.com.bancolombia.driven.adapter.mongo.repository.MongoFranchiseRepository;
import co.com.bancolombia.model.Franchise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FranchiseMongoAdapterTest {

    MongoFranchiseRepository repository = mock(MongoFranchiseRepository.class);
    FranchiseMapper mapper = mock(FranchiseMapper.class);
    FranchiseMongoAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new FranchiseMongoAdapter(repository, mapper);
    }

    @Test
    void shouldSaveFranchise() {
        Franchise franchise = Franchise.builder().id("1").build();
        FranchiseDocument doc = FranchiseDocument.builder().id("1").build();
        when(mapper.toDocument(franchise)).thenReturn(doc);
        when(repository.save(doc)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(franchise);

        Mono<Franchise> result = adapter.save(franchise);

        assertEquals(franchise, result.block());
        verify(mapper).toDocument(franchise);
        verify(repository).save(doc);
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldFindById() {
        String id = "1";
        FranchiseDocument doc = FranchiseDocument.builder().id("1").build();
        Franchise franchise = Franchise.builder().id(id).build();
        when(repository.findById(id)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(franchise);

        Mono<Franchise> result = adapter.findById(id);

        assertEquals(franchise, result.block());
        verify(repository).findById(id);
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldFindAll() {
        FranchiseDocument doc = FranchiseDocument.builder().build();
        Franchise franchise = Franchise.builder().id("1").build();
        when(repository.findAll()).thenReturn(Flux.just(doc));
        when(mapper.toModel(doc)).thenReturn(franchise);

        Flux<Franchise> result = adapter.findAll();

        assertEquals(franchise, result.blockFirst());
        verify(repository).findAll();
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldUpdateFranchise() {
        Franchise franchise = Franchise.builder().id("1").build();
        FranchiseDocument doc = FranchiseDocument.builder().build();
        when(mapper.toDocument(franchise)).thenReturn(doc);
        when(repository.save(doc)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(franchise);

        Mono<Franchise> result = adapter.update(franchise);

        assertEquals(franchise, result.block());
        verify(mapper).toDocument(franchise);
        verify(repository).save(doc);
        verify(mapper).toModel(doc);
    }

}