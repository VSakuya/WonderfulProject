package jp.co.sss.shop.util;

/**
 * 独自JPQLを定義するためのクラス
 *
 * @author System Shared
 */
public class JPQLConstant {

	/** 商品情報を全件検索(新着順) 管理者機能で利用*/
	public static final String FIND_ALL_ITEMS_ORDER_BY_INSERT_DATE = "SELECT i FROM Item i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC,i.id DESC";

	/** 注文情報を全件検索(新着順) 管理者機能で利用*/
	public static final String FIND_ALL_ORDERS_ORDER_BY_INSERT_DATE = "SELECT o FROM Order o ORDER BY o.insertDate DESC,o.id DESC";

	/** カテゴリ情報を全件検索(新着順) 管理者機能で利用*/
	public static final String FIND_ALL_CATEGORIES_ORDER_BY_INSERT_DATE = "SELECT c FROM Category c WHERE c.deleteFlag =:deleteFlag ORDER BY c.insertDate DESC,c.id DESC";

	/** 会員情報を全件検索(登録降順) 管理者機能で利用*/
	public static final String FIND_USERS_LIST_ORDER_BY_INSERT_DATE = "SELECT u FROM User u WHERE u.deleteFlag =:deleteFlag AND u.authority >= :authority ORDER BY u.insertDate DESC,u.id DESC";
	
	/** 注文された商品情報を条件検索（注文された個数が多い順）*/
	public static final String FIND_DATA_FOR_HOT_SELL = 
			
			"SELECT i.*, c.name, im.image_path FROM items i "
			+ "JOIN (SELECT item_id, SUM(quantity) AS total_quantity "
			+ "FROM order_items GROUP BY item_id) o ON i.id = o.item_id "
			+ "JOIN categories c on i.category_id = c.id "
			+ "JOIN images im ON i.id = im.item_id "
			+ "WHERE i.delete_flag = 0 ORDER BY o.total_quantity DESC ";
	
	
//			"SELECT i FROM items i JOIN categories c ON i.category_id = c.id"
//			+ "JOIN order_items oi ON oi.item_id = i.id "
//			+ "JOIN images im ON i.id = im.item_id ";
//			+ "WHERE i.delete_flag = 0 ";
//			+ "GROUP BY i.id,i.name,i.price,c.name,im.image_path "
//			+ "ORDER BY  SUM(oi.quantity)DESC,i.id ASC; SELECT * FROM items";

//			"SELECT i.* FROM items i JOIN (SELECT item_id, SUM(quantity) AS total_quantity FROM order_items GROUP BY item_id) o ON i.id = o.item_id WHERE i.delete_flag = ?0 ORDER BY o.total_quantity DESC";
	
	
//		"SELECT i.id, i.name, i.price, c.name FROM Item i "
/*			"SELECT i, c.name, im.imagePath FROM Item i "
			+ "INNER JOIN Category c ON c.id = i.category "
			+ "INNER JOIN OrderItem oi ON oi.id = i.id "
			+ "INNER JOIN Image im ON i.id = im.items "
	        + "WHERE i.deleteFlag = 0 ";
		    + "GROUP BY i, c.name, im.imagePath ";			
		    + "ORDER BY SUM (oi.quantity)DESC, i.id ASC ";
*/

	/** 商品情報を全権検索（新着順） client側で使用 @author Wonder Wada*/

	public static final String FIND_DATE_FOR_NEW_SELL = "SELECT img FROM Image img INNER JOIN img.items i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC, i.id DESC ";

}
