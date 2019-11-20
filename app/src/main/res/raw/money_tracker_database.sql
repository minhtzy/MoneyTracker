CREATE TABLE IF NOT EXISTS tbl_wallets(
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
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
, currencySymbol TEXT
, decimalPoint TEXT
, groupSeparator TEXT
, currencyCode TEXT COLLATE NOCASE NOT NULL UNIQUE
);
INSERT INTO tbl_currency_format (_id, name, currencySymbol, decimalPoint, groupSeparator, currencyCode) VALUES
  (1, 'United States dollar', '$', '.', ' ', 'USD'),
  (2, 'European euro', '€', '.', ' ', 'EUR'),
  (3, 'UK Pound', '£', '.', ' ', 'GBP'),
  (4, 'Russian Ruble', '', ',', ' ', 'RUB'),
  (5, 'Ukrainian hryvnia', '₴', ',', ' ', 'UAH'),
  (6, 'Afghan afghani', '؋', '.', ' ', 'AFN'),
  (7, 'Albanian lek', '', '.', ' ', 'ALL'),
  (8, 'Algerian dinar', 'دج', '.', ' ', 'DZD'),
  (9, 'Angolan kwanza', '', '.', ' ', 'AOA'),
  (10, 'East Caribbean dollar', 'EC$', '.', ' ', 'XCD'),
  (11, 'Argentine peso', 'AR$', ',', '.', 'ARS'),
  (12, 'Armenian dram', '', '.', ' ', 'AMD'),
  (13, 'Aruban florin', 'ƒ', '.', ' ', 'AWG'),
  (14, 'Australian dollar', '$', '.', ',', 'AUD'),
  (15, 'Azerbaijani manat', '', '.', ' ', 'AZN'),
  (16, 'Bahamian dollar', 'B$', '.', ' ', 'BSD'),
  (17, 'Bahraini dinar', '', '.', ' ', 'BHD'),
  (18, 'Bangladeshi taka', '', '.', ' ', 'BDT'),
  (19, 'Barbadian dollar', 'Bds$', '.', ' ', 'BBD'),
  (20, 'Belarusian ruble', 'Br', ',', ' ', 'BYR'),
  (21, 'Belize dollar', 'BZ$', '.', ' ', 'BZD'),
  (22, 'West African CFA franc', 'CFA', '.', ' ', 'XOF'),
  (23, 'Bermudian dollar', 'BD$', '.', ' ', 'BMD'),
  (24, 'Bhutanese ngultrum', 'Nu.', '.', ' ', 'BTN'),
  (25, 'Bolivian boliviano', 'Bs.', '.', ' ', 'BOB'),
  (26, 'Bosnia and Herzegovina konvertibilna marka', 'KM', ',', '.', 'BAM'),
  (27, 'Botswana pula', 'P', '.', ' ', 'BWP'),
  (28, 'Brazilian real', 'R$', '.', ' ', 'BRL'),
  (29, 'Brunei dollar', 'B$', '.', ' ', 'BND'),
  (30, 'Bulgarian lev', '', '.', ' ', 'BGN'),
  (31, 'Burundi franc', 'FBu', '.', ' ', 'BIF'),
  (32, 'Cambodian riel', '', '.', ' ', 'KHR'),
  (33, 'Central African CFA franc', 'CFA', '.', ' ', 'XAF'),
  (34, 'Canadian dollar', '$', '.', ' ', 'CAD'),
  (35, 'Cape Verdean escudo', 'Esc', '.', ' ', 'CVE'),
  (36, 'Cayman Islands dollar', 'KY$', '.', ' ', 'KYD'),
  (37, 'Chilean peso', '$', '.', ' ', 'CLP'),
  (38, 'Chinese renminbi', '¥', '.', ' ', 'CNY'),
  (39, 'Colombian peso', 'Col$', '.', ' ', 'COP'),
  (40, 'Comorian franc', '', '.', ' ', 'KMF'),
  (41, 'Congolese franc', 'F', '.', ' ', 'CDF'),
  (42, 'Costa Rican colon', '₡', '.', ' ', 'CRC'),
  (43, 'Croatian kuna', 'kn', '.', ' ', 'HRK'),
  (44, 'Czech koruna', 'Kč', '.', ' ', 'CZK'),
  (45, 'Danish krone', 'Kr', '.', ' ', 'DKK'),
  (46, 'Djiboutian franc', 'Fdj', '.', ' ', 'DJF'),
  (47, 'Dominican peso', 'RD$', '.', ' ', 'DOP'),
  (48, 'Egyptian pound', '£', '.', ' ', 'EGP'),
  (49, 'Eritrean nakfa', 'Nfa', '.', ' ', 'ERN'),
  (50, 'Ethiopian birr', 'Br', '.', ' ', 'ETB'),
  (51, 'Falkland Islands pound', '£', '.', ' ', 'FKP'),
  (52, 'Fijian dollar', 'FJ$', '.', ' ', 'FJD'),
  (53, 'CFP franc', 'F', '.', ' ', 'XPF'),
  (54, 'Gambian dalasi', 'D', '.', ' ', 'GMD'),
  (55, 'Georgian lari', '', '.', ' ', 'GEL'),
  (56, 'Ghanaian cedi', '', '.', ' ', 'GHS'),
  (57, 'Gibraltar pound', '£', '.', ' ', 'GIP'),
  (58, 'Guatemalan quetzal', 'Q', '.', ' ', 'GTQ'),
  (59, 'Guinean franc', 'FG', '.', ' ', 'GNF'),
  (60, 'Guyanese dollar', 'GY$', '.', ' ', 'GYD'),
  (61, 'Haitian gourde', 'G', '.', ' ', 'HTG'),
  (62, 'Honduran lempira', 'L', '.', ' ', 'HNL'),
  (63, 'Hong Kong dollar', 'HK$', '.', ' ', 'HKD'),
  (64, 'Hungarian forint', 'Ft', '.', ' ', 'HUF'),
  (65, 'Icelandic króna', 'kr', '.', ' ', 'ISK'),
  (66, 'Indian rupee', '₹', '.', ' ', 'INR'),
  (67, 'Indonesian rupiah', 'Rp', '.', ' ', 'IDR'),
  (68, 'Special Drawing Rights', 'SDR', '.', ' ', 'XDR'),
  (69, 'Iranian rial', '', '.', ' ', 'IRR'),
  (70, 'Iraqi dinar', '', '.', ' ', 'IQD'),
  (71, 'Israeli new shekel', '₪', '.', ' ', 'ILS'),
  (72, 'Jamaican dollar', 'J$', '.', ' ', 'JMD'),
  (73, 'Japanese yen', '¥', '.', ' ', 'JPY'),
  (74, 'Jordanian dinar', '', '.', ' ', 'JOD'),
  (75, 'Kazakhstani tenge', 'T', '.', ' ', 'KZT'),
  (76, 'Kenyan shilling', 'KSh', '.', ' ', 'KES'),
  (77, 'North Korean won', 'W', '.', ' ', 'KPW'),
  (78, 'South Korean won', 'W', '.', ' ', 'KRW'),
  (79, 'Kuwaiti dinar', '', '.', ' ', 'KWD'),
  (80, 'Kyrgyzstani som', '', '.', ' ', 'KGS'),
  (81, 'Lao kip', 'KN', '.', ' ', 'LAK'),
  (82, 'Latvian lats', 'Ls', '.', ' ', 'LVL'),
  (83, 'Lebanese lira', '', '.', ' ', 'LBP'),
  (84, 'Lesotho loti', 'M', '.', ' ', 'LSL'),
  (85, 'Liberian dollar', 'L$', '.', ' ', 'LRD'),
  (86, 'Libyan dinar', 'LD', '.', ' ', 'LYD'),
  (87, 'Lithuanian litas', 'Lt', '.', ' ', 'LTL'),
  (88, 'Macanese pataca', 'P', '.', ' ', 'MOP'),
  (89, 'Macedonian denar', '', '.', ' ', 'MKD'),
  (90, 'Malagasy ariary', 'FMG', '.', ' ', 'MGA'),
  (91, 'Malawian kwacha', 'MK', '.', ' ', 'MWK'),
  (92, 'Malaysian ringgit', 'RM', '.', ' ', 'MYR'),
  (93, 'Maldivian rufiyaa', 'Rf', '.', ' ', 'MVR'),
  (94, 'Mauritanian ouguiya', 'UM', '.', ' ', 'MRO'),
  (95, 'Mauritian rupee', 'Rs', '.', ' ', 'MUR'),
  (96, 'Mexican peso', '$', '.', ' ', 'MXN'),
  (97, 'Moldovan leu', '', '.', ' ', 'MDL'),
  (98, 'Mongolian tugrik', '₮', '.', ' ', 'MNT'),
  (99, 'Moroccan dirham', '', '.', ' ', 'MAD'),
  (100, 'Myanma kyat', 'K', '.', ' ', 'MMK'),
  (101, 'Namibian dollar', 'N$', '.', ' ', 'NAD'),
  (102, 'Nepalese rupee', 'NRs', '.', ' ', 'NPR'),
  (103, 'Netherlands Antillean gulden', 'NAƒ', '.', ' ', 'ANG'),
  (104, 'New Zealand dollar', 'NZ$', '.', ' ', 'NZD'),
  (105, 'Nicaraguan córdoba', 'C$', '.', ' ', 'NIO'),
  (106, 'Nigerian naira', '₦', '.', ' ', 'NGN'),
  (107, 'Norwegian krone', 'kr', '.', ' ', 'NOK'),
  (108, 'Omani rial', '', '.', ' ', 'OMR'),
  (109, 'Pakistani rupee', 'Rs.', '.', ' ', 'PKR'),
  (110, 'Panamanian balboa', 'B./', '.', ' ', 'PAB'),
  (111, 'Papua New Guinean kina', 'K', '.', ' ', 'PGK'),
  (112, 'Paraguayan guarani', '', '.', ' ', 'PYG'),
  (113, 'Peruvian nuevo sol', 'S/.', '.', ' ', 'PEN'),
  (114, 'Philippine peso', '₱', '.', ' ', 'PHP'),
  (115, 'Polish zloty', '', '.', ' ', 'PLN'),
  (116, 'Qatari riyal', 'QR', '.', ' ', 'QAR'),
  (117, 'Romanian leu', 'L', '.', ' ', 'RON'),
  (118, 'Rwandan franc', 'RF', '.', ' ', 'RWF'),
  (119, 'São Tomé and Príncipe dobra', 'Db', '.', ' ', 'STD'),
  (120, 'Saudi riyal', 'SR', '.', ' ', 'SAR'),
  (121, 'Serbian dinar', 'din.', '.', ' ', 'RSD'),
  (122, 'Seychellois rupee', 'SR', '.', ' ', 'SCR'),
  (123, 'Sierra Leonean leone', 'Le', '.', ' ', 'SLL'),
  (124, 'Singapore dollar', 'S$', '.', ' ', 'SGD'),
  (125, 'Solomon Islands dollar', 'SI$', '.', ' ', 'SBD'),
  (126, 'Somali shilling', 'Sh.', '.', ' ', 'SOS'),
  (127, 'South African rand', 'R', '.', ' ', 'ZAR'),
  (128, 'Sri Lankan rupee', 'Rs', '.', ' ', 'LKR'),
  (129, 'Saint Helena pound', '£', '.', ' ', 'SHP'),
  (130, 'Sudanese pound', '', '.', ' ', 'SDG'),
  (131, 'Surinamese dollar', '$', '.', ' ', 'SRD'),
  (132, 'Swazi lilangeni', 'E', '.', ' ', 'SZL'),
  (133, 'Swedish krona', 'kr', '.', ' ', 'SEK'),
  (134, 'Swiss franc', 'Fr.', '.', ' ', 'CHF'),
  (135, 'Syrian pound', '', '.', ' ', 'SYP'),
  (136, 'New Taiwan dollar', 'NT$', '.', ' ', 'TWD'),
  (137, 'Tajikistani somoni', '', '.', ' ', 'TJS'),
  (138, 'Tanzanian shilling', '', '.', ' ', 'TZS'),
  (139, 'Thai baht', '฿', '.', ' ', 'THB'),
  (140, 'Trinidad and Tobago dollar', 'TT$', '.', ' ', 'TTD'),
  (141, 'Tunisian dinar', 'DT', '.', ' ', 'TND'),
  (142, 'Turkish lira', '₺', '.', ' ', 'TRY'),
  (143, 'Turkmen manat', 'm', '.', ' ', 'TMT'),
  (144, 'Ugandan shilling', 'USh', '.', ' ', 'UGX'),
  (145, 'UAE dirham', '', '.', ' ', 'AED'),
  (146, 'Uruguayan peso', '$U', '.', ' ', 'UYU'),
  (147, 'Uzbekistani som', '', '.', ' ', 'UZS'),
  (148, 'Vanuatu vatu', 'VT', '.', ' ', 'VUV'),
  (149, 'Vietnamese dong', '₫', '.', ' ', 'VND'),
  (150, 'Samoan tala', 'WS$', '.', ' ', 'WST'),
  (151, 'Yemeni rial', '', '.', ' ', 'YER'),
  (152, 'Venezuelan Bolívar', 'Bs.', '.', ',', 'VEF');

-- trigger update wallet

create trigger update_wallet_insert_transactions after insert on tbl_transactions
begin
    update tbl_wallets
    set currentBalance = currentBalance + new.amount
    where _id = new.walletId;
end;

create trigger update_wallet_update_transactions after insert on tbl_transactions
begin
    update tbl_wallets
    set currentBalance = currentBalance + new.amount - old.amount
    where _id = new.walletId;
end;

create trigger update_wallet_delete_transactions after insert on tbl_transactions
begin
    update tbl_wallets
    set currentBalance = currentBalance - old.amount
    where _id = new.walletId;
end;