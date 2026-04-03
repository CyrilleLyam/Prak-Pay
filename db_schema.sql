-- =============================================================
-- Prak-Pay Banking System - Database Schema
-- =============================================================

-- -------------------------------------------------------------
-- DATABASE: account
-- -------------------------------------------------------------

\c account;

CREATE TABLE IF NOT EXISTS accounts (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(100)    NOT NULL,
    email       VARCHAR(150)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    status      VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE'
                                CHECK (status IN ('ACTIVE', 'SUSPENDED', 'PENDING')),
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

-- -------------------------------------------------------------
-- DATABASE: transaction
-- -------------------------------------------------------------

\c transaction;

CREATE TABLE IF NOT EXISTS wallets (
    id          BIGSERIAL       PRIMARY KEY,
    account_id  BIGINT          NOT NULL UNIQUE,
    balance     NUMERIC(19, 2)  NOT NULL DEFAULT 0.00
                                CHECK (balance >= 0),
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS transactions (
    id              BIGSERIAL       PRIMARY KEY,
    wallet_id       BIGINT          NOT NULL REFERENCES wallets(id),
    type            VARCHAR(20)     NOT NULL
                                    CHECK (type IN ('DEPOSIT', 'WITHDRAW', 'TRANSFER_IN', 'TRANSFER_OUT')),
    amount          NUMERIC(19, 2)  NOT NULL CHECK (amount > 0),
    related_wallet_id BIGINT        REFERENCES wallets(id),  -- used for transfers
    note            VARCHAR(255),
    created_at      TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_transactions_wallet_id   ON transactions(wallet_id);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at  ON transactions(created_at);

-- -------------------------------------------------------------
-- DATABASE: notification
-- -------------------------------------------------------------

\c notification;

CREATE TABLE IF NOT EXISTS notifications (
    id          BIGSERIAL       PRIMARY KEY,
    account_id  BIGINT          NOT NULL,
    type        VARCHAR(50)     NOT NULL
                                CHECK (type IN ('TRANSACTION', 'ACCOUNT', 'SYSTEM')),
    title       VARCHAR(255)    NOT NULL,
    message     TEXT            NOT NULL,
    is_read     BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP       NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_notifications_account_id ON notifications(account_id);
CREATE INDEX IF NOT EXISTS idx_notifications_created_at ON notifications(created_at);
