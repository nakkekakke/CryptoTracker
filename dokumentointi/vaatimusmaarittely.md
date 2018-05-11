# Vaatimusmäärittely

## Sovelluksen tarkoitus

Tämän sovelluksen avulla käyttäjä pystyy pitämään kirjaa omista kryptovaluuttasijoituksistaan lisäämällä niiden tiedot portfolioon. Sovelluksella voi olla monta rekisteröitynyttä käyttäjää, ja kirjautumalla sisään jokainen käyttäjä näkee oman portfolionsa.

## Käyttäjät

Sovelluksella on vain yksi käyttäjäryhmä, _normaali käyttäjä_. Lisäoikeuksilla varustettu _pääkäyttäjä_ saatetaan lisätä tulevaisuudessa.

## Toiminnallisuudet

Sovelluksessa nykyisin olevat toiminnallisuudet.

### Ennen sisäänkirjautumista

- käyttäjä voi luoda käyttäjätunnuksen
  - käyttäjätunnuksen on oltava uniikki
  - käyttäjätunnus 4-20 merkkiä pitkä

- käyttäjä voi kirjautua sisään
  - jos kirjautuminen epäonnistuu, ilmoitetaan siitä käyttäjälle

### Sisäänkirjautumisen jälkeen

- käyttäjä näkee oman portfolionsa
  - portfoliossa näkyy siihen lisätyt kryptovaluutat
  - jokaisen kryptovaluutan kohdalta pääsee tarkastelemaan kryptovaluutan **ostoeriä**
    - jokaisen ostoerän kohdalla näkyy valuutan määrä, ostoarvo euroina sekä ostopäivämäärä
    - ostoerät ovat poistettavissa yksittäisesti
  - portfolio näkyy vain sisäänkirjautuneelle käyttäjälle
  
- käyttäjä voi lisätä kryptovaluuttoja omaan portfolioonsa
  - lisättäessä syötetään valuutan nimi, lisättävä määrä, ostohinta sekä ostopäivämäärä
  - jos portfoliossa on jo valuuttaa, jota sinne lisätään, lisätään olemassa olevaan valuuttaan vain uusi ostoerä

- käyttäjä voi poistaa kryptovaluuttoja portfoliostaan
  - jokaisen valuutan kohdalla on poistonäppäin
  - kryptovaluutan poistaminen poistaa myös kaikki siihen liittyvät ostoerät
  
- käyttäjä voi kirjautua ulos

## Ideoita jatkokehitykseen

Voidaan lisätä tulevaisuudessa (kurssin jälkeen).

### Sisäänkirjautuminen

- käyttäjätunnuksen luonnin (ja sisäänkirjautumisen) yhteydessä käyttäjältä vaaditaan myös salasana

### Portfolionhallinta
  
- ostoerää lisättäessä käyttäjältä vaaditaan valuutta, jolla ostos tehtiin
  - valuutta joko euro tai toinen kryptovaluutta
    - jos kryptovaluutta, vähennetään vastaava määrä lähtövaluuttaa portfoliosta (valuutanvaihto)

### Portfolion tietojen esitys

- käyttäjä pystyy seuraamaan sekä koko portfolionsa että yksittäisten portfolion kryptovaluuttojen arvoa euroissa (euroarvo voitaisiin hakea esimerkiksi coinmarketcap.com:in [API](https://coinmarketcap.com/api/):sta).

- jokaisen ostoerän kohdalla näkyy omistuksen arvo euroina

- sovelluksessa näkyy euromäärä, jonka käyttäjä on sijoittanut portfolioonsa, sekä voiton/tappion suuruus

- sovelluksessa kuvataan portfolion arvonmuutos viimeisen tunnin, päivän ja viikon aikana
  - koko portfolion arvonmuutos sekä jokaisen kryptovaluutan arvonmuutos erikseen
  
- sovelluksessa on (esim.) taulukko, jossa kuvataan portfolion arvonkehitystä
  - tarkkuus esim. kuukausittainen
  - seuraa portfolion absoluuttista arvoa ja prosentuaalista kehitystä
  
- käyttäjä voi muokata ostoerän ostohintaa ja ostetun valuutan määrää
  
- sovelluksessa on ympyrädiagrammi, joka kuvaa portfolion eri kryptovaluuttojen osuutta koko portfoliosta

- sovelluksessa on viivadiagrammi, joka kuvaa portfolion arvonkehitystä
