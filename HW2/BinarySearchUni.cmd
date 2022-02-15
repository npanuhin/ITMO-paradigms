@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "search/BinarySearchUni.java"
java -cp "C:\Cache" -ea search.BinarySearchUni 3 2 1 1 2 3 4 5
java -cp "C:\Cache" -ea search.BinarySearchUni "5" "4" "3" "-1" "1"
java -cp "C:\Cache" -ea search.BinarySearchUni "2" "0" "0" "1" "4"
