package com.deskover.reponsitory;

import com.deskover.dto.dashboard.OrderReport;
import com.deskover.entity.Discount;
import com.deskover.entity.FlashSale;
import com.deskover.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByActived(Boolean actived, Pageable Page);

    //Like Name
    Page<Product> findByNameContaining(String name, Pageable Page);

    Page<Product> findByPriceBetweenAndNameContainingAndSubCategoryNameContainingAndSubCategoryCategoryNameContainingAndBrandNameContaining(Double min, Double max, String name, String sub, String cate, String brand, Pageable Page);

    Page<Product> findByPriceBetweenAndActivedAndSubCategoryNameLikeAndSubCategoryCategoryNameLike(Double min, Double max, Boolean active, String sub, String cate, Pageable Page);

    //Like SubCategory
    Page<Product> findBySubCategoryNameContaining(String name, Pageable Page);

    //Like Category
    Page<Product> findBySubCategoryCategoryNameContaining(String name, Pageable Page);

    Boolean existsBySlug(String slug);

    Product findBySlug(String slug);

    List<Product> findBySubCategoryId(Long id);

    List<Product> findByFlashSale(FlashSale flashSale);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE p.name LIKE %?1% "
            + "AND p.subCategory.category.name LIKE %?2% "
            + "AND p.subCategory.name LIKE %?3% "
            + "AND p.price BETWEEN ?4 AND ?5 "
            + "AND (coalesce(?6) is null OR p.brand.name IN ?6) ",
            nativeQuery = false)
    List<Product> search(
            String keyword,
            String categories,
            String subcategories,
            Double minPrice,
            Double maxPrice,
            List<String> brands);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE CONCAT(p.name, p.description, p.brand.name, p.subCategory.name, p.subCategory.category.name) LIKE %?1% "
            + "AND p.subCategory.category.slug LIKE %?2% "
            + "AND p.subCategory.slug LIKE %?3% "
            + "AND p.price BETWEEN ?4 AND ?5 "
            + "AND (coalesce(?6) is null OR p.brand.name IN ?6) ",
            nativeQuery = false)
    Page<Product> searchPage(
            String keyword,
            String categories,
            String subcategories,
            Double minPrice,
            Double maxPrice,
            List<String> brands,
            Pageable pageable);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE p.subCategory.category.id = ?1 "
            + "AND p.actived = 1",
            nativeQuery = false)
    Page<Product> getProductBasedOnCategoryID(Long category, Pageable Page);

    @Query(value = "SELECT p FROM Product p "
            + "WHERE p.actived = 1 ",
            nativeQuery = false)
    Page<Product> getProduct(Pageable Page);


    //app custumer
    Page<Product> findByActivedAndAndDiscountAndQuantityGreaterThanOrderByModifiedAtDesc(Boolean active, Discount discount, Long quantity, Pageable Page);
    
    List<Product> findByActivedAndQuantityGreaterThanOrderByModifiedAtDesc(Boolean active, Long quantity);

    Page<Product> findByActivedAndSubCategoryCategoryId(Boolean active, Long categoryId, Pageable Page);

    Page<Product> findByActivedAndSubCategoryId(Boolean active, Long categoryId, Pageable Page);

    Page<Product> findByActivedAndAndDiscountActivedAndQuantityGreaterThan(Boolean productActive, Boolean discountActice, Long quantity, Pageable Page);

    @Query(value = "Select pr From Product pr Where pr.name = ?1")
    Product getAllByName(String name);

    Page<Product> findByFlashSaleActivedAndDiscountActived(Boolean activeFlashSale, Boolean activeDiscount, Pageable Page);
    
    List<Product> findByFlashSaleActivedAndDiscountActived(Boolean activeFlashSale, Boolean activeDiscount);

    Page<Product> findByNameContainingOrSubCategoryNameContainingOrSubCategoryCategoryNameContainingOrBrandNameContaining(String name, String sub, String cate, String brand, Pageable Page);

    Page<Product> findByActivedAndSubCategoryCategoryIdAndDiscount(Boolean active, Long categoryId, Discount discount, Pageable Page);

    // Tổng số lượng hàng tồn kho
    @Query(value = "select sum(p.quantity) from Product p where p.actived = true")
    Long totalQuantityProducts();

    // Tổng số lượng hàng tồn kho theo danh mục
    @Query(value = "select new OrderReport(p.subCategory, sum(p.quantity))" +
            "from Product p " +
            "where p.actived = true " +
            "group by p.subCategory.category " +
            "order by sum(p.quantity) desc")
    List<OrderReport> getTotalQuantityProductsByCategory();

}