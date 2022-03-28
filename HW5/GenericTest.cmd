@echo off
cls
call "../src/clear_cache"
javac -Xlint:unchecked -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;." "..\tests\java\expression\generic\GenericTest.java"
java -cp "C:\Cache" -ea expression.generic.GenericTest hard CmmUlt
