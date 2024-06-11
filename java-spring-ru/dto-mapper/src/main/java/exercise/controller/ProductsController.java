package exercise.controller;

import exercise.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @Autowired
    private ProductMapper mapper;

    @GetMapping("")
    public List<ProductDTO> getAll() {

        var products = productRepository.findAll();
        List<ProductDTO> dtos = new ArrayList<>();
        for (Product p : products) {
            dtos.add(mapper.map(p));
        }

        return dtos;
    }

    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));

        return mapper.map(product);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO createDTO) {
        var newProduct = mapper.map(createDTO);
        productRepository.save(newProduct);
        return mapper.map(newProduct);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable long id, @RequestBody ProductUpdateDTO createDTO) {
        var productToUpdate = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found"));
        mapper.update(createDTO, productToUpdate);
        productRepository.save(productToUpdate);

        return mapper.map(productToUpdate);
    }


    // END
}
