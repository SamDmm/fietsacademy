insert into docenten(voornaam, familienaam, wedde, emailadres, geslacht) values('testM', 'testM', 1000, 'testM@fietsacademy.be', 'MAN');
insert into docenten(voornaam, familienaam, wedde, emailadres, geslacht) values('testV', 'testV', 1000, 'testV@fietsacademy.be', 'VROUW');
insert into docentenbijnamen(docentid, bijnaam) values ((select id from docenten where voornaam='testM'), 'test');