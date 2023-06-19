package jp.co.sss.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Favorite;
import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
	
	
	List<Favorite> findByItemAndUser(Item item, User user);
}
