create table front.bot_user(
  id serial PRIMARY KEY,
  chat_id integer NOT NULL ,
  first_name varchar(200) NOT NULL ,
  last_name varchar(200),
  language_code varchar(10),
  created timestamp DEFAULT current_timestamp,
  updated timestamp,
  client_id integer,
  channel varchar(30) not NULL --what the bot is? telegram, facebook etc
);
insert INTO front.bot_user (id,chat_id, first_name, last_name, language_code,client_id, channel)
VALUES (1,0,'your_app','ua','en-US',0,'tbotapi');
select * from front.bot_user order by 1 desc;

select * from front.bot_user;
--delete from front.bot_user
--drop table front.creditbot_command;
create table front.creditbot_command (
  id integer PRIMARY KEY,
  name varchar(50),
  description varchar(200),
  channel varchar(30) not NULL
);
insert into front.creditbot_command(id, name,description, channel) VALUES
  (1,'/start','run your process(command).','telegrambotapi');

select * from front.creditbot_command;
--drop table front.creditbot_question;
create table front.creditbot_question (
  id serial PRIMARY KEY,
  name varchar(30) NOT NULL,
  question_text_en text,
  question_text_loc1 text NOT NULL,
  question_text_loc2 text,
  invalid_answer_text text,
  param_name varchar(200) NOT NULL,
  param_reqexp varchar(80),
  command_id integer  NOT NULL-- id of the path chose by client
);
CREATE UNIQUE INDEX creditbot_question_name_uindex ON front.creditbot_question (name);
CREATE UNIQUE INDEX creditbot_question_param_name_uindex ON front.creditbot_question (param_name);

select * from front.creditbot_question order by 1;

create table front.creditbot_step (
  id serial PRIMARY KEY,
  name varchar(50),
  next_step_id integer,
  question_id INTEGER,
  command_id integer,
  is_finish boolean
);
select * from front.creditbot_step;
-- drop table front.creditbot_chat;
create table front.creditbot_chat(
  id serial PRIMARY KEY,
  chat_id INTEGER NOT NULL,
  message_id integer NOT NULL,
  from_user_id integer NOT NULL,
  to_user_id integer,
  created timestamp DEFAULT current_timestamp,
  updated timestamp ,
  message_date timestamp NOT NULL,
  message_text text NOT NULL,
  req text ,
  resp text ,
  step_id integer,
  command_id integer
);
--delete  from front.creditbot_chat;

select * from front.creditbot_chat order by 1 desc;

create table front.creditbot_user_answer(
  id serial PRIMARY KEY,
  bot_user_id integer NOT NULL,
  client_id integer,
  param_name varchar(100) NOT NULL,
  question_id integer NOT NULL,
  text text,
  created TIMESTAMP default current_timestamp NOT NULL,
  deactivated TIMESTAMP

);