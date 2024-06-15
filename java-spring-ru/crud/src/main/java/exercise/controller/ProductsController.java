package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.ProductMapper;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // BEGIN
    @Autowired
    CategoryRepository categoryRepository;
    @GetMapping("")
    public List<ProductDTO> getAll() {
        var products = productRepository.findAll();
        return products.stream().map(productMapper::map).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO get(@PathVariable long id) {
        var product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        var dto = productMapper.map(product);
        dto.setCategoryName(product.getCategory().getName());
        dto.setCategoryId(product.getCategory().getId());
        return dto;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody @Valid ProductCreateDTO createDTO) {
        var newProduct = productMapper.map(createDTO);
        productRepository.save(newProduct);
        return productMapper.map(newProduct);
    }

    @PutMapping("/{id}")
    public ProductDTO update(@RequestBody @Valid ProductUpdateDTO updateDto, @PathVariable long id) {
        var productToUpdate = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
        productMapper.update(updateDto, productToUpdate);
        if(updateDto.getCategoryId().isPresent()) {
            var category = categoryRepository.findById(updateDto.getCategoryId().get()).orElseThrow(() -> new ResourceNotFoundException("Not Found!"));
            productToUpdate.setCategory(category);
        }
        productRepository.save(productToUpdate);
        return productMapper.map(productToUpdate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        productRepository.deleteById(id);
    }

    // END
}
