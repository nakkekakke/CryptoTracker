# Testausdokumentti

Ohjelmaa on testattu automatisoiduilla yksikkö- ja integraatiotesteillä käyttäen JUnitia ja [Mockitoa](http://site.mockito.org/), sekä lisäksi järjestelmätestaamista on tehty manuaalisesti.

## Yksikkö- ja integraatiotestaus

### Sovelluslogiikka

Automaattisten testien ytimen muodostavat pakkauksen [cryptotracker.domain](https://github.com/nakkekakke/CryptoTracker/tree/master/src/main/java/cryptotracker/domain) luokkia testaavat testit.

Pakkauksen dataluokkia testaavat testiluokat [UserTest](https://github.com/nakkekakke/CryptoTracker/blob/master/src/test/java/cryptotracker/domain/UserTest.java), [PortfolioTest](https://github.com/nakkekakke/CryptoTracker/blob/master/src/test/java/cryptotracker/domain/PortfolioTest.java), [CryptocurrencyTest](https://github.com/nakkekakke/CryptoTracker/blob/master/src/test/java/cryptotracker/domain/CryptocurrencyTest.java) ja [CryptoBatchTest](https://github.com/nakkekakke/CryptoTracker/blob/master/src/test/java/cryptotracker/domain/CryptoBatchTest.java). Näiden luokkien testit ovat pääosin yksikkötestejä, jotka testaavat yksinkertaisia operaatioita, kuten equals-metodeja ja konstruktoreita.

Varsinaista sovelluslogiikan toimintaa testataan luokassa [CryptoServiceTest](https://github.com/nakkekakke/CryptoTracker/blob/master/src/test/java/cryptotracker/domain/CryptoServiceTest.java). Tässä testaus on lähinnä integraatiotestausta; DAO-luokat mockataan Mockitolla ja dataluokista tehdään oikeat instanssit.

### DAO-luokat

DAO-luokkien toiminnallisuus on testattu pitkälti hyödyntämällä Mockitoa; tietokanta ja siihen liittyvät osat on mockattu, mutta tarkastetaan joka testissä, että tietokantakysely konstruktoidaan oikein ja että kysely oikeasti suoritetaan. Välillä käytetään myös Mockiton spy-toiminnallisuutta, jotta voidaan kustomoida halutut metodit palauttamaan haluttuja arvoja.

### Testauskattavuus

Käyttöliittymäpakkausta lukuun ottamatta sovelluksen testien rivikattavuus on 89% ja haarautumakattavuus 87%.

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/screenshot_jacoco.png">

Testatessa _DBCryptoBatchDao_-luokassa syntyi ongelmia, kun yritettiin CryptoBatchia luodessa parsea merkkijonosta LocalDate-olio. Myös CryptoService-luokasta jäi muutamia CryptoBatch-metodia testaamatta.

## Järjestelmätestaus

Sovellusta on testattu manuaalisesti Windowsilla ja Linuxilla. Kaikki käyttöohjeessa ja vaatimusmäärittelyssä olevat toiminnallisuudet on testattu, eikä bugeja ole löytynyt (paitsi jos suoritushakemistossa ei ole oikeanlaista _config.properties_-tiedostoa). Kaikkia testikenttiä on yritetty täyttää erilaisilla virheellisillä arvoilla.

## Sovellukseen jääneet laatuongelmat

Sovellus ei anna kunnollisia virheilmoituksia, jos tietokantayhteyden aikana syntyy _SQLException_.
