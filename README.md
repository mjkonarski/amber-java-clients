panda-navi
==================

[![Master Build Status](https://travis-ci.org/paoolo/panda-navi.png?branch=master)](https://travis-ci.org/paoolo/panda-navi)
[![Devel Build Status](https://travis-ci.org/paoolo/panda-navi.png?branch=devel)](https://travis-ci.org/paoolo/panda-navi)

## Kompilacja

Gdyby jednak naszła kogoś ochota na samodzielną kompilacją to do zbudowania pliku JAR z bibliotekami wymagane są:
- maven3
- protoc - kompilator plików Protocol Buffer, do pobrania [stąd](https://code.google.com/p/protobuf/)

Po sklonowaniu repozytorium należy wydać polecenie `mvn package` w głównym katalogu. Archiwa JAR pojawią się w katalogach `target/` poszczególnych projektów.
