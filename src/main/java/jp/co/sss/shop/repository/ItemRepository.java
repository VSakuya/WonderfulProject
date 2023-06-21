package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Category;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.util.JPQLConstant;

/**
 * itemsテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

	/**
	 * 商品情報を登録日付順に取得 管理者機能で利用
	 * @param deleteFlag 削除フラグ
	 * @param pageable ページング情報
	 * @return 商品エンティティのページオブジェクト
	 */
	@Query(JPQLConstant.FIND_ALL_ITEMS_ORDER_BY_INSERT_DATE)
	Page<Item> findByDeleteFlagOrderByInsertDateDescPage(
	        @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);

	/**
	 * 商品IDと削除フラグを条件に検索（管理者機能で利用）
	 * @param id 商品ID
	 * @param deleteFlag 削除フラグ
	 * @return 商品エンティティ
	 */
	Item findByIdAndDeleteFlag(Integer id, int deleteFlag);

	/**
	 * 商品名と削除フラグを条件に検索 (ItemValidatorで利用)
	 * @param name 商品名
	 * @param notDeleted 削除フラグ
	 * @return 商品エンティティ
	 */
	Item findByNameAndDeleteFlag(String name, int notDeleted);

	/**
	 * カテゴリーidで条件検索
	 * @param category カテゴリーid
	 * @return 商品エンティティ
	 * @author tanaka
	 */
	List<Item> findByCategory(Category category);

	/**
	 * idで降順でソートする
	 * @param insertDate 登録日
	 * @return 商品エンティティのリスト
	 * @author Tomita
	 */
	List<Item> findAllByOrderByIdDesc();
	
	/**
	 * 商品ID、商品名、価格、カテゴリ名、画像パスでグループ化
	 * 削除フラグを条件に検索
	 * 注文された個数の合計（降順）と商品ID（昇順）で並び替え
	 * @author inaba
	 */
	@Query(value = JPQLConstant.FIND_DATA_FOR_HOT_SELL, nativeQuery = true)
	List<Item> findDataForHotSell();

	/** 
	 * 
	 * @param deleteFlag
	 * @param pageable
	 * @return
	 * @author Tomita
	 */


//	@Query(JPQLConstant.FIND_ALL_ITEMS_ORDER_BY_INSERT_DATE)
//	Page<Item> findByDeleteFlagOrderByInsertDateDescIdDescPage(@Param (value = "deleteFlag")int deleteFlag, Pageable pageable);

	
	/**
	 * Itemの情報を登録日付順に所得
	 * @param deleteFlag 削除フラグ
	 * @param pageable
	 * @return 商品エンティティ
	 * @author Tomita
	 */
	
	@Query(JPQLConstant.FIND_DATE_FOR_NEW_SELL)
	Page<Object[]> findByDeleteFlagOrderByInsertDateDescIdDescPage(@Param (value = "deleteFlag")int deleteFlag, Pageable pageable);


	//@Query(JPQLConstant.FIND_DATE_FOR_NEW_SELLS)
	//Page<Item> findByDeleteFlagOrderByInsertDateDescIdDescPage(@Param (value = "deleteFlag")int deleteFlag, Pageable pageable);
	
//	@Query(JPQLConstant.FIND_DATE_FOR_NEW_SELL)
//	Page<Object[]> findByDeleteFlagOrderByInsertDateDescPages(
//			 @Param(value = "deleteFlag") int deleteFlag, Pageable pageable);
	
	/**
	 * 商品idで条件検索
	 * @param id 商品id
	 * @return 商品エンティティ
	 * @author tanaka
	 */
	//@Query("SELECT i FROM Item i WHERE i.id = :id")
	//public List<Item> findByIdQuery(@Param("id") Integer id);
	
	

	
}

