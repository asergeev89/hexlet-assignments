package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @GetMapping("")
    public List<Product> getProducts(@RequestParam Map<String, String> params) {
        if (params.isEmpty()) {
            return productRepository.findAll();
        }

        String min = params.get("min");
        String max = params.get("max");

        if (min != null && max != null) {
            return productRepository.findByPriceBetweenOrderByPrice(Integer.parseInt(min), Integer.parseInt(max));
        }

        return productRepository.findByPriceOrderByPrice(min != null ? Integer.parseInt(min) : Integer.parseInt(max));

    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
