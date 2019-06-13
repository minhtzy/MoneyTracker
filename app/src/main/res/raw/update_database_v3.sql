-- Thêm budget

CREATE TABLE IF NOT EXISTS tbl_budgets (
    _id integer primary key  AUTOINCREMENT NOT NULL,
    transactionId integer NOT NULL,
    walletId integer NOT NULL,
    timeStart datetime NOT NULL,
    timeEnd datetime NOT NULL,
    loop integer NOT NULL,
    status varchar NOT NULL
);