@echo off
cls
call "../src/clear_cache"
javac -Xlint:unchecked -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "Main.java"
java -cp "C:\Cache" Main -i "(((x + y + z)))"
