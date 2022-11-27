# 3.2

> Made for ex2 of the Cassandra Lab

Here we explore the basics of Cassandra
- Keyspace/Tables Creation
- Table insertion
- Queries 


# Creation

Create KeySpace:

```sql
CREATE KEYSPACE video_db_3_2 WITH replication={'class':'SimpleStrategy', 'replication_factor':3};
```

Users:
```sql
CREATE COLUMNFAMILY User ( email text, username text, name text, reg_timestamp timestamp, PRIMARY KEY(email));
```

Videos:
```sql
CREATE COLUMNFAMILY Video (id uuid, author text, name text, upload_timestamp timestamp, description text, tags list<text>, PRIMARY KEY(id, author, upload_timestamp)) WITH CLUSTERING ORDER BY (author ASC, upload_timestamp DESC);

CREATE INDEX ON video (tags);
```

Comments:
```sql
CREATE TABLE Comment_per_author(id uuid,video_id uuid,author text,comment text,upload_timestamp timestamp,PRIMARY KEY (author, upload_timestamp))WITH CLUSTERING ORDER BY (upload_timestamp DESC);
CREATE TABLE Comment_per_video(id uuid,video_id uuid,author text,comment text,upload_timestamp timestamp,PRIMARY KEY (video_id, upload_timestamp))WITH CLUSTERING ORDER BY (upload_timestamp DESC);
```

Events:
```sql
CREATE COLUMNFAMILY Follower(user text,video_id uuid,PRIMARY KEY ((user, video_id)));
```

Followers:
```sql
CREATE COLUMNFAMILY Event(id uuid,user text,video_id uuid,action text,real_timestamp timestamp,video_timestamp int,PRIMARY KEY((user, video_id), real_timestamp, video_timestamp))WITH CLUSTERING ORDER BY (real_timestamp DESC);

create index on follower (video_id) ;
```

Ratings:
```sql
CREATE COLUMNFAMILY Rating(id uuid,video_id uuid,value int,PRIMARY KEY(video_id, id));   

create index on rating (value)
```

 
# Insertion: 

```sql

INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user1@ua.pt', 'User1', dateof(now()), 'User_1');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user2@ua.pt', 'User2', dateof(now()), 'User_2');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user3@ua.pt', 'User3', dateof(now()), 'User_3');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user4@ua.pt', 'User4', dateof(now()), 'User_4');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user5@ua.pt', 'User5', dateof(now()), 'User_5');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user6@ua.pt', 'User6', dateof(now()), 'User_6');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user7@ua.pt', 'User7', dateof(now()), 'User_7');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user8@ua.pt', 'User8', dateof(now()), 'User_8');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user9@ua.pt', 'User9', dateof(now()), 'User_9');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user10@ua.pt', 'User10', dateof(now()), 'User_10');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user11@ua.pt', 'User11', dateof(now()), 'User_11');
INSERT INTO User (email, name, reg_timestamp, username) VALUES ('user12@ua.pt', 'User12', dateof(now()), 'User_12');

INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user1@ua.pt', dateof(now()), 'Description1', 'Video1_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user2@ua.pt', dateof(now()), 'Description2', 'Video2_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user3@ua.pt', dateof(now()), 'Description3', 'Video3_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user4@ua.pt', dateof(now()), 'Description4', 'Video4_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user5@ua.pt', dateof(now()), 'Description5', 'Video5_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user6@ua.pt', dateof(now()), 'Description6', 'Video6_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user7@ua.pt', dateof(now()), 'Description7', 'Video7_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user8@ua.pt', dateof(now()), 'Description8', 'Video8_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user9@ua.pt', dateof(now()), 'Description9', 'Video9_name', ['Tag1', 'Tag2','Tag3']);
INSERT INTO Video (id, author, upload_timestamp, description, name, tags) VALUES (now(), 'user10@ua.pt', dateof(now()), 'Description10', 'Video10_name', ['Tag1', 'Tag2','Tag3']);

INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'alor', dateof(now())) ;
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'bonzao', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user1@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user3@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user2@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'discordo', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user4@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user5@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'fr fr', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user6@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e, 'user12@ua.pt', 'no cap', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e, 'user9@ua.pt', 'muito poggers', dateof(now()));
INSERT INTO Comment_per_author (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b0b2d0-6e32-11ed-9687-77da2a24bc1e , 'user11@ua.pt', 'bem noggers', dateof(now()));

INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'alor', dateof(now())) ;
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'bonzao', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user1@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user3@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user2@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'discordo', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user4@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user5@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user10@ua.pt', 'fr fr', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'user6@ua.pt', '+/-', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e, 'user12@ua.pt', 'no cap', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e, 'user9@ua.pt', 'muito poggers', dateof(now()));
INSERT INTO Comment_per_video (id, video_id, author, comment, upload_timestamp) VALUES ( now(), 91b0b2d0-6e32-11ed-9687-77da2a24bc1e , 'user11@ua.pt', 'bem noggers', dateof(now()));

INSERT INTO Follower (user,video_id) VALUES ('user2@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user3@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user4@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user5@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user6@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user7@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user8@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user9@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );
INSERT INTO Follower (user,video_id) VALUES ('user10@ua.pt', 91b0b2d0-6e32-11ed-9687-77da2a24bc1e);
INSERT INTO Follower (user,video_id) VALUES ('user10@ua.pt',  91ae68e0-6e32-11ed-9687-77da2a24bc1e );

INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt', 91b45c50-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 0);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt', 91b45c50-6e32-11ed-9687-77da2a24bc1e, 'Pause', dateof(now()), 310);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt', 91b45c50-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 310);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user7@ua.pt ', 91ae68e0-6e32-11ed-9687-77da2a24bc1e , 'Play', dateof(now()), 0);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user12@ua.pt',91b4d180-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 0);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user12@ua.pt',91b4d180-6e32-11ed-9687-77da2a24bc1e, 'Stop', dateof(now()), 69);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt',91b0b2d0-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 0);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt',91b0b2d0-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 150);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt',91b0b2d0-6e32-11ed-9687-77da2a24bc1e, 'Pause', dateof(now()), 200);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt',91b0b2d0-6e32-11ed-9687-77da2a24bc1e, 'Play', dateof(now()), 200);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user10@ua.pt',91b0b2d0-6e32-11ed-9687-77da2a24bc1e, 'Stop', dateof(now()), 349);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user11@ua.pt',92a9b8d0-6e32-11ed-9687-77da2a24bc1e , 'Start', dateof(now()), 0);
INSERT INTO Event (id, user, video_id, action, real_timestamp, video_timestamp) VALUES (now(),'user11@ua.pt',92a9b8d0-6e32-11ed-9687-77da2a24bc1e , 'Stop', dateof(now()), 50);
                      
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 3);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 5);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 2);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 5);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 5);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 3);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 5);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 2);
INSERT INTO Rating (id, video_id, value) VALUES (now(),  91ae68e0-6e32-11ed-9687-77da2a24bc1e , 1);
INSERT INTO Rating (id, video_id, value) VALUES (now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e , 4);
INSERT INTO Rating (id, video_id, value) VALUES (now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e , 2);
INSERT INTO Rating (id, video_id, value) VALUES (now(), 91b4d180-6e32-11ed-9687-77da2a24bc1e , 1);

```

 

# Queries

Get the jsons

``` sql
select JSON * from user;
select JSON * from video;
select JSON * from Comment_per_author;
select JSON * from Comment_per_video;
select JSON * from Follower;
select JSON * from Event;
select JSON * from Rating;
```

c)

7. Pesquisa de todos os vídeos de determinado autor (e.x todos os videos do user DS)
``` sql
SELECT * FROM Video WHERE author = 'user1@ua.pt';

 id                                   | author      | upload_timestamp                | description  | name        | tags
--------------------------------------+-------------+---------------------------------+--------------+-------------+--------------------------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | user1@ua.pt | 2022-11-27 09:04:58.990000+0000 | Description1 | Video1_name | ['Tag1', 'Tag2', 'Tag3']

```
 
8. Pesquisa de comentários por utilizador, ordenado inversamente pela data 
``` sql
SELECT * FROM Comment_per_author WHERE author = 'user10@ua.pt'; 

 author       | upload_timestamp                | comment  | id                                   | video_id
--------------+---------------------------------+----------+--------------------------------------+--------------------------------------
 user10@ua.pt | 2022-11-27 09:11:20.156000+0000 |    fr fr | 74dfb9c1-6e33-11ed-9687-77da2a24bc1e | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
 user10@ua.pt | 2022-11-27 09:11:20.142000+0000 | discordo | 74ddbdf0-6e33-11ed-9687-77da2a24bc1e | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
 user10@ua.pt | 2022-11-27 09:11:20.117000+0000 |   bonzao | 74d9c651-6e33-11ed-9687-77da2a24bc1e | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
 user10@ua.pt | 2022-11-27 09:11:20.109000+0000 |     alor | 74d88dd1-6e33-11ed-9687-77da2a24bc1e | 91ae68e0-6e32-11ed-9687-77da2a24bc1e

``` 
 
9. Pesquisa de comentários por vídeos, ordenado inversamente pela data 
``` sql
SELECT * FROM Comment_per_video WHERE video_id = 91b4d180-6e32-11ed-9687-77da2a24bc1e;
 
 video_id                             | upload_timestamp                | author       | comment       | id
--------------------------------------+---------------------------------+--------------+---------------+--------------------------------------
 91b4d180-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.215000+0000 |  user9@ua.pt | muito poggers | 74e8ba71-6e33-11ed-9687-77da2a24bc1e
 91b4d180-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.210000+0000 | user12@ua.pt |        no cap | 74e7f721-6e33-11ed-9687-77da2a24bc1e

``` 
 
10. Pesquisa do rating médio de um vídeo e quantas vezes foi votado;
``` sql
SELECT avg(value) as Average_rating, count(value) as No_Reviews FROM Rating WHERE video_id = 91b4d180-6e32-11ed-9687-77da2a24bc1e;
 
 average_rating | no_reviews
----------------+------------
              2 |          3

``` 


d) 

1. Os últimos 3 comentários introduzidos para um vídeo;
```sql
SELECT * FROM Comment_per_video WHERE video_id = 91ae68e0-6e32-11ed-9687-77da2a24bc1e LIMIT 3;

 video_id                             | upload_timestamp                | author       | comment | id
--------------------------------------+---------------------------------+--------------+---------+--------------------------------------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.207000+0000 |  user6@ua.pt |     +/- | 74e781f1-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.203000+0000 | user10@ua.pt |   fr fr | 74e6e5b1-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.200000+0000 |  user5@ua.pt |     +/- | 74e67081-6e33-11ed-9687-77da2a24bc1e

```

2. Lista das tags de determinado vídeo;
```sql
SELECT tags FROM Video WHERE id = 91b4d180-6e32-11ed-9687-77da2a24bc1e;

 tags
--------------------------
 ['Tag1', 'Tag2', 'Tag3']
 
```

3. Todos os vídeos com a tag Aveiro;
```sql 
SELECT * FROM Video WHERE tags contains 'Aveiro';

 id | author | upload_timestamp | description | name | tags
----+--------+------------------+-------------+------+------

-- Empty because I dont have any video with that tag but...

SELECT * FROM Video WHERE tags contains 'Tag1';

 id                                   | author       | upload_timestamp                | description   | name         | tags
--------------------------------------+--------------+---------------------------------+---------------+--------------+--------------------------
 6f2693a0-6e33-11ed-9687-77da2a24bc1e | user10@ua.pt | 2022-11-27 09:11:10.554000+0000 | Description10 | Video10_name | ['Tag1', 'Tag2', 'Tag3']
 91ae68e0-6e32-11ed-9687-77da2a24bc1e |  user1@ua.pt | 2022-11-27 09:04:58.990000+0000 |  Description1 |  Video1_name | ['Tag1', 'Tag2', 'Tag3']
 91b4d180-6e32-11ed-9687-77da2a24bc1e |  user8@ua.pt | 2022-11-27 09:04:59.032000+0000 |  Description8 |  Video8_name | ['Tag1', 'Tag2', 'Tag3']
 91b28790-6e32-11ed-9687-77da2a24bc1e |  user4@ua.pt | 2022-11-27 09:04:59.017000+0000 |  Description4 |  Video4_name | ['Tag1', 'Tag2', 'Tag3']
 91b0b2d0-6e32-11ed-9687-77da2a24bc1e |  user3@ua.pt | 2022-11-27 09:04:59.005000+0000 |  Description3 |  Video3_name | ['Tag1', 'Tag2', 'Tag3']
 91b34ae0-6e32-11ed-9687-77da2a24bc1e |  user5@ua.pt | 2022-11-27 09:04:59.022000+0000 |  Description5 |  Video5_name | ['Tag1', 'Tag2', 'Tag3']
 91b45c50-6e32-11ed-9687-77da2a24bc1e |  user7@ua.pt | 2022-11-27 09:04:59.029000+0000 |  Description7 |  Video7_name | ['Tag1', 'Tag2', 'Tag3']
 91af0520-6e32-11ed-9687-77da2a24bc1e |  user2@ua.pt | 2022-11-27 09:04:58.994000+0000 |  Description2 |  Video2_name | ['Tag1', 'Tag2', 'Tag3']
 91b3c010-6e32-11ed-9687-77da2a24bc1e |  user6@ua.pt | 2022-11-27 09:04:59.025000+0000 |  Description6 |  Video6_name | ['Tag1', 'Tag2', 'Tag3']
 92a9b8d0-6e32-11ed-9687-77da2a24bc1e |  user9@ua.pt | 2022-11-27 09:05:00.638000+0000 |  Description9 |  Video9_name | ['Tag1', 'Tag2', 'Tag3']
 
```

4. Os últimos 5 eventos de determinado vídeo realizados por um utilizador;
```sql
SELECT * FROM Event WHERE user = 'user10@ua.pt' AND video_id = 91b0b2d0-6e32-11ed-9687-77da2a24bc1e LIMIT 5;

 user         | video_id                             | real_timestamp                  | video_timestamp | action | id
--------------+--------------------------------------+---------------------------------+-----------------+--------+--------------------------------------
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:59.852000+0000 |             349 |   Stop | 8c88dac1-6e33-11ed-9687-77da2a24bc1e
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:59.848000+0000 |             200 |   Play | 8c883e81-6e33-11ed-9687-77da2a24bc1e
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:59.845000+0000 |             200 |  Pause | 8c87c951-6e33-11ed-9687-77da2a24bc1e
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:59.841000+0000 |             150 |   Play | 8c872d11-6e33-11ed-9687-77da2a24bc1e
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:59.837000+0000 |               0 |   Play | 8c86b7e0-6e33-11ed-9687-77da2a24bc1e

```

5. Vídeos partilhados por determinado utilizador num determinado período de tempo;
```sql
SELECT * FROM Video WHERE author = 'user1@ua.pt' AND upload_timestamp < '2024-02-03 12:26:00' allow filtering;

 id                                   | author      | upload_timestamp                | description  | name        | tags
--------------------------------------+-------------+---------------------------------+--------------+-------------+--------------------------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | user1@ua.pt | 2022-11-27 09:04:58.990000+0000 | Description1 | Video1_name | ['Tag1', 'Tag2', 'Tag3']
 
```

6. Os últimos 10 vídeos, ordenado inversamente pela data da partilhada;
```sql
-- Impossible beacause of the lack of Global Querying.
```

7. Todos os seguidores (followers) de determinado vídeo;
```sql
Select * from follower WHERE video_id = 91ae68e0-6e32-11ed-9687-77da2a24bc1e;

 user         | video_id
--------------+--------------------------------------
 user10@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user8@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user4@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user6@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user3@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user5@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user2@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user7@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
  user9@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
 
```


8. Todos os comentários (dos vídeos) que determinado utilizador está a seguir (following);
```sql
SELECT * FROM Follower WHERE user = 'user10@ua.pt' allow filtering;

 user         | video_id
--------------+--------------------------------------
 user10@ua.pt | 91ae68e0-6e32-11ed-9687-77da2a24bc1e
 user10@ua.pt | 91b0b2d0-6e32-11ed-9687-77da2a24bc1e

SELECT * FROM comment_per_video WHERE video_id = 91ae68e0-6e32-11ed-9687-77da2a24bc1e;

 video_id                             | upload_timestamp                | author       | comment  | id
--------------------------------------+---------------------------------+--------------+----------+--------------------------------------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.207000+0000 |  user6@ua.pt |      +/- | 74e781f1-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.203000+0000 | user10@ua.pt |    fr fr | 74e6e5b1-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.200000+0000 |  user5@ua.pt |      +/- | 74e67081-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.196000+0000 |  user4@ua.pt |      +/- | 74e5fb50-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.192000+0000 | user10@ua.pt | discordo | 74e53801-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.188000+0000 |  user2@ua.pt |      +/- | 74e49bc1-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.185000+0000 |  user3@ua.pt |      +/- | 74e42691-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.182000+0000 |  user1@ua.pt |      +/- | 74e3b161-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.178000+0000 | user10@ua.pt |   bonzao | 74e31521-6e33-11ed-9687-77da2a24bc1e
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.171000+0000 | user10@ua.pt |     alor | 74e203b1-6e33-11ed-9687-77da2a24bc1e


SELECT * FROM comment_per_video WHERE video_id = 91b0b2d0-6e32-11ed-9687-77da2a24bc1e;

 video_id                             | upload_timestamp                | author       | comment     | id
--------------------------------------+---------------------------------+--------------+-------------+--------------------------------------
 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | 2022-11-27 09:11:20.218000+0000 | user11@ua.pt | bem noggers | 74e92fa1-6e33-11ed-9687-77da2a24bc1e
 
```

9. Os 5 vídeos com maior rating;
```sql
-- Impossible beacause of the lack of Global Querying.
```


10. Uma query que retorne todos os vídeos e que mostre claramente a forma pela qual estão
ordenados;

```sql
SELECT * FROM Video WHERE id in (91ae68e0-6e32-11ed-9687-77da2a24bc1e,91b0b2d0-6e32-11ed-9687-77da2a24bc1e);

 id                                   | author      | upload_timestamp                | description  | name        | tags
--------------------------------------+-------------+---------------------------------+--------------+-------------+--------------------------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | user1@ua.pt | 2022-11-27 09:04:58.990000+0000 | Description1 | Video1_name | ['Tag1', 'Tag2', 'Tag3']
 91b0b2d0-6e32-11ed-9687-77da2a24bc1e | user3@ua.pt | 2022-11-27 09:04:59.005000+0000 | Description3 | Video3_name | ['Tag1', 'Tag2', 'Tag3']

 
```


11. Lista com as Tags existentes e o número de vídeos catalogados com cada uma delas;
```sql
SELECT Tags FROM Video;

 tags
--------------------------
 ['Tag1', 'Tag2', 'Tag3']

SELECT count(*) FROM Video WHERE Tags contains 'Tag1';

 count
-------
    10
```

12. Pesquisa do rating médio de todos os videos;
``` sql
SELECT avg(value) as Average_rating FROM Rating;

 average_rating
----------------
              3
```

12. Pesquisa os videos com o maior rating

``` sql

select max(value) as max_rating from rating;

        max_rating
-------------------
                 5

select * from rating where value = 5;

 video_id                             | id                                   | value
--------------------------------------+--------------------------------------+-------
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 8c8bc0f0-6e33-11ed-9687-77da2a24bc1e |     5
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 8c8cab50-6e33-11ed-9687-77da2a24bc1e |     5
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 8c8d2080-6e33-11ed-9687-77da2a24bc1e |     5
 91ae68e0-6e32-11ed-9687-77da2a24bc1e | 8c8e0ae0-6e33-11ed-9687-77da2a24bc1e |     5

```

 