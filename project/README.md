# Laboratorio di Sistemi Software 2018 - Final task - Project

Questo è il progetto, importabile preferibilmente in Eclipse (ma funzionante in altri IDE) per il progetto finale di Laboratorio di Sistemi Software.

## Configurazione e import

### Eclipse

La versione consigliate è Eclipse for Java developers, ottenibile qui: [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)

### Xtend plugin

Per generare codice dal metamodello, è necessario che siano installati i plugin per Xtext e Xtend.

<details>
  <summary>Dal marketplace:</summary>
  <p>

  - Help > Eclipse Marketplace...
  - Cercare "Xtext"
  - Installare "Eclipse Xtend" e "Eclipse Xtext"

  </p>
</details>

<details>
  <summary>Dal repo ufficiale:</summary>
  <p>

  - Help > Install New Software...
  - Add...
  - Impostare il nome "Xtext" e la Location " http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/ "
  - Installare "Xtend" e "Xtext"

  </p>
</details>

### QA plugin fornito dal professore

  - Ottenere i file jar dalla cartella [``plugins``](https://github.com/anatali/iss2018/tree/master/it.unibo.issMaterial/plugins) del repositori del corso:
    - [it.unibo.xtext.qactor_1.5.15](https://github.com/anatali/iss2018/raw/master/it.unibo.issMaterial/plugins/it.unibo.xtext.qactor_1.5.15.jar)
    - [it.unibo.xtext.qactor.ui_1.5.15](https://github.com/anatali/iss2018/raw/master/it.unibo.issMaterial/plugins/it.unibo.xtext.qactor.ui_1.5.15.jar)
  - Mettere i file nella cartella ``dropins`` nella root della cartella di installazione di Eclipse (per esempio, ``C:\Users\<username>\eclipse\java\eclipse\dropins`` o ``/usr/lib/eclipse/dropins/``)
  - Ricaricare Eclipse

### Gradle

Una volta aperto il file QA mentre i plugin Xtext e QA sono installati, Eclipse chiederà di convertire il progetto in un progetto Xtext;
una volta accettato, il metamodello permetterà di generare una-tantum un file gradle per la configurazione della IDE e il building del progetto.

[Una versione testata del buildscript](./build_ctxRobot.gradle) è tracciata in questa cartella;
si sconsiglia di importare il progetto come progetto Gradle, bensì utilizzare Gradle da riga di comando per il building diretto o per generare le configurazioni per Eclipse.

In particolare, la procedura è la seguente:

  - ``gradle -b build_ctxRobot.gradle qawrapper`` permette di generare un Gradle wrapper compatibile con il buildscript (attualmente, ne è già tracciata una versione compatibile)

  - ``.\gradlew.bat -b build_ctxRobot.gradle eclipse`` o ``./gradlew -b build_ctxRobot.gradle eclipse`` permette di generare la configurazione di Eclipse (**non tracciata** in questo repo) e i riferimenti alle librerie.

    - In caso di problemi, lanciare il task Gradle ``cleanEclipse`` per cancellare una precedente configurazione di Eclipse

### Intellij IDEA

Qualora si desideri importare il progetto in Intellij IDEA, si consiglia di seguire la procedura di importazione per Eclipse e successivamente importare il progetto così configurato come progetto Eclipse in IDEA.

## Struttura delle cartelle

Il fatto che non sia un progetto Gradle standard bensì più un progetto Eclipse è evidente dalla suddivisione delle cartelle:

  - ``sites/``
    contiene codice HTML auto-generato una-tantum per la stesura di documentazione.

    I file (e la cartella) non sono tracciati in quanto non utilizzati.

  - [``src/``](./src)
    contiene i file sorgente Java principali, nonché la [specifica formale dell'architettura logica](./src/robot.qa) tramite metamodello QA.

    Alcuni dei file Java sono auto-generati una-tantum e sono dunque tracciati per via delle eventuali ulteriori modifiche effettuate.

  - [``src-gen/``](./src-gen)
    contiene i file Java generati dal plugin QA e Xtext a partire dalla specifica.

    I file Java, tutti auto-generati, sono mantenuti tracciati poiché senza Eclipse Travis CI non sarebbe in grado di eseguire la build del progetto.

  - [``srcMore/``](./srcMore)
    contiene risorse aggiuntive per il progetto, alcune generate una-tantum ed altre auto-generate ad ogni modifica del file QA.

  - [``test/``](./soffritti)
    contiene i file sorgente Java per i test JUnit.

  - [``build_ctxRobot.gradle``](./build_ctxRobot.gradle)
    è il buildscript utilizzato per generare le configurazioni di Eclipse (cartella ``.settings`` e file ``.classpath`` e ``.project``) e per compilare il progetto da linea di comando.
