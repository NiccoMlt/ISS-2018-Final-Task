# Laboratorio di Sistemi Software 2018 - Final task

[![Commitizen friendly](https://img.shields.io/badge/commitizen-friendly-brightgreen.svg)](http://commitizen.github.io/cz-cli/)
[![Build Status](https://travis-ci.com/NiccoMlt/ISS-2018-Final-Task.svg?branch=master)](https://travis-ci.com/NiccoMlt/ISS-2018-Final-Task)

Elaborato finale per Laboratorio di Sistemi Software 2018.

## Struttura del repository

Il repository è composto dai seguenti sottoprogetti:

  - [``arduino``](./arduino)
    è un progetto PlatformIO per Arduino Uno contenente il codice del Robot fisico.

  - [``it.unibo.libs2018``](./it.unibo.libs2018)
    contiene i Jar delle librerie per Gradle e Eclipse; **deve** essere collocato in questa specifica posizione relativa alla cartella [``project``](./project) perché possa essere importata correttamente coi settaggi attuali.

  - [``project``](./project)
    contiene il [codice del metamodello](./project/src/robot.qa) secondo il linguaggio QA e il codice Java, sia generato dalla software factory (plugin di Eclipse), sia scritto ad hoc.

    È un progetto Eclipse, ma può essere importato anche in altre IDE come Intellij IDEA, ma non essendo compatibili con il plugin fornito per generare codice a partire dal metamodello QA, questa funzione non sarà disponibile.

  - [``report``](./report)
    contiene il progetto LaTeX per generare il documento della relazione.

    È basato sul [template fornito dal professore](https://github.com/anatali/iss2018/blob/master/it.unibo.issMaterial/issdocs/Lab/kitISLatex.zip), opportunamente adattato;
    si è scelto di utilizzare Arara per la compilazione e richiede JVM, Graphviz e opzione di shell-escape permessa perchè vada a buon fine.

  - [``soffritti``](./soffritti)
    contiene un ambiente di simulazione Web per l'implementazione virtuale del Robot.

    È basato sul [codice fornito dal professore](https://github.com/anatali/iss2018Lab/tree/master/it.unibo.mbot2018/Soffritti), a sua volta derivato da un [progetto di Pierfrancesco Soffritti](https://github.com/PierfrancescoSoffritti/configurable-threejs-app).
