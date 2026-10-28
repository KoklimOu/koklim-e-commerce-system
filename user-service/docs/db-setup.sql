-- Run once as the Postgres admin (POSTGRES_USER = ecommerce_user, port 8432 in your setup):
--   PGPASSWORD=mypassword psql -h localhost -p 8432 -U ecommerce_user -d postgres -f user-service/docs/db-setup.sql
-- Tables are created by Hibernate (ddl-auto: update), so only the database is needed.

SELECT 'CREATE DATABASE db_user OWNER ecommerce_user'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'db_user')\gexec

GRANT ALL PRIVILEGES ON DATABASE db_user TO ecommerce_user;
