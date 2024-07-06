# multi-tenant-service

```postgresql
CREATE USER developer WITH ENCRYPTED PASSWORD 'developer';
CREATE DATABASE multitenant;
GRANT ALL PRIVILEGES ON DATABASE multitenant TO developer;
\c multitenant developer;

CREATE SCHEMA master;
CREATE SCHEMA template;
CREATE SCHEMA firsttenant;
CREATE SCHEMA secondtenant;

CREATE TABLE master.tenantinfo (
                                   oid VARCHAR(255) PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL UNIQUE,
                                   schema VARCHAR(255) NOT NULL UNIQUE,
                                   created_by VARCHAR(255) NOT NULL DEFAULT 'System',
                                   created_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE template.calendar (
                                   oid VARCHAR(255) PRIMARY KEY,
                                   calendar_day_id VARCHAR(255) NOT NULL UNIQUE,
                                   financial_period_id VARCHAR(255),
                                   calendar_date DATE,
                                   calendar_year INTEGER,
                                   day_of_week INTEGER,
                                   month_of_year INTEGER,
                                   day_of_month INTEGER,
                                   day_of_year INTEGER,
                                   is_working_day VARCHAR(255),
                                   office_id VARCHAR(255),
                                   mfi_id VARCHAR(255),
                                   status VARCHAR(255)
);

CREATE TABLE firsttenant.calendar (
                                      oid VARCHAR(255) PRIMARY KEY,
                                      calendar_day_id VARCHAR(255) NOT NULL UNIQUE,
                                      financial_period_id VARCHAR(255),
                                      calendar_date DATE,
                                      calendar_year INTEGER,
                                      day_of_week INTEGER,
                                      month_of_year INTEGER,
                                      day_of_month INTEGER,
                                      day_of_year INTEGER,
                                      is_working_day VARCHAR(255),
                                      office_id VARCHAR(255),
                                      mfi_id VARCHAR(255),
                                      status VARCHAR(255)
);

CREATE TABLE secondtenant.calendar (
                                       oid VARCHAR(255) PRIMARY KEY,
                                       calendar_day_id VARCHAR(255) NOT NULL UNIQUE,
                                       financial_period_id VARCHAR(255),
                                       calendar_date DATE,
                                       calendar_year INTEGER,
                                       day_of_week INTEGER,
                                       month_of_year INTEGER,
                                       day_of_month INTEGER,
                                       day_of_year INTEGER,
                                       is_working_day VARCHAR(255),
                                       office_id VARCHAR(255),
                                       mfi_id VARCHAR(255),
                                       status VARCHAR(255)
);

CREATE TABLE template.holiday (
                                  oid VARCHAR(255) PRIMARY KEY,
                                  holiday_id VARCHAR(255),
                                  calendar_day_id VARCHAR(255),
                                  office_id VARCHAR(255),
                                  holiday_date DATE,
                                  holiday_type VARCHAR(255),
                                  title_en VARCHAR(255),
                                  title_bn VARCHAR(255),
                                  mfi_id VARCHAR(255),
                                  status VARCHAR(255),

                                  FOREIGN KEY (calendar_day_id) REFERENCES template.calendar (calendar_day_id)
);

CREATE TABLE firsttenant.holiday (
                                     oid VARCHAR(255) PRIMARY KEY,
                                     holiday_id VARCHAR(255),
                                     calendar_day_id VARCHAR(255),
                                     office_id VARCHAR(255),
                                     holiday_date DATE,
                                     holiday_type VARCHAR(255),
                                     title_en VARCHAR(255),
                                     title_bn VARCHAR(255),
                                     mfi_id VARCHAR(255),
                                     status VARCHAR(255),

                                     FOREIGN KEY (calendar_day_id) REFERENCES firsttenant.calendar (calendar_day_id)
);


CREATE TABLE secondtenant.holiday (
                                      oid VARCHAR(255) PRIMARY KEY,
                                      holiday_id VARCHAR(255),
                                      calendar_day_id VARCHAR(255),
                                      office_id VARCHAR(255),
                                      holiday_date DATE,
                                      holiday_type VARCHAR(255),
                                      title_en VARCHAR(255),
                                      title_bn VARCHAR(255),
                                      mfi_id VARCHAR(255),
                                      status VARCHAR(255),

                                      FOREIGN KEY (calendar_day_id) REFERENCES secondtenant.calendar (calendar_day_id)
);

INSERT INTO master.tenantinfo (oid, name, schema)
VALUES ('a24b3c2d-801c-411a-a701-e4992f06ab2a', 'Template_Tenant', 'template'),
       ('5861fc2f-fa49-42df-8159-f24f09e25fc6', 'First_Tenant', 'firsttenant'),
       ('9cd43a87-054e-407c-a193-eb5271fe4016', 'Second_Tenant', 'secondtenant');

INSERT INTO "template".calendar ("oid", calendar_day_id, financial_period_id, calendar_date, calendar_year, day_of_week, month_of_year, day_of_month, day_of_year, is_working_day, office_id, mfi_id, status)
VALUES('20231015142552715-6faca8b0-c8a4-4412-92a8-9ac70bda1be8', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 2023, 5, 1, 6, 6, 'No', '0002', 'M1001', 'Opened');

INSERT INTO "firsttenant".calendar ("oid", calendar_day_id, financial_period_id, calendar_date, calendar_year, day_of_week, month_of_year, day_of_month, day_of_year, is_working_day, office_id, mfi_id, status)
VALUES('20231015142552715-6faca8b0-c8a4-4412-92a8-9ac70bda1be8', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 2023, 5, 1, 6, 6, 'No', '0002', 'M1001', 'Opened');

INSERT INTO "secondtenant".calendar ("oid", calendar_day_id, financial_period_id, calendar_date, calendar_year, day_of_week, month_of_year, day_of_month, day_of_year, is_working_day, office_id, mfi_id, status)
VALUES('20231015142552715-6faca8b0-c8a4-4412-92a8-9ac70bda1be8', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 2023, 5, 1, 6, 6, 'No', '0002', 'M1001', 'Opened');

INSERT INTO "template".holiday ("oid", holiday_id, calendar_day_id, office_id, holiday_date, holiday_type, title_en, title_bn, mfi_id, status)
VALUES('20231015142552716-f4ddd61f-e1f4-4d71-850f-368ae088709d', '20231015142552716-2e7204e2-4f49-4394-a37c-a3f7f408209f', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 'Fixed', 'Weekly Holiday', 'সাপ্তাহিক ছুটি', 'M1001', 'Active');

INSERT INTO "firsttenant".holiday ("oid", holiday_id, calendar_day_id, office_id, holiday_date, holiday_type, title_en, title_bn, mfi_id, status)
VALUES('20231015142552716-f4ddd61f-e1f4-4d71-850f-368ae088709d', '20231015142552716-2e7204e2-4f49-4394-a37c-a3f7f408209f', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 'Fixed', 'Weekly Holiday', 'সাপ্তাহিক ছুটি', 'M1001', 'Active');

INSERT INTO "secondtenant".holiday ("oid", holiday_id, calendar_day_id, office_id, holiday_date, holiday_type, title_en, title_bn, mfi_id, status)
VALUES('20231015142552716-f4ddd61f-e1f4-4d71-850f-368ae088709d', '20231015142552716-2e7204e2-4f49-4394-a37c-a3f7f408209f', '20231015142552715-05d1377f-69e2-44d9-8550-daf1fdf74757', NULL, '2023-01-06', 'Fixed', 'Weekly Holiday', 'সাপ্তাহিক ছুটি', 'M1001', 'Active');
```
