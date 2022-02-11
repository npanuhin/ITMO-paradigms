@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "search/BinarySearch.java"
java -cp "C:\Cache" -ea search.BinarySearch 3 5 4 3 2 1
java -cp "C:\Cache" -ea search.BinarySearch 100 5 4 3 2 1
java -cp "C:\Cache" -ea search.BinarySearch -100 5 4 3 2 1
