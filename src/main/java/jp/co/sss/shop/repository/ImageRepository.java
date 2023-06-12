package jp.co.sss.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.shop.entity.Image;

/**
 * categoriesテーブル用リポジトリ
 *
 * @author System Shared
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {


}
