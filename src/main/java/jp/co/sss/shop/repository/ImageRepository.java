package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.shop.entity.Image;
import jp.co.sss.shop.entity.Item;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
public interface ImageRepository extends JpaRepository<Image, Integer> {


	/**
	 * 画像パスと商品情報に基づいて検索
	 *
	 * @return 結果の画像
	 */
	Image findByImagePathAndItems(String imagePath, Item item);

	/**
	 * 商品情報に基づいて画像情報を削除する
	 *
	 * @return 削除された行数
	 */
	@Transactional
	void deleteByItems(Item item);

}
