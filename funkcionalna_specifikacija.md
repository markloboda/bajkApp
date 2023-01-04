# Seminarska naloga pri predmetu Elektronsko in mobilno poslovanje: Bajk
Avtor: Mark Loboda 63200174

## 1. Predstavitev ideje
Ideja je narediti mobilno aplikacijo, v kateri si uporabnik lahko izposodi kolo.
Po mestu bodo raztrosene kolesne postaje, v katerih bodo parkirana kolesa, nato pa si bo uporabnik na neki lokaciji lahko izposodil kolo, v drugi pa ga vrnil nazaj.

## 2. Funkcionalna specifikacija
Delovala bo na operacijskem sistemu Android Lollipop. Podatki se bodo shranjevali v SQLite podatkovni bazi. V to podatkovno bazo se bodo pri vsaki izposoji shranjevali podatki:
- ime in priimek izposojevalca
- termin izposoje in vrnitve
- lokacija izposoje in vrnitve

Prav tako pa bomo za vsako kolo shranjevali:
- število izposoj kolesa
- zgodovino izposoj kolesa

Prav tako pa bo aplikacija hranila status o postajah po mestu in kolesih na voljo. Ko bo uporabnik odprl aplikacijo, se mu bodo prikazalo več funkcij/gumbov:
- izposodi kolo
- vrni kolo
- preveri postaje

V primeru, da izbere funkcijo **izposodi kolo**, se mu bo prikazalo okno za izposojo kolesa. Nato bo uporabnik glede na trenutno lokacijo prejel priporočilo, na kateri postaji je, možnost pa bo imel to postajo spremeniti. Nato bo vpisal podatke in pritisnil gumb **Izposodi**.

V primeru, da izbere funkcijo **vrni kolo**, se mu bo prav tako kot pri izposoji pokazalo priporočilo, na katero postajo želi vrniti kolo, kar pa bo lahko spremenil. Nato bo pritisnil gumb **Vrni**.

*V končnem produktu bo vsaka postaja vsebovala tudi preverbo, ali je neko kolo res vrnjeno ali ne. V mojem primeru pa bo aplikacija le "proof of concept" in tega preverjanja ne bo.*

V primeru, da izbere funkcijo **preveri postaje**, se mu odpre zemljevid, na katerem so označene vse postaje po mestu. Ko uporabnik klikne na postajo, se mu prikažejo podatki o postaji:
- ime postaje
- naslov postaje
- podatki o kolesih na voljo