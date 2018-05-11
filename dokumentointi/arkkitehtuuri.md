# Arkkitehtuurikuvaus

## Ohjelman rakenne

Ohjelman rakenne koostuu kolmesta peruskerroksesta, joita ovat **[käyttöliittymä](https://github.com/nakkekakke/CryptoTracker/blob/master/dokumentointi/arkkitehtuuri.md#käyttöliittymä)**, **[sovelluslogiikka](https://github.com/nakkekakke/CryptoTracker/blob/master/dokumentointi/arkkitehtuuri.md#sovelluslogiikka)** ja **[pysyväistallennus](https://github.com/nakkekakke/CryptoTracker/blob/master/dokumentointi/arkkitehtuuri.md#pysyväistallennus)**. Ohjelman koodissa näitä kerroksia vastaavat pakkaukset **ui**, **domain** ja **dao**.

Alla oleva luokka-/pakkauskaavio kuvaa sovelluksen eri osien suhteita toisiinsa. Sovelluslogiikasta vastaava CryptoService-luokka käyttää kaikkia domain-pakkauksen luokkia sisäänkirjautuneen käyttäjän (User) kautta.

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/luokkapakkausdiagrammi.png">

Tarkastellaan seuraavaksi kunkin kerroksen toimintaa ja rakennetta vielä tarkemmin.

## Käyttöliittymä 
Vastaava pakkaus [cryptotracker.ui](https://github.com/nakkekakke/CryptoTracker/tree/master/src/main/java/cryptotracker/ui).

Ohjelman käyttöliittymä on toteutettu JavaFX:llä.
Käyttöliittymässä on neljä eri näkymää
- sisäänkirjautuminen ja rekisteröityminen
- kryptovaluuttalista
- kryptovaluutan/ostoerän lisääminen
- kryptovaluutan ostoerälista
Jokainen näistä näkymästä on toteutettu sovelluksessa omana [Scene](https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Scene.html)-olionaan. Ohjelmassa näkyy kerrallaan vain yksi ikkuna ([Stage](https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Stage.html)), johon näytettävä näkymä (Scene) sijoitetaan. Käyttöliittymä rakennetaan luokassa [cryptotracker.ui.CryptoUI](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/ui/CryptoUI.java).

Käyttöliittymä on pyritty eristämään sovelluslogiikasta, joten se vain kutsuu sovelluslogiikkaluokan ja -luokan olion metodeja kun käyttäjä tekee jotain sovelluksessa.

## Sovelluslogiikka
Vastaava pakkaus [cryptotracker.domain](https://github.com/nakkekakke/CryptoTracker/tree/master/src/main/java/cryptotracker/domain).

Sovelluksen ydin muodostuu "**dataluokkien**" [User](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/User.java), [Portfolio](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/Portfolio.java), [Cryptocurrency](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/Cryptocurrency.java) ja [CryptoBatch](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/CryptoBatch.java) olioista. Näimä oliot toimivat väliaikaisena talletuskohteena kaikelle datalle, mikä halutaan tallettaa pysyvästi, tai vastaavasti näyttää ohjelman käyttäjälle.

Ohjelmassa tapahtuvat toiminnalliset kokonaisuudet kuuluvat luokan [CryptoService](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/CryptoService.java) ainoalle oliolle. Luokka tarjoaa metodeita käyttöliittymän toiminnoille. Näitä ovat esimerkiksi:
- boolean login(String username)
- boolean createUser(String username)
- CryptoBatch createCrypto(String name, int amount, int totalPaid, LocalDate date)
- void deleteBatch(int id)

_CryptoService_ tallentaa ja hakee dataa pysyväistalletuksesta vastaavassa pakkauksessa _cryptotracker.dao_ olevien, Dao-rajapinnan toteuttavien luokkien kautta. Jokaista dao-oliota on vain yksi kappale, ja ne injektoidaan _CryptoService_-oliolle [konstruktorissa](https://github.com/nakkekakke/CryptoTracker/blob/82215b1097a1064cfb681fcdb84f21cdc785bf2c/src/main/java/cryptotracker/domain/CryptoService.java#L24). Lisäksi, kun kryptovaluuttoja lisättäessä halutaan tarkistaa käyttäjän syöttämien syötteien kelvollisuus, turvaudutaan luokan [InputChecker](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/domain/InputChecker.java) ainoan olion metodeihin. Näitä metodeja kutsutaan suoraan käyttöliittymästä.

## Pysyväistallennus
Vastaava pakkaus [cryptotracker.dao](https://github.com/nakkekakke/CryptoTracker/tree/master/src/main/java/cryptotracker/dao).

Luokkien [DBUserDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/DBUserDao.java), [DBPortfolioDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/DBPortfolioDao.java), [DBCryptocurrencyDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/DBCryptocurrencyDao.java) ja [DBCryptoBatchDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/DBCryptoBatchDao.java) oliot vastaavat datan tallentamisesta tietokantaan ja datan hakemisesta tietokannasta. Näitä olioita on ohjelmassa vain yksi jokaista. Nämä luokat noudattavat [DAO-mallia](https://en.wikipedia.org/wiki/Data_access_object), minkä takia tietokantatallennus voitaisiin vaihtaa johonkin toiseen talletusmuotoon koskematta pahemmin ohjelman muuhun koodiin. Edellä luetellut DAO-luokat onkin eristetty rajapintojen [UserDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/UserDao.java), [PortfolioDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/PortfolioDao.java), [CryptocurrencyDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/CryptocurrencyDao.java) ja [CryptoBatchDao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/CryptoBatchDao.java) taakse, ja sovelluslogiikka käyttää luokkia näiden abstraktioiden kautta. Nämäkin abstraktiot on vielä yleistetty niin, että jokainen perii yleisen [Dao](https://github.com/nakkekakke/CryptoTracker/blob/master/src/main/java/cryptotracker/dao/Dao.java)-rajapinnan.

### Tietokanta

Sovellus tallentaa dataluokkia (User, Portfolio, Cryptocurrency, CryptoBatch) vastaavat tiedot tietokantaan. Tietokannan sijainti määritellään tiedostossa [config.properties](https://github.com/nakkekakke/CryptoTracker/blob/master/config.properties).

Tietokannanhallintajärjestelmänä käytetään [SQLiteä](https://www.sqlite.org/about.html). Apuna tietokannan kanssa kommunikoimiseen käytetään Javan [JDBC](https://en.wikipedia.org/wiki/Java_Database_Connectivity)-rajapintaa.

Tietokantataulut ja niiden sarakkeet selviävät Database-luokan metodissa [initializeTables()](https://github.com/nakkekakke/CryptoTracker/blob/82215b1097a1064cfb681fcdb84f21cdc785bf2c/src/main/java/cryptotracker/dao/Database.java#L23) määritellyistä "CREATE TABLE"-lauseista.

## Päätoiminnallisuudet

Kuvataan ohjelman keskeisimpiä toimintoja ja niiden suoritusta sekvenssikaavioina.

### Sisäänkirjautuminen

Sisäänkirjautuminen tapahtuu ohjelman aloitusnäkymässä. Käyttäjä voi kirjautua sisään syöttämällä aloitusnäkymän tekstikenttään oman käyttäjänimensä ja painaa "Login"-näppäintä.

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/sekvenssikaavio_login.png">

Tapahtumakäsittelijä kutsuu sovelluslogiikasta vastaavan luokan _CryptoService_ metodia [login](https://github.com/nakkekakke/CryptoTracker/blob/5eec2a01380550264e897e71b4b0ec71380c8977/src/main/java/cryptotracker/domain/CryptoService.java#L88) parametrinaan käyttäjän syöttämä käyttäjänimi. Luokan _UserDao_ avulla selvitetään, onko kyseisellä käyttäjänimellä rekisteröidytty sovellukseen. Tietokantakysely tuottaa lopulta vastauksen; jos saatu ResultSet on epätyhjä, niin käyttäjä löytyi. Tällöin metodi login palauttaa arvon true, jolloin sovelluslogiikka kirjaa käyttäjän sisään. Lopulta käyttäjä siirretään uuteen näkymään.

<br>
Tietokantakyselyt noudattavat samaa periaatetta myös muiden operaatioiden kohdalla (luodaan Connection ja Statement, sekä tietoa haettaessa tutkitaan ResultSettiä), joten seuraavia sekvenssikaavioita on yksinkertaistettu piilottamalla kyselyt.

### Rekisteröityminen

Rekisteröityminen tapahtuu sisäänkirjautumisen tapaan ohjelman aloitusnäkymässä. Ohjelman käyttäjä voi rekisteröidä tunnuksen sovellukseen syöttämällä aloitusnäkymässä olevaan tekstikenttään haluamansa käyttäjänimen ja painamalla "Register"-näppäintä. 

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/sekvenssikaavio_registering.png">

"Register"-napin tapahtumakäsittelijä kutsuu _CryptoServicen_ metodia [createUser](https://github.com/nakkekakke/CryptoTracker/blob/51198c571e7368dce51192cad83a6d2699e4cfc5/src/main/java/cryptotracker/domain/CryptoService.java#L127), parametrina käyttäjän tekstikenttään syöttämä käyttäjänimi. Luokan _UserDao_ avulla selvitetään, onko kyseinen käyttäjänimi jo varattu. Jos ei, luodaan uusi tunnus sekvenssikaavion mukaisesti. Lopulta käyttäjälle ilmoitetaan onnistuneesta rekisteröitymisestä.

### Kryptovaluutan lisääminen

Kryptovaluuttojen lisääminen lisätä portfolioon tapahtuu sille varatussa näkymässä painamalla sisäänkirjautumisen jälkeen portfolionäkymässä nappia "Add Crypto". Tässä näkymässä kryptovaluutan lisääminen onnistuu täyttämällä tekstikenttiin kryptovaluutan nimi, määrä, hinta ja ostopäivämäärä, ja painamalla tämän lopuksi nappia "Add crypto".

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/sekvenssikaavio_addcrypto.png">

Kryptovaluutan lisäysnäkymässä olevan "Add crypto"-napin tapahtumakäsittelijä kutsuu _CryptoServicen_ metodia [createCrypto](https://github.com/nakkekakke/CryptoTracker/blob/51198c571e7368dce51192cad83a6d2699e4cfc5/src/main/java/cryptotracker/domain/CryptoService.java#L259) antaen parametriksi kryptovaluutan nimen, määrän, hinnan ja ostopäivämäärän. Aluksi selvitetään luokan _CryptocurrencyDao_ avulla, onko portfoliossa jo valmiiksi samaa kryptovaluuttaa. Jos ei, luodaan portfoliolle uusi kryptovaluuttainstanssi, sekä lisätään tähän juuri luotuun instanssiin uusi kryptovaluutan ostoerä (CryptoBatch). Tämä skenaario on kuvattu yllä olevassa sekvenssikaaviossa. Jos portfoliossa kuitenkin olisi jo lisättävää kryptovaluuttaa, ei uutta instanssia luotaisi vaan olemassa olevalle kryptovaluutalle lisättäisiin vain uusi ostoerä.

## Ohjelmaan jääneet rakenteelliset heikkoudet

### DAO-luokat

Monet sovelluksessa olevat, eri DAO-luokkien metodit ovat hyvin samanlaisia. Näitä metodeja olisi voinut abstraktoida paremmin, esimerkiksi luomalla yleiskäyttöisiä SQL-metodeja. Tämä jäi tekemättä, koska en osannut tehdä tällaista ilman, että siihen menisi hirveästi aikaa.
