package co.com.bancolombia.driven.adapter.mongo.mapper;

import co.com.bancolombia.driven.adapter.mongo.entity.BranchDocument;
import co.com.bancolombia.driven.adapter.mongo.entity.ProductDocument;
import co.com.bancolombia.model.Branch;
import co.com.bancolombia.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BranchMapper {

    Branch toModel(BranchDocument document);
    BranchDocument toDocument(Branch model);

    List<Branch> toModelList(List<BranchDocument> documents);

    Product toProductModel(ProductDocument document);
    ProductDocument toProductDocument(Product model);
}
