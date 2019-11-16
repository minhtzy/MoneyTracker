CREATE TABLE IF NOT EXISTS tbl_wallets(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    initBalance REAL NOT NULL,
    currencyCode TEXT NOT NULL,
    walletType TEXT NOT null,
    icon TEXT,
    userId TEXT NOT null,
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
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    amount REAL NOT NULL,
    time INTEGER NOT NULL,
    note TEXT,
    mediaUri TEXT,
    locationId TEXT,
    eventId integer,
    categoryId INTEGER NOT NULL,
    walletId INTEGER NOT NULL,
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
    timeExpire INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_locations (
    _id TEXT PRIMARY KEY,
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
    walletId integer NOT NULL,
    amount real NOT null,
    timeStart integer  NOT NULL,
    timeEnd integer  NOT NULL
);

CREATE TABLE IF NOT EXISTS tbl_splits_transactions
(
    _id integer primary key autoincrement,
    tranId int not null,
    cateId int not null,
    trans_amount real not null
);

CREATE TABLE tbl_currency_format(
_id integer primary key
, name TEXT COLLATE NOCASE NOT NULL UNIQUE
, pfxSymbol TEXT
, sfxSymbol TEXT
, decimalPoint TEXT
, groupSeparator TEXT
, unitName TEXT COLLATE NOCASE
, centName TEXT COLLATE NOCASE
, scale integer
, baseConvRate numeric
, currencySymbol TEXT COLLATE NOCASE NOT NULL UNIQUE
);
CREATE INDEX IDX_CURRENCYFORMATS_SYMBOL ON CURRENCYFORMATS_V1(CURRENCY_SYMBOL);

INSERT INTO CURRENCYFORMATS_V1 VALUES(1,'United States dollar','$','','.',' ','','',100,1,'USD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(2,'European euro','€','','.',' ','','',100,1,'EUR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(3,'UK Pound','£','','.',' ','Pound','Pence',100,1,'GBP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(4,'Russian Ruble','','р',',',' ','руб.','коп.',100,1,'RUB');
INSERT INTO CURRENCYFORMATS_V1 VALUES(5,'Ukrainian hryvnia','₴','',',',' ','','',100,1,'UAH');
INSERT INTO CURRENCYFORMATS_V1 VALUES(6,'Afghan afghani','؋','','.',' ','','pul',100,1,'AFN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(7,'Albanian lek','','L','.',' ','','',1,1,'ALL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(8,'Algerian dinar','دج','','.',' ','','',100,1,'DZD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(9,'Angolan kwanza','','Kz','.',' ','','Céntimo',100,1,'AOA');
INSERT INTO CURRENCYFORMATS_V1 VALUES(10,'East Caribbean dollar','EC$','','.',' ','','',100,1,'XCD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(11,'Argentine peso','AR$','',',','.','','centavo',100,1,'ARS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(12,'Armenian dram','','','.',' ','','',1,1,'AMD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(13,'Aruban florin','ƒ','','.',' ','','',100,1,'AWG');
INSERT INTO CURRENCYFORMATS_V1 VALUES(14,'Australian dollar','$','','.',',','','',100,1,'AUD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(15,'Azerbaijani manat','','','.',' ','','',100,1,'AZN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(16,'Bahamian dollar','B$','','.',' ','','',100,1,'BSD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(17,'Bahraini dinar','','','.',' ','','',100,1,'BHD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(18,'Bangladeshi taka','','','.',' ','','',100,1,'BDT');
INSERT INTO CURRENCYFORMATS_V1 VALUES(19,'Barbadian dollar','Bds$','','.',' ','','',100,1,'BBD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(20,'Belarusian ruble','Br','',',',' ','','',1,1,'BYR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(21,'Belize dollar','BZ$','','.',' ','','',100,1,'BZD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(22,'West African CFA franc','CFA','','.',' ','','',100,1,'XOF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(23,'Bermudian dollar','BD$','','.',' ','','',100,1,'BMD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(24,'Bhutanese ngultrum','Nu.','','.',' ','','',100,1,'BTN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(25,'Bolivian boliviano','Bs.','','.',' ','','',100,1,'BOB');
INSERT INTO CURRENCYFORMATS_V1 VALUES(26,'Bosnia and Herzegovina konvertibilna marka','KM','',',','.','','',100,1,'BAM');
INSERT INTO CURRENCYFORMATS_V1 VALUES(27,'Botswana pula','P','','.',' ','','',100,1,'BWP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(28,'Brazilian real','R$','','.',' ','','',100,1,'BRL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(29,'Brunei dollar','B$','','.',' ','','',100,1,'BND');
INSERT INTO CURRENCYFORMATS_V1 VALUES(30,'Bulgarian lev','','','.',' ','','',100,1,'BGN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(31,'Burundi franc','FBu','','.',' ','','',1,1,'BIF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(32,'Cambodian riel','','','.',' ','','',100,1,'KHR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(33,'Central African CFA franc','CFA','','.',' ','','',1,1,'XAF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(34,'Canadian dollar','$','','.',' ','','',100,1,'CAD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(35,'Cape Verdean escudo','Esc','','.',' ','','',100,1,'CVE');
INSERT INTO CURRENCYFORMATS_V1 VALUES(36,'Cayman Islands dollar','KY$','','.',' ','','',100,1,'KYD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(37,'Chilean peso','$','','.',' ','','',1,1,'CLP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(38,'Chinese renminbi','¥','','.',' ','','',100,1,'CNY');
INSERT INTO CURRENCYFORMATS_V1 VALUES(39,'Colombian peso','Col$','','.',' ','','',100,1,'COP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(40,'Comorian franc','','','.',' ','','',1,1,'KMF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(41,'Congolese franc','F','','.',' ','','',100,1,'CDF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(42,'Costa Rican colon','₡','','.',' ','','',1,1,'CRC');
INSERT INTO CURRENCYFORMATS_V1 VALUES(43,'Croatian kuna','kn','','.',' ','','',100,1,'HRK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(44,'Czech koruna','Kč','','.',' ','','',100,1,'CZK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(45,'Danish krone','Kr','','.',' ','','',100,1,'DKK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(46,'Djiboutian franc','Fdj','','.',' ','','',1,1,'DJF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(47,'Dominican peso','RD$','','.',' ','','',100,1,'DOP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(48,'Egyptian pound','£','','.',' ','','',100,1,'EGP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(49,'Eritrean nakfa','Nfa','','.',' ','','',100,1,'ERN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(50,'Ethiopian birr','Br','','.',' ','','',100,1,'ETB');
INSERT INTO CURRENCYFORMATS_V1 VALUES(51,'Falkland Islands pound','£','','.',' ','','',100,1,'FKP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(52,'Fijian dollar','FJ$','','.',' ','','',100,1,'FJD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(53,'CFP franc','F','','.',' ','','',100,1,'XPF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(54,'Gambian dalasi','D','','.',' ','','',100,1,'GMD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(55,'Georgian lari','','','.',' ','','',100,1,'GEL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(56,'Ghanaian cedi','','','.',' ','','',100,1,'GHS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(57,'Gibraltar pound','£','','.',' ','','',100,1,'GIP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(58,'Guatemalan quetzal','Q','','.',' ','','',100,1,'GTQ');
INSERT INTO CURRENCYFORMATS_V1 VALUES(59,'Guinean franc','FG','','.',' ','','',1,1,'GNF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(60,'Guyanese dollar','GY$','','.',' ','','',100,1,'GYD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(61,'Haitian gourde','G','','.',' ','','',100,1,'HTG');
INSERT INTO CURRENCYFORMATS_V1 VALUES(62,'Honduran lempira','L','','.',' ','','',100,1,'HNL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(63,'Hong Kong dollar','HK$','','.',' ','','',100,1,'HKD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(64,'Hungarian forint','Ft','','.',' ','','',1,1,'HUF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(65,'Icelandic króna','kr','','.',' ','','',1,1,'ISK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(66,'Indian rupee','₹','','.',' ','','',100,1,'INR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(67,'Indonesian rupiah','Rp','','.',' ','','',1,1,'IDR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(68,'Special Drawing Rights','SDR','','.',' ','','',100,1,'XDR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(69,'Iranian rial','','','.',' ','','',1,1,'IRR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(70,'Iraqi dinar','','','.',' ','','',1,1,'IQD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(71,'Israeli new shekel','₪','','.',' ','','',100,1,'ILS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(72,'Jamaican dollar','J$','','.',' ','','',100,1,'JMD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(73,'Japanese yen','¥','','.',' ','','',1,1,'JPY');
INSERT INTO CURRENCYFORMATS_V1 VALUES(74,'Jordanian dinar','','','.',' ','','',100,1,'JOD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(75,'Kazakhstani tenge','T','','.',' ','','',100,1,'KZT');
INSERT INTO CURRENCYFORMATS_V1 VALUES(76,'Kenyan shilling','KSh','','.',' ','','',100,1,'KES');
INSERT INTO CURRENCYFORMATS_V1 VALUES(77,'North Korean won','W','','.',' ','','',100,1,'KPW');
INSERT INTO CURRENCYFORMATS_V1 VALUES(78,'South Korean won','W','','.',' ','','',1,1,'KRW');
INSERT INTO CURRENCYFORMATS_V1 VALUES(79,'Kuwaiti dinar','','','.',' ','','',100,1,'KWD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(80,'Kyrgyzstani som','','','.',' ','','',100,1,'KGS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(81,'Lao kip','KN','','.',' ','','',100,1,'LAK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(82,'Latvian lats','Ls','','.',' ','','',100,1,'LVL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(83,'Lebanese lira','','','.',' ','','',1,1,'LBP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(84,'Lesotho loti','M','','.',' ','','',100,1,'LSL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(85,'Liberian dollar','L$','','.',' ','','',100,1,'LRD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(86,'Libyan dinar','LD','','.',' ','','',100,1,'LYD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(87,'Lithuanian litas','Lt','','.',' ','','',100,1,'LTL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(88,'Macanese pataca','P','','.',' ','','',100,1,'MOP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(89,'Macedonian denar','','','.',' ','','',100,1,'MKD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(90,'Malagasy ariary','FMG','','.',' ','','',100,1,'MGA');
INSERT INTO CURRENCYFORMATS_V1 VALUES(91,'Malawian kwacha','MK','','.',' ','','',1,1,'MWK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(92,'Malaysian ringgit','RM','','.',' ','','',100,1,'MYR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(93,'Maldivian rufiyaa','Rf','','.',' ','','',100,1,'MVR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(94,'Mauritanian ouguiya','UM','','.',' ','','',100,1,'MRO');
INSERT INTO CURRENCYFORMATS_V1 VALUES(95,'Mauritian rupee','Rs','','.',' ','','',1,1,'MUR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(96,'Mexican peso','$','','.',' ','','',100,1,'MXN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(97,'Moldovan leu','','','.',' ','','',100,1,'MDL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(98,'Mongolian tugrik','₮','','.',' ','','',100,1,'MNT');
INSERT INTO CURRENCYFORMATS_V1 VALUES(99,'Moroccan dirham','','','.',' ','','',100,1,'MAD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(100,'Myanma kyat','K','','.',' ','','',1,1,'MMK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(101,'Namibian dollar','N$','','.',' ','','',100,1,'NAD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(102,'Nepalese rupee','NRs','','.',' ','','',100,1,'NPR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(103,'Netherlands Antillean gulden','NAƒ','','.',' ','','',100,1,'ANG');
INSERT INTO CURRENCYFORMATS_V1 VALUES(104,'New Zealand dollar','NZ$','','.',' ','','',100,1,'NZD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(105,'Nicaraguan córdoba','C$','','.',' ','','',100,1,'NIO');
INSERT INTO CURRENCYFORMATS_V1 VALUES(106,'Nigerian naira','₦','','.',' ','','',100,1,'NGN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(107,'Norwegian krone','kr','','.',' ','','',100,1,'NOK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(108,'Omani rial','','','.',' ','','',100,1,'OMR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(109,'Pakistani rupee','Rs.','','.',' ','','',1,1,'PKR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(110,'Panamanian balboa','B./','','.',' ','','',100,1,'PAB');
INSERT INTO CURRENCYFORMATS_V1 VALUES(111,'Papua New Guinean kina','K','','.',' ','','',100,1,'PGK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(112,'Paraguayan guarani','','','.',' ','','',1,1,'PYG');
INSERT INTO CURRENCYFORMATS_V1 VALUES(113,'Peruvian nuevo sol','S/.','','.',' ','','',100,1,'PEN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(114,'Philippine peso','₱','','.',' ','','',100,1,'PHP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(115,'Polish zloty','','','.',' ','','',100,1,'PLN');
INSERT INTO CURRENCYFORMATS_V1 VALUES(116,'Qatari riyal','QR','','.',' ','','',100,1,'QAR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(117,'Romanian leu','L','','.',' ','','',100,1,'RON');
INSERT INTO CURRENCYFORMATS_V1 VALUES(118,'Rwandan franc','RF','','.',' ','','',1,1,'RWF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(119,'São Tomé and Príncipe dobra','Db','','.',' ','','',100,1,'STD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(120,'Saudi riyal','SR','','.',' ','','',100,1,'SAR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(121,'Serbian dinar','din.','','.',' ','','',1,1,'RSD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(122,'Seychellois rupee','SR','','.',' ','','',100,1,'SCR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(123,'Sierra Leonean leone','Le','','.',' ','','',100,1,'SLL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(124,'Singapore dollar','S$','','.',' ','','',100,1,'SGD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(125,'Solomon Islands dollar','SI$','','.',' ','','',100,1,'SBD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(126,'Somali shilling','Sh.','','.',' ','','',1,1,'SOS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(127,'South African rand','R','','.',' ','','',100,1,'ZAR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(128,'Sri Lankan rupee','Rs','','.',' ','','',100,1,'LKR');
INSERT INTO CURRENCYFORMATS_V1 VALUES(129,'Saint Helena pound','£','','.',' ','','',100,1,'SHP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(130,'Sudanese pound','','','.',' ','','',100,1,'SDG');
INSERT INTO CURRENCYFORMATS_V1 VALUES(131,'Surinamese dollar','$','','.',' ','','',100,1,'SRD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(132,'Swazi lilangeni','E','','.',' ','','',100,1,'SZL');
INSERT INTO CURRENCYFORMATS_V1 VALUES(133,'Swedish krona','kr','','.',' ','','',100,1,'SEK');
INSERT INTO CURRENCYFORMATS_V1 VALUES(134,'Swiss franc','Fr.','','.',' ','','',100,1,'CHF');
INSERT INTO CURRENCYFORMATS_V1 VALUES(135,'Syrian pound','','','.',' ','','',1,1,'SYP');
INSERT INTO CURRENCYFORMATS_V1 VALUES(136,'New Taiwan dollar','NT$','','.',' ','','',100,1,'TWD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(137,'Tajikistani somoni','','','.',' ','','',100,1,'TJS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(138,'Tanzanian shilling','','','.',' ','','',1,1,'TZS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(139,'Thai baht','฿','','.',' ','','',100,1,'THB');
INSERT INTO CURRENCYFORMATS_V1 VALUES(140,'Trinidad and Tobago dollar','TT$','','.',' ','','',100,1,'TTD');
INSERT INTO CURRENCYFORMATS_V1 VALUES(141,'Tunisian dinar','DT','','.',' ','','',100,1,'TND');
INSERT INTO CURRENCYFORMATS_V1 VALUES(142,'Turkish lira','₺','','.',' ','','',100,1,'TRY');
INSERT INTO CURRENCYFORMATS_V1 VALUES(143,'Turkmen manat','m','','.',' ','','',100,1,'TMT');
INSERT INTO CURRENCYFORMATS_V1 VALUES(144,'Ugandan shilling','USh','','.',' ','','',1,1,'UGX');
INSERT INTO CURRENCYFORMATS_V1 VALUES(145,'UAE dirham','','','.',' ','','',100,1,'AED');
INSERT INTO CURRENCYFORMATS_V1 VALUES(146,'Uruguayan peso','$U','','.',' ','','',100,1,'UYU');
INSERT INTO CURRENCYFORMATS_V1 VALUES(147,'Uzbekistani som','','','.',' ','','',1,1,'UZS');
INSERT INTO CURRENCYFORMATS_V1 VALUES(148,'Vanuatu vatu','VT','','.',' ','','',100,1,'VUV');
INSERT INTO CURRENCYFORMATS_V1 VALUES(149,'Vietnamese dong','₫','','.',' ','','',1,1,'VND');
INSERT INTO CURRENCYFORMATS_V1 VALUES(150,'Samoan tala','WS$','','.',' ','','',100,1,'WST');
INSERT INTO CURRENCYFORMATS_V1 VALUES(151,'Yemeni rial','','','.',' ','','',1,1,'YER');
INSERT INTO CURRENCYFORMATS_V1 VALUES(152,'Venezuelan Bolívar','Bs.','','.',',','bolívar','céntimos',100,1,'VEF');
