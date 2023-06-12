package jp.co.sss.shop.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 注文情報のエンティティクラス
 *
 * @author SystemShared
 */
@Entity
@Table(name = "favorite")
public class Favorite {
	/**
	 * いいねID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_favorite_gen")
	@SequenceGenerator(name = "seq_favorite_gen", sequenceName = "seq_favorite", allocationSize = 1)
	private Integer id;

	/**
	 * 登録日付
	 */
	@Column(insertable = false)
	private Date insertDate;
	
	
	/**
	 * 関連商品情報
	 */
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item item;

	/**
	 * 関連会員情報
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
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
