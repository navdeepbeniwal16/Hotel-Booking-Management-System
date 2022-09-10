
.PHONY: db_up
db_up: initialise_db seed_db

.PHONY: initialise_db
initialise_db:
	psql -h localhost -p 5432 -U postgres --dbname=lans_hotels -f ./database/setup.sql

.PHONY: seed_db
seed_db:
	psql -h localhost -p 5432 -U postgres --dbname=lans_hotels -f ./database/seed.sql

