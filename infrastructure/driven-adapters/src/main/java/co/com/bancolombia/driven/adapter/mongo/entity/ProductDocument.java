package co.com.bancolombia.driven.adapter.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {
    private String id;
    private String name;
    private int stock;
    private String branchId;
}
