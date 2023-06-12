--systemユーザで接続後、下記のSQLを実行する。
-- PDBに切り替え
ALTER SESSION SET CONTAINER = xepdb1;
-- ユーザの作成
CREATE USER shared_shop_user IDENTIFIED BY systemsss;
-- 権限の付与
GRANT ALL PRIVILEGES TO shared_shop_user;

--shared_shop_userで接続した後、下記のテーブル、シーケンス作成及びデータの追加を行う
-- カテゴリテーブルの作成
CREATE TABLE categories (
  id NUMBER(2) PRIMARY KEY,
  name VARCHAR2(15 CHAR) NOT NULL,
  description VARCHAR2(30 CHAR),
  delete_flag NUMBER(1) DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT SYSDATE NOT NULL
);

-- 商品テーブルの作成
CREATE TABLE items (
  id NUMBER(6) PRIMARY KEY,
  name VARCHAR2(100 CHAR) NOT NULL,
  price NUMBER(7) NOT NULL,
  description VARCHAR2(400 CHAR),
  stock NUMBER(4) DEFAULT 0 NOT NULL,
  delete_flag NUMBER(1) DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT SYSDATE NOT NULL,
  category_id NUMBER(2) REFERENCES categories(id) NOT NULL
);

-- 会員テーブルの作成
CREATE TABLE users (
  id NUMBER(6) PRIMARY KEY,
  email VARCHAR2(256) UNIQUE NOT NULL,
  password VARCHAR2(64) NOT NULL,
  name VARCHAR2(30 CHAR) NOT NULL,
  postal_code VARCHAR2(7) NOT NULL,
  address VARCHAR2(150 CHAR) NOT NULL,
  phone_number VARCHAR2(11) NOT NULL,
  authority NUMBER(1) NOT NULL,
  delete_flag NUMBER(1) DEFAULT 0 NOT NULL,
  insert_date DATE DEFAULT SYSDATE NOT NULL
);

-- 注文テーブル
CREATE TABLE orders (
  id NUMBER(6) PRIMARY KEY,
  postal_code VARCHAR2(7) NOT NULL,
  address VARCHAR2(150 CHAR) NOT NULL,
  name VARCHAR2(30 CHAR) NOT NULL,
  phone_number VARCHAR2(11) NOT NULL,
  pay_method NUMBER(1) NOT NULL,
  insert_date DATE DEFAULT SYSDATE NOT NULL,
  user_id NUMBER(6) REFERENCES users(id) NOT NULL
);

-- 注文商品テーブル
CREATE TABLE order_items (
  id NUMBER(6) PRIMARY KEY,
  quantity NUMBER(4) NOT NULL,
  price NUMBER(7) NOT NULL,
  order_id NUMBER(6) REFERENCES orders(id) NOT NULL,
  item_id NUMBER(6) REFERENCES items(id) NOT NULL
);

-- 画像テーブル
CREATE TABLE images (
  id NUMBER(6) PRIMARY KEY,
  image_path VARCHAR2(128) NOT NULL,
  upload_date DATE DEFAULT SYSDATE NOT NULL,
  item_id NUMBER(6) REFERENCES items(id) NOT NULL
);

-- いいねテーブル
CREATE TABLE favorite (
  id NUMBER(6) PRIMARY KEY,
  insert_date DATE DEFAULT SYSDATE NOT NULL,
  item_id NUMBER(6) REFERENCES items(id) NOT NULL,
  user_id NUMBER(6) REFERENCES users(id) NOT NULL
);

-- シーケンスの作成(カテゴリテーブル用)
CREATE SEQUENCE seq_categories NOCACHE;

-- シーケンスの作成(商品テーブル用)
CREATE SEQUENCE seq_items NOCACHE;

-- シーケンスの作成(会員テーブル用)
CREATE SEQUENCE seq_users NOCACHE;

-- シーケンスの作成(注文テーブル用)
CREATE SEQUENCE seq_orders NOCACHE;

-- シーケンスの作成(注文商品テーブル用)
CREATE SEQUENCE seq_order_items NOCACHE;

-- シーケンスの作成(画像テーブル用)
CREATE SEQUENCE seq_images NOCACHE;

-- シーケンスの作成(いいねテーブル用)
CREATE SEQUENCE seq_favorite NOCACHE;