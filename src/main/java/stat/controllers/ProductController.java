package stat.controllers;

import stat.exception.ProductNameException;
import stat.graphics.NewProductPage;
import stat.service.ProductService;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by Uğur Özkan.
 *
 * ugur.ozkan@ozu.edu.tr
 */

@Component
// Required to not run this class in a test environment
@ConditionalOnProperty(value = "java.awt.headless", havingValue = "false")
public class ProductController {

    @Autowired
    private NewProductPage productPage;

    @Autowired
    private ProductService productService;

    public void saveProduct(String productName, String productDescription, BigDecimal productPrice) {
        try {
            productService.createNewProduct(productName, productDescription, productPrice);
        } catch (ProductNameException pne) {
            System.out.println("productController pne ");
        }

        productPage.clearInputFields();
    }

}