
.PHONY: setup_db
setup_db: schema seed_db

.PHONY: schema
schema:
	@echo
	@echo Initialising database schema - DB password required.
	psql -h localhost -p 5432 -U postgres -W --dbname=lans_hotels -f ./database/schema.sql
	@echo

.PHONY: seed_db
seed_db:
	@echo
	@echo Seeding database with test data - DB password required.
	psql -h localhost -p 5432 -U postgres -W --dbname=lans_hotels -f ./database/seed.sql
	@echo

#.PHONY install
#install:
#	mvn clean install -U