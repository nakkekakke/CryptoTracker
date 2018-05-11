# Käyttöohje

Lataa tiedostot [CryptoTracker.jar ja config.properties](https://github.com/mluukkai/OtmTodoApp/releases/tag/0.1). Pidä huoli, että molemmat tiedostot ovat samassa kansiossa!

## Konfigurointi

Ohjelma olettaa, että sen suoritushakemistossa on konfiguraatiotiedosto _config.properties_, joka määrittelee tietokannan sijainnin. Jos tietokantaa ei ole, ohjelma luo sellaisen. Tiedosto sisältää seuraavan rivin tekstiä

```
databaseAddress=cryptotracker.db
```

## Rekisteröityminen ja sisäänkirjautuminen

Sovellus käynnistyy rekisteröitymis- ja sisäänkirjautumisnäkymään:

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/screenshot_login.png">

Uuden käyttäjätunnuksen rekisteröinti tapahtuu kirjoittamalla tekstikenttään käyttäjätunnus ja painamalla "Register". Rekisteröityminen epäonnistuu, jos syötetyllä käyttäjänimellä on jo olemassa käyttäjä. Sisäänkirjautuminen onnistuu kirjoittamalla aikaisemmin luotu käyttäjätunnus tekstikenttään ja painamalla "Login".

## Portfolionäkymä

Kun käyttäjä kirjautuu sovellukseen sisään, siirrytään portfolionäkymään. Tässä näkymässä listataan kaikki käyttäjän portfolioonsa lisäämät kryptovaluutat:

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/screenshot_portfolio.png">

Näkymässä käyttäjän on mahdollista poistaa kryptovaluuttoja painamalla poistettavan krypton kohdalla "Delete crypto", jolloin myös kaikki kyseiseen kryptovaluuttaan liittyvät ostoerät poistetaan. Käyttäjä voi myös siirtyä lisäämään portfolioonsa kryptovaluuttoja painamalla ylhäältä "Add Crypto". Painettaessa "See batches" siirrytään ostoeränäkymään, jossa käyttäjä näkee tarkempaa tietoa yhden kryptovaluutan sijoituksistaan. Tässä näkymässä käyttäjä voi myös kirjautua ulos, jolloin palataan sisäänkirjautumisnäkymään.

## Kryptovaluuttojen lisääminen

Painettaessa portfolionäkymässä nappia "Add Crypto" siirrytään näkymään, jossa käyttäjä pystyy lisäämään portfolioonsa kryptovaluuttoja:

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/screenshot_addcrypto.png">

Kryptovaluutan lisääminen vaatii jokaisen kentän täyttämisen valideilla arvoilla. Erityisesti päivämäärän on oltava täsmälleen vaaditussa formaatissa (YYYY-MM-DD). Kentät "Amount of crypto" ja "Price paid" tukevat myös ainoastaan kokonaislukuarvoja. Jos johonkin kenttään sijoitettu arvo ei ole validi, kerrotaan siitä käyttäjälle.

Kryptovaluutta lisätään, kun kaikki arvot ovat valideja ja käyttäjä painaa "Add crypto". Tällöin käyttäjän portfolioon lisätään uusi kryptovaluutta, ja luodulle kryptovaluutalle uusi ostoerä. Jos lisättävä kryptovaluutta on jo käyttäjän portfoliossa, ei uutta kryptovaluuttaa luoda vaan vain lisätään olemassa olevalle uusi ostoerä.

Painettaessa "Go back" siirrytään takaisin portfolionäkymään.

## Ostoeränäkymä

Portfolionäkymässä käyttäjän painaessa jonkin kryptovaluutan kohdalla "See batches" siirrytään näkymään, jossa käyttäjä näkee listan kryptovaluuttaan lisäämistään ostoeristä:

<img src="https://raw.githubusercontent.com/nakkekakke/CryptoTracker/master/dokumentointi/kuvat/screenshot_batches.png">

Jokaisen ostoerän kohdalla näytetään ostopäivämäärä, ostettu määrä kryptovaluuttana ja erän ostohinta euroina. Tässä näkymässä käyttäjä voi myös halutessaan poistaa yksittäisiä ostoeriä painamalla ostoerän kohdalla "Delete batch". Jos käyttäjä poistaa kryptovaluuttasijoituksestaan kaikki ostoerät, poistetaan myös ns. "tyhjä" kryptovaluutta käyttäjän portfoliosta. Painamalla "Go back" käyttäjä pääsee takaisin portfolionäkymään.
