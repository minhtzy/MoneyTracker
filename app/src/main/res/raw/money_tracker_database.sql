CREATE TABLE IF NOT EXISTS tbl_wallets(
    _id TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    initBalance REAL NOT NULL,
    currentBalance REAL NOT NULL,
    currencyCode TEXT NOT NULL,
    walletType TEXT NOT null,
    userId TEXT NOT null,
    icon TEXT,
    note TEXT,
    accountNumber TEXT,
    creditLimit real,
    timestamp INTEGER);

CREATE TABLE IF NOT EXISTS tbl_categories(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    type INTEGER NOT NULL,
    name TEXT NOT NULL,
    icon TEXT NOT NULL,
    parentId INTEGER,
    CONSTRAINT fk_transaction_parent_id
    FOREIGN KEY (parentId)
    REFERENCES tbl_categories(_id)
    ON UPDATE CASCADE ON DELETE CASCADE);

INSERT INTO tbl_categories VALUES(1,1,'Ăn uống','icon_116.png',null);
INSERT INTO tbl_categories VALUES(2,1,'Cà phê','icon_15.png',1);
INSERT INTO tbl_categories VALUES(3,1,'Ăn chung','icon_2.png',1);
INSERT INTO tbl_categories VALUES(4,1,'Nhà hàng','icon_133.png',1);
INSERT INTO tbl_categories VALUES(5,1,'Hóa đơn & Tiện ích','icon_135.png',null);
INSERT INTO tbl_categories VALUES(6,1,'Điện thoại','icon_134.png',5);
INSERT INTO tbl_categories VALUES(7,1,'Nước','icon_124.png',5);
INSERT INTO tbl_categories VALUES(8,1,'Điện','icon_125.png',5);
INSERT INTO tbl_categories VALUES(9,1,'Gas','icon_139.png',5);
INSERT INTO tbl_categories VALUES(10,1,'Internet','icon_126.png',5);
INSERT INTO tbl_categories VALUES(11,1,'TV','icon_84.png',5);
INSERT INTO tbl_categories VALUES(12,1,'Thuê nhà','icon_136.png',5);
INSERT INTO tbl_categories VALUES(13,1,'Di chuyển','icon_12.png',null);
INSERT INTO tbl_categories VALUES(14,1,'Tắc xi','icon_127.png',13);
INSERT INTO tbl_categories VALUES(15,1,'Gửi xe','icon_128.png',13);
INSERT INTO tbl_categories VALUES(16,1,'Xăng dầu','icon_129.png',13);
INSERT INTO tbl_categories VALUES(17,1,'Bảo dưỡng','icon_130.png',13);
INSERT INTO tbl_categories VALUES(18,1,'Mua sắm','icon_7.png',null);
INSERT INTO tbl_categories VALUES(19,1,'Quần áo','icon_17.png',18);
INSERT INTO tbl_categories VALUES(20,1,'Giày dép','icon_131.png',18);
INSERT INTO tbl_categories VALUES(21,1,'Phụ kiện','icon_63.png',18);
INSERT INTO tbl_categories VALUES(22,1,'Thiết bị điện tử','icon_9.png',18);
INSERT INTO tbl_categories VALUES(23,1,'Bạn bè & Người yêu','icon_1.png',null);
INSERT INTO tbl_categories VALUES(24,1,'Giải trí','icon_49.png',null);
INSERT INTO tbl_categories VALUES(25,1,'Phim ảnh','icon_6.png',24);
INSERT INTO tbl_categories VALUES(26,1,'Trò chơi','icon_33.png',24);
INSERT INTO tbl_categories VALUES(27,1,'Du lịch','icon_122.png',null);
INSERT INTO tbl_categories VALUES(28,1,'Sức khỏe','icon_58.png',null);
INSERT INTO tbl_categories VALUES(29,1,'Thể thao','icon_70.png',28);
INSERT INTO tbl_categories VALUES(30,1,'Khám chữa bệnh','icon_143',28);
INSERT INTO tbl_categories VALUES(31,1,'Thuốc','icon_142.png',28);
INSERT INTO tbl_categories VALUES(32,1,'Chăm sóc cá nhân','icon_132.png',28);
INSERT INTO tbl_categories VALUES(33,1,'Quà tặng & Quyên góp','icon_144.png',null);
INSERT INTO tbl_categories VALUES(34,1,'Cưới hỏi','icon_10.png',33);
INSERT INTO tbl_categories VALUES(35,1,'Tang lễ','icon_11.png',33);
INSERT INTO tbl_categories VALUES(36,1,'Từ thiện','icon_117.png',33);
INSERT INTO tbl_categories VALUES(37,1,'Gia đình','icon_8.png',null);
INSERT INTO tbl_categories VALUES(38,1,'Con cái','icon_38.png',37);
INSERT INTO tbl_categories VALUES(39,1,'Sửa chữa nhà cửa','icon_115.png',37);
INSERT INTO tbl_categories VALUES(40,1,'Dịch vụ gia đinh','icon_54.png',37);
INSERT INTO tbl_categories VALUES(41,1,'Vật nuôi','icon_53.png',37);
INSERT INTO tbl_categories VALUES(42,1,'Giáo dục','icon_113.png',null);
INSERT INTO tbl_categories VALUES(43,1,'Sách','icon_35.png',42);
INSERT INTO tbl_categories VALUES(44,1,'Đầu tư','icon_119.png',null);
INSERT INTO tbl_categories VALUES(45,1,'Kinh doanh','icon_59.png',null);
INSERT INTO tbl_categories VALUES(46,1,'Bảo hiểm','icon_137.png',null);
INSERT INTO tbl_categories VALUES(47,1,'Chi phí','icon_138.png',null);
INSERT INTO tbl_categories VALUES(48,1,'Rút tiền','icon_145.png',null);
INSERT INTO tbl_categories VALUES(49,1,'Khoản chi khác','icon_22.png',null);
INSERT INTO tbl_categories VALUES(50,1,'Mua đồ chung','icon_7.png',null);
INSERT INTO tbl_categories VALUES(51,2,'Thưởng','icon_111.png',null);
INSERT INTO tbl_categories VALUES(52,2,'Tiền lãi','icon_118.png',null);
INSERT INTO tbl_categories VALUES(53,2,'Lương','icon_75.png',null);
INSERT INTO tbl_categories VALUES(54,2,'Được tặng','icon_117.png',null);
INSERT INTO tbl_categories VALUES(55,2,'Bán đồ','icon_121.png',null);
INSERT INTO tbl_categories VALUES(56,2,'Khoản thu khác','icon_23.png',null);
INSERT INTO tbl_categories VALUES(57,3,'Cho vay','icon_120.png',null);
INSERT INTO tbl_categories VALUES(58,3,'Trả nợ','icon_141.png',null);
INSERT INTO tbl_categories VALUES(59,4,'Đi vay','icon_112.png',null);
INSERT INTO tbl_categories VALUES(60,4,'Thu nợ','icon_140.png',null);

CREATE TABLE IF NOT EXISTS tbl_transactions(
    _id TEXT PRIMARY KEY,
    amount REAL NOT NULL,
    time INTEGER NOT NULL,
    note TEXT,
    mediaUri TEXT,
    locationId TEXT,
    eventId integer,
    categoryId INTEGER NOT NULL,
    walletId TEXT NOT NULL,
    timestamp INTEGER,
    CONSTRAINT fk_categories_id
    FOREIGN KEY (categoryId)
    REFERENCES tbl_categories(_id)
    ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT fk_transaction_wallet_id
    FOREIGN KEY (walletId)
    REFERENCES tbl_wallets(_id)
    ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT fk_transaction_event_id
    FOREIGN KEY (eventId)
    REFERENCES tbl_events(_id)
    ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_transaction_location_id
    FOREIGN KEY (locationId)
    REFERENCES tbl_locations(_id)
    ON UPDATE CASCADE ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS tbl_recurring_transactions(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    time INTEGER NOT NULL,
    note TEXT,
    locationId TEXT,
    eventId integer,
    categoryId INTEGER NOT NULL,
    walletId INTEGER NOT NULL,
    recurring TEXT NOT NULL,
    repeat INTEGER NOT NULL,
    nextOccurrencesDate INTEGER NOT NULL,
    numOccurrences INTEGER NOT NULL,
    CONSTRAINT fk_categories_id
    FOREIGN KEY (categoryId)
    REFERENCES tbl_categories(_id)
    ON UPDATE CASCADE ON DELETE CASCADE ,
    CONSTRAINT fk_transaction_wallet_id
    FOREIGN KEY (walletId)
    REFERENCES tbl_wallets(_id)
    ON UPDATE CASCADE ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS tbl_events (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    icon TEXT NOT NULL,
    timeExpire INTEGER NOT NULL,
    currencyCode TEXT NOT NULL,
    status TEXT NOT NULL,
    lockWallet TEXT
);

CREATE TABLE IF NOT EXISTS tbl_locations (
    _id TEXT PRIMARY KEY,
    name TEXT,
    country TEXT,
    city TEXT,
    district TEXT,
    locality TEXT,
    route TEXT,
    streetNumber TEXT
);

CREATE TABLE IF NOT EXISTS tbl_budgets (
    _id integer primary key  AUTOINCREMENT NOT NULL,
    name TEXT NOT NULL,
    categoryId integer NOT NULL,
    walletId TEXT NOT NULL,
    amount real NOT null,
    timeStart integer  NOT NULL,
    timeEnd integer  NOT NULL,
    repeat integer,
    icon TEXT,
    status TEXT
);

CREATE TABLE tbl_currency_format(
_id integer primary key
, name TEXT COLLATE NOCASE NOT NULL UNIQUE
, currencySymbol TEXT
, decimalPoint TEXT
, groupSeparator TEXT
, currencyCode TEXT COLLATE NOCASE NOT NULL UNIQUE
);
INSERT INTO tbl_currency_format VALUES (1,'United States dollar','$','.',' ','USD');
INSERT INTO tbl_currency_format VALUES (2,'European euro','€','.',' ','EUR');
INSERT INTO tbl_currency_format VALUES (3,'UK Pound','£','.',' ','GBP');
INSERT INTO tbl_currency_format VALUES (5,'Ukrainian hryvnia','₴',',',' ','UAH');
INSERT INTO tbl_currency_format VALUES (6,'Afghan afghani','؋','.',' ','AFN');
INSERT INTO tbl_currency_format VALUES (8,'Algerian dinar','دج','.',' ','DZD');
INSERT INTO tbl_currency_format VALUES (10,'East Caribbean dollar','EC$','.',' ','XCD');
INSERT INTO tbl_currency_format VALUES (11,'Argentine peso','AR$',',','.','ARS');
INSERT INTO tbl_currency_format VALUES (13,'Aruban florin','ƒ','.',' ','AWG');
INSERT INTO tbl_currency_format VALUES (14,'Australian dollar','$','.',',','AUD');
INSERT INTO tbl_currency_format VALUES (16,'Bahamian dollar','B$','.',' ','BSD');
INSERT INTO tbl_currency_format VALUES (19,'Barbadian dollar','Bds$','.',' ','BBD');
INSERT INTO tbl_currency_format VALUES (20,'Belarusian ruble','Br',',',' ','BYR');
INSERT INTO tbl_currency_format VALUES (21,'Belize dollar','BZ$','.',' ','BZD');
INSERT INTO tbl_currency_format VALUES (22,'West African CFA franc','CFA','.',' ','XOF');
INSERT INTO tbl_currency_format VALUES (23,'Bermudian dollar','BD$','.',' ','BMD');
INSERT INTO tbl_currency_format VALUES (24,'Bhutanese ngultrum','Nu.','.',' ','BTN');
INSERT INTO tbl_currency_format VALUES (25,'Bolivian boliviano','Bs.','.',' ','BOB');
INSERT INTO tbl_currency_format VALUES (26,'Bosnia and Herzegovina konvertibilna marka','KM',',','.','BAM');
INSERT INTO tbl_currency_format VALUES (27,'Botswana pula','P','.',' ','BWP');
INSERT INTO tbl_currency_format VALUES (28,'Brazilian real','R$','.',' ','BRL');
INSERT INTO tbl_currency_format VALUES (29,'Brunei dollar','B$','.',' ','BND');
INSERT INTO tbl_currency_format VALUES (31,'Burundi franc','FBu','.',' ','BIF');
INSERT INTO tbl_currency_format VALUES (33,'Central African CFA franc','CFA','.',' ','XAF');
INSERT INTO tbl_currency_format VALUES (34,'Canadian dollar','$','.',' ','CAD');
INSERT INTO tbl_currency_format VALUES (35,'Cape Verdean escudo','Esc','.',' ','CVE');
INSERT INTO tbl_currency_format VALUES (36,'Cayman Islands dollar','KY$','.',' ','KYD');
INSERT INTO tbl_currency_format VALUES (37,'Chilean peso','$','.',' ','CLP');
INSERT INTO tbl_currency_format VALUES (38,'Chinese renminbi','¥','.',' ','CNY');
INSERT INTO tbl_currency_format VALUES (39,'Colombian peso','Col$','.',' ','COP');
INSERT INTO tbl_currency_format VALUES (41,'Congolese franc','F','.',' ','CDF');
INSERT INTO tbl_currency_format VALUES (42,'Costa Rican colon','₡','.',' ','CRC');
INSERT INTO tbl_currency_format VALUES (43,'Croatian kuna','kn','.',' ','HRK');
INSERT INTO tbl_currency_format VALUES (44,'Czech koruna','Kč','.',' ','CZK');
INSERT INTO tbl_currency_format VALUES (45,'Danish krone','Kr','.',' ','DKK');
INSERT INTO tbl_currency_format VALUES (46,'Djiboutian franc','Fdj','.',' ','DJF');
INSERT INTO tbl_currency_format VALUES (47,'Dominican peso','RD$','.',' ','DOP');
INSERT INTO tbl_currency_format VALUES (48,'Egyptian pound','£','.',' ','EGP');
INSERT INTO tbl_currency_format VALUES (49,'Eritrean nakfa','Nfa','.',' ','ERN');
INSERT INTO tbl_currency_format VALUES (50,'Ethiopian birr','Br','.',' ','ETB');
INSERT INTO tbl_currency_format VALUES (51,'Falkland Islands pound','£','.',' ','FKP');
INSERT INTO tbl_currency_format VALUES (52,'Fijian dollar','FJ$','.',' ','FJD');
INSERT INTO tbl_currency_format VALUES (53,'CFP franc','F','.',' ','XPF');
INSERT INTO tbl_currency_format VALUES (54,'Gambian dalasi','D','.',' ','GMD');
INSERT INTO tbl_currency_format VALUES (57,'Gibraltar pound','£','.',' ','GIP');
INSERT INTO tbl_currency_format VALUES (58,'Guatemalan quetzal','Q','.',' ','GTQ');
INSERT INTO tbl_currency_format VALUES (59,'Guinean franc','FG','.',' ','GNF');
INSERT INTO tbl_currency_format VALUES (60,'Guyanese dollar','GY$','.',' ','GYD');
INSERT INTO tbl_currency_format VALUES (61,'Haitian gourde','G','.',' ','HTG');
INSERT INTO tbl_currency_format VALUES (62,'Honduran lempira','L','.',' ','HNL');
INSERT INTO tbl_currency_format VALUES (63,'Hong Kong dollar','HK$','.',' ','HKD');
INSERT INTO tbl_currency_format VALUES (64,'Hungarian forint','Ft','.',' ','HUF');
INSERT INTO tbl_currency_format VALUES (65,'Icelandic króna','kr','.',' ','ISK');
INSERT INTO tbl_currency_format VALUES (66,'Indian rupee','₹','.',' ','INR');
INSERT INTO tbl_currency_format VALUES (67,'Indonesian rupiah','Rp','.',' ','IDR');
INSERT INTO tbl_currency_format VALUES (68,'Special Drawing Rights','SDR','.',' ','XDR');
INSERT INTO tbl_currency_format VALUES (71,'Israeli new shekel','₪','.',' ','ILS');
INSERT INTO tbl_currency_format VALUES (72,'Jamaican dollar','J$','.',' ','JMD');
INSERT INTO tbl_currency_format VALUES (73,'Japanese yen','¥','.',' ','JPY');
INSERT INTO tbl_currency_format VALUES (75,'Kazakhstani tenge','T','.',' ','KZT');
INSERT INTO tbl_currency_format VALUES (76,'Kenyan shilling','KSh','.',' ','KES');
INSERT INTO tbl_currency_format VALUES (77,'North Korean won','W','.',' ','KPW');
INSERT INTO tbl_currency_format VALUES (78,'South Korean won','W','.',' ','KRW');
INSERT INTO tbl_currency_format VALUES (81,'Lao kip','KN','.',' ','LAK');
INSERT INTO tbl_currency_format VALUES (82,'Latvian lats','Ls','.',' ','LVL');
INSERT INTO tbl_currency_format VALUES (84,'Lesotho loti','M','.',' ','LSL');
INSERT INTO tbl_currency_format VALUES (85,'Liberian dollar','L$','.',' ','LRD');
INSERT INTO tbl_currency_format VALUES (86,'Libyan dinar','LD','.',' ','LYD');
INSERT INTO tbl_currency_format VALUES (87,'Lithuanian litas','Lt','.',' ','LTL');
INSERT INTO tbl_currency_format VALUES (88,'Macanese pataca','P','.',' ','MOP');
INSERT INTO tbl_currency_format VALUES (90,'Malagasy ariary','FMG','.',' ','MGA');
INSERT INTO tbl_currency_format VALUES (91,'Malawian kwacha','MK','.',' ','MWK');
INSERT INTO tbl_currency_format VALUES (92,'Malaysian ringgit','RM','.',' ','MYR');
INSERT INTO tbl_currency_format VALUES (93,'Maldivian rufiyaa','Rf','.',' ','MVR');
INSERT INTO tbl_currency_format VALUES (94,'Mauritanian ouguiya','UM','.',' ','MRO');
INSERT INTO tbl_currency_format VALUES (95,'Mauritian rupee','Rs','.',' ','MUR');
INSERT INTO tbl_currency_format VALUES (96,'Mexican peso','$','.',' ','MXN');
INSERT INTO tbl_currency_format VALUES (98,'Mongolian tugrik','₮','.',' ','MNT');
INSERT INTO tbl_currency_format VALUES (100,'Myanma kyat','K','.',' ','MMK');
INSERT INTO tbl_currency_format VALUES (101,'Namibian dollar','N$','.',' ','NAD');
INSERT INTO tbl_currency_format VALUES (102,'Nepalese rupee','NRs','.',' ','NPR');
INSERT INTO tbl_currency_format VALUES (103,'Netherlands Antillean gulden','NAƒ','.',' ','ANG');
INSERT INTO tbl_currency_format VALUES (104,'New Zealand dollar','NZ$','.',' ','NZD');
INSERT INTO tbl_currency_format VALUES (105,'Nicaraguan córdoba','C$','.',' ','NIO');
INSERT INTO tbl_currency_format VALUES (106,'Nigerian naira','₦','.',' ','NGN');
INSERT INTO tbl_currency_format VALUES (107,'Norwegian krone','kr','.',' ','NOK');
INSERT INTO tbl_currency_format VALUES (109,'Pakistani rupee','Rs.','.',' ','PKR');
INSERT INTO tbl_currency_format VALUES (110,'Panamanian balboa','B./','.',' ','PAB');
INSERT INTO tbl_currency_format VALUES (111,'Papua New Guinean kina','K','.',' ','PGK');
INSERT INTO tbl_currency_format VALUES (113,'Peruvian nuevo sol','S/.','.',' ','PEN');
INSERT INTO tbl_currency_format VALUES (114,'Philippine peso','₱','.',' ','PHP');
INSERT INTO tbl_currency_format VALUES (116,'Qatari riyal','QR','.',' ','QAR');
INSERT INTO tbl_currency_format VALUES (117,'Romanian leu','L','.',' ','RON');
INSERT INTO tbl_currency_format VALUES (118,'Rwandan franc','RF','.',' ','RWF');
INSERT INTO tbl_currency_format VALUES (119,'São Tomé and Príncipe dobra','Db','.',' ','STD');
INSERT INTO tbl_currency_format VALUES (120,'Saudi riyal','SR','.',' ','SAR');
INSERT INTO tbl_currency_format VALUES (121,'Serbian dinar','din.','.',' ','RSD');
INSERT INTO tbl_currency_format VALUES (122,'Seychellois rupee','SR','.',' ','SCR');
INSERT INTO tbl_currency_format VALUES (123,'Sierra Leonean leone','Le','.',' ','SLL');
INSERT INTO tbl_currency_format VALUES (124,'Singapore dollar','S$','.',' ','SGD');
INSERT INTO tbl_currency_format VALUES (125,'Solomon Islands dollar','SI$','.',' ','SBD');
INSERT INTO tbl_currency_format VALUES (126,'Somali shilling','Sh.','.',' ','SOS');
INSERT INTO tbl_currency_format VALUES (127,'South African rand','R','.',' ','ZAR');
INSERT INTO tbl_currency_format VALUES (128,'Sri Lankan rupee','Rs','.',' ','LKR');
INSERT INTO tbl_currency_format VALUES (129,'Saint Helena pound','£','.',' ','SHP');
INSERT INTO tbl_currency_format VALUES (131,'Surinamese dollar','$','.',' ','SRD');
INSERT INTO tbl_currency_format VALUES (132,'Swazi lilangeni','E','.',' ','SZL');
INSERT INTO tbl_currency_format VALUES (133,'Swedish krona','kr','.',' ','SEK');
INSERT INTO tbl_currency_format VALUES (134,'Swiss franc','Fr.','.',' ','CHF');
INSERT INTO tbl_currency_format VALUES (136,'New Taiwan dollar','NT$','.',' ','TWD');
INSERT INTO tbl_currency_format VALUES (139,'Thai baht','฿','.',' ','THB');
INSERT INTO tbl_currency_format VALUES (140,'Trinidad and Tobago dollar','TT$','.',' ','TTD');
INSERT INTO tbl_currency_format VALUES (141,'Tunisian dinar','DT','.',' ','TND');
INSERT INTO tbl_currency_format VALUES (142,'Turkish lira','₺','.',' ','TRY');
INSERT INTO tbl_currency_format VALUES (143,'Turkmen manat','m','.',' ','TMT');
INSERT INTO tbl_currency_format VALUES (144,'Ugandan shilling','USh','.',' ','UGX');
INSERT INTO tbl_currency_format VALUES (146,'Uruguayan peso','$U','.',' ','UYU');
INSERT INTO tbl_currency_format VALUES (148,'Vanuatu vatu','VT','.',' ','VUV');
INSERT INTO tbl_currency_format VALUES (149,'Vietnamese dong','₫','.',' ','VND');
INSERT INTO tbl_currency_format VALUES (150,'Samoan tala','WS$','.',' ','WST');
INSERT INTO tbl_currency_format VALUES (152,'Venezuelan Bolívar','Bs.','.',',','VEF');
