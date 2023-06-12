package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Favorite;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {


}
