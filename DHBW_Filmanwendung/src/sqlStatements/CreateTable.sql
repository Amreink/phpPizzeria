CREATE TABLE Movielist(
ID int,
UserID int,
MovieID text,
MerkList bit,
FavList bit,
UserRate char(3)
);

Create TABLE Movie(
MovieID text,
Title char(20),
Myear char(20),
Runtime char(20),
Genre char(200),
Poster text,
Director char(200),
Released char(20), /*Vllt als date?*/
Plot text,
ImdbRate char(3)
);

CREATE TABLE UserT(
UserID int,
Username char(200)
);