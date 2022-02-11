@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "Main.java"
java -cp "C:\Cache" Main
