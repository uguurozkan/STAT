package stat.ui.product.main;

import stat.domain.Product;
import stat.service.ProductService;
import stat.service.exception.ProductNameException;
import stat.service.exception.SoldProductDeletionException;
import stat.ui.Page;
import stat.ui.PageController;
import stat.ui.product.detail.ProductDetailController;
import stat.ui.product.main.view.ProductAddPage;
import stat.ui.product.main.view.ProductMainPage;
import stat.ui.product.main.view.helper.ProductColType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Created by Uğur Özkan.
 * <p>
 * ugur.ozkan@ozu.edu.tr
 */

@Component
// Required to not run this class in a test environment
@ConditionalOnProperty(value = "java.awt.headless", havingValue = "false")
public class ProductController implements PageController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMainPage productMainPage;

    @Autowired
    private ProductDetailController productDetailController;

    private ArrayList<Integer> productIDList = new ArrayList<>();

    public void saveProduct(ProductAddPage productAddPage, String productName, String productDescription, BigDecimal productPrice) {
        if (productPrice.signum() == -1) {
            productAddPage.displayValidationError();
            return;
        }
        try {
            productService.createNewProduct(productName, productDescription, productPrice);
            refreshPage();
            productAddPage.displaySuccess();
            productAddPage.clearInputFields();
        } catch (ProductNameException pne) {
            productAddPage.displayNameError();
        }
    }

    public void refreshProductListTable() {
        productIDList.clear();
        Set<Product> allProducts = productService.getAllProducts();
        Product[] products = allProducts.toArray(new Product[allProducts.size()]);
        Object[][] productTableObjects = new Object[products.length][ProductColType.getColNameList().length];
        for (int i = 0; i < products.length; i++) {
            Product product = products[i];
            productTableObjects[i] = new Object[] { product.getName(), product.getDescription(), product.getPrice() };
            productIDList.add(i, product.getProductId());
        }
        productMainPage.setProductsList(productTableObjects);
    }

    public void addProductButtonClicked() {
        ProductAddPage productAddPage = new ProductAddPage(this);
        Page.showPopup(productAddPage);
    }

    public void removeProductButtonClicked(int row) {
        if (row == -1) { // If no row has been chosen
            return;
        }
        //TODO: Confirm option must be added.
        int productIdToRemove = productIDList.remove(row);
        try {
            productService.deleteProduct(productIdToRemove);
            refreshProductListTable();
        } catch (SoldProductDeletionException e) {
            productMainPage.displayProductDeletionError(e);
        }
    }

    public void viewProductButtonClicked(int row) {
        if (row == -1) { // If no row has been chosen
            return;
        }
        productDetailController.showDetailsOfProductWithId(productIDList.get(row));
    }

    @Override
    public void refreshPage() {
        refreshProductListTable();
    }
}