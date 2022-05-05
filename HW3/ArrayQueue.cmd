@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/TestArrayQueueModule.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/TestArrayQueueADT.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "../tests/java;../;." "queue/TestArrayQueue.java"
java -cp "C:\Cache" queue.TestArrayQueueModule
java -cp "C:\Cache" queue.TestArrayQueueADT
java -cp "C:\Cache" queue.TestArrayQueue
