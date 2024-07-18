package com.technet.backend.repository.inventario;
import com.technet.backend.model.entity.inventario.Producto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductoSpecifications {
    public static Specification<Producto> withFilters(
            List<String> marcas,
            List<String> categorias,
            List<String> subcategorias,
            String search) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (marcas != null && !marcas.isEmpty()) {
                predicates.add(root.get("categoriamarca").get("marca").get("nombre").in(marcas));
            }

            if (categorias != null && !categorias.isEmpty()) {
                predicates.add(root.get("subcategoria").get("categoria").get("nombre").in(categorias));
            }

            if (subcategorias != null && !subcategorias.isEmpty()) {
                predicates.add(root.get("subcategoria").get("nombre").in(subcategorias));
            }

            if (search != null && !search.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("nombre"), "%" + search + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
