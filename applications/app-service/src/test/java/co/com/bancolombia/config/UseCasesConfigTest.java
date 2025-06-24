package co.com.bancolombia.config;

import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.log.LoggerAdapter;
import co.com.bancolombia.model.log.Logger;
import co.com.bancolombia.usecase.branch.AddBranchToFranchiseUseCase;
import co.com.bancolombia.usecase.franchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.product.UpdateProductStockUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    private UseCasesConfig config;

    @BeforeEach
    void setUp() {
        config = new UseCasesConfig();
    }

    @Test
    void shouldCreateAddBranchToFranchiseLogger() {
        Logger logger = config.addBranchToFranchiseLogger();
        assertNotNull(logger);
        assertTrue(logger instanceof LoggerAdapter);
    }

    @Test
    void shouldCreateCreateFranchiseUseCase() {
        FranchiseRepository franchiseRepository = Mockito.mock(FranchiseRepository.class);
        Logger logger = Mockito.mock(Logger.class);
        CreateFranchiseUseCase useCase = config.createFranchiseUseCase(franchiseRepository, logger);
        assertNotNull(useCase);
    }

    @Test
    void shouldCreateAddBranchToFranchiseUseCase() {
        FranchiseRepository franchiseRepository = Mockito.mock(FranchiseRepository.class);
        BranchRepository branchRepository = Mockito.mock(BranchRepository.class);
        Logger logger = Mockito.mock(Logger.class);
        AddBranchToFranchiseUseCase useCase = config.addBranchToFranchiseUseCase(franchiseRepository, branchRepository, logger);
        assertNotNull(useCase);
    }

    @Test
    void shouldCreateUpdateProductStockUseCase() {
        BranchRepository branchRepository = Mockito.mock(BranchRepository.class);
        Logger logger = Mockito.mock(Logger.class);
        UpdateProductStockUseCase useCase = config.updateProductStockUseCase(branchRepository, logger);
        assertNotNull(useCase);
    }

}