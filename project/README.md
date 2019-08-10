## Laboratorio di Sistemi Software 2018 - Final task - Project

Questo è il progetto, importabile preferibilmente in Eclipse (ma funzionante in altri IDE) per il progetto finale di Laboratorio di Sistemi Software.

### Configurazione e import

#### Eclipse

La versione consigliate è Eclipse for Java developers, ottenibile qui: [https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)

#### Xtend plugin

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

#### QA plugin fornito dal professore

- Ottenere i file jar dalla cartella [``plugins``](https://github.com/anatali/iss2018/tree/master/it.unibo.issMaterial/plugins) del repositori del corso:
  - ([it.unibo.xtext.qactor_1.5.15](https://github.com/anatali/iss2018/raw/master/it.unibo.issMaterial/plugins/it.unibo.xtext.qactor_1.5.15.jar)
  - [it.unibo.xtext.qactor.ui_1.5.15](https://github.com/anatali/iss2018/raw/master/it.unibo.issMaterial/plugins/it.unibo.xtext.qactor.ui_1.5.15.jar))
- Mettere i file nella cartella ``dropins`` nella root della cartella di installazione di Eclipse (per esempio, ``C:\Users\<username>\eclipse\java\eclipse\dropins`` o ``/usr/lib/eclipse/dropins/``)
- Ricaricare Eclipse

#### Gradle

Una volta aperto il file QA mentre i plugin Xtext e QA sono installati, Eclipse chiederà di convertire il progetto in un progetto Xtext;
una volta accettato, il metamodello permetterà di generare una-tantum un file gradle per la configurazione della IDE e il building del progetto.

[Una versione testata del buildscript](./build_ctxRobot.gradle) è tracciata in questa cartella;
si sconsiglia di importare il progetto come progetto Gradle, bensì utilizzare Gradle da riga di comando per il building diretto o per generare le configurazioni per Eclipse.

In particolare, la procedura è la seguente:

- ``gradle -b build_ctxRobot.gradle qawrapper`` permette di generare un Gradle wrapper compatibile con il buildscript (attualmente, ne è già tracciata una versione compatibile)

- ``.\gradlew.bat -b build_ctxRobot.gradle eclipse`` o ``./gradlew -b build_ctxRobot.gradle eclipse`` permette di generare la configurazione di Eclipse (**non tracciata** in questo repo) e i riferimenti alle librerie.

  - In caso di problemi, lanciare il task Gradle ``cleanEclipse`` per cancellare una precedente configurazione di Eclipse

#### Intellij IDEA

Qualora si desideri importare il progetto in Intellij IDEA, si consiglia di seguire la procedura di importazione per Eclipse e successivamente importare il progetto così configurato come progetto Eclipse in IDEA.
