package jp.co.sss.shop.bean;

import java.io.Serializable;
import java.sql.Date;

import jp.co.sss.shop.entity.Item;
import jp.co.sss.shop.entity.User;

/**
 * お気に入り情報クラス
 *
 * @author Wada
 */

public class FavoriteBean implements Serializable{
	/**
	 * いいねID
	 */
	private Integer id;

	/**
	 * 登録日付
	 */
	private Date insertDate;
	
	
	/**
	 * 関連商品情報
	 */
	private Item item;

	/**
	 * 関連会員情報
	 */
	private User user;

	/**
	 * いいねIDの所得
	 * @return いいねID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * いいねIDのセット
	 * @param id いいねID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 登録日付の所得
	 * @return 登録日付
	 */
	public Date getInsertDate() {
		return insertDate;
	}

	/**
	 * 登録日付のセット
	 * @param insertDate 登録日付
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * 関連商品情報の所得
	 * @return 関連商品情報
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * 関連商品情報のセット
	 * @param item 関連商品情報
	 */
	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * 関連会員情報の所得
	 * @return 関連会員情報
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 関連会員情報のセット
	 * @param user 関連会員情報
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	

}