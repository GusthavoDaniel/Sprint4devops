BEGIN;

-- ===== TABELAS =====
CREATE TABLE IF NOT EXISTS public.filial (
  id_filial   BIGSERIAL PRIMARY KEY,
  nome        VARCHAR(120) NOT NULL,
  cidade      VARCHAR(120) NOT NULL,
  estado      VARCHAR(2)   NOT NULL
);

-- garante idempotência para os seeds
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint
    WHERE conname = 'uq_filial_nome_cidade_estado'
  ) THEN
    ALTER TABLE public.filial
      ADD CONSTRAINT uq_filial_nome_cidade_estado
      UNIQUE (nome, cidade, estado);
  END IF;
END$$;

CREATE TABLE IF NOT EXISTS public.moto (
  id_moto     BIGSERIAL PRIMARY KEY,
  placa       VARCHAR(10) UNIQUE NOT NULL,
  modelo      VARCHAR(120) NOT NULL,
  ano         INT,
  status      VARCHAR(30) NOT NULL DEFAULT 'ATIVA',
  filial_id   BIGINT REFERENCES public.filial (id_filial),
  created_at  TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS public.registrorfid (
  id            BIGSERIAL PRIMARY KEY,
  data_hora     TIMESTAMP,
  ponto_leitura VARCHAR(120),
  moto_id       BIGINT REFERENCES public.moto (id_moto)
);

-- ===== SEEDS (idempotentes) =====
INSERT INTO public.filial (nome, cidade, estado) VALUES
('Filial Centro', 'São Paulo', 'SP')
ON CONFLICT (nome, cidade, estado) DO NOTHING;

INSERT INTO public.filial (nome, cidade, estado) VALUES
('Filial Zona Sul', 'São Paulo', 'SP')
ON CONFLICT (nome, cidade, estado) DO NOTHING;

INSERT INTO public.moto (placa, modelo, ano, status, filial_id) VALUES
('JKL8M90', 'Honda XRE 300', 2024, 'ATIVA',
   (SELECT id_filial FROM public.filial WHERE nome='Filial Centro' LIMIT 1))
ON CONFLICT (placa) DO NOTHING;

INSERT INTO public.moto (placa, modelo, ano, status, filial_id) VALUES
('XYZ1A23', 'Yamaha Fazer 250', 2023, 'ATIVA',
   (SELECT id_filial FROM public.filial WHERE nome='Filial Zona Sul' LIMIT 1))
ON CONFLICT (placa) DO NOTHING;

INSERT INTO public.registrorfid (data_hora, ponto_leitura, moto_id) VALUES
(NOW(), 'Portaria 01', (SELECT id_moto FROM public.moto WHERE placa='JKL8M90' LIMIT 1));

INSERT INTO public.registrorfid (data_hora, ponto_leitura, moto_id) VALUES
(NOW(), 'Estação 02', (SELECT id_moto FROM public.moto WHERE placa='XYZ1A23' LIMIT 1));

COMMIT;
