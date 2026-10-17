# Arbeidsmåte

Startet med å få oversikt over datatypene og planla klassene. Lagde `PolitiBil`-klassen først for å ha et objekt å jobbe med. Deretter ble `Client`-klassen laget for å hente inn dataene, og `PolitiBilListe`-klassen for å lagre bilene midlertidig i en liste. Brukte javlin, her siden jeg har ikke så mye erfaring med springboot. 

Underveis la jeg til en regel om at biler med status "Under vedlikehold" ikke kan få oppdrag. 
Klasse diagrammet og README ble laget sist.


## Kom i gang

**Krav:** Java 17 eller nyere og Maven.
Personlig bygget jeg og kjørte prosjektet i IntelliJ.
Prosjektet bygger med print av alle bilene som blir hentet ut og alle biler som er tilgjengelige.

Bygg prosjektet og kjør testene:

```bash
mvn clean test
```

Start serveren, enten ved å kjøre `Main` fra IDE-en eller med:

```bash
 mvn compile exec:java "-Dexec.mainClass=Main"
```

API-et kjører på `http://localhost:8080`. Ved oppstart henter applikasjonen dummy-data fra
<https://erikrolspo.github.io/politibiler.json>, så maskinen må ha internettilgang.

## Avhengigheter

Avhengighetene er definert i `pom.xml` og lastes ned automatisk av Maven.

| Avhengighet | Versjon  | Brukes til |
|-------------|---------|------------|
| `javalin` | 6.3.0 | Webserveren som eksponerer REST-endepunktene. |
| `jackson` | 2.20.0 | Gjør JSON om til Java-objekter og omvendt. |
| `slf4j` | 2.0.16  | Enkel logging til konsollen. |
| `jupiter` | 5.11.3 | Rammeverk for enhetstestene. |

- **Javalin** er et lett web-rammeverk for Java. Det brukes i `Main.java` til å starte serveren på port 8080 og definere endepunktene. Javalin ble valgt fordi det krever lite oppsett sammenlignet med større rammeverk som Spring Boot, og passer godt til et lite API.
- **Jackson** brukes i `Client.java` til å lese JSON-dataene fra det eksterne endepunktet og gjøre dem om til `PolitiBil`-objekter. `@JsonProperty` i `PolitiBil.java` kobler feltnavnene i JSON til feltene i klassen. Javalin bruker også Jackson til å gjøre objektene om til JSON når API-et svarer.
- **SLF4J Simple** er en logg-implementasjon. Javalin logger gjennom SLF4J, og uten en implementasjon på classpath vises bare en advarsel og ingen logger. `slf4j-simple` skriver loggene rett til konsollen, slik at man ser når serveren starter og hvilke forespørsler som kommer inn.
- **JUnit Jupiter** (JUnit 5) brukes til enhetstestene i `src/test/java/`. Den har `test`-scope, så den er bare med når testene kjøres og ikke i selve applikasjonen.

I tillegg brukes `java.net.http.HttpClient` fra Javas standardbibliotek til å hente dataene.


## Endepunkter


| Metode | Sti | Beskrivelse |
|--------|-----|-------------|
| `GET`  | `/politibiler?regNr={regNr}` | Henter én bestemt bil med merke, modell, årsmodell, registreringsnummer, status og oppdrag. |
| `GET`  | `/politibiler/tilgjengelige` | Henter alle biler med status "Tilgjengelig", sortert etter ID i stigende rekkefølge. |
| `POST` | `/politibiler/oppdrag?regNr={regNr}&oppdrag={oppdrag}` | Gir bilen et oppdrag og setter statusen til "I oppdrag". Biler med status "Under vedlikehold" blir ikke endret. |

### Eksempler

```bash
# Hent én bil (mellomrom i regNr skrives som %20)
curl "http://localhost:8080/politibiler?regNr=DY%2033852"

# Hent alle tilgjengelige biler
curl "http://localhost:8080/politibiler/tilgjengelige"

# Gi en bil et oppdrag
curl -X POST "http://localhost:8080/politibiler/oppdrag?regNr=DY%2033852&oppdrag=Patruljering"
```

Eksempel på politibil dataobjekt:

```json
{
  "id": 12,
  "merke": "Toyota",
  "modell": "Hiace",
  "årsmodell": 2016,
  "regNr": "DY 33852",
  "status": "Tilgjengelig",
  "oppdrag": "Patruljerer"
}
```

## Struktur

| Fil | Ansvar |
|-----|--------|
| `Main.java` | Starter applikasjonen, laster data og definerer endepunktene. |
| `Client.java` | Henter JSON-data fra det eksterne endepunktet og gjør det om til objekter. |
| `PolitiBil.java` | Modellen for én politibil. |
| `PolitiBilListe.java` | Logikk for sortering, filtrering, oppslag og tildeling av oppdrag. |
| `src/test/java/` | Enhetstester for logikken i `PolitiBilListe`. |

## Antagelser


- **ID sorteres som tall.** I dataene er `id` en tekststreng, men den sorteres som et tall slik at `4` kommer før `12`.
- **Lagring i minnet.** Dataene hentes én gang ved oppstart og ligger deretter bare i minnet. Endringer, som nye oppdrag, forsvinner når applikasjonen startes på nytt. En løsning kan være å bruke persistent lagring i database.
- **Tildeling av oppdrag.** Når en bil får et oppdrag, overskrives det forrige oppdraget og statusen blir "I oppdrag". Oppdraget er fritekst, for eksempel "Patruljering", "Nødssituasjon" eller "Spesiell operasjon". Biler som har status "Under vedlikehold", kan ikke få oppdrag.


## Kjente begrensninger og videre arbeid

Dette ville jeg forbedret med mer tid:

- **Feilhåndtering.** API-et bør svare med `404 Not Found` når en bil ikke finnes og `400 Bad Request` når parametere mangler. I dag svarer tildeling alltid "Oppdrag tildelt", også når registreringsnummeret ikke finnes.

- **Typer.** Status og oppdragstype bør være `enum` i stedet for fritekst, slik at ugyldige verdier blir avvist.
- **REST-design.** Mer robuste metoder for å hente ut informasjon, med JSON i body i stedet for query-parametere.
- **Robusthet.** Hvis det eksterne endepunktet ikke svarer, bør applikasjonen gi en tydelig feilmelding eller bruke lokale reservedata i stedet for å krasje.

- **Flere tester.** Det mangler integrasjonstester mot selve API-et, for eksempel med `javalin-testtools`, og tester for feiltilfeller.



## Klassediagram
Bildet skal være svg, så det er mulig å zoome in om det er vanskelig å se.
![Diagram](https://www.plantuml.com/plantuml/svg/XLNDSjis4BxZANRKYpGZqsqFyOp6zYHAx4Hj4tkYf9TE3v2mXDO40Su2T4Vjz77y3hdhnKg054rp7FS6xC-t--r-15_x83dKfH6-85j9bYLiZVJ-JGXCcphWQkliclv6-74GudTIkR_8QiYbyJZm6B8OzXM2ux1p0uTr2ZrMy6g8mz8MqNWsD5eNS2ctXMPNM_NM6SVmm_NrzRkhit6KOuNSHIpVBSzUBSME50AMLTX3YUK6sGiX4YNuACd2FmAWR0vJoTh30jQ1oUe__fmrvtj7IlmhH9_nrX3Qa7AsxN417qAeMdj2gYhandgpkSDj-9YkGW3i6l-jCnHeGmPv6k7R-vBCR-G3naAH11ZC0xWSZg3lhxx0hYdY5wUdo5ImCxvocQQmgpSPkTFg4JUxyyu2YAwHZ56gtwMfiIceYSSbqQeqk2F1aJyIZR2HK3ISz-vBaHWjUovZju0iWxNZW76SpKK0-7Iz2W5vfQOcLlXkwZMPm8FSMEqB6Ns7LgCXZMtfbq5kgieREK1eN2kRkrlv6VMdBdiE72zF5jAdApaaW5PnBBmECjJ-EPGvs7q8CP6bxYMKqBu6Np3b0QQ5izuPCsj1RvbiU3ht5h9F7yTCDsIEpburJ7eNPJDUCPnlBfyC_lnqSocwrI13AcMXEZn0Sr0O06CSQ0pDacOm9HjcSm7UK04oT7eeKG4f-0aALvR8a1--yLFNpvb8jPzVNDl4-yC3G_HOLnuRSHeOGyqsTOSMxBvrYOYSQbW4XGm-RG4kBc0oB339H5TvlF2kwkSElclR1uoe2RsKOTkaAUosAG2DGVPWQsCwGYCH396loLhajiU4br0o-71F_jWk5GK2ie39LnjPtACoLE3E6JMPWu_3z-1Q0S75K4u2dEv9msJLHKN2iB8QRT9d5EikrckxG8PwNATZrNRNDfJ7pdt7y1b5mkb8WvsA2gRxbl4KUGQ137JgHWkvOyYHZ3zyAyZgx8Nd9huqfFhdD-wQ2npUdK9Z1WR9lhUH3Q9bCmZe9jKR-g7s5Y509x7ZZLwtX-diXGyg3QU5ZVGN8ztDXpkPVsV1azl3GsEYDAeaDUDnaf1xHX4S37hqaBE4NzOtdqvoD043J0uFnwY9U9nd-fETPzdn1xKOYjoGFuPasULZI4iPI5kfeGwxnafMfv9lRbUF2ICbBE1N6UMJdeofFpxnym64qqSfpa3Uj--2wEh-3s8S8CV6ZCUOcvgFWFa-XP2r7Tu81Dp92Gm4jUZ2NgDLTMd-0m00)
