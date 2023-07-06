--shared_shop_userで接続した後、下記のテーブル、シーケンス作成及びデータの追加を行う
-- カテゴリテーブルの作成
CREATE TABLE categories (
  id INTEGER PRIMARY KEY,
  name VARCHAR(15) NOT NULL,
  description VARCHAR(30),
  delete_flag INTEGER DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT CURRENT_DATE NOT NULL
);

-- 商品テーブルの作成
CREATE TABLE items (
  id INTEGER PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  price INTEGER NOT NULL,
  description VARCHAR(400),
  stock INTEGER DEFAULT 0 NOT NULL,
  delete_flag INTEGER DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT CURRENT_DATE NOT NULL,
  category_id INTEGER REFERENCES categories(id) NOT NULL
);

-- 会員テーブルの作成
CREATE TABLE users (
  id INTEGER PRIMARY KEY,
  email VARCHAR(256) UNIQUE NOT NULL,
  password VARCHAR(64) NOT NULL,
  name VARCHAR(30) NOT NULL,
  postal_code VARCHAR(7) NOT NULL,
  address VARCHAR(150) NOT NULL,
  phone_number VARCHAR(11) NOT NULL,
  authority INTEGER NOT NULL,
  delete_flag INTEGER DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT CURRENT_DATE NOT NULL
);

-- 注文テーブル
CREATE TABLE orders (
  id INTEGER PRIMARY KEY,
  postal_code VARCHAR(7) NOT NULL,
  address VARCHAR(150) NOT NULL,
  name VARCHAR(30) NOT NULL,
  phone_number VARCHAR(11) NOT NULL,
  pay_method INTEGER NOT NULL,
  insert_date DATE DEFAULT CURRENT_DATE NOT NULL,
  user_id INTEGER REFERENCES users(id) NOT NULL
);

-- 注文商品テーブル
CREATE TABLE order_items (
  id INTEGER PRIMARY KEY,
  quantity INTEGER NOT NULL,
  price INTEGER NOT NULL,
  order_id INTEGER REFERENCES orders(id) NOT NULL,
  item_id INTEGER REFERENCES items(id) NOT NULL
);

-- 画像テーブル
CREATE TABLE images (
  id INTEGER PRIMARY KEY,
  image_path VARCHAR(128) NOT NULL,
  upload_date DATE DEFAULT CURRENT_DATE NOT NULL,
  item_id INTEGER REFERENCES items(id) NOT NULL
);

-- いいねテーブル
CREATE TABLE favorite (
  id INTEGER PRIMARY KEY,
  insert_date DATE DEFAULT CURRENT_DATE NOT NULL,
  item_id INTEGER REFERENCES items(id) NOT NULL,
  user_id INTEGER REFERENCES users(id) NOT NULL
);

-- シーケンスの作成(カテゴリテーブル用)
CREATE SEQUENCE seq_categories;

-- シーケンスの作成(商品テーブル用)
CREATE SEQUENCE seq_items;

-- シーケンスの作成(会員テーブル用)
CREATE SEQUENCE seq_users;

-- シーケンスの作成(注文テーブル用)
CREATE SEQUENCE seq_orders;

-- シーケンスの作成(注文商品テーブル用)
CREATE SEQUENCE seq_order_items;

-- シーケンスの作成(画像テーブル用)
CREATE SEQUENCE seq_images;

-- シーケンスの作成(いいねテーブル用)
CREATE SEQUENCE seq_favorite;