:title Hazelcast queues er ikke partisjonerte
:published 2014-05-01
:dek Skral dokumentasjon
:body

Der. Da er det sagt, i tilfelle du leter i [dokumentasjonen](http://www.hazelcast.org/docs/latest/manual/html/queue.html) og savner en bedre beskrivelse. 

Først og fremst, jeg liker Hazelcast; det har et enkelt API, kan embeddes i applikasjonen din og funker ut av boksen når du trenger å distribuere datastrukturer på tvers av prosesser/maskiner.

Så hvordan funker køene i Hazelcast, egentlig?

Ut fra diskusjon på [forumet deres](https://groups.google.com/forum/#!searchin/hazelcast/conceptual$20overview$20of$20how$20queues/hazelcast/Gvq2TTAaWrE/eogDIYadf2EJ), så tror jeg det følgende gjelder:

* En kø eksisterer kun på en node i et cluster
* En kø kan ha backups på andre noder
* Klientkoden trenger ikke å bry se om hvor køen er
* Du har ingen garanti for rekkefølgen meldinger konsumeres på
* Ettersom en kø kun eksisterer på en node i sin helhet, så kan du ikke øke maksimal størrelse på køen ved å skalere horisontalt.
* Meldinger kan kun tas av fra en kø enkeltvis i en transaksjon; dvs hvis du ønsker å prosessere flere meldinger i bulk, så kan du velge mellom [dårlig ytelse eller å miste meldinger](https://groups.google.com/d/msg/hazelcast/Gvq2TTAaWrE/RJBvR8jK1XAJ)
* En kø kan persisteres hvis du implementerer dette selv, typisk til et [sentralt repo](https://groups.google.com/forum/#!topic/hazelcast/Wa6gELKB3fk).

Her er et eksempel på et cluster med en kø og en backup. 

![Hazelcast queue](/images/hazelcast_queue.png)

Et triks for å skalere størrelsen på en kø er å stripe den; dvs opprette flere køer som representerer en kø. Dvs du skriver til hver av køene round-robin style; og poller fra alle køene. Åpenbart ikke en ekstremt skalerbar løsning.

Ting jeg ikke vet så mye om enda:

* Hvordan bestemmer Hazelcast hvilken node som skal holde på en kø?
* Hvordan påvirkes ytelsen når antall noder som leser fra kø øker? 