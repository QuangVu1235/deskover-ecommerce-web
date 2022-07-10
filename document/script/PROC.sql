use deskover;
SELECT SUM(quantity*price) as 'totalPrice' FROM order_item WHERE id =1;

SELECT count(orders.modified_by) as 'totalOrder' from orders where month(created_at) = '07'  and modified_by='minhnh';

SELECT sum(order_item.quantity * order_item.price) as 'total',product.id,product.name
from product inner join order_item on product.id = order_item.product_id
GROUP BY product_id;

SELECT category.id,category.name, sum(order_item.quantity * order_item.price) as 'totalProduct'
from 
	product 
		inner join order_item 
					on product.id = order_item.product_id 
		join subcategory
					on product.sub_category_id = subcategory.id 
		join category 
					on product.sub_category_id = category.id
GROUP BY category.id;

SELECT category.name,  sum(order_item.quantity * order_item.price) as 'totalProduct'
from 
	category 
		join subcategory
					on subcategory.category_id = category.id 
		inner join product 
					on subcategory.id = product.sub_category_id 

		join order_item 
					on product.id = order_item.product_id
GROUP BY category.id;





SELECT sum(order_item.quantity * order_item.price),order_item.product_id from order_item GROUP BY product_id;  

-- doanh thu ngay
-- SELECT SUM( order_item.quantity * order_item.price) as 'totalBy'
-- 	FROM 
-- 		orders 
-- 			INNER JOIN order_item ON orders.id = order_item.order_id
-- 	WHERE
-- 		DAY('2022-07-03');


DROP procedure IF EXISTS `getTotalPrice_Shipping_PerDay`;
DELIMITER $$
CREATE PROCEDURE `getTotalPrice_Shipping_PerDay`(IN `day` varchar(2),IN `month` varchar(2),IN `year` varchar(4), IN modified_by varchar(50))
BEGIN
	DECLARE totalPricePerDay varchar(20) DEFAULT 0;
	SET totalPricePerDay = 
    ( 
					SELECT SUM( order_item.quantity * order_item.price) as 'total'
			FROM 
				orders 
					INNER JOIN order_item ON orders.id = order_item.order_id
			WHERE
                DAY(orders.created_at) = `day`
                AND MONTH(orders.created_at)= `month`
                AND YEAR(orders.created_at) = `year`
                AND orders.modified_by = modified_by
    );
    SELECT totalPricePerDay;
END$$

DELIMITER ;
;
call deskover.getTotalPrice_Shipping_PerDay('07', '07', '2022', 'minhnh');
-- doanh thu thang

DROP procedure IF EXISTS `getTotalPrice_Shiping_PerMonth`;

DELIMITER $$
CREATE  PROCEDURE `getTotalPrice_Shiping_PerMonth`(IN `month` varchar(2),IN `year` varchar(4), IN modified_by varchar(50))
BEGIN
		DECLARE totalPricePerMonth varchar(20) DEFAULT 0;
		SET totalPricePerMonth = 
    ( 
					SELECT SUM( order_item.quantity * order_item.price) as 'Total'
			FROM 
				orders 
					INNER JOIN order_item ON orders.id = order_item.order_id
			WHERE
				MONTH(orders.created_at)= `month`
                AND YEAR(orders.created_at) = `year`
                AND orders.modified_by = modified_by
    );
    SELECT totalPricePerMonth;
END$$
DELIMITER ;
;
call deskover.getTotalPrice_Shiping_PerMonth('07', '2022', 'minhnh');


-- doanh thu nam
DROP procedure IF EXISTS `getTotalPricePerYear`;

DELIMITER $$
CREATE PROCEDURE `getTotalPricePerYear` (IN `year` varchar(50))
BEGIN
	DECLARE totalPricePerYear varchar(20) DEFAULT 0;
		SET totalPricePerYear = 
    ( 
					SELECT SUM( order_item.quantity * order_item.price) as 'Total_year'
			FROM 
				orders 
					INNER JOIN order_item ON orders.id = order_item.order_id
			WHERE
			     YEAR(orders.created_at) = `year`
    );
    SELECT totalPricePerYear;
END$$
DELIMITER ;

call deskover.getTotalPricePerYear('2022');

DROP procedure IF EXISTS `totalOrderPerMonth`;

DELIMITER $$
CREATE PROCEDURE `totalOrderPerMonth` (IN `month` varchar(2), IN modified_by varchar(50))
BEGIN
	DECLARE totalOrder varchar(20) DEFAULT 0;
    SET totalOrder =(
		SELECT count(orders.modified_by) as 'totalOrder' 
			from orders 
			where 
				orders.modified_by= modified_by
				and month(orders.created_at) = `month`  
				
    );
    SELECT totalOrder;
END$$

USE `deskover`;
DROP procedure IF EXISTS `totalByNameCategory`;

DELIMITER $$
USE `deskover`$$
CREATE PROCEDURE `totalByNameCategory` (IN `month` varchar(2),IN `year` varchar(4))
BEGIN
	SELECT category.name as 'totalProduct'
	from 
		category 
			join subcategory
						on subcategory.category_id = category.id 
			inner join product 
						on subcategory.id = product.sub_category_id 

			join order_item 
						on product.id = order_item.product_id
			join orders
						on order_item.order_id = orders.id
			where
						month(orders.created_at) = `month`
						and year(orders.created_at) = `year`
	GROUP BY category.id;
	
END$$

DELIMITER ;

call deskover.totalByNameCategory('07', '2022');


USE `deskover`;
DROP procedure IF EXISTS `totalPriceByCategory`;

DELIMITER $$
USE `deskover`$$
CREATE PROCEDURE `totalPriceByCategory` (IN `month` varchar(2),IN `year` varchar(4))
BEGIN
	SELECT  sum(order_item.quantity * order_item.price) as 'totalProduct'
	from 
		category 
			join subcategory
						on subcategory.category_id = category.id 
			inner join product 
						on subcategory.id = product.sub_category_id 

			join order_item 
						on product.id = order_item.product_id
			join orders
						on order_item.order_id = orders.id
			where
						month(orders.created_at) = `month`
						and year(orders.created_at) = `year`
	GROUP BY category.id;
	
END$$

DELIMITER ;




