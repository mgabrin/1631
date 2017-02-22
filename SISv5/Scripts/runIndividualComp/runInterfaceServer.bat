@echo off
title InterfaceServer

javac -sourcepath ../../Components/InterfaceServer -cp ../../Components/* ../../Components/InterfaceServer/*.java
start "InterfaceServer" /d "../../Components/InterfaceServer" java -cp .;../* CreateInterfaceServer

pause
