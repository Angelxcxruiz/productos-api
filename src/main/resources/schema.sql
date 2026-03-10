CREATE TABLE IF NOT EXISTS productos (
    id          BIGSERIAL PRIMARY KEY,
    nombre      VARCHAR(150)   NOT NULL,
    descripcion TEXT,
    precio      NUMERIC(10, 2) NOT NULL,
    stock       INTEGER        NOT NULL DEFAULT 0,
    categoria   VARCHAR(100),
    activo      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP      NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP      NOT NULL DEFAULT NOW()
);
