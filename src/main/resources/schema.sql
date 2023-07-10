--shared_shop_userで接続した後、下記のテーブル、シーケンス作成及びデータの追加を行う
-- カテゴリテーブルの作成
DROP TABLE IF EXISTS categories CASCADE;
CREATE TABLE categories (
  id INT PRIMARY KEY,
  name VARCHAR(15) NOT NULL,
  description VARCHAR(30),
  delete_flag INT DEFAULT 0 NOT NULL,
  insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 商品テーブルの作成
DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE items (
  id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  price INT NOT NULL,
  description VARCHAR(400),
  stock INT DEFAULT 0 NOT NULL,
  delete_flag INT DEFAULT 0 NOT NULL,
  insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  category_id INT REFERENCES categories(id) DEFERRABLE NOT NULL
);

-- 会員テーブルの作成
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
  id INT PRIMARY KEY,
  email VARCHAR(256) UNIQUE NOT NULL,
  password VARCHAR(64) NOT NULL,
  name VARCHAR(30) NOT NULL,
  postal_code VARCHAR(7) NOT NULL,
  address VARCHAR(150) NOT NULL,
  phone_number VARCHAR(11) NOT NULL,
  authority INT NOT NULL,
  delete_flag INT DEFAULT 0 NOT NULL,
  insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- 注文テーブル
DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE orders (
  id INT PRIMARY KEY,
  postal_code VARCHAR(7) NOT NULL,
  address VARCHAR(150) NOT NULL,
  name VARCHAR(30) NOT NULL,
  phone_number VARCHAR(11) NOT NULL,
  pay_method INT NOT NULL,
  insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  user_id INT REFERENCES users(id) DEFERRABLE NOT NULL
);

-- 注文商品テーブル
DROP TABLE IF EXISTS order_items CASCADE;
CREATE TABLE order_items (
  id INT PRIMARY KEY,
  quantity INT NOT NULL,
  price INT NOT NULL,
  order_id INT REFERENCES orders(id) DEFERRABLE NOT NULL,
  item_id INT REFERENCES items(id) DEFERRABLE NOT NULL
);

-- 画像テーブル
DROP TABLE IF EXISTS images CASCADE;
CREATE TABLE images (
  id INT PRIMARY KEY,
  image_path VARCHAR(128) NOT NULL,
  upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  item_id INT REFERENCES items(id) DEFERRABLE NOT NULL
);

-- いいねテーブル
DROP TABLE IF EXISTS favorite CASCADE;
CREATE TABLE favorite (
  id INT PRIMARY KEY,
  insert_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  item_id INT REFERENCES items(id) DEFERRABLE NOT NULL,
  user_id INT REFERENCES users(id) DEFERRABLE NOT NULL
);

-- シーケンスの作成(カテゴリテーブル用)
DROP SEQUENCE IF EXISTS seq_categories;
CREATE SEQUENCE seq_categories;

-- シーケンスの作成(商品テーブル用)
DROP SEQUENCE IF EXISTS seq_items;
CREATE SEQUENCE seq_items;

-- シーケンスの作成(会員テーブル用)
DROP SEQUENCE IF EXISTS seq_users;
CREATE SEQUENCE seq_users;

-- シーケンスの作成(注文テーブル用)
DROP SEQUENCE IF EXISTS seq_orders;
CREATE SEQUENCE seq_orders;

-- シーケンスの作成(注文商品テーブル用)
DROP SEQUENCE IF EXISTS seq_order_items;
CREATE SEQUENCE seq_order_items;

-- シーケンスの作成(画像テーブル用)
DROP SEQUENCE IF EXISTS seq_images;
CREATE SEQUENCE seq_images;

-- シーケンスの作成(いいねテーブル用)
DROP SEQUENCE IF EXISTS seq_favorite;
CREATE SEQUENCE seq_favorite;