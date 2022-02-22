@echo off
cls
call "../src/clear_cache"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "queue/ArrayQueueModule.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "queue/ArrayQueueADT.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "queue/ArrayQueue.java"
javac -cp "C:\Cache" -d "C:\Cache" -sourcepath "..\tests\java;../;." "..\tests\java\queue\ArrayQueueTest.java"
java -cp "C:\Cache" -ea queue.ArrayQueueTest DequeIndex
