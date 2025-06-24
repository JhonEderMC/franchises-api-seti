package co.com.bancolombia.driven.adapter.mongo.mapper;

import co.com.bancolombia.driven.adapter.mongo.entity.FranchiseDocument;
import co.com.bancolombia.model.Franchise;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FranchiseMapper {

    @Mapping(target = "branches", ignore = true)
    Franchise toModel(FranchiseDocument document);

    @Mapping(target = "branches", ignore = true)
    FranchiseDocument toDocument(Franchise model);

    List<Franchise> toModelList(List<FranchiseDocument> documents);
}
