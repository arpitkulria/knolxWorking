# --- Created by Slick DDL
# To stop Slick DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table "knolx" ("name" VARCHAR(254) NOT NULL,"address" VARCHAR(254) NOT NULL,"password" VARCHAR(254) NOT NULL,"company" VARCHAR(254) NOT NULL,"emailid" VARCHAR(254) NOT NULL,"phone" VARCHAR(254) NOT NULL,"created" DATE NOT NULL,"updated" DATE NOT NULL,"userType" INTEGER NOT NULL,"knolx_id" SERIAL NOT NULL PRIMARY KEY);
create unique index "emailid" on "knolx" ("emailid");

# --- !Downs

drop table "knolx";

