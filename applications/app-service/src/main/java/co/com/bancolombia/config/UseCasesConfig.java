package co.com.bancolombia.config;

import co.com.bancolombia.gateway.BranchRepository;
import co.com.bancolombia.gateway.FranchiseRepository;
import co.com.bancolombia.log.LoggerAdapter;
import co.com.bancolombia.model.log.Logger;
import co.com.bancolombia.usecase.branch.AddBranchToFranchiseUseCase;
import co.com.bancolombia.usecase.branch.UpdateBranchNameUseCase;
import co.com.bancolombia.usecase.franchise.CreateFranchiseUseCase;
import co.com.bancolombia.usecase.franchise.UpdateFranchiseNameUseCase;
import co.com.bancolombia.usecase.product.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase", //TODO: modificar si se cambia co.com.bancolombia.usecase por otro paquete
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

    @Bean
    @Qualifier("addBranchToFranchiseLogger")
    public Logger addBranchToFranchiseLogger() {
        return new LoggerAdapter(AddBranchToFranchiseUseCase.class);
    }

    @Bean
    @Qualifier("updateBranchNameLogger")
    public Logger updateBranchNameLogger() {
        return new LoggerAdapter(UpdateBranchNameUseCase.class);
    }

    @Bean
    @Qualifier("createFranchiseLogger")
    public Logger createFranchiseLogger() {
        return new LoggerAdapter(CreateFranchiseUseCase.class);
    }

    @Bean
    @Qualifier("updateFranchiseNameLogger")
    public Logger updateFranchiseNameLogger() {
        return new LoggerAdapter(UpdateFranchiseNameUseCase.class);
    }

    @Bean
    @Qualifier("addProductToBranchLogger")
    public Logger addProductToBranchLogger() {
        return new LoggerAdapter(AddProductToBranchUseCase.class);
    }

    @Bean
    @Qualifier("getMaxStockProductsByFranchiseLogger")
    public Logger getMaxStockProductsByFranchiseLogger() {
        return new LoggerAdapter(GetMaxStockProductsByFranchiseUseCase.class);
    }

    @Bean
    @Qualifier("removeProductFromBranchLogger")
    public Logger removeProductFromBranchLogger() {
        return new LoggerAdapter(RemoveProductFromBranchUseCase.class);
    }

    @Bean
    @Qualifier("updateProductNameLogger")
    public Logger updateProductNameLogger() {
        return new LoggerAdapter(UpdateProductNameUseCase.class);
    }

    @Bean
    @Qualifier("updateProductStockLogger")
    public Logger updateProductStockLogger() {
        return new LoggerAdapter(UpdateProductStockUseCase.class);
    }

    @Bean
    public CreateFranchiseUseCase createFranchiseUseCase(FranchiseRepository franchiseRepository, @Qualifier("createFranchiseLogger") Logger logger) {
        return new CreateFranchiseUseCase(franchiseRepository, logger);
    }

    @Bean
    public UpdateFranchiseNameUseCase updateFranchiseNameUseCase(FranchiseRepository franchiseRepository, @Qualifier("updateFranchiseNameLogger") Logger logger) {
        return new UpdateFranchiseNameUseCase(franchiseRepository, logger);
    }

    @Bean
    public AddBranchToFranchiseUseCase addBranchToFranchiseUseCase(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            @Qualifier("addBranchToFranchiseLogger") Logger logger) {
        return new AddBranchToFranchiseUseCase(franchiseRepository, branchRepository, logger);
    }

    @Bean
    public UpdateBranchNameUseCase updateBranchNameUseCase(BranchRepository branchRepository, @Qualifier("updateBranchNameLogger") Logger logger) {
        return new UpdateBranchNameUseCase(branchRepository, logger);
    }

    @Bean
    public AddProductToBranchUseCase addProductToBranchUseCase(BranchRepository branchRepository, @Qualifier("addProductToBranchLogger") Logger logger) {
        return new AddProductToBranchUseCase(branchRepository, logger);
    }

    @Bean
    public RemoveProductFromBranchUseCase removeProductFromBranchUseCase(BranchRepository branchRepository, @Qualifier("removeProductFromBranchLogger") Logger logger) {
        return new RemoveProductFromBranchUseCase(branchRepository, logger);
    }

    @Bean
    public UpdateProductStockUseCase updateProductStockUseCase(BranchRepository branchRepository, @Qualifier("updateProductStockLogger") Logger logger) {
        return new UpdateProductStockUseCase(branchRepository, logger);
    }

    @Bean
    public UpdateProductNameUseCase updateProductNameUseCase(BranchRepository branchRepository, @Qualifier("updateProductNameLogger") Logger logger) {
        return new UpdateProductNameUseCase(branchRepository, logger);
    }

    @Bean
    public GetMaxStockProductsByFranchiseUseCase getMaxStockProductsByFranchiseUseCase(
            FranchiseRepository franchiseRepository,
            BranchRepository branchRepository,
            @Qualifier("getMaxStockProductsByFranchiseLogger") Logger logger) {
        return new GetMaxStockProductsByFranchiseUseCase(franchiseRepository, branchRepository, logger);
    }
}
