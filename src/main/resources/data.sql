--insert into airport(code, city, state) values ('DAL', 'Dallas', 'TX');
--insert into airport(code, city, state) values ('ORD', 'Chicago', 'IL');

insert into passenger(first_name, last_name) values ('James', 'Bond');
insert into passenger(first_name, last_name) values ('Don', 'Saint');

insert into flight(departure_date, departure_time, origination_code, destination_code) values ('2022-10-01', '13:45', 'DAL', 'ORD');
insert into flight(departure_date, departure_time, origination_code, destination_code) values ('2022-10-01', '17:45', 'ORD', 'DAL');