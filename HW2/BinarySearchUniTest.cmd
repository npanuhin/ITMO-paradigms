@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "..\tests\java\search\BinarySearchTest.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "search/BinarySearch.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "search/BinarySearchUni.java"
java -cp "C:\Cache" search.BinarySearchTest Uni
