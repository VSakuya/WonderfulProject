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
 * 画像のエンティティクラス
 *
 * @author Tan
 */
@Entity
@Table(name = "images")
public class Image {
	/**
	 * 画像ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_images_gen")
	@SequenceGenerator(name = "seq_images_gen", sequenceName = "seq_images", allocationSize = 1)
	private Integer id;

	/**
	 * 画像パス
	 */
	@Column
	private String imagePath;

	/**
	 * アップロード日付
	 */
	@Column(insertable = false)
	private Date uploadDate;

	/**
	 * 対応している商品
	 */
	@ManyToOne
	@JoinColumn(name = "item_id", referencedColumnName = "id")
	private Item items;

	/**
	 * 画像IDの所得
	 * @return 画像ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 画像IDのセット
	 * @param id 画像ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 画像パスの所得
	 * @return 画像パス
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * 画像パスのセット
	 * @param imagePath 画像パス
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * アップロード日付んの所得
	 * @return アップロード日付
	 */
	public Date getUploadDate() {
		return uploadDate;
	}

	/**
	 * アップロード日付のセット
	 * @param uploadDate アップロード日付
	 */
	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * 対応している商品の所得
	 * @return 対応している商品の参照値
	 */
	public Item getItems() {
		return items;
	}

	/**
	 * 対応している商品のセット
	 * @param items 対応している商品の参照値
	 */
	public void setItems(Item items) {
		this.items = items;
	}
	
	
}
