@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/ArrayQueueModuleTest.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/ArrayQueueADTTest.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/ArrayQueueTest.java"
java -cp "C:\Cache" queue.ArrayQueueModuleTest
java -cp "C:\Cache" queue.ArrayQueueADTTest
java -cp "C:\Cache" queue.ArrayQueueTest
