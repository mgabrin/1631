@echo off
title InterfaceServer

javac -sourcepath ../../Components/InterfaceServer -cp ../../Components/* ../../Components/InterfaceServer/*.java
start "InterfaceServer" /D"../../Components/InterfaceServer" java -cp .;../* CreateInterfaceServer
