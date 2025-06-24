package co.com.bancolombia.driven.adapter.mongo;

import co.com.bancolombia.driven.adapter.mongo.entity.BranchDocument;
import co.com.bancolombia.driven.adapter.mongo.mapper.BranchMapper;
import co.com.bancolombia.driven.adapter.mongo.repository.MongoBranchRepository;
import co.com.bancolombia.model.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BranchMongoAdapterTest {

    MongoBranchRepository repository = mock(MongoBranchRepository.class);
    BranchMapper mapper = mock(BranchMapper.class);
    BranchMongoAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BranchMongoAdapter(repository, mapper);
    }

    @Test
    void shouldSaveBranch() {
        Branch branch = Branch.builder().id("1").build();
        BranchDocument doc = BranchDocument.builder().id(branch.getId()).name(branch.getName()).franchiseId(branch.getFranchiseId()).build();
        when(mapper.toDocument(branch)).thenReturn(doc);
        when(repository.save(doc)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(branch);

        Mono<Branch> result = adapter.save(branch);

        assertEquals(branch, result.block());
        verify(mapper).toDocument(branch);
        verify(repository).save(doc);
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldFindById() {
        String id = "1";
        BranchDocument doc = BranchDocument.builder().id(id).build();
        Branch branch = Branch.builder().id(id).build();
        when(repository.findById(id)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(branch);

        Mono<Branch> result = adapter.findById(id);

        assertEquals(branch, result.block());
        verify(repository).findById(id);
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldFindByFranchiseId() {
        String franchiseId = "f1";
        BranchDocument doc = BranchDocument.builder().build();
        Branch branch = Branch.builder().id("1").build();
        when(repository.findByFranchiseId(franchiseId)).thenReturn(Flux.just(doc));
        when(mapper.toModel(doc)).thenReturn(branch);

        Flux<Branch> result = adapter.findByFranchiseId(franchiseId);

        assertEquals(branch, result.blockFirst());
        verify(repository).findByFranchiseId(franchiseId);
        verify(mapper).toModel(doc);
    }

    @Test
    void shouldUpdateBranch() {
        Branch branch = Branch.builder().id("1").build();
        BranchDocument doc = BranchDocument.builder().build();
        when(mapper.toDocument(branch)).thenReturn(doc);
        when(repository.save(doc)).thenReturn(Mono.just(doc));
        when(mapper.toModel(doc)).thenReturn(branch);

        Mono<Branch> result = adapter.update(branch);

        assertEquals(branch, result.block());
        verify(mapper).toDocument(branch);
        verify(repository).save(doc);
        verify(mapper).toModel(doc);
    }

}