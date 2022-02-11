@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;." "..\tests\java\expression\exceptions\ExceptionsTest.java"
java -cp "C:\Cache" -ea expression.exceptions.ExceptionsTest hard Base
