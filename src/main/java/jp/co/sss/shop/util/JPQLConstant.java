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

	/** 注文された商品情報を条件検索（注文された個数の合計が多い順） @author inaba*/
	public static final String FIND_DATA_FOR_HOT_SELL =
			"SELECT i.* FROM items i JOIN (SELECT item_id, SUM(quantity) AS total_quantity FROM order_items GROUP BY item_id) o ON i.id = o.item_id WHERE i.delete_flag = 0 ORDER BY o.total_quantity DESC ";

	/** 商品情報を全権検索（新着順） client側で使用 @author tanaka*/

	//public static final String FIND_DATE_FOR_NEW_SELLS = "SELECT img FROM Image img INNER JOIN img.items i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC, i.id DESC ";

	public static final String FIND_DATE_FOR_NEW_SELLS = "SELECT i FROM Item i WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC, i.id DESC ";

	//public static final String FIND_DATE_FOR_NEW_SELL = "SELECT img FROM Image img INNER JOIN img.items i INNER JOIN i.category c WHERE i.deleteFlag =:deleteFlag ORDER BY i.insertDate DESC, i.id DESC ";


	/** 商品情報を商品名、商品紹介と価格からの検索 @author tan **/
	public static final String FIND_BY_STR_AND_PRICE_RANGE = "SELECT i FROM Item i WHERE (i.name LiKE %:searchStr% OR i.description LIKE %:searchStr%) AND i.price >= :minPrice AND i.price <= :maxPrice AND i.deleteFlag = :deleteFlag";

}
